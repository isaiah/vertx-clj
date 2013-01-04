(ns vertx.route-match.core
  (:require (vertx-clj [core :as c]
                       [route-matcher :as r]))
  (:use [vertx-clj http utils]))

(c/defverticle router-example
  (http-route 8080 "localhost"
                (r/get router "/details/:user/:id"
                         #(end-req % (str "User: "
                                            (params % "user") " ID: "
                                            (params % "id"))))
                (r/get-with-regexp router ".*"
                    #(send-file % (webroot "index.html")))))
