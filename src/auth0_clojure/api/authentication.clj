(ns auth0-clojure.api.authentication
  (:require [auth0-clojure.utils.edn :as edn]
            [auth0-clojure.utils.urls :as urls]
            [auth0-clojure.utils.requests :as requests]
            [clj-http.client :as client]
            [org.bovinegenius.exploding-fish :as uri]))

;; TODO - do I need ^String, ^PersistentHashMap, etc.?

(def global-config
  (atom {}))

(defn set-config! [new-config]
  (reset! global-config new-config))

;; TODO - redirect-uri is a MUST
;; TODO - check if the same is valid for scope: openid
;; TODO - spec for valid keys here: scope, state, audience, connection, response-type
(defn authorize-url
  "Must have param: redirect-uri
  Valid params: connection audience scope state response-type"
  ([params]
   (authorize-url @global-config params))
  ([config
    {:as           params
     :keys         [:auth0/scope]
     custom-params :auth0/params}]
   (let [base-url       (urls/base-url config)
         auth-url       (uri/path base-url "/authorize")
         scope          (edn/parse-scope scope)
         ;; TODO - validate scope here
         param-auth-url (urls/build-url-params
                          auth-url
                          (merge
                            (select-keys
                              params
                              [:auth0/redirect-uri
                               :auth0/state
                               :auth0/audience
                               :auth0/connection
                               :auth0/response-type])
                            custom-params
                            {:auth0/scope (edn/unparse-scope scope)}
                            (select-keys config [:auth0/client-id])))
         string-url     (-> param-auth-url uri/uri->map uri/map->string)]
     string-url)))

;; TODO - return-to is a MUST
(defn logout-url
  "Must have param: return-to
  Valid params: federated"
  ([params]
   (logout-url @global-config params))
  ([config {:as params :keys [:auth0/set-client-id]}]
   (let [base-url         (urls/base-url config)
         logout-url       (uri/path base-url "/v2/logout")
         param-logout-url (urls/build-url-params
                            logout-url
                            (merge
                              (select-keys params [:auth0/return-to :auth0/federated])
                              (when set-client-id (select-keys config [:auth0/client-id]))))
         string-url       (-> param-logout-url uri/uri->map uri/map->string)]
     string-url)))

(comment

  (authorize-url
    {:auth0/response-type "code"
     :auth0/scope         "openid profile"
     :auth0/redirect-uri  "http://localhost:1111/login-user"})

  (logout-url
    {:auth0/return-to "http://localhost:1111/login"
     :auth0/federated true}))

;; SAML
;; the only param is connection and it is optional
(defn accept-saml-url
  ([params]
   (accept-saml-url @global-config params))
  ([{:as   config
     :keys [:auth0/client-id]}
    params]
   (let [base-url       (urls/base-url config)
         saml-url       (uri/path base-url (str "/samlp/" client-id))
         param-saml-url (urls/build-url-params saml-url params)
         string-url     (-> param-saml-url uri/uri->map uri/map->string)]
     string-url)))

(defn saml-metadata-url
  ([]
   (saml-metadata-url @global-config))
  ([{:as   config
     :keys [:auth0/client-id]}]
   (let [base-url   (urls/base-url config)
         saml-url   (uri/path base-url (str "/samlp/metadata/" client-id))
         string-url (-> saml-url uri/uri->map uri/map->string)]
     string-url)))

;; providing a connection is optional;
;; no connection -> SP initiated url
;; connection -> IdP initiated url
(defn sp-idp-init-flow-url
  ([params]
   (sp-idp-init-flow-url @global-config params))
  ([config params]
   (let [base-url       (urls/base-url config)
         saml-url       (uri/path base-url "/login/callback")
         param-saml-url (urls/build-url-params saml-url params)
         string-url     (-> param-saml-url uri/uri->map uri/map->string)]
     string-url)))

(comment

  ;; get default auth0 login screen
  (accept-saml-url
    {})

  ;; get the login screen for the respective connection
  (accept-saml-url
    {:auth0/connection "<samlp-connection-name>"})

  (saml-metadata-url))

;; requests start from here

(def authorization-header "Authorization")
(def bearer "Bearer ")

(comment
  ;; TODO - make each request return the actual map and force this pattern:
  (auth/request
    (auth/oauth-token
      {:auth0/code         "CODE_HERE"
       :auth0/redirect-uri "http://localhost:1111/"
       :auth0/grant-type   :auth0.grant-type/authorization-code}))
  ;; the rationale being that one can examine the actual request being made
  ;; if only auth/oauth-token is being called
  ;; it is also easier for testing
  )

