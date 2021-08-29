(ns cljdb.db
  (:require [cheshire.core :as json]
            [cheshire.parse :as parse]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [cljdb.util :refer [delete-directory-recursive]]))

(defn db-create
  [params env]
  (let [struct-path (format "./%s/%s/struct.json" (:directory env) (:name params))]
    (io/make-parents struct-path)
    (spit struct-path
          (json/generate-string params))))

(defn db-drop
  [params env]
  ;; TODO: exists check and move temp directory.
  (delete-directory-recursive (delete-directory-recursive
                               (format "./%s/%s/" (:directory env) (:name params)))))

(defn db-alter
  [params env])
