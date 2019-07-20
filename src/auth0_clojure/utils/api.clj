(ns auth0-clojure.utils.api)

(def api-description-test
  {:a {:name        :get-connection
       :description "local"
       :method      :get}})

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
