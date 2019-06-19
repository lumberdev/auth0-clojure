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

(defn kw->json-attr [k]
  (-> k
      name
      dashes->underscores))

(defn json-attr->kw [str]
  (->> str
       underscores->dashes
       (keyword "auth0")))

(defn edn->json [edn]
  (json/generate-string
    edn
    {:key-fn kw->json-attr}))

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
                                     (json/parse-stream br json-attr->kw))))
                             (finally (.close br)))]
    (assoc resp :body json-body)))