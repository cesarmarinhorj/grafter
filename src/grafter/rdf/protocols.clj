(ns grafter.rdf.protocols
  "Grafter protocols and types for RDF processing"
  (:require [clojure.set :as set])
  (:import [org.openrdf.model Statement Value Resource Literal URI BNode ValueFactory]))

(defprotocol IStatement
  "An RDF triple"
  (subject [statement])
  (predicate [statement])
  (object [statement])
  (context [statement]))

(defprotocol ITripleWriteable
  (add-statement
    [this statement]
    [this graph statement])

  (add [this triples]
    [this graph triples]))

(defprotocol ITripleReadable
  "Use the higher level wrapper function statements if you just wish to read in some RDF.

  This protocol exists for implementers to hook in additional sources of statements.

  Takes a source of statements or triples and converts it into a seq
  of triples.

  A hash of options is passed to each implementation, they may be
  ignored or handled depending on the circumstance."
  (to-statements [this options]))

(defprotocol ITransactable
  "Low level protocol for transactions support.  Most users probably
  want to use grafter.rdf.sesame/with-transaction"
  (begin [repo] "Start a transaction")
  (commit [repo] "Commit a transaction")
  (rollback [repo] "Rollback a transaction"))

(defrecord Triple
    [s p o]
  IStatement
  (subject [s] (.s s))
  (predicate [s] (.p s))
  (object [s] (.o s))
  (context [s] nil))

(defrecord Quad
    [s p o c]
  IStatement
  (subject [s] (.s s))
  (predicate [s] (.p s))
  (object [s] (.o s))
  (context [s] (.c s)))

(extend-type clojure.lang.IPersistentVector
  IStatement
  (subject [this]
    (first this))
  (predicate [this]
    (second this))
  (object [this]
    (nth this 2)))

(comment
  (expand-subject [:rick
                   [:a :Person]
                   [:age 35]
                   [:married_to :katie]]))
