(ns auth0-clojure.utils.json
  (:require [auth0-clojure.utils.edn :as edn]
            [cheshire.core :as json]))

(defn edn->json [edn]
  (json/generate-string
    edn
    {:key-fn edn/kw->str-val}))

(defn coerce-response-body-to-auth0-edn [{:keys [body] :as resp}]
  ;; Currently cheshire has an issue where if the body is an array
  ;; it will be parsed lazily; however the stream gets closed right after that
  ;; so most often than not an exception will be thrown
  ;; Using a stream + lazy seq might be suitable for the long user lists;
  ;; however for simplicity's sake just slurp & parse as string.
  ;; More about the issue - https://github.com/dakrone/clj-http/issues/489
  (assoc resp :body (-> body
                        slurp
                        (json/parse-string-strict (partial edn/str-val->kw (name :auth0))))))
