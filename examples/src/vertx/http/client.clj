(ns vertx.http.client
  (:require [vertx-clj.core :as c])
  (:use [vertx-clj http])
  (:use [clojure.tools.logging :only [info]]))

(c/defverticle http-client
  (http-connect 8080 "localhost"
                  (get-now client "/"
                             (info buf))))
