(ns grafter.error
  (:refer-clojure :exclude [type]))

(defprotocol IIsError
  (error? [this]))

(extend-protocol IIsError
  Object
  (error? [this]
    nil)

  Throwable
  (error? [this]
    this))

(defn rdf-error?
  "Returns true if the value is a Grafter error type or nil.  RDF errors are
  either an error or nil."
  [v]
  (or (nil? v)
      (satisfies? IIsError v)))

(defprotocol IError
  (message [this]
    "Get the error message string associated with this error.")
  (type [this]
    "Returns a string representing the type of error.")

  (stacktrace [this]
    "Returns the errors stacktrace if it has one, otherwise returns nil.")

  (cause [this]
    "Returns a sequence of causes for this error.  The last cause will be the
    primary error."))

;; TODO support serialization of stack traces and causes
(defrecord GrafterError [msg type cause context]
  IError
  (message [this]
    (:msg this))

  (type [this]
    (str (:type this)))

  Object
  (toString [this]
    (:msg this) ))

(extend-protocol IError
  Throwable

  (message [this]
    (.getMessage this))

  (type [this]
    (.getName (class this))))

;; TODO consider not doing it this way, and instead providing protection within
;; dataset functions try/catch style.  That way users don't have to wrap their
;; functions and they can choose to handle errors inside them how they wish.

(defn protect
  "Protect the given function from receiving Errors.  If any of arguments passed
  to it are an Error it will return an Error, rather than running the supplied
  function."
  [f]
  (fn [& args]
    (if-let [error-args (seq (filter error? args))]
      (->GrafterError (str "The function " f " received at least one unexpected Error.")
                      :unexpected-error-received
                      error-args)
      (apply f args))))
