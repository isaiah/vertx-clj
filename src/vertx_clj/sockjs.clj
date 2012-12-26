(ns vertx-clj.sockjs
  (:import [org.vertx.java.core.json JsonObject JsonArray]))

(declare bound-obj prefix)

(defn install-app [sockjs-server pfx handler]
  (.installApp sockjs-server (prefix pfx) handler))

(defn bridge [sockjs-server pfx inbound outbound]
  (let [in-permitted (bound-obj inbound)
        out-permitted (bound-obj outbound)
        eb-prefix (prefix pfx)]
    (.bridge sockjs-server eb-prefix in-permitted out-permitted)))

(defn- ^JsonArray bound-obj [clj-map]
  (let [permitted (JsonObject.)]
    (doseq [[k v] clj-map]
      (.putString permitted (name k) (str v)))
    (-> (JsonArray.) (.add permitted))))

(defn- ^JsonObject prefix [s]
  (-> (JsonObject.) (.putString "prefix" s)))
