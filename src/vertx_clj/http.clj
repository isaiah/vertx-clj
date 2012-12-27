(ns vertx-clj.http
  (:use vertx-clj.core)
  (:import [org.vertx.java.core.http RouteMatcher HttpServerRequest]
           [org.vertx.java.core Handler]))

(defmacro http-connect
  "Create a HttpClient instance."
  [port host & body]
  `(fn [vertx# _#]
     (let [http-client# (.createHttpClient vertx#)]
       (doto http-client# (.setPort ~port) (.setHost ~host))
       ((fn [~'vertx ~'client] ~@body) vertx# http-client#))))

(defmacro http-listen
  "Create a HttpServer instance that listens on the specified port and host.
   instance of Vertx and HttpServer is available in the body as vertx
   and http-server."
  [port host & body]
  `(fn [vertx# _#]
     (let [http-server# (.createHttpServer vertx#)]
       ((fn [~'vertx ~'http-server] ~@body) vertx# http-server#)
       (.listen http-server# ~port ~host))))

(defmacro http-route
  "Sinatra like route matching by ```RouterMatcher```, a ```RouterMatcher```
  instance is available for the body."
  [port host & body]
  `(fn [vertx# container#]
     (let [http# (.createHttpServer vertx#)
           router# (RouteMatcher.)]
       ((fn [~'router] ~@body) router#)
       (-> http#
           (.requestHandler router#)
           (.listen ~port ~host)))))

(defmacro ws-handler
  "Create a handler for websocket request."
  [http-server expr & body]
  `(.websocketHandler ~http-server
                      (handler ~expr ~@body)))

(defmacro req-handler
  "Create a handler for http request."
  [http-server expr & body]
  `(.requestHandler ~http-server
                    (handler ~expr ~@body)))

(defn params
  "Retrieve the value associated with the key from request parameters."
  [^HttpServerRequest req key]
  (-> req .params (.get key)))

(defn headers
  "Get the headers of the request."
  [^HttpServerRequest req]
  (.headers req))

(defmacro get-now
  "Send a get request and block before the body returned."
  [client path & body]
  `(.getNow ~client ~path
            (proxy [Handler] []
              (handle [resp#]
                (.bodyHandler resp#
                              (proxy [Handler] []
                                (handle [data#]
                                  ((fn [~'buf] ~@body) data#))))))))

(defn end-req
  "Syntax sugar for ending request."
  ([req buf]
     (.end (.response req) buf))
  ([req]
     (.end (.response req))))

(defn end-handler
  "Handler for request end event."
  [req callback]
  (.endHandler req callback))

(defn send-file
  "Http SendFile."
  [req & paths]
  (.sendFile (.response req) (apply str paths)))
