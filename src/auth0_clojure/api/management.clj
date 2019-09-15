(ns auth0-clojure.api.management
  (:require [auth0-clojure.utils.data-api :as data-api]
            [auth0-clojure.descriptors.management :as mgmt-desc]))

;; TODO - finish the api descriptor
;; TODO - fix the array issue
;; TODO - add exploration support, i.e. an fn to explore all available operations + a doc fn


;; TODO-lib - extract the fn that builds the request in a separate lib
(defn invoke [config options]
  (data-api/invoke-base
    (merge
      config
      {:api-descriptor mgmt-desc/api-descriptor})
    options))
