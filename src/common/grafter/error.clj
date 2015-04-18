(ns grafter.error
  (:refer-clojure :exclude [type]))

(defprotocol IIsError
  (error? [this]))

(extend-protocol IIsError
  Object
  (error? [this]
    false)

  Throwable
  (error? [this]
    true))

(defn rdf-error?
  "Returns true if the value is a Grafter error type or nil."
  [v]
  (or (nil? v)
      (satisfies? IIsError v)))

(defprotocol IError
  (message [this]
    "Get the error message string associated with this error.")
  (type [this]
    "Returns a string representing the type of error."))

;; TODO support serialization of stack traces and causes
(defrecord GrafterError [msg type cause]
  IError
  (message [this]
    (:msg this))

  (type [this]
    (str (:type this))))

(extend-protocol IError
  Throwable

  (message [this]
    (.getMessage this))

  (type [this]
    (.getName (class this))))
