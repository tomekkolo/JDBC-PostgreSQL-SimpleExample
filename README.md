# immudb and jdbc
(Based on https://github.com/Rafael0360/JDBC-PostgreSQL-SimpleExample/tree/master)

```bash
docker pull codenotary/immudb
docker container run -it --rm -e IMMUDB_PGSQL_SERVER=true -p 5432:5432 -p 3322:3322 codenotary/immudb

mvn compile
mvn exec:java -Dexec.mainClass=Main
```