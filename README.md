# ...

```
cd service-api
mvn clean package -B
docker build -t sanaloveyou/serviceapi .
docker push sanaloveyou/serviceapi
cd ..

cd service-UI
npm run build
docker build -t sanaloveyou/serviceui .
docker push sanaloveyou/serviceui
cd ..

```

