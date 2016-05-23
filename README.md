# datomic-export

A Clojure library designed to export datomic data to flat files.

## Usage

### CLI

```
$ lein db:export datomic:dev://localhost:4334/database /tmp/file
```

### Clojure

```clojure
user> (require '[datomic-export.core :as export])
nil
user> (require '[datomic.api :as d])
nil
user> (def conn (d/connect "datomic:dev://localhost:4334/database"))
#'user/conn
user> (export/to-csv (d/db conn) "/tmp/file")
```

### Options

`--exclude (:company/name)` - Will not return entities with _any_ of these attributes. Takes precedence over `--include`.

`--include (:person/name :person/email)` - Only returns entities with _any_ of these attributes. And writes only these attributes.

`--verbose`

#### Complete example

```
$ lein db:export datomic:dev://localhost:4334/database /tmp/file --verbose --include (:person/name :person/email :person/company :company/name)
$ cat /tmp/file
:db/id,:company/name,:person/company,:person/email,:person/name
1234,,8765,john@foo.com,John Johnson
5678,,4321,alice@bar.com,Alice Alison
8765,The Foo Company,,,
4321,The Bar Company,,,
```

```clojure
user> (require '[datomic-export.core :as export])
nil
user> (require '[datomic.api :as d])
nil
user> (def conn (d/connect "datomic:dev://localhost:4334/database"))
#'user/conn
user> (export/to-csv (d/db conn) "/tmp/file" :verbose true :include '(:person/name :person/email :person/company :company/name))
```

## License

Copyright © 2016 Matthew Boston


Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
