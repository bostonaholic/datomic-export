(ns datomic-export.core
  (:require [clojure.pprint :refer [pprint]]
            [datomic.api :as d]
            [datomic-export.attributes-filterer :refer [filter-attributes]]
            [datomic-export.csv-writer :as csv]
            [datomic-export.entity-puller :refer [pull-entities]]))

(def ^:dynamic *verbose* false)

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

   --exclude - exclude these attributes, takes precedence over :include

   --include - include only these attributes"

  {:arglists '([datomic-uri file-url] [datomic-uri file-url & options]) :added "0.1.0"}

  [datomic-uri file-url & options]

  (let [{:keys [exclude include]} options
        db (d/db (d/connect datomic-uri))
        attributes (filter-attributes db exclude include)
        entities (pull-entities db attributes)]
    (when *verbose*
      (println "=== Connected to" datomic-uri)
      (println "\n=== Found" (count attributes) (pluralize "attribute" (count attributes)))
      (pprint (sort attributes))
      (println "\n=== Found" (count entities) (pluralize "entity" (count entities)))
      (pprint entities)
      (println "\n=== Writing to" file-url))
    (csv/write file-url entities attributes)))

(defn -main [datomic-uri file-url & options]
  (binding [*verbose* (some #{"--verbose"} options)]
    (let [opts (apply hash-map (remove #{"--verbose"} options))
          exclude (when (get opts "--exclude")
                    (read-string (get opts "--exclude")))
          include (when (get opts "--include")
                    (read-string (get opts "--include")))]
      (to-csv datomic-uri file-url :exclude exclude :include include)))
  (System/exit 0))
