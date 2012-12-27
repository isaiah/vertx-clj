(ns vertx-clj.utils
  (:import [org.vertx.java.core.json JsonObject]))

(defn ^JsonObject json
  "Convert a map to ```JsonObject```."
  [clj-map]
  (let [ret (JsonObject.)]
    (doseq [[k v] clj-map]
      (.putString ret (name k) (str v)))
    ret))
