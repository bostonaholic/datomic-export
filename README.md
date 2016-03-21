# datomic-export

A Clojure library designed to export datomic data to flat files.

## Usage

### CLI

```
$ lein db:export datomic:dev://localhost:4334/database /tmp/file
```

### Clojure

```clojure
(require '[datomic-export.core :as export])
(export/to-csv "datomic:dev://localhost:4334/database" "/tmp/file")
```

### Options

`--exclude (:company/name)` - Will not return entities with _any_ of these attributes. Takes precedence over `--include`.

`--include (:person/name :person/email)` - Only returns entities with _any_ of these attributes. And writes only these attributes.

`--verbose`

#### Complete example

```
$ lein db:export datomic:dev://localhost:4334/database /tmp/file --verbose --include (:person/name :person/email)
```

## License

Copyright © 2016 Matthew Boston


Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
