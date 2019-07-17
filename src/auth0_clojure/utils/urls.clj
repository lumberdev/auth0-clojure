(ns auth0-clojure.utils.urls
  (:require [org.bovinegenius.exploding-fish :as uri]
            [auth0-clojure.utils.edn :as edn]))

(def https-scheme "https")

;; TODO - memoize base-url based on default & custom domains
(defn base-url [{:keys [:auth0/default-domain
                        :auth0/custom-domain]}]
  (uri/map->uri {:scheme https-scheme
                 :host   (or custom-domain default-domain)}))

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
            (edn/kw->str-val k)
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

(defn build-auth0-url
  ([config path]
   (build-auth0-url config path nil))
  ([config path params]
   (let [base-url       (base-url config)
         path-url       (uri/path base-url path)
         param-url      (if params (build-url-params path-url params) path-url)
         string-url     (-> param-url uri/uri->map uri/map->string)]
     string-url)))
