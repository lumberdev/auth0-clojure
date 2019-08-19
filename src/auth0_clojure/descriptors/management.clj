(ns auth0-clojure.descriptors.management)

(def api-descriptor
  {:version    "2.0"
   :metadata   {:endpoint-prefix "/api/v2/"}
   :operations {:get-connection    {:name          :get-connection
                                    :documentation "Gets a connection"
                                    :http          {:path          ["connections" :id]
                                                    :method        :get
                                                    :response-code 200}}
                :update-connection {:name          :update-connection
                                    :documentation "Updates a connection"
                                    :http          {:path          ["connections" :id]
                                                    :method        :patch
                                                    :headers       {:custom "random"}
                                                    :response-code 200}}}})
