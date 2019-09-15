(ns auth0-clojure.utils.data-api
  (:require [auth0-clojure.utils.requests :as req]
            [auth0-clojure.utils.urls :as urls]
            [clojure.string :as string]))

(defn api-descriptor-resolver [api-descriptor]
  (cond
    (map? api-descriptor) api-descriptor
    (fn? api-descriptor) (api-descriptor)
    :else (throw
            (ex-info
              "Api descriptor is not a map or function!"
              {:type :validation-failed}))))

(defn operation-resolver [api-descriptor operation]
  (let [operation-data (get-in
                         api-descriptor
                         [:operations operation])]
    (or operation-data
        (throw
          (ex-info
            (format "No such operation: %s!" operation)
            {:type      :operation-not-valid
             :operation operation})))))

(defn invoke-base [{:keys [:api-descriptor]
                    :as config}
                   {:keys [:operation
                           :path-params
                           :query-params
                           :body]}]
  (let [api-descriptor (api-descriptor-resolver api-descriptor)
        {{:keys [method path headers]} :http} (operation-resolver api-descriptor operation)
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
