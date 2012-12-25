(ns vertx.route-match.core
  (:require (vertx-clj [core :as c]
                       [route-matcher :as r]))
  (:use [vertx-clj http]))

(def webroot "/home/isaiah/codes/java/vert.x/vertx-examples/src/main/clojure/src/vertx/route_match/")

(c/defverticle router-example
  (http-route 8080 "localhost"
                (r/get router "/details/:user/:id"
                         #(end-req % (str "User: "
                                            (params % "user") " ID: "
                                            (params % "id"))))
                (r/get-with-regexp router ".*"
                    #(send-file % (str webroot "index.html")))))
