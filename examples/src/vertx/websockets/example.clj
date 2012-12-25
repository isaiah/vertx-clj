(ns vertx.websockets.example
  (:require [vertx.core :as v]))

(def webroot "/home/isaiah/codes/java/vert.x/vertx-examples/src/main/clojure/src/vertx/websockets/")

(v/defverticle websockets-example
  (v/http-listen 8080 "localhost"
                 (v/ws-handler http-server [ws]
                            (if (= (.path ws) "/myapp" )
                              (v/data-handler ws [data]
                                              (.writeTextFrame ws (str data))) ; write it back
                              (.reject ws)))
                 (v/req-handler http-server [req]
                            (if (= (.path req) "/")
                              (-> req .response (.sendFile (str webroot "ws.html")))))))
