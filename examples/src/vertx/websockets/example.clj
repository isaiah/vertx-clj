(ns vertx.websockets.example
  (:require [vertx-clj.core :as v])
  (:use [vertx-clj http utils]))

(v/defverticle websockets-example
  (http-listen 8080 "localhost"
                 (ws-handler http-server [ws]
                            (if (= (.path ws) "/myapp" )
                              (v/data-handler ws [data]
                                              (.writeTextFrame ws (str data))) ; write it back
                              (.reject ws)))
                 (req-handler http-server [req]
                            (if (= (.path req) "/")
                              (-> req .response (.sendFile (webroot "ws.html")))))))
