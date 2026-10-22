# Release 3.0.0

**Release Date:** 2026-10-22

3.0.0 is the first release of the library as Edo Framework lorem, published as `com.edoframework::edo-lorem` under the Apache License 2.0. Its API is the same as `fish.genius::genius-lorem:2.0.0` under a new name. The generators, the word and name lists, and the behaviour have not changed. The package is now `com.edoframework.lorem`, and the library depends on `edo-i18n` 3.0.0 for `Language`.

The release also moves the library to Scala 3.9 LTS and Mill 1.3.1 and refreshes its dependencies. The build now needs only Maven Central and no credentials, so anyone can clone and build it. MiMa checks binary compatibility from this release onward. CI uses pinned actions and read-only permissions, and a DCO check runs on every pull request.

## Highlights

- **New coordinates and package**: `com.edoframework::edo-lorem` with the package `com.edoframework.lorem`, replacing `fish.genius::genius-lorem` and `genius.lorem`.
- **Apache License 2.0**: the library moves from MIT to Apache 2.0, with a `NOTICE`. Versions up to and including 2.0.0 stay available under MIT.
- **Scala 3.9 LTS**: compiled with Scala 3.9.0 and Scala.js 1.22.0.
- **Builds from Maven Central alone**: the private release plugin and the authenticated repositories are gone. The release flow now lives in the Makefile, and the version comes from the git tag.
- **Binary compatibility checks**: `make validate` runs MiMa against the newest release, starting with 3.0.0.

## Breaking Changes

This is a major release because the coordinates, the package and the Scala version change. The API itself is the same. To migrate from `genius-lorem` 2.0.0:

- Replace the dependency with `com.edoframework::edo-lorem:3.0.0` (use `::` for Scala.js).
- Replace `genius.lorem` with `com.edoframework.lorem` in your imports.
- `LoremIpsum.language` and `LoremIpsum.languages` now return `com.edoframework.i18n.Language` from edo-i18n 3.0.0, not `genius.i18n.Language`.
- Your build needs Scala 3.9 or later.
- The library now brings `zio-json` 1.1.1 instead of 0.9.2.
- The classpath location of the bundled name lists moved from `/genius/lorem/sources/` to `/com/edoframework/lorem/sources/`. This only matters if you read those JSON files directly.

```scala
// Before
// libraryDependencies += "fish.genius" %% "genius-lorem" % "2.0.0"
import genius.lorem.LoremIpsum
import genius.i18n.Language

// After
// mvn"com.edoframework::edo-lorem:3.0.0"
import com.edoframework.lorem.LoremIpsum
import com.edoframework.i18n.Language

LoremIpsum.name       // "Olivia Hernandez"
LoremIpsum.email      // "olivia.hernandez@brightwavehub.com"
LoremIpsum.paragraph  // a paragraph of lorem filler
```

## Changelog

### Added
- `NOTICE` file with the copyright, the licence history of earlier versions, and the public-domain origin of the Lorem ipsum passage.
- MiMa binary compatibility check in `make validate` (`make mima`), with 3.0.0 as the first baseline.
- Makefile release flow: `release-prepare TYPE=patch|minor|major`, `release-finalize`, `release-abort`, `release-preflight` and `publish-dry-run`.
- DCO workflow that checks every commit in a pull request for a `Signed-off-by` line matching its author.
- "Migrating from genius-lorem" section in the README.

### Changed
- Coordinates changed from `fish.genius::genius-lorem` to `com.edoframework::edo-lorem`, and the package from `genius.lorem` to `com.edoframework.lorem`. Sources, tests and resources moved accordingly.
- The `Language` dependency changed from `fish.genius::genius-i18n` 1.0.0 to `com.edoframework::edo-i18n` 3.0.0.
- Licence changed from MIT to Apache 2.0. The POM now names Innovenso BV as developer and `github.com/edoframework/lorem` as the project.
- Scala upgraded from 3.8.3 to 3.9.0, Scala.js from 1.21.0 to 1.22.0, ZIO from 2.1.26 to 2.1.27, zio-json from 0.9.2 to 1.1.1, and scala-java-time from 2.6.0 to 2.8.0.
- Mill upgraded from 1.1.5 to 1.3.1, and mill-scalafix from 0.6.0 to 0.6.2.
- The version is now computed in `build.mill` from the git tag. An exact `v`-tag on a clean tree is a release; anything else is a SNAPSHOT of the last tag.
- Releases now publish to Maven Central first and push the tag only after the publish succeeds.
- CI resolves everything from Maven Central without credentials, runs with read-only permissions, pins its actions to commit hashes, and runs MiMa.
- The Claude Code skill reference in `docs/skill/` now documents the new coordinates and package.

### Fixed
- `release-prepare` no longer stages reformatting made by `validate`, which used to make the release stop on a tree it then saw as dirty. It now refuses a dirty tree before validating, and if validation changes files it stops and lists them so they can be reviewed and committed.

## Module Changes

- `lorem.jvm` (`edo-lorem` for the JVM): renamed artifact and package, new dependency on `edo-i18n` 3.0.0.
- `lorem.js` (`edo-lorem` for Scala.js): renamed artifact and package. The embedded name lists use the new resource paths.
- Both modules now carry MiMa and the shared Edo publishing settings.

## Compatibility

- Scala 3.9.0 (LTS). Users need Scala 3.9 or later.
- Scala.js 1.22.0.
- `com.edoframework::edo-i18n` 3.0.0.
- `dev.zio::zio-json` 1.1.1.
- `dev.zio::zio-test` 2.1.27 (tests only).
- On Scala.js: `io.github.cquiroz::scala-java-time` and `scala-java-time-tzdb` 2.8.0, and `org.scala-js::scalajs-java-securerandom` 1.0.0.
- Built with Mill 1.3.1. CI uses Java 25 and Node 20.
- This release is not binary compatible with `genius-lorem` 2.0.0. It is the first baseline for binary compatibility under `com.edoframework`.
