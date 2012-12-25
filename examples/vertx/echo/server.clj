(ns vertx.echo.server
  (:use (vertx core)))

(defverticle echo-server
  (sock-listen 1234 "localhost"
               (connect-handler sock-server [sock]
                                (pump sock sock true))))
