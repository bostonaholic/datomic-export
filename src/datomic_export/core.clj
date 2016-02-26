(ns datomic-export.core
  (:require [clojure.pprint :refer [pprint]]
            [datomic.api :as d]))

(def exclusions #{:db/add :db/cardinality :db/code :db/doc :db/excise :db/fn :db/fulltext
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

(defn- schema-attributes [db]
  (let [idents (map :v (d/datoms db :avet :db/ident))]
    (->> idents
         (remove exclusions)
         sort)))

(defn to-csv
  "Export a datomic database to a CSV file."
  {:arglists '([uri filepath]) :added "0.1.0"}
  [uri filepath]
  (let [conn (d/connect uri)
        db (d/db conn)
        attributes (schema-attributes db)]
    (println "=== Connected to" uri "\n")
    (println "=== Found" (count attributes) "attributes")
    (pprint attributes)))
