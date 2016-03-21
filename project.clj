(defproject datomic-export "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.csv "0.1.3"]]
  :profiles {:dev {:dependencies [[com.datomic/datomic-free "0.9.5350"]]}
             :test {:dependencies [[com.datomic/datomic-free "0.9.5350"]]}}
  :aliases {"db:export" ["run" "-m" datomic-export.core]})
