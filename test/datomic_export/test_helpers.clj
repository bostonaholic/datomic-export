(ns datomic-export.test-helpers
  (:require [datomic.api :as d]))

(def uri "datomic:mem://test")

(defn db-setup [schema]
  (d/delete-database uri)
  (d/create-database uri)
  (let [conn (d/connect uri)]
    (d/transact conn schema)
    conn))