;; TODO - spec the map later
(defn oauth-token
  ([opts]
   (oauth-token @global-config opts))
  ([{:as   config
     :keys [:auth0/client-id :auth0/client-secret]}
    opts]
   (let [request (requests/auth0-request
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
  (oauth-token
    {:auth0/code         "CODE_HERE"
     :auth0/redirect-uri "http://localhost:1111/"
     :auth0/grant-type   :auth0.grant-type/authorization-code}))

;; only :auth0/token is required
(defn oauth-revoke
  ([opts]
   (oauth-revoke @global-config opts))
  ([{:as   config
     :keys [:auth0/client-id
            :auth0/client-secret]}
    opts]
   (let [request (requests/auth0-request
                   config
                   "/oauth/revoke"
                   {:method :post
                    :body   (merge
                              {:auth0/client-id     client-id
                               :auth0/client-secret client-secret}
                              opts)})]
     (client/request request))))

;; TODO - access-token is a MUST - needs spec
(defn userinfo
  ([access-token]
   (userinfo @global-config access-token))
  ([config access-token]
   (let [request (requests/auth0-request
                   config
                   "/userinfo"
                   {:headers {authorization-header (str bearer access-token)}})]
     (client/request request))))

(defn- passwordless-start
  ([opts]
   (passwordless-start @global-config opts))
  ([{:as   config
     :keys [:auth0/client-id]}
    opts]
   (let [request (requests/auth0-request
                   config
                   "/passwordless/start"
                   {:method :post
                    :body   (merge
                              {:auth0/client-id client-id}
                              opts)})]
     (client/request request))))

(comment

  (passwordless-start
    {:auth0/connection "email"
     :auth0/email      "irina.yaroslavova@ignorabilis.com"
     ;:auth0/send       "link"           ;; can be link/code
     ;; TODO - validate this nested scope as well
     ;:auth0/authParams {:auth0/scope "openid"}
     }))

(defn- passwordless-verify
  ([opts]
   (passwordless-verify @global-config opts))
  ([{:as   config
     :keys [:auth0/client-id]}
    opts]
   (let [request (requests/auth0-request
                   config
                   "/passwordless/verify"
                   {:method :post
                    :body   (merge
                              {:auth0/client-id client-id}
                              opts)})]
     (client/request request))))

(defn signup
  ([opts]
   (signup @global-config opts))
  ([{:as   config
     :keys [:auth0/client-id]}
    opts]
   (let [request (requests/auth0-request
                   config
                   "/dbconnections/signup"
                   {:method :post
                    :body   (merge
                              {:auth0/client-id client-id}
                              opts)})]
     (client/request request))))

(comment

  ;; minimal
  (signup
    {:auth0/email      "irina@lumberdev.nyc"
     :auth0/password   "123"
     :auth0/connection "Username-Password-Authentication"})

  ;; all
  (signup
    {:auth0/email         "irina@lumberdev.nyc"
     :auth0/password      "123"
     :auth0/connection    "Username-Password-Authentication"
     :auth0/username      "ignorabilis" ;; ignored if not required by DB
     :auth0/given-name    "Irina"
     :auth0/family-name   "Stefanova"
     :auth0/name          "Irina Yaroslavova Stefanova"
     :auth0/nickname      "Iri"
     :auth0/picture       "https://image.shutterstock.com/image-vector/woman-profile-picture-vector-260nw-438753232.jpg"
     :auth0/user-metadata {:some-random "metadata"}})
  )

;; TODO - error responses return json; take care of the inconsistent API and convert those responses to json manually
(defn change-password
  ([opts]
   (change-password @global-config opts))
  ([{:as   config
     :keys [:auth0/client-id]}
    opts]
   (let [request (requests/auth0-request
                   config
                   "/dbconnections/change_password"
                   {:method :post
                    :accept :text
                    :as     :text
                    :body   (merge
                              {:auth0/client-id client-id}
                              opts)})]
     (client/request request))))

(comment
  (change-password
    {:auth0/email      "irina@lumberdev.nyc"
     :auth0/connection "Username-Password-Authentication"}))

(defn dynamically-register-client
  ([opts]
   (dynamically-register-client @global-config opts))
  ([{:as   config
     :keys [:auth0/client-id]}
    {:as   opts
     :keys [:auth0/redirect-uris]}]
   (let [request (requests/auth0-request
                   config
                   "/oidc/register"
                   {:method :post
                    :body   (merge
                              {:auth0/redirect-uris (or redirect-uris [])}
                              opts)})]
     (client/request request))))

(comment
  (dynamically-register-client
    {:auth0/client-name "New client"}))
