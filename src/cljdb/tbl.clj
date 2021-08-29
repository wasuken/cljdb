(ns cljdb.tbl
  (:require [cheshire.core :as json]
            [cheshire.parse :as parse]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn tbl-create
  [params env]
  (let [struct-path (format "./%s/%s/%s/struct.json" (:directory env) (:database env) (:from params))]
    (io/make-parents struct-path)
    (spit struct-path
          (json/generate-string params))
    (with-open [writer (io/writer (format "./%s/%s/%s/rows.csv" (:directory env) (:database env) (:from params)))]
      (csv/write-csv writer
                     [(:columns params)]))))

(defn tbl-drop
  [params env])

(defn tbl-alter
  [params env])

;;; TODO: join
;;; TODO: group by
;;; TODO: order by
;;; TODO: limit
;;; TODO: offset
(defn tbl-select
  [params env]
  (let [structure (json/decode (slurp (format "./%s/%s/%s/struct.json" (:directory env) (:database env) (:from params))))
        rows (csv/read-csv (io/reader (format "./%s/%s/%s/rows.csv" (:directory env) (:database env) (:from params))))]
    (filter #((:where params) %) rows)))

(defn tbl-delete
  [params env])

(defn tbl-insert
  [params env])

(defn tbl-update
  [params env])
