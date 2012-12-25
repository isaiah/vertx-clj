(ns vertx.http.client
  (:require [vertx.core :as c])
  (:use [clojure.tools.logging :only [info]]))

(c/defverticle http-client
  (c/http-connect 8080 "localhost"
                  (c/get-now client "/"
                             (info buf))))
