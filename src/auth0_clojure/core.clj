(ns auth0-clojure.core
  (:require [clojure.string :as string]
            [org.bovinegenius.exploding-fish :as uri])
  (:import (com.auth0.client.auth AuthorizeUrlBuilder AuthAPI LogoutUrlBuilder)))

;; TODO - probably use ns kw, like :auth0/client-id or ::client-id
;; TODO - bump clojure to 1.10
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
                          (.authorizeUrl "https://ignorabilis.com/login-user")
                          (.withScope "openid profile email"))
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

(defn dissoc-nil [map]
  (apply
    dissoc
    map
    (for [[k v] map
          :when (nil? v)]
      k)))

;; Generate query param values like `federated` with an equal sign;
;; it's the safe bet since Auth0 is doing the same
(defn parse-value [k v]
  (prn k v)
  (case k
    :federated (when v "")
    v))

(defn build-url-params [uri-url params-map]
  ;; remove any nil values, otherwise they get added to query params without an equal sign
  (let [params-map (dissoc-nil params-map)]
    (reduce
      (fn [auth-url [k v]]
        (uri/param
          auth-url
          (encode-underscore-key k)
          (parse-value k v)))
      uri-url
      params-map)))

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

