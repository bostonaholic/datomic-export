(ns datomic-export.core
  (:require [clojure.pprint :refer [pprint]]
            [datomic.api :as d]
            [datomic-export.attributes-filterer :refer [filter-attributes]]))

(defn- pluralize [s count]
  (if (= count 1)
    s
    (str s "s")))

(defn to-csv
  "Export a datomic database to a CSV file.

   Accepts the following options:

   :exclude - exclude these attributes, takes precedence over :include

   :include - include only these attributes"

  {:arglists '([datomic-uri file-url] [datomic-uri file-url & options]) :added "0.1.0"}

  [datomic-uri file-url & options]

  (let [{:keys [exclude include]} options
        db (d/db (d/connect datomic-uri))
        attributes (filter-attributes db exclude include)]
    (println "=== Connected to" datomic-uri "\n")
    (println "=== Found" (count attributes) (pluralize "attribute" (count attributes)))
    (pprint (sort attributes))))
