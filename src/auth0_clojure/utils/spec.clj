(ns auth0-clojure.utils.spec
  (:require [clojure.spec.alpha :as s]))

(def valid-scope-vals
  #{:auth0.scope/openid
    :auth0.scope/profile
    :auth0.scope/email
    :auth0.scope/address
    :auth0.scope/phone})

(defn valid-scope? [scope]
  (contains? valid-scope-vals scope))

(s/def :auth0/scope (s/* valid-scope?))

