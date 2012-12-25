(ns vertx.websockets.example
  (:require [vertx-clj.core :as v])
  (:use [vertx-clj http]))

(def webroot "/home/isaiah/codes/java/vert.x/vertx-examples/src/main/clojure/src/vertx/websockets/")

(v/defverticle websockets-example
  (http-listen 8080 "localhost"
                 (ws-handler http-server [ws]
                            (if (= (.path ws) "/myapp" )
                              (v/data-handler ws [data]
                                              (.writeTextFrame ws (str data))) ; write it back
                              (.reject ws)))
                 (req-handler http-server [req]
                            (if (= (.path req) "/")
                              (-> req .response (.sendFile (str webroot "ws.html")))))))
