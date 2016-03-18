(ns datomic-export.entity-puller
  (:require [datomic.api :as d]))

(defn pull-entities [db attrs]
  (let [pull-attrs (partial d/pull db (conj attrs :db/id))
        entities (partial d/datoms db :aevt)]
    (map pull-attrs
         (distinct (map :e
                        (mapcat entities attrs))))))
