(ns cljdb.db-test
  (:require [cljdb.db :as sut]
            [cljdb.util :refer [delete-directory-recursive]]
            [clojure.test :refer [is deftest use-fixtures]]
            [clojure.java.io :as io]))

(def params {:name "test"
             :columns {:hoge :text
                       :fuga :int}})
(def env {:directory "test-db"})

(deftest test-create
  (sut/db-create params env)
  (is (.exists (io/as-file (format "./test-db/%s/" (:name params))))))

(deftest test-drop
  (sut/db-create params env)
  (sut/db-drop params env)
  (is (not (.exists (io/as-file (format "./test-db/%s/" (:name params)))))))

(deftest test-create-failed
  (sut/db-create params env)
  (is (thrown? clojure.lang.ExceptionInfo (sut/db-create params env))))

(deftest test-drop-failed
  (is (thrown? clojure.lang.ExceptionInfo (sut/db-drop params env))))

(defn setup
  []
  (when (.exists (io/as-file "./test-db/"))
    (delete-directory-recursive (io/as-file "./test-db/"))))

(defn cleanup
  []
  (when (.exists (io/as-file "./test-db/"))
    (delete-directory-recursive (io/as-file "./test-db/"))))

(defn fixture [test-func]
  (setup)
  (test-func)
  (cleanup))

(use-fixtures :each fixture)
