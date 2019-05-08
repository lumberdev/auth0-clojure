(ns auth0-clojure.core
  (:require [clojure.string :as string]
            [org.bovinegenius.exploding-fish :as uri])
  (:import (com.auth0.client.auth AuthorizeUrlBuilder AuthAPI)
           (clojure.lang PersistentHashMap)))

;; ok, here we go!!!!

;; TODO - add spec for
(def default-domain "ignorabilis.auth0.com")
(def custom-domain "ignorabilis.auth0.com")
(def client-id "wWiPfXbLs3OUbR74JpXXhF9jrWi3Sgd8")
(def client-secret "0fkMFyofJiWinkwnl4Udcs_oAf7P4e6-WKTx8TAyC8Gh_CyrzytOylsD6bftrRoO")

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

;; can add spec here so that redirect uri is valid,
;; scope & state are strings, etc.
;; TODO - do I need ^String, ^PersistentHashMap, etc.?

(def https-scheme "https")

(defn create-base-url
  ([]
   (create-base-url @global-config))
  ([{:as config
     :keys [:default-domain
            :custom-domain]}]
   (let [{:as   base-url
          :keys [:scheme :host]} (uri/uri domain)]
     (if (and (or (= http-scheme scheme)
                  (= https-scheme scheme))
              (or (= default-domain host)
                  (= custom-domain host)))
       (uri/uri->map base-url)
       (uri/uri->map
         (uri/map->uri {:scheme https-scheme
                        :host   domain}))))))

(defn authorize-url [{:keys [:client-id :redirect-uri :scope :state]}]
  )
