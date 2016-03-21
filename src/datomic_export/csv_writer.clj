(ns datomic-export.csv-writer
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn headers [attributes]
  (mapv str attributes))

(defn entity->vec [attributes entity]
  (mapv entity attributes))

(defn write [file-url entities attributes]
  (let [attrs (into [:db/id] (sort (remove #{:db/id} attributes)))
        headers (headers attrs)
        data (map (partial entity->vec attrs) entities)]
    (with-open [file-writer (io/writer file-url)]
      (csv/write-csv file-writer (conj data headers)))))
