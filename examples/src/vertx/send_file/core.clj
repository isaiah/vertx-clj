(ns vertx.send-file.core
  (:use [clojure.tools.logging :only [info]])
  (:require [vertx.core :as c]))

(def webroot "/home/isaiah/codes/java/vert.x/vertx-examples/src/main/clojure/src/vertx/send_file/")

(c/defverticle sendfile-example
  (c/http-listen 8080 "localhost"
                 (let [path (.path req)]
                   (info (class path))
                   (info path)
                   (info (= path "/"))
                   (info (str webroot "index.html"))
                   (if (= path "/")
                     (c/send-file req (str webroot "index.html"))
                     (c/send-file req (str webroot path))))))
