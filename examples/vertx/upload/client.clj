(ns vertx.upload.client
  (:require [vertx.core :as v])
  (:use [clojure.tools.logging :only [info]]
        [clojure.java.io :only [file]]))

(def webroot "/home/isaiah/codes/java/vert.x/vertx-examples/src/main/clojure/src/vertx/upload/")

(v/defverticle upload-client
  (v/http-connect
   8080 "localhost"
   (let [req (.put client "/"
                   (v/handler [response]
                              (info "File uploaded: " (.statusCode response))))
         filename (str webroot "upload.txt")]
     (-> req .headers (.put "Content-Length" (.length (file filename))))
     (v/open-file
      vertx filename
      (v/async-result-handler [async-result]
                              (let [f (.result async-result)
                                    p (v/pump (.getReadStream f) req)]
                                (.start p)
                                (v/end-handler (.getReadStream f)
                                               (v/handler [_]
                                                          (v/close-file f
                                                                        (v/async-result-handler [ar]
                                                                                                (if (nil? (.exception ar))
                                                                                                  (do
                                                                                                    (.end req)
                                                                                                    (info "Sent request")))))))))))))
