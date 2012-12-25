(ns vertx.route-match.core
  (:require (vertx [core :as c]
               [route-matcher :as r])))

(def webroot "/home/isaiah/codes/java/vert.x/vertx-examples/src/main/clojure/src/vertx/route_match/")

(c/defverticle router-example
  (c/http-route 8080 "localhost"
                (r/get router "/details/:user/:id"
                         #(c/end-req % (str "User: "
                                            (c/params % "user") "ID: "
                                            (c/params % "id"))))
                (r/get-with-regexp router ".*"
                    #(c/send-file % (str webroot "index.html")))))
