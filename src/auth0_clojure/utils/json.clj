(ns auth0-clojure.utils.json
  (:require [clojure.string :as string]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [clj-http.util :as util])
  (:import (java.io EOFException BufferedReader)))

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

(defn edn->json [edn]
  (json/generate-string
    edn
    {:key-fn kw->str-val}))

(defn coerce-responce-body-to-auth0-edn [{:keys [body] :as resp}]
  ;; this snippet is based on client/decode-json-body, which is private
  (let [^BufferedReader br (io/reader (util/force-stream body))
        json-body          (try
                             (.mark br 1)
                             (let [^int first-char (try (.read br) (catch EOFException _ -1))]
                               (case first-char
                                 -1 nil
                                 (do (.reset br)
                                     ;; TODO - use parse-stream-strict?
                                     (json/parse-stream br (partial str-val->kw "auth0")))))
                             (finally (.close br)))]
    (assoc resp :body json-body)))