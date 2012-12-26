(ns vertx.eventbus-bridge.server
  (:use [vertx-clj core http sockjs]
        [clojure.tools.logging :only [info]]))

(def webroot "/home/isaiah/codes/clojure/vertx-clj/examples/src/vertx/eventbus_bridge/")

(defverticle bridge-server
  (http-listen 8080 "localhost"
               (req-handler http-server [req]
                            (let [path (.path req)]
                              (cond
                               (= path "/") (do
                                              (info (str webroot "index.html"))
                                              (send-file req webroot "index.html"))
                               (= path "/vertxbus.js") (do
                                                         (info (str webroot path))
                                                         (send-file req webroot "vertxbus.js"))
                               :else (do
                                       (info (str webroot path))
                                       (end-req req "not found")))))
               ;;; Note: have to bind sockjs server later than req-handler, to put the request pipeline
               ;;; before the normal request handler
               (let [sockjs-server (.createSockJSServer vertx http-server)]
                 (bridge sockjs-server "/eventbus" {} {}))))
