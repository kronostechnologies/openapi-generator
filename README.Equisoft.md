## Rebuild and publish to maven local
```
 ./mvnw install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Djacoco.skip=true
```

## Bump version

Edit pom.xml at the root and in all modules directories. Look for "equisoft"

## Run against sample

Create a sample definition file bin/configs/FILE.yaml

```
./bin/generate-samples.sh bin/configs/FILE.yaml
```
