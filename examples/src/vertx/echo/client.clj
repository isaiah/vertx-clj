(ns vertx.echo.client
  (:use [vertx-clj core net]
        [clojure.tools.logging :only [info]])
  (:import (org.vertx.java.core.buffer Buffer)))

(defverticle echo-client
  (sock-connect 1234 "localhost"
                (handler [sock]
                         (.dataHandler sock
                                       (handler [buf]
                                                (info "net client receiving:" buf)))
                         (doseq [i (range 10)]
                           (let [s (str "hello" i "\n")]
                             (info "net client sending:" s)
                             (->> s (Buffer.) (.write sock)))))))
