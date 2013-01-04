(ns vertx.send-file.core
  (:use [clojure.tools.logging :only [info]]
        [vertx-clj http]
        vertx-clj.utils)
  (:require [vertx-clj.core :as c]
            [clojure.java.io :as io]))

(c/defverticle sendfile-example
  (http-listen 8080 "localhost"
               (req-handler http-server [req]
                            (let [path (.path req)]
                              (if (= path "/")
                                (send-file req (webroot "index.html"))
                                (send-file req (webroot path)))))))
