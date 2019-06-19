(ns auth0-clojure.utils.common)

(defn edit-if
  "'Edits' a value in an associative structure, where k is a
  key and f is a function that will take the old value if it exists
  and any supplied args and return the new value, and returns a new
  structure. If the value does not exist returns the structure supplied."
  [m k f & args]
  (if (contains? m k)
    (let [val (get m k)]
      (assoc m k (apply (partial f val) args)))
    m))
