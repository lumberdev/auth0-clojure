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

(defn path->fn-args
  "Converts the path provided to a destructuring map. Each keyword is considered
  a parameter.
  Example:
  [\"connections\" :id] -> {id :id :as params}"
  [path]
  (let [args (->> path
                  ;; TODO-param-val - implement parameters validation using a hash map
                  (filter #(or (keyword? %) #_(map? %)))
                  (map #(identity [(symbol %) %]))
                  (into {}))
        args (assoc args :as 'params)]
    args))

(defn runtime-path
  "Matches params keys provided with their actual values at runtime."
  [path params]
  (map
    #(if-let [param (get params %)]
       ;; TODO-param-val - support hash map & validation here as well
       param
       %)
    path))

(defn create-api-call
  "Creates an API function based on an EDN description."
  [[name {:keys [:description :path]}]]
  (let []
    `(defn ~(symbol name)
       ~description
       [~(path->fn-args path)]
       (runtime-path ~path ~'params))))

(defmacro implement-api
  "Given an EDN API description generates functions
  which receive http method, parameters and other custom data
  and return an EDN request, which in turn can be used to invoke
  an endpoint."
  [api-description]
  (let [api-description (eval api-description)]
    `(do
       ~@(map create-api-call api-description))))
