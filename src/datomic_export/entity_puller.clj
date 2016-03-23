(ns datomic-export.entity-puller
  (:require [datomic.api :as d]))

(defn- unwrap-ref [[k v]]
  (list k (or (:db/id v) v)))

(defn unwrap-nested-refs [entity]
  (apply hash-map (mapcat unwrap-ref entity)))

(defn pull-entities [db attrs]
  (let [pull-attrs (partial d/pull db (conj attrs :db/id))
        entities (partial d/datoms db :aevt)]
    (map (comp unwrap-nested-refs pull-attrs)
         (distinct (map :e
                        (mapcat entities attrs))))))
