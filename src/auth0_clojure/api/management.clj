(ns auth0-clojure.api.management
  (:require [auth0-clojure.utils.data-api :as data-api]
            [auth0-clojure.descriptors.management :as mgmt-desc]))

(defn op-request [config options]
  (data-api/op-request
    mgmt-desc/api-descriptor
    config
    options))

(defn ops-list []
  (data-api/ops-list
    mgmt-desc/api-descriptor))

(defn op-data [op-key]
  (data-api/op-data
    mgmt-desc/api-descriptor
    op-key))

(defn op-doc [op-key]
  (data-api/op-doc
    mgmt-desc/api-descriptor
    op-key))
