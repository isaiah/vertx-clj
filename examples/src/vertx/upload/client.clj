(ns vertx.upload.client
  (:require [vertx-clj.core :as v])
  (:use [vertx-clj http file]
        [clojure.tools.logging :only [info]]
        [clojure.java.io :only [file]]))

(def webroot "/home/isaiah/codes/java/vert.x/vertx-examples/src/main/clojure/src/vertx/upload/")

(v/defverticle upload-client
  (http-connect
   8080 "localhost"
   (let [req (.put client "/"
                   (v/handler [response]
                              (info "File uploaded: " (.statusCode response))))
         filename (str webroot "upload.txt")]
     (-> req .headers (.put "Content-Length" (.length (file filename))))
     (open-file
      vertx filename
      (async-result-handler [async-result]
                              (let [f (.result async-result)
                                    p (v/pump (.getReadStream f) req)]
                                (.start p)
                                (end-handler (.getReadStream f)
                                               (v/handler [_]
                                                          (close-file f
                                                                        (async-result-handler [ar]
                                                                                                (if (nil? (.exception ar))
                                                                                                  (do
                                                                                                    (.end req)
                                                                                                    (info "Sent request")))))))))))))
