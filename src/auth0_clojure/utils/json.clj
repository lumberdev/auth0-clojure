(ns auth0-clojure.utils.json
  (:require [auth0-clojure.utils.edn :as edn]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [clj-http.util :as util])
  (:import (java.io EOFException BufferedReader)))

(defn edn->json [edn]
  (json/generate-string
    edn
    {:key-fn edn/kw->str-val}))

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
                                     (json/parse-stream br (partial edn/str-val->kw (name :auth0))))))
                             (finally (.close br)))]
    (assoc resp :body json-body)))