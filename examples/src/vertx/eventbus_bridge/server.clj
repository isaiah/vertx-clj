(ns vertx.eventbus-bridge.server
  (:use [vertx-clj core http sockjs utils]
        [clojure.tools.logging :only [info]]))

(defverticle bridge-server
  (http-listen 8080 "localhost"
               (req-handler http-server [req]
                            (let [path (.path req)]
                              (cond
                               (= path "/") (send-file req (webroot "index.html"))
                               (= path "/vertxbus.js") (send-file req (webroot "vertxbus.js"))
                               :else (end-req req "not found"))))
               ;;; Note: have to bind sockjs server later than req-handler, to put the request pipeline
               ;;; before the normal request handler
               (let [sockjs-server (.createSockJSServer vertx http-server)]
                 (bridge sockjs-server "/eventbus" {} {}))))
