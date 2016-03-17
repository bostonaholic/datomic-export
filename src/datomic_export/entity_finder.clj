(ns datomic-export.entity-finder
  (:require [datomic.api :as d]))

(defmulti find-entities (fn [arg1 arg2] (type arg2)))

(defmethod find-entities clojure.lang.Keyword [db attribute]
  (distinct (map :e (d/datoms db :aevt attribute))))

(defmethod find-entities clojure.lang.PersistentList [db attributes]
  (distinct (mapcat (partial find-entities db) attributes)))

(defmethod find-entities clojure.lang.PersistentHashSet [db attributes]
  (find-entities db (seq attributes)))
