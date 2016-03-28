(ns datomic-export.core
  (:require [clojure.pprint :refer [pprint]]
            [clojure.string :as str]
            [datomic.api :as d]
            [datomic-export.attributes-filterer :refer [filter-attributes]]
            [datomic-export.csv-writer :as csv]
            [datomic-export.entity-puller :refer [pull-entities]]))

(defn- pluralize [s count]
  (cond
    (= count 1) s
    (= (last s) \y) (str (str/join (butlast s)) "ies")
    (= (last s) \x) (str s "es")
    :else (str s "s")))

(defn to-csv
  "Export a datomic database to a CSV file.

   Accepts the following options:

   :verbose

   :exclude - Will not return entities with any of these attributes. Takes precedence over ':include'.

   :include - Only returns entities with any of these attributes. And writes only these attributes."

  {:arglists '([db file-url] [db file-url & options]) :added "0.1.0"}

  [db file-url & options]

  (let [{:keys [verbose exclude include]} options
        attributes (filter-attributes db exclude include)
        entities (pull-entities db attributes)]
    (when verbose
      (println "\n=== Found" (count attributes) (pluralize "attribute" (count attributes)))
      (pprint (sort attributes))
      (println "\n=== Found" (count entities) (pluralize "entity" (count entities)))
      (pprint entities)
      (println "\n=== Writing to" file-url))
    (csv/write file-url entities attributes)))

(defn -main [datomic-uri file-url & options]
  (let [verbose (some #{"--verbose"} options)
        opts (apply hash-map (remove #{"--verbose"} options))
        exclude (when (get opts "--exclude")
                  (read-string (get opts "--exclude")))
        include (when (get opts "--include")
                  (read-string (get opts "--include")))
        db (d/db (d/connect datomic-uri))]
    (to-csv db file-url :verbose verbose :exclude exclude :include include))
  (System/exit 0))
