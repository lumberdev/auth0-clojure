(ns auth0-clojure.core
  (:require [auth0-clojure.json :as json]
            [clojure.string :as string]
            [org.bovinegenius.exploding-fish :as uri]
            [clj-http.client :as client])
  (:import (com.auth0.client.auth AuthorizeUrlBuilder AuthAPI LogoutUrlBuilder)))

;; TODO - probably use ns kw, like :auth0/client-id or ::client-id
;; TODO - add spec for domains & subdomains

(def default-domain "ignorabilis.auth0.com")
(def custom-domain "ignorabilis.auth0.com")
(def client-id "wWiPfXbLs3OUbR74JpXXhF9jrWi3Sgd8")
(def client-secret "0fkMFyofJiWinkwnl4Udcs_oAf7P4e6-WKTx8TAyC8Gh_CyrzytOylsD6bftrRoO")

(def some-default-config
  {:default-domain "ignorabilis.auth0.com"
   :custom-domain  "ignorabilis.auth0.com"
   :client-id      "wWiPfXbLs3OUbR74JpXXhF9jrWi3Sgd8"
   :client-secret  "0fkMFyofJiWinkwnl4Udcs_oAf7P4e6-WKTx8TAyC8Gh_CyrzytOylsD6bftrRoO"})

(def global-config
  (atom {}))

(defn set-config! [new-config]
  (reset! global-config new-config))

(defn auth0-auth-api []
  (let [a0-client-id     client-id
        a0-client-secret client-secret]
    (AuthAPI. default-domain a0-client-id a0-client-secret)))

