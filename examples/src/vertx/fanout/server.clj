(ns vertx.fanout.server
  (:use [vertx-clj core net]))

(defverticle fanout-server
  (sock-listen 1234 "localhost"
               (let [conns (-> vertx .sharedData (.getSet "conns"))]
                 (connect-handler sock-server [sock]
                                  (.add conns (.writeHandlerID sock))
                                  (data-handler sock [buf]
                                                (doseq [actor conns]
                                                  (-> vertx .eventBus (.publish actor buf))))
                                  (closed-handler sock [_] ; for SimpleHandler pass _ as parameter
                                                  (.remove conns (.writeHandlerID sock)))))))
