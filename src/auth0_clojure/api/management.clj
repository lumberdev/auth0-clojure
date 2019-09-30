(ns auth0-clojure.api.management
  (:require [auth0-clojure.utils.data-api :as data-api]
            [auth0-clojure.descriptors.management :as mgmt-desc]))

(defn op-request [config options]
  (data-api/op-request
    mgmt-desc/api-descriptor
    config
    options))

(defn ops
  "A map of all available operations and their short description."
  []
  (data-api/ops
    mgmt-desc/api-descriptor))

(defn op-data
  "Given an operation name (keyword) returns a map containing the operation data."
  [op-key]
  (data-api/op-data
    mgmt-desc/api-descriptor
    op-key))

(defn op-doc
  "Given an operation name (keyword) returns a doc string."
  [op-key]
  (data-api/op-doc
    mgmt-desc/api-descriptor
    op-key))
