(ns auth0-clojure.api.management
  (:require [auth0-clojure.utils.data-api :as data-api]
            [auth0-clojure.descriptors.management :as mgmt-desc]))

;; TODO-lib - extract the fn that builds the request in a separate lib
(defn invoke [config options]
  (data-api/invoke-base
    (merge
      config
      {:api-descriptor mgmt-desc/api-descriptor})
    options))
