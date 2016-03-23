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
                 :db.install/_attribute :db.part/db}
                {:db/id (d/tempid :db.part/db)
                 :db/ident :quxx
                 :db/valueType :db.type/ref
                 :db/cardinality :db.cardinality/one
                 :db.install/_attribute :db.part/db}]
        tempid1 (d/tempid :db.part/user -1)
        tempid2 (d/tempid :db.part/user -2)
        tempid3 (d/tempid :db.part/user -3)
        fixtures [{:db/id tempid1
                   :foo "foo" :bar "bar" :baz "baz"}

                  {:db/id tempid2
                   :bar "bar2" :baz "baz2"}

                  {:db/id tempid3
                   :baz "baz3" :quxx {:db/id tempid1}}]
        conn (helpers/db-setup schema)
        tx-result (d/with (d/db conn) fixtures)
        db (:db-after tx-result)
        id1 (d/resolve-tempid db (:tempids tx-result) tempid1)
        id2 (d/resolve-tempid db (:tempids tx-result) tempid2)
        id3 (d/resolve-tempid db (:tempids tx-result) tempid3)]

    (testing "pulls attributes of entities"
      (is (= (set (list {:db/id id1 :foo "foo" :bar "bar"}
                        {:db/id id2 :bar "bar2"}))
             (set (pull-entities db '(:foo :bar))))))

    (testing "pulls only :db/id of refs"
      (is (= (set (list {:db/id id1 :baz "baz"}
                        {:db/id id2 :baz "baz2"}
                        {:db/id id3 :baz "baz3" :quxx id1}))
             (set (pull-entities db '(:baz :quxx))))))))

(deftest unwrap-nested-ref-test
  (testing "a simple map"
    (is (= {:foo "foo" :bar "bar"}
           (unwrap-nested-refs {:foo "foo" :bar "bar"}))))

  (testing "a nested map"
    (is (= {:foo "foo" :bar 1234M}
           (unwrap-nested-refs {:foo "foo" :bar {:db/id 1234M}}))))

  (testing "multiple nested map"
    (is (= {:foo "foo" :bar 1234M :baz 5678M}
           (unwrap-nested-refs {:foo "foo" :bar {:db/id 1234M} :baz {:db/id 5678M}})))))
