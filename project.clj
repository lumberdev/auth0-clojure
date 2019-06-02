(defproject auth0-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [clj-http "3.10.0"]
                 [org.bovinegenius/exploding-fish "0.3.6"]
                 [cheshire "5.8.1"]
                 ;; TODO - remove this one
                 [com.auth0/auth0 "1.9.1"]]
  :repl-options {:port 1111})
