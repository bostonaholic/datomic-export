(ns datomic-export.core
  (:require [clojure.pprint :refer [pprint]]
            [datomic.api :as d]
            [datomic-export.attributes-filterer :refer [filter-attributes]]
            [datomic-export.csv-writer :as csv]
            [datomic-export.entity-puller :refer [pull-entities]]))

(defn- pluralize [s count]
  (cond
    (= count 1) s
    (= (last s) \y) (str (apply str (butlast s)) "ies")
    (= (last s) \x) (str s "es")
    :else (str s "s")))

(defn to-csv
  "Export a datomic database to a CSV file.

   Accepts the following options:

   --verbose

   :exclude - exclude these attributes, takes precedence over :include

   :include - include only these attributes"

  {:arglists '([datomic-uri file-url] [datomic-uri file-url & options]) :added "0.1.0"}

  [datomic-uri file-url & options]

  (let [{:keys [exclude include verbose]} options
        db (d/db (d/connect datomic-uri))
        attributes (filter-attributes db exclude include)
        entities (pull-entities db attributes)]
    (when verbose
      (println "=== Connected to" datomic-uri)
      (println "\n=== Found" (count attributes) (pluralize "attribute" (count attributes)))
      (pprint (sort attributes))
      (println "\n=== Found" (count entities) (pluralize "entity" (count entities)))
      (pprint entities)
      (println "\n=== Writing to" file-url))
    (csv/write file-url entities attributes)))

(defn -main [datomic-uri file-url & options]
  (let [verbose (some #{"--verbose"} options)
        options (remove #{"--verbose"} options)
        options (apply hash-map options)
        exclude (when (get options ":exclude")
                  (read-string (get options ":exclude")))
        include (when (get options ":include")
                  (read-string (get options ":include")))]
    (to-csv datomic-uri file-url :exclude exclude :include include :verbose verbose))
  (System/exit 0))
