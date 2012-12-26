(ns vertx.sockjs.server
  (:use [vertx-clj core http sockjs]))

(def webroot "/home/isaiah/codes/clojure/vertx-clj/examples/src/vertx/sockjs/")

(defverticle sockjs-server
  (http-listen 8080 "localhost"
               (req-handler http-server [req]
                            (if (= (.path req) "/")
                              (send-file req webroot "index.html")
                              (end-req req "resource not found")))
               (let [sockjs-server (.createSockJSServer vertx http-server)]
                 (install-app sockjs-server "/testapp"
                              (handler [sock]
                                       (data-handler sock [data]
                                                     (.writeBuffer sock data) ; echo it back
                                                     ))))))
