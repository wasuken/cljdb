(ns cljdb.db-test
  (:require [cljdb.db :as sut]
            [cljdb.util :refer [delete-directory-recursive]]
            [clojure.test :refer [is testing with-test use-fixtures]]
            [clojure.java.io :as io]))


(testing "Test database"
  (with-test
    (def params
      {:name "test"
       :columns {:hoge :text
                 :fuga :int}})
    (def env {:directory "test-db"})
    (testing "create"
      (with-test
        (def params
          {:name "test"
           :columns {:hoge :text
                     :fuga :int}
       ;; constは後程。
       ;; :constraint {
       ;;              }
           })
        (def env {:directory "test-db"})
        (sut/db-create params env)
        (is (.exists (io/as-file (format "./test-db/%s/" (:name params)))))))
    (testing "drop ops."
      (with-test
        (def params
          {:name "test"
           :columns {:hoge :text
                     :fuga :int}})
        (def env {:directory "test-db"})
        (sut/db-drop params env)
        (is (not (.exists (io/as-file (format "./test-db/%s/" (:name params))))))))))


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

(use-fixtures :once fixture)
