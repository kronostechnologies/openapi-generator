# Equisoft OpenAPI fork

## Rebuild and publish to maven local
```
 ./mvnw clean install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Djacoco.skip=true
```

## Bump version

Edit pom.xml at the root and in all modules directories. This can be done with :

```
./mvnw versions:set -DnewVersion=7.13.0-equisoft1
./mvnw versions:commit
```

Edit the version for the gradle plugin in [gradle.properties](modules/openapi-generator-gradle-plugin/gradle.properties)

New development can be tested with `SNAPSHOT` version. Ex: `7.17.0-equisoft2-SNAPSHOT`


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

## Agent tooling (APM)

This fork uses APM **0.35.0**, pinned in `.apm-version`, to deploy shared Equisoft AWT
agent instructions, skills, reviewers, and Jira tooling for Copilot. The common
`org-standards`, `apm-usage`, `code-review`, and `jira` packages are pinned together to
agent-toolkit **v1.3.0** (`d959fbfb35ed469aa24de3fca327396b28bbdaa7`).
Generated samples and supported output languages do not determine package selection:
no language-specific package is installed for this generator fork.

Install the pinned CLI using the [APM installation instructions](https://microsoft.github.io/apm/),
then, from the repository root:

```sh
apm --version # Must match .apm-version
apm install
apm audit --ci --no-cache
```

Private repository access is required for both
`kronostechnologies/agent-toolkit` and the organization policy in
`kronostechnologies/.github-private`. Provide a suitably scoped `GITHUB_APM_PAT`
through your credential manager or environment; never commit credentials.
Jira MCP authentication is separate from GitHub authentication.

Commit `apm.lock.yaml` and the files deployed under `.github/instructions/`,
`.github/prompts/`, `.github/agents/`, `.github/mcp.json`, and `.agents/skills/`
together. Do not edit deployed files or the lockfile manually. Local dependencies
in `apm_modules/` and the `.apm/` cache are ignored. To update tooling, change all
agent-toolkit dependency pins in `apm.yml` to the same reviewed release commit,
run `apm install`, review the generated changes, and rerun the audit. Change
shared content in agent-toolkit instead of patching its deployed copy.

The APM workflow uses `equisoft-actions/setup-apm@v1` and `.apm-version`.
Configure the repository or organization variable `APM_APP_ID` and secret
`APM_APP_PRIVATE_KEY` for a GitHub App installed with read access to
`agent-toolkit` and `.github-private`. The action exports its short-lived
`GITHUB_APM_PAT`; the default workflow `GITHUB_TOKEN` cannot read these private
dependencies. The audit must report the organization policy as loaded, not
silently pass without it. Missing CI credentials block validation and must be
resolved before marking the PR ready.

These files are agent tooling only; the upstream README, API contracts,
generator behavior, generated clients, and samples are unchanged.
