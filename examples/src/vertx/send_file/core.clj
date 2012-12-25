(ns vertx.send-file.core
  (:use [clojure.tools.logging :only [info]]
        [vertx-clj http])
  (:require [vertx-clj.core :as c]))

(def webroot "/home/isaiah/codes/java/vert.x/vertx-examples/src/main/clojure/src/vertx/send_file/")

(c/defverticle sendfile-example
  (http-listen 8080 "localhost"
               (req-handler http-server [req]
                            (let [path (.path req)]
                              (if (= path "/")
                                (send-file req (str webroot "index.html"))
                                (send-file req (str webroot path)))))))
