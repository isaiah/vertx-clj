(ns vertx.upload.server
  (:require [vertx-clj.core :as v])
  (:use [clojure.tools.logging :only [info]]
        [vertx-clj http file]))

(def webroot "/home/isaiah/codes/java/vert.x/vertx-examples/src/main/clojure/src/vertx/upload/")

(defn uuid []
  (str (java.util.UUID/randomUUID)))

(defn gen-target-file []
  (str webroot "file-" (uuid) ".upload"))

(v/defverticle upload-server
  (http-listen
   8080 "localhost"
   (req-handler
    http-server [req]
       ;;; we first pause the request so we don't receive any data
       ;;; between now and when the file is opened
    (.pause req)
    (let [filename (gen-target-file)]
      (open-file
       vertx filename
       (async-result-handler [async-result]
                               (let [file (.result async-result)
                                     p (v/pump req (.getWriteStream file))
                                     start (System/currentTimeMillis)]
                                 (end-handler req
                                                (v/handler [_]
                                                           (close-file file
                                                                         (async-result-handler [_]
                                                                                                 (end-req req)
                                                                                                 (let [end (System/currentTimeMillis)]
                                                                                                   (info "uploaded" (.getBytesPumped p)
                                                                                                         "byte to" filename "in" (- end start) "ms"))))))
                                 (.start p)
                                 (.resume req))))))))
