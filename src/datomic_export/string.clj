(ns datomic-export.string
  (require [clojure.string :as str]))

(defn- ends-in? [s c]
  (= (last s) c))

(defn pluralize [s count]
  (cond
    (= count 1) s
    (ends-in? s \y) (str (str/join (butlast s)) "ies")
    (ends-in? s \x) (str s "es")
    :else (str s "s")))
