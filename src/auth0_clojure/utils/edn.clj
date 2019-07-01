(ns auth0-clojure.utils.edn
  (:require [clojure.string :as string]))

(defn dashes->underscores [str]
  (string/replace str \- \_))

(defn underscores->dashes [str]
  (string/replace str \_ \-))

(defn kw->str-val [k]
  (-> k
      name
      dashes->underscores))

(defn str-val->kw
  ([str]
   (str-val->kw nil str))
  ([ns str]
   (let [kw-fn (if ns
                 (partial keyword ns)
                 keyword)]
     (->> str
          underscores->dashes
          kw-fn))))

(defn scope-kw [str]
  (keyword (name :auth0.scope) str))

(defn ensure-ns [scope]
  (map
    (fn [sc-val]
      (scope-kw (name sc-val)))
    scope))

(defn parse-scope [scope]
  (if (string? scope)
    (map
      (fn [sc-val]
        (scope-kw sc-val))
      (string/split scope #"s"))
    (ensure-ns scope)))

(defn unparse-scope [scope-coll]
  (string/join " " (map name scope-coll)))
