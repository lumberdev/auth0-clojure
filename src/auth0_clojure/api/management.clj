(ns auth0-clojure.api.management
  (:require [auth0-clojure.utils.data-api :as data-api]
            [auth0-clojure.descriptors.management :as mgmt-desc]))

;; TODO - add exploration support, i.e. an fn to explore all available operations + a doc fn
;; TODO - add query params support, i.e. ?param1=xxx&param2=xxx
;; TODO - fix the array issue
;; TODO - finish the api descriptor

;; TODO-lib - extract the fn that builds the request in a separate lib
(defn invoke [config options]
  (data-api/invoke-base
    (merge
      config
      {:api-descriptor mgmt-desc/api-descriptor})
    options))
