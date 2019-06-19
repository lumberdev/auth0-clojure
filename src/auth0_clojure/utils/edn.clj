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
