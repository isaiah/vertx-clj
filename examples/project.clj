(defproject vertx-examples "0.1.0-SNAPSHOT"
  :description "Vertx examples in clojure"
  :url "http://vertx.io"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/core.match "0.2.0-alpha11"]
                 [org.clojure/tools.logging "0.2.3"]
                 [org.vert-x/vertx-core "1.3.0.final"]
                 [org.vert-x/vertx-platform "1.3.0.final"]
                 ;[vertx-clj "0.1.1-SNAPSHOT"]
                 ]
  :source-paths ["src", "../src"]
  :plugins [[lein-vertx "0.1.0-SNAPSHOT"]]
  )
