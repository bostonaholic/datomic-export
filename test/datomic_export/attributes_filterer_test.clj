(ns datomic-export.attributes-filterer-test
  (:require [clojure.test :refer :all]
            [datomic.api :as d]
            [datomic-export.attributes-filterer :refer :all]
            [datomic-export.test-helpers :as helpers]))

(deftest filter-attributes-test
  (let [schema [{:db/id (d/tempid :db.part/db)
                 :db/ident :foo}
                {:db/id (d/tempid :db.part/db)
                 :db/ident :bar}
                {:db/id (d/tempid :db.part/db)
                 :db/ident :baz}]
        conn (helpers/db-setup schema)
        db (d/db conn)]

    (testing "filters all datomic attributes"
      (is (= #{:foo :bar :baz}
             (filter-attributes db nil nil))))

    (testing "exclusions"
      (is (= #{:bar :baz}
             (filter-attributes db #{:foo} nil))))

    (testing "inclusions"
      (is (= #{:bar :baz}
             (filter-attributes db nil #{:bar :baz}))))

    (testing "exclusions take precedence over inclusions"
      (is (= #{:bar :baz}
             (filter-attributes db #{:foo} #{:bar :baz}))))))
