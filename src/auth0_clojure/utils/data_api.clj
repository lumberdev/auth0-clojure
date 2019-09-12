(ns auth0-clojure.utils.data-api
  (:require [auth0-clojure.utils.requests :as req]
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
                           :body]}]
  (let [api-descriptor (api-descriptor-resolver api-descriptor)
        {{:keys [method path headers]} :http} (operation-resolver api-descriptor operation)
        path-prefix (get-in api-descriptor [:metadata :endpoint-prefix])
        path        (map
                      #(if (keyword? %)
                         ;; TODO - throw exception if no such param???
                         (get path-params %)
                         %)
                      path)
        path        (str path-prefix (string/join "/" path))
        req-data    (merge
                      {:method  method
                       :headers headers}
                      (when body
                        {:body body}))]
    ;; TODO-lib - extract the fn that builds the request in a separate lib
    (req/auth0-mgmt-request
      config
      path
      req-data)))
