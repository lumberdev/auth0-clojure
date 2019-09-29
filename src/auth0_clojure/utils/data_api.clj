(ns auth0-clojure.utils.data-api
  (:require [auth0-clojure.utils.requests :as req]
            [auth0-clojure.utils.urls :as urls]
            [clojure.string :as string]))

;; TODO-lib - extract these fns in a separate lib
;; since the actual descriptor could be something entirely different

(defn api-descriptor-resolver [api-descriptor]
  (cond
    (map? api-descriptor) api-descriptor
    (fn? api-descriptor) (api-descriptor)
    :else (throw
            (ex-info
              "Api descriptor is not a map or function!"
              {:type :validation-failed}))))

(defn op-resolver [api-descriptor operation]
  (let [operation-data (get-in
                         api-descriptor
                         [:operations operation])]
    (or operation-data
        (throw
          (ex-info
            (format "No such operation: %s!" operation)
            {:type      :operation-not-valid
             :operation operation})))))

(defn op-request [api-descriptor
                  config
                   {:keys [:operation
                           :path-params
                           :query-params
                           :body]}]
  (let [api-descriptor (api-descriptor-resolver api-descriptor)
        {{:keys [method path headers]} :http} (op-resolver api-descriptor operation)
        path-prefix    (get-in api-descriptor [:metadata :endpoint-prefix])
        path-parts     (map
                         #(if (keyword? %)
                            ;; TODO - throw exception if no such path param???
                            (get path-params %)
                            %)
                         path)
        path           (str path-prefix (string/join "/" path-parts))
        ;; TODO - throw exception if no such query param???
        path           (if query-params
                         (urls/build-url-params path query-params)
                         path)
        req-data       (merge
                         {:method  method
                          :headers headers}
                         (when body
                           {:body body}))]
    ;; TODO-lib - extract the fn that builds the request in a separate lib
    (req/auth0-mgmt-request
      config
      path
      req-data)))

(defn update-values [m f & args]
  (reduce
    (fn [r [k v]]
      (assoc r k (apply f v args)))
    {}
    m))

(defn ops-list [api-descriptor]
  (update-values
    (:operations api-descriptor)
    :doc))

(defn op-data [api-descriptor op-key]
  (-> api-descriptor
      :operations
      (get op-key)))

(defn op-doc [api-descriptor op-key]
  (let [{op-name :name
         :keys [doc doc-url http]} (op-data api-descriptor op-key)
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
