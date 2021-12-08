```
docker run --rm \
  -p 5432:5432 \
  -e POSTGRES_DB=demo \
  -e POSTGRES_USER=demo \
  -e POSTGRES_PASSWORD=demo \
  bitnami/postgresql:11
```