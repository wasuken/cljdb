(ns cljdb.tbl
  (:require [cheshire.core :as json]
            [cheshire.parse :as parse]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn tbl-create
  [params env]
  (let [tbl-path (format "./%s/%s/%s" (:directory env) (:use env) (:from params))]
    (when (.exists (io/as-file tbl-path))
      (throw (ex-info "Already exists database." {})))
    (io/make-parents (format "%s/a" tbl-path))
    (spit (format "%s/struct.json" tbl-path)
          (json/generate-string params))
    (with-open [writer (io/writer (format "%s/rows.csv" tbl-path))]
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
