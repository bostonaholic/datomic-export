(ns datomic-export.core
  (:require [clojure.pprint :refer [pprint]]
            [datomic.api :as d]
            [datomic-export.attributes-filterer :refer [filter-attributes]]
            [datomic-export.entity-finder :refer [find-entities]]))

(defn- pluralize [s count]
  (cond
    (= count 1) s
    (= (last s) \y) (str (apply str (butlast s)) "ies")
    (= (last s) \x) (str s "es")
    :else (str s "s")))

(defn to-csv
  "Export a datomic database to a CSV file.

   Accepts the following options:

   :exclude - exclude these attributes, takes precedence over :include

   :include - include only these attributes"

  {:arglists '([datomic-uri file-url] [datomic-uri file-url & options]) :added "0.1.0"}

  [datomic-uri file-url & options]

  (let [{:keys [exclude include]} options
        db (d/db (d/connect datomic-uri))
        attributes (filter-attributes db exclude include)
        entities (find-entities db attributes)]
    (println "=== Connected to" datomic-uri)
    (println "\n" "=== Found" (count attributes) (pluralize "attribute" (count attributes)))
    (pprint (sort attributes))
    (println "\n" "=== Found" (count entities) (pluralize "entity" (count entities)))
    (pprint (sort entities))))
