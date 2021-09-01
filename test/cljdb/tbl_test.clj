(ns cljdb.tbl-test
  (:require [cljdb.tbl :as sut]
            [cljdb.db :refer [db-create]]
            [cljdb.util :refer [delete-directory-recursive]]
            [clojure.test :refer [is deftest use-fixtures]]
            [clojure.java.io :as io]))

(def env {:directory "test-db"
          :use "test"})

(deftest test-create
  (let [params {:from "hgoe" :columns ["a" "b" "c"]}
        tbl-path (format "./%s/%s/%s" (:directory env) (:use env) (:from params))]
    (sut/tbl-create params env)
    (is (.exists (io/as-file (format "%s/struct.json" tbl-path))))
    (is (.exists (io/as-file (format "%s/rows.csv" tbl-path))))))
(deftest test-create-failed
  (let [params {:from "hgoe" :columns ["a" "b" "c"]}]
    (sut/tbl-create params env)
    (is (thrown? clojure.lang.ExceptionInfo (sut/tbl-create params env)))))
(deftest test-drop
  (let [params {:from "hgoe" :columns ["a" "b" "c"]}
        tbl-path (format "./%s/%s/%s" (:directory env) (:use env) (:from params))]
    (sut/tbl-create params env)
    (sut/tbl-drop params env)
    (is (not (.exists (io/as-file (format "%s/struct.json" tbl-path)))))
    (is (not (.exists (io/as-file (format "%s/rows.csv" tbl-path)))))
    (is (not (.exists (io/as-file tbl-path))))))
(deftest test-drop-failed
  (let [params {:from "hgoe" :columns ["a" "b" "c"]}]
    (is (thrown? clojure.lang.ExceptionInfo (sut/tbl-drop params env)))))
;; (deftest test-alter)
;; (deftest test-alter-failed)

;; (deftest test-insert)
;; (deftest test-insert-failed)
;; (deftest test-delete)
;; (deftest test-delete-failed)
;; (deftest test-update)
;; (deftest test-update-failed)

(defn setup
  []
  (when (.exists (io/as-file "./test-db/"))
    (delete-directory-recursive (io/as-file "./test-db/")))
  (db-create {:name "test"
              :columns {:hoge :text
                        :fuga :int}} env))

(defn cleanup
  []
  (when (.exists (io/as-file "./test-db/"))
    (delete-directory-recursive (io/as-file "./test-db/"))))

(defn fixture [test-func]
  (setup)
  (test-func)
  (cleanup))

(use-fixtures :each fixture)
