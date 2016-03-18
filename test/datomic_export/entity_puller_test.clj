(ns datomic-export.entity-puller-test
  (:require [clojure.test :refer :all]
            [datomic.api :as d]
            [datomic-export.entity-puller :refer :all]
            [datomic-export.test-helpers :as helpers]))

(deftest pull-entities-test
  (let [schema [{:db/id (d/tempid :db.part/db)
                 :db/ident :foo
                 :db/valueType :db.type/string
                 :db/cardinality :db.cardinality/one
                 :db.install/_attribute :db.part/db}
                {:db/id (d/tempid :db.part/db)
                 :db/ident :bar
                 :db/valueType :db.type/string
                 :db/cardinality :db.cardinality/one
                 :db.install/_attribute :db.part/db}
                {:db/id (d/tempid :db.part/db)
                 :db/ident :baz
                 :db/valueType :db.type/string
                 :db/cardinality :db.cardinality/one
                 :db.install/_attribute :db.part/db}]
        tempid1 (d/tempid :db.part/user -1)
        tempid2 (d/tempid :db.part/user -2)
        fixtures [{:db/id tempid1
                   :foo "foo" :bar "bar" :baz "baz"}

                  {:db/id tempid2
                   :bar "bar2" :baz "baz2"}]
        conn (helpers/db-setup schema)
        tx-result (d/with (d/db conn) fixtures)
        db (:db-after tx-result)
        id1 (d/resolve-tempid db (:tempids tx-result) tempid1)
        id2 (d/resolve-tempid db (:tempids tx-result) tempid2)]

    (testing "pulls attributes of entities"
      (is (= (set (list {:db/id id1 :foo "foo" :bar "bar"}
                        {:db/id id2 :bar "bar2"}))
             (set (pull-entities db '(:foo :bar))))))))
