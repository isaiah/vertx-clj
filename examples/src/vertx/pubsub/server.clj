(ns vertx.pubsub.server
  (:use [vertx-clj core net]
        [clojure.tools.logging :only [info]])
  (:require [clojure.string :as s])
  (:import [org.vertx.java.core.buffer Buffer]
           [org.vertx.java.core.parsetools RecordParser]))

(defverticle pubsub-server
  (sock-listen 1234 "localhost"
               (connect-handler sock-server [sock]
                             (frame-handler
                              sock [frame]
                              (let [line (-> frame str s/trim)
                                    parts (.split line ",")]
                                (cond
                                 (.startsWith line "subscribe") (do
                                                                  (info "Topic is" (second parts))
                                                                  (-> vertx .sharedData (.getSet (second parts))
                                                                      (.add (.writeHandlerID sock))))
                                 (.startsWith line "unsubscribe") (-> vertx .sharedData (.getSet (second parts))
                                                                      (.remove (.writeHandlerID sock)))
                                 (.startsWith line "publish") (do
                                                                (info "publish to topic" (second parts))
                                                                (let [actor-ids (-> vertx .sharedData (.getSet (second parts)))]
                                                                  (doseq [actor actor-ids]
                                                                    (info "Sending to verticle")
                                                                    (-> vertx .eventBus (.publish actor (Buffer. (nth parts 2)))))))))))))
