(ns cljdb.util
  (:require [clojure.java.io :as io]))

(defn delete-directory-recursive
  [file]
  (when (.isDirectory file)
    (doseq [file-in-dir (.listFiles file)]
      (delete-directory-recursive file-in-dir)))
  (io/delete-file file))
