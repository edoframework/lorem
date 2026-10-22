# Release 4.0.0

**Release Date:** 2026-10-22

The library is renamed. It is now Taihei Framework lorem, published as `dev.taihei::taihei-lorem` with the package `dev.taihei.lorem`, and lives in the `taiheiframework` organisation on GitHub. It was previously released as `com.edoframework::edo-lorem` (3.0.0). The generator, its word and name lists, and its behaviour are unchanged. Every type and method has the same name and signature as in 3.0.0. Only the coordinates and the package are different.

This is a major release because the package name changed: code compiled against 3.0.0 does not link against 4.0.0. To upgrade, change the dependency and replace `com.edoframework.lorem` with `dev.taihei.lorem` in your imports. The library now depends on `dev.taihei::taihei-i18n` 4.0.0 instead of `com.edoframework::edo-i18n` 3.0.0, so `Language` is imported from `dev.taihei.i18n`.

## Highlights

- **New coordinates** - `dev.taihei::taihei-lorem` on Maven Central, published as `taihei-lorem_3` (JVM) and `taihei-lorem_sjs1_3` (Scala.js), in place of `com.edoframework::edo-lorem`.
- **New package** - `dev.taihei.lorem` and `dev.taihei.lorem.sources` replace `com.edoframework.lorem` and `com.edoframework.lorem.sources`.
- **On taihei-i18n 4.0.0** - `Language` now comes from `dev.taihei.i18n`.
- **No functional change** - The generators, sample images and data lists are the same as in 3.0.0.

## Changelog

### Changed
- Maven coordinates: `com.edoframework::edo-lorem` becomes `dev.taihei::taihei-lorem`.
- Package: `com.edoframework.lorem` becomes `dev.taihei.lorem`. This covers `LoremIpsum`, `SampleImages` and every word collection in `sources` (`Characters`, `Cities`, `Countries`, `DomainNames`, `FemaleNames`, `FileExtensions`, `Lorem`, `MaleNames`, `MimeTypes`, `SampleWordCollection`, `StateAbbreviations`, `StateFullNames`, `Surnames`) and the platform `ResourceLoader`.
- Classpath resources for the name lists moved from `/com/edoframework/lorem/sources/*.json` to `/dev/taihei/lorem/sources/*.json`. This only affects code that called `ResourceLoader.loadStringList` with an explicit path.
- Dependency: `com.edoframework::edo-i18n` 3.0.0 is replaced by `dev.taihei::taihei-i18n` 4.0.0.
- POM metadata: the project URL and SCM point to `github.com/taiheiframework/lorem`, and the developer URL points to `taihei.dev`.
- `NOTICE` names the Taihei Framework and records that 3.0.0 was published as `com.edoframework::edo-lorem` under the Apache License 2.0. Versions up to and including 2.0.0 (`fish.genius::genius-lorem`) remain under the MIT License.
- Build: MiMa's first baseline is now 4.0.0, because earlier tags were published under other coordinates and there is nothing on Central to compare them with. CI, scalafix import grouping and the Makefile's Central path use the new names.
- Documentation (README and the Claude Code skill reference in `docs/skill/`) uses the new coordinates and package.

### Migration

```scala
// before
mvn"com.edoframework::edo-lorem:3.0.0"
import com.edoframework.lorem.LoremIpsum
import com.edoframework.i18n.Language

// after
mvn"dev.taihei::taihei-lorem:4.0.0"
import dev.taihei.lorem.LoremIpsum
import dev.taihei.i18n.Language
```

Coming from `fish.genius::genius-lorem` 2.0.0, replace `genius.lorem` and `genius.i18n` with `dev.taihei.lorem` and `dev.taihei.i18n`. The changes that came with 3.0.0 apply as well: Scala 3.9 or later, `zio-json` 1.1 instead of 0.9, and the Apache License 2.0.

## Module Changes

- `taihei-lorem_3` (JVM), formerly `edo-lorem_3`: renamed artifact and package. Name lists are still loaded from classpath resources, now at the new paths.
- `taihei-lorem_sjs1_3` (Scala.js), formerly `edo-lorem_sjs1_3`: renamed artifact and package. The embedded name lists are keyed on the new resource paths.

## Compatibility

- Scala 3.9.0 (LTS), Scala.js 1.22.0
- Mill 1.3.1
- `dev.taihei::taihei-i18n` 4.0.0
- ZIO 2.1.27 (`zio-test`), `zio-json` 1.1.1
- Scala.js only: scala-java-time 2.8.0 (with tzdb), scalajs-java-securerandom 1.0.0
- Not binary or source compatible with `com.edoframework::edo-lorem` 3.0.0 because the package changed. The API is otherwise identical.
- Licence: Apache License 2.0
