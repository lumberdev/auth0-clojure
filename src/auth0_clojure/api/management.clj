(ns auth0-clojure.api.management
  (:require [auth0-clojure.utils.data-api :as data-api]
            [auth0-clojure.descriptors.management :as mgmt-desc]
            [clojure.string :as string]))

;; TODO-lib - extract these fns in a separate lib
;; since the actual descriptor could be something entirely different

;; TODO - some refactoring & cleanup
;; TODO - this needs to be smthng like create-request
(defn invoke [config options]
  (data-api/invoke-base
    (merge
      config
      {:api-descriptor mgmt-desc/api-descriptor})
    options))

(defn op [op-key]
  (-> mgmt-desc/api-descriptor
      :operations
      (get op-key)))

(defn op-doc [op-key]
  (let [{op-name :name
         :keys [doc doc-url http]} (op op-key)
        {:keys [path]} http
        op-title (string/join " " (map string/capitalize (string/split (name op-name) #"-")))
        path-params (seq (filter keyword? path))
        query-params (seq [:filter])]
    (string/join
      "\n"
      (cond-> ["##########################"
               op-title
               (str "(" op-name ")")
               "##########################"]
        doc
        (into [""
               doc])
        doc-url
        (into [""
               "Official docs:"
               doc-url])
        path-params
        (into [""
               "-------------------------"
               "Path Parameters:"
               (string/join ", " path-params)])
        query-params
        (into [""
               "-------------------------"
               "Query Parameters:"
               (string/join ", " query-params)])))))

(defn update-values [m f & args]
  (reduce
    (fn [r [k v]]
      (assoc r k (apply f v args)))
    {}
    m))

(defn ops-list []
  (update-values
    (:operations mgmt-desc/api-descriptor)
    :doc))