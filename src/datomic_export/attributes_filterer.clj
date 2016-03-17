(ns datomic-export.attributes-filterer
  (:require [clojure.set :as set]
            [datomic.api :as d]))

(def datomic-attributes #{:db/add :db/cardinality :db/code :db/doc :db/excise :db/fn :db/fulltext
                          :db/ident :db/index :db/isComponent :db/lang :db/noHistory :db/retract
                          :db/txInstant :db/unique :db/valueType
                          :db.alter/attribute
                          :db.bootstrap/part
                          :db.cardinality/many :db.cardinality/one
                          :db.excise/attrs :db.excise/before :db.excise/beforeT
                          :db.fn/cas :db.fn/retractEntity
                          :db.install/attribute :db.install/function :db.install/partition :db.install/valueType
                          :db.lang/clojure :db.lang/java
                          :db.part/db :db.part/tx :db.part/user
                          :db.sys/partiallyIndexed :db.sys/reId
                          :db.type/bigdec :db.type/bigint :db.type/boolean :db.type/bytes :db.type/double
                          :db.type/float :db.type/fn :db.type/instant :db.type/keyword :db.type/long :db.type/ref
                          :db.type/string :db.type/uri :db.type/uuid
                          :db.unique/identity :db.unique/value
                          :fressian/tag})

(defn- attributes [db]
  (map :v (d/datoms db :avet :db/ident)))

(defn- user-filters [s exclude include]
  (cond
    (not-empty exclude) (set/difference s (set exclude))
    (not-empty include) (set/intersection s (set include))
    :else s))

(defn filter-attributes
  ([db] (filter-attributes db #{} #{}))
  ([db exclude include]
   (-> (attributes db)
       set
       (set/difference datomic-attributes)
       (user-filters exclude include))))
