(ns datomic-export.test-helpers
  (:require [datomic.api :as d]))

(def uri "datomic:mem://datomic-export-test")

(defn db-setup
  ([]
   (d/delete-database uri)
   (when (d/create-database uri)
     (d/connect uri)))

  ([schema]
   (db-setup schema nil))

  ([schema fixtures]
   (let [conn (db-setup)]
     @(d/transact conn (into schema fixtures))
     conn)))
