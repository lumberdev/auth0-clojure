(ns auth0-clojure.utils.api)

(def api-description-test
  {:get-connection    {:description "Gets a connection"
                       :path        ["connections" :id]
                       :method      :get}
   :update-connection {:description "Updates a connection"
                       :path        ["connections" :id]
                       :method      :patch
                       :body        {}
                       ;; TODO - implement body validation
                       ;:body-validation nil
                       :headers     {:custom "random"}}})

(defn create-api-call [[_ {:keys [:name :description :method]}]]
  (let []
    `(defn ~(symbol name)
       ~description
       []
       ~description)))

(defmacro implement-api
  "Given an EDN API description generates functions
  which receive http method, parameters and other custom data
  and return an EDN request, which in turn can be used to invoke
  an endpoint."
  [api-description]
  (let [api-description (eval api-description)]
    `(do
       ~@(map create-api-call
              api-description))))
