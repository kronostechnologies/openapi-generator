# Equisoft OpenAPI fork

## Rebuild and publish to maven local
```
 ./mvnw clean install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Djacoco.skip=true
```

## Bump version

Edit pom.xml at the root and in all modules directories. This can be done with :

```
./mvnw versions:set -DnewVersion=7.1.0-equisoft1
./mvnw versions:commit
```

Edit the version for the gradle plugin in [gradle.properties](modules/openapi-generator-gradle-plugin/gradle.properties)

New development can be tested with `SNAPSHOT` version. Ex: `7.1.0-equisoft2-SNAPSHOT`


## Run against sample

Create a sample definition file bin/configs/FILE.yaml

```
./bin/generate-samples.sh bin/configs/FILE.yaml
```

## Publish to github maven repository

Configure your github credentials in ~/.m2/settings.xml with a github personal access token (PAT) with write:packages permission.

```
<?xml version="1.0"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>GITHUB_USERNAME</username>
      <password>GITHUB_PAT</password>
    </server>
  </servers>
</settings>
```

Deploy the maven part

```
./mvnw deploy -P equisoft-openapi-generator
```

Deploy the gradle plugin

```
cd modules/openapi-generator-gradle-plugin && ./gradlew publish
```
