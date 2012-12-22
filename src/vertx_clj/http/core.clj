(ns vertx-clj.http.core
  (:use vertx-clj.core)
  (:import [org.vertx.java.core.http RouteMatcher HttpServerRequest]
           [org.vertx.java.core Handler]))

(defmacro http-connect [port host & body]
  `(fn [vertx# _#]
     (let [http-client# (.createHttpClient vertx#)]
       (doto http-client# (.setPort ~port) (.setHost ~host))
       ((fn [~'vertx ~'client] ~@body) vertx# http-client#))))

(defmacro http-listen [port host & body]
  `(fn [vertx# _#]
     (let [http-server# (.createHttpServer vertx#)]
       ((fn [~'vertx ~'http-server] ~@body) vertx# http-server#)
       (.listen http-server# ~port ~host))))

(defmacro http-route
  "Sinatra like route matching"
  [port host & body]
  `(fn [vertx# container#]
     (let [http# (.createHttpServer vertx#)
           router# (RouteMatcher.)]
       ((fn [~'router] ~@body) router#)
       (-> http#
           (.requestHandler router#)
           (.listen ~port ~host)))))

(defmacro ws-handler [http-server expr & body]
  `(.websocketHandler ~http-server
                      (handler ~expr ~@body)))

(defmacro req-handler [http-server expr & body]
  `(.requestHandler ~http-server
                    (handler ~expr ~@body)))

(defn params [req key]
  "syntax sugar for get parameters"
  (-> req .params (.get key)))

(defn headers
  "Get the headers of the request"
  [^HttpServerRequest req]
  (.headers req))

(defmacro get-now [client path & body]
  "Send a get request and block before the body returned"
  `(.getNow ~client ~path
            (proxy [Handler] []
              (handle [resp#]
                (.bodyHandler resp#
                              (proxy [Handler] []
                                (handle [data#]
                                  ((fn [~'buf] ~@body) data#))))))))

(defn end-req
  "syntax sugar for end request"
  ([req buf]
     (.end (.response req) buf))
  ([req]
     (.end (.response req))))

(defn end-handler
  "call the callback when the request ends"
  [req callback]
  (.endHandler req callback))

(defn send-file [req file]
  "syntax sugar for sendFile"
  (.sendFile (.response req) file))