;; TODO - these could be exposed too
;; withScope should work with plain string like
;; "openid email profile"
;; or with a set or vector of strings/keywords, like this:
(comment
  [:auth0.scope/openid :auth0.scope/email]
  #{:auth0.scope/openid :auth0.scope/profile}
  ;alternatives
  #{:openid :email})
;; These then get converted to a set, then to string
;; TODO - scope validation (if string convert to set & validate)
(defn java-login-url [{{:keys [:redirect-to]} :params}]
  (let [authorize-url (-> (auth0-auth-api)
                          (.authorizeUrl "http://localhost:1111/user")
                          (.withScope "openid"))
        authorize-url (if redirect-to
                        (.withState
                          ^AuthorizeUrlBuilder authorize-url
                          ^String redirect-to)
                        authorize-url)]
    (.build ^AuthorizeUrlBuilder authorize-url)))

(defn java-logout-url []
  (let [logout-builder (.logoutUrl
                         ^AuthAPI
                         (auth0-auth-api)
                         "https://ignorabilis.com/login"
                         true)
        logout-builder (-> logout-builder
                           (.useFederated true))
        logout-url     (.build ^LogoutUrlBuilder logout-builder)]
    logout-url))

;; can add spec here so that redirect uri is valid,
;; scope & state are strings, etc.
;; TODO - do I need ^String, ^PersistentHashMap, etc.?

;; comment/uncomment this for some default config
(set-config! some-default-config)

(def https-scheme "https")

;; TODO - memoize base-url based on default & custom domains
(defn base-url
  ([]
   (base-url @global-config))
  ([{:keys [:default-domain
            :custom-domain]}]
   (uri/map->uri {:scheme https-scheme
                  :host   (or custom-domain default-domain)})))

(defn decode-underscore-key
  [k]
  (-> k
      (string/replace "_" "-")
      keyword))

(defn encode-underscore-key
  [k]
  (-> k
      name
      (string/replace "-" "_")))

;; Generate query param values like `federated` with an equal sign;
;; it's the safe bet since Auth0 is doing the same
(defn parse-value [k v]
  (case k
    :auth0/federated (when v "")
    v))

(def raw-param-ks
  #{:auth0/redirect-uri})

(defn param-key->param-fn
  "Some query parameters should be raw, depending on the key."
  [param-key]
  (if (contains? raw-param-ks param-key)
    uri/param-raw
    uri/param))

(defn build-url-params-base [uri params-map]
  (reduce
    (fn [auth-url [k v]]
      (let [parsed-val (parse-value k v)
            param-fn   (param-key->param-fn k)]
        ;; remove any nil values, otherwise they get added to query params without an equal sign
        ;; for example {:federated nil} -> ... &federated&some_other=1 ...
        (if (nil? parsed-val)
          auth-url
          (param-fn
            auth-url
            (encode-underscore-key k)
            parsed-val))))
    uri
    params-map))

(defn build-url-params [uri params-map]
  ;; TODO - adding raw params in the end is a workaround for that issue:
  ;; https://github.com/wtetzner/exploding-fish/issues/26
  ;; revert once it is fixed
  (let [raw-params-map (select-keys params-map raw-param-ks)
        params-map     (apply dissoc params-map raw-param-ks)
        params-uri     (build-url-params-base uri params-map)
        raw-params-uri (build-url-params-base params-uri raw-params-map)]
    raw-params-uri))

(comment

  (authorize-url
    {:auth0/response-type "code"
     :auth0/scope "openid profile"
     :auth0/redirect-uri "http://localhost:1111/login-user"})

  (logout-url
    {:auth0/return-to "http://localhost:1111/login"
     :auth0/federated true}))

;; TODO - redirect-uri is a MUST
;; TODO - check if the same is valid for scope: openid
;; TODO - spec for valid keys here: scope, state, audience, connection, response-type
(defn authorize-url
  "Must have param: redirect-uri
  Valid params: connection audience scope state response-type"
  ([params]
   (authorize-url @global-config params))
  ([{:as config :keys [:client-id]} params]
   (let [base-url       (base-url config)
         auth-url       (uri/path base-url "/authorize")
         param-auth-url (build-url-params
                          auth-url
                          (merge
                            params
                            (select-keys config [:client-id])))
         string-url     (-> param-auth-url uri/uri->map uri/map->string)]
     string-url)))

;; TODO - return-to is a MUST
;; TODO - is setClientId from the Java version needed?
(defn logout-url
  "Must have param: return-to
  Valid params: federated"
  ([params]
   (logout-url @global-config params))
  ([{:as config :keys [:client-id]} params]
   (let [base-url         (base-url config)
         logout-url       (uri/path base-url "/v2/logout")
         param-logout-url (build-url-params
                            logout-url
                            (merge
                              params
                              (select-keys config [:client-id])))
         string-url       (-> param-logout-url uri/uri->map uri/map->string)]
     string-url)))


;; TODO - refactor in utils, urls, requests

;; requests start from here

(def authorization-header "Authorization")
(def bearer "Bearer ")

(defn edit-if
  "'Edits' a value in an associative structure, where k is a
  key and f is a function that will take the old value if it exists
  and any supplied args and return the new value, and returns a new
  structure. If the value does not exist returns the structure supplied."
  [m k f & args]
  (if (contains? m k)
    (let [val (get m k)]
      (assoc m k (apply (partial f val) args)))
    m))

(def oauth-ks
  #{:auth0/grant-type})

(defn oauth-vals-edn->json [body]
  (let [edn-vals  (select-keys body oauth-ks)
        json-vals (into {} (for [[k v] edn-vals] [k (json/kw->json-attr v)]))
        body      (merge body json-vals)]
    body))

(defmethod client/coerce-response-body :auth0-edn [_ resp]
  (json/coerce-responce-body-to-auth0-edn resp))

;; TODO - move this in a different ns
(defn auth0-request [config path options]
  (let [base-url      (base-url config)
        user-info-url (uri/path base-url path)
        string-url    (-> user-info-url uri/uri->map uri/map->string)]
    (merge
      ;; TODO - getting EDN is cool, but in some cases JSON might be preferable - make this configurable
      {:url              string-url
       :method           :get
       :content-type     :json
       :accept           :json
       :as               :auth0-edn
       :throw-exceptions false}
      (edit-if
        options
        :body
        (fn [body]
          (-> body
              oauth-vals-edn->json
              json/edn->json))))))

;; TODO - spec the map later
(defn exchange-code
  ([opts]
   (exchange-code @global-config opts))
  ([{:as   config
     :keys [:client-id :client-secret]}
    opts]
   (let [request (auth0-request
                   config
                   "/oauth/token"
                   {:method :post
                    :body   (merge
                              {:auth0/client-id     client-id
                               :auth0/client-secret client-secret}
                              opts)})]
     (client/request request))))

(comment
  ;; this is the login url used for testing - only openid scope
  "https://ignorabilis.auth0.com/authorize?response_type=code&scope=openid&client_id=wWiPfXbLs3OUbR74JpXXhF9jrWi3Sgd8&redirect_uri=http://localhost:1111/user"
  ;; this is the login url used for testing - openid profile email
  "https://ignorabilis.auth0.com/authorize?response_type=code&scope=openid+profile+email&client_id=wWiPfXbLs3OUbR74JpXXhF9jrWi3Sgd8&redirect_uri=http://localhost:1111/user"

  ;; this is the req for getting an access-token; just change the code
  (exchange-code
    {:auth0/code         "CODE_HERE"
     :auth0/redirect-uri "http://localhost:1111/"
     :auth0/grant-type   :auth0.grant-type/authorization-code}))

;; TODO - access-token is a MUST - needs spec
(defn user-info
  ([access-token]
   (user-info @global-config access-token))
  ([config access-token]
   (let [request (auth0-request
                   config
                   "/userinfo"
                   {:headers {authorization-header (str bearer access-token)}})]
     (client/request request))))
