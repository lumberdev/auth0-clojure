(ns auth0-clojure.core
  (:require [clojure.string :as string]
            [org.bovinegenius.exploding-fish :as uri]
            [clj-http.client :as client]
            [cheshire.core :as json])
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
    :federated (when v "")
    v))

(def raw-param-ks
  #{:redirect-uri})

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
                            {:response-type "code"}
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

;; TODO - these are some sort of utils, so move them later
(defn dashes->underscores [str]
  (string/replace str \- \_))
(defn underscores->dashes [str]
  (string/replace str \_ \-))

(defn keyword->json-attribute [k]
  (-> k
      name
      dashes->underscores))

(defn edn->json [edn]
  (json/generate-string
    edn
    {:key-fn keyword->json-attribute}))

(defn exchange-code
  ([code redirect-uri]
   (exchange-code @global-config code redirect-uri))
  ([{:as config :keys [:client-id :client-secret]} code redirect-uri]
   (let [base-url      (base-url config)
         user-info-url (uri/path base-url "/oauth/token")
         string-url    (-> user-info-url uri/uri->map uri/map->string)]
     (client/post
       string-url
       {:content-type :json
        :accept       :json
        :body (format
                "{\"client_id\": \"%s\", \"client_secret\": \"%s\", \"grant_type\": \"authorization_code\", \"code\": \"%s\", \"redirect_uri\": \"%s\"}"
                client-id
                client-secret
                code
                redirect-uri)}))))

;; TODO - getting base url,
;; then appending segment
;; then doing something else (optional)
;; then converting to string
;; is also a pattern; refactor later
;; TODO - access-token is a MUST
(defn user-info
  ([access-token]
   (user-info @global-config access-token))
  ([config access-token]
   (let [base-url      (base-url config)
         user-info-url (uri/path base-url "/userinfo")
         string-url    (-> user-info-url uri/uri->map uri/map->string)]
     (client/get
       string-url
       {:headers {authorization-header (str bearer access-token)}}))))
