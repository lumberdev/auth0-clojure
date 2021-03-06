(ns auth0-clojure.utils.requests
  (:require [auth0-clojure.utils.common :as common]
            [auth0-clojure.utils.json :as json]
            [auth0-clojure.utils.edn :as edn]
            [auth0-clojure.utils.urls :as urls]
            [clj-http.client :as client]
            [org.bovinegenius.exploding-fish :as uri]))

(def authorization-header "Authorization")
(def bearer "Bearer ")

(defn bearer-header [access-token]
  {authorization-header (str bearer access-token)})

(def oauth-ks
  #{:auth0/grant-type})

(defn oauth-vals-edn->json [body]
  (let [edn-vals  (select-keys body oauth-ks)
        json-vals (into {} (for [[k v] edn-vals] [k (edn/kw->str-val v)]))
        body      (merge body json-vals)]
    body))

(defmethod client/coerce-response-body :auth0-edn [_ resp]
  (json/coerce-response-body-to-auth0-edn resp))

(defn auth0-request [config path options]
  (let [base-url    (urls/base-url
                      (select-keys
                        config
                        [:auth0/default-domain
                         :auth0/custom-domain]))
        request-url (uri/path base-url path)
        string-url  (-> request-url uri/uri->map uri/map->string)]
    (merge
      ;; TODO - getting EDN is cool, but in some cases JSON might be preferable - make this configurable
      {:url              string-url
       :method           :get
       :content-type     :json
       :accept           :json
       :as               :auth0-edn
       :throw-exceptions false}
      (common/edit-if
        options
        :body
        (fn [body]
          (-> body
              oauth-vals-edn->json
              json/edn->json))))))

(defn auth0-mgmt-request [{:keys [:auth0/mgmt-access-token] :as config} path options]
  (auth0-request
    config
    path
    (merge
      options
      {:headers (bearer-header mgmt-access-token)})))
