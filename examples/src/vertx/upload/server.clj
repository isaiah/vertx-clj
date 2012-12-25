(ns vertx.upload.server
  (:require [vertx.core :as v])
  (:use [clojure.tools.logging :only [info]]))

(def webroot "/home/isaiah/codes/java/vert.x/vertx-examples/src/main/clojure/src/vertx/upload/")

(defn uuid []
  (str (java.util.UUID/randomUUID)))

(defn gen-target-file []
  (str webroot "file-" (uuid) ".upload"))

(v/defverticle upload-server
  (v/http-listen
   8080 "localhost"
   (v/req-handler
    http-server [req]
       ;;; we first pause the request so we don't receive any data
       ;;; between now and when the file is opened
    (.pause req)
    (let [filename (gen-target-file)]
      (v/open-file
       vertx filename
       (v/async-result-handler [async-result]
                               (let [file (.result async-result)
                                     p (v/pump req (.getWriteStream file))
                                     start (System/currentTimeMillis)]
                                 (v/end-handler req
                                                (v/handler [_]
                                                           (v/close-file file
                                                                         (v/async-result-handler [_]
                                                                                                 (v/end-req req)
                                                                                                 (let [end (System/currentTimeMillis)]
                                                                                                   (info "uploaded" (.getBytesPumped p)
                                                                                                         "byte to" filename "in" (- end start) "ms"))))))
                                 (.start p)
                                 (.resume req))))))))
