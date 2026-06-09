# Release 2.0.0

**Release Date:** 2026-06-09

Version 2.0.0 is a ground-up rewrite of the library. The single, JVM-only sbt project built on Scala 2.13 and the third-party `com.thedeanda:lorem` Java library has been replaced by a **cross-platform Scala 3 library** that cross-compiles for both the **JVM and Scala.js**. The build moved from sbt to Mill, the public API was consolidated and relocated from `fish.genius.lorem.SampleText` to a single `genius.lorem.LoremIpsum` object, and all sample data is now bundled with the library itself (JSON name lists plus embedded word collections) rather than pulled from an external dependency.

Because this involves new Maven coordinates, a new package, a new Scala major version, and a reworked API surface, **2.0.0 is not source- or binary-compatible with 1.x** and requires code changes to adopt. In exchange you get browser/Node support via Scala.js, a richer generator set (sentences, paragraphs, passwords, file/MIME types, relative dates, internationalized place names), and zero reliance on the now-unmaintained `thedeanda` library.

## Highlights

- **Cross-platform (JVM + Scala.js)** — the generator and its data live in `shared/`; only the resource loader is platform-specific, so the same API runs server-side and in the browser/Node.
- **Scala 3 + Mill** — migrated from Scala 2.13.10/sbt to Scala 3.8.3 built with Mill, with Scalafmt + Scalafix wired in.
- **Self-contained data** — removed the `com.thedeanda:lorem` dependency; names and surnames ship as JSON resources and words/cities/countries as Scala sources, parsed with `zio-json`.
- **Unified `LoremIpsum` API** — `SampleText` is gone; everything lives on `genius.lorem.LoremIpsum`, with many new generators.
- **Internationalization via `genius-i18n`** — `java.util.Locale` is replaced by the `genius.i18n.Language` enum, and city/country names are returned in their native scripts.

## New Features

### Unified `LoremIpsum` generator

All generators now hang off a single object in the new `genius.lorem` package:

```scala
import genius.lorem.LoremIpsum

LoremIpsum.name          // "Olivia Hernandez"
LoremIpsum.email         // "olivia.hernandez@brightwavehub.com"
LoremIpsum.paragraph     // a paragraph of lorem filler
LoremIpsum.address       // "lorem ipsum dolor 42, 1234 Tokyo"
LoremIpsum.localDate     // a random java.time.LocalDate
LoremIpsum.id            // a UUID-shaped identifier
```

### Expanded generator set

Generators new in 2.0.0 (beyond the 1.x text/name/address set):

```scala
LoremIpsum.sentence      // capitalized sentence ending in "."
LoremIpsum.sentences     // several sentences
LoremIpsum.paragraph     // multi-sentence paragraph
LoremIpsum.paragraphs    // multi-paragraph block
LoremIpsum.password      // 30-character random password
LoremIpsum.character     // a single random character
LoremIpsum.words         // multiple words

LoremIpsum.street        // street name
LoremIpsum.today         // relative dates (UTC)
LoremIpsum.yesterday
LoremIpsum.tomorrow
LoremIpsum.futureDate
LoremIpsum.pastDate

LoremIpsum.fileExtension            // ".pdf"
LoremIpsum.fileExtensionWithoutDot  // "pdf"
LoremIpsum.mimeType                 // "application/pdf"

LoremIpsum.long(bound)              // random Long
```

### Internationalized language, cities, and countries

```scala
LoremIpsum.language      // a random genius.i18n.Language
LoremIpsum.languages     // all available Language values
LoremIpsum.city          // native-script city name, e.g. "東京"
LoremIpsum.country       // international country name
```

Every value is still freshly randomized, backed by `java.security.SecureRandom` (polyfilled on Scala.js via `scalajs-java-securerandom`).

### Sample images on both platforms

`SampleImages.landscape` and `SampleImages.portrait` return base64-encoded PNGs and now compile for both the JVM and Scala.js.

## Changelog

### Added
- Scala.js cross-compilation producing a `genius-lorem_sjs1` artifact that runs in the browser and on Node.
- Self-contained sample data: `female-names.json`, `male-names.json`, `surnames.json` resources plus Scala-sourced word, city, country, character, domain-name, file-extension, MIME-type, and US state collections.
- New generators: `sentence`, `sentences`, `paragraph`, `paragraphs`, `password`, `character`, `words`, `street`, `today`, `yesterday`, `tomorrow`, `futureDate`, `pastDate`, `fileExtension`, `fileExtensionWithoutDot`, `mimeType`, and `long(bound)`.
- `language` / `languages` backed by the `genius.i18n.Language` enum.
- ZIO Test suite running on both the JVM and Node, plus Scalafmt/Scalafix configuration.

### Changed
- Build tool migrated from **sbt → Mill** (Mill 1.1.5).
- Language upgraded from **Scala 2.13.10 → Scala 3.8.3** (Scala.js 1.21.0).
- Package renamed from `fish.genius.lorem` to **`genius.lorem`**.
- Public API consolidated from `SampleText` into **`LoremIpsum`**.
- JSON parsing now uses **`zio-json`**; `htmlParagraphs`/`paragraphs` and friends are generated internally rather than via the external library.
- Cities and countries are now returned in their native scripts.
- Publishing target and artifact coordinates changed to `fish.genius::genius-lorem` (JVM) and `fish.genius::genius-lorem_sjs1` (JS) under the MIT license.

### Removed
- Dependency on the third-party **`com.thedeanda:lorem`** Java library.
- Genius Fish `config`, `logging`, and `io` dependencies.
- `java.util.Locale`-based `locale` / `locales` API (superseded by `Language`).
- The `SampleText` object.

## Breaking Changes

- **New coordinates:** depend on `fish.genius::genius-lorem` (JVM) or `fish.genius::genius-lorem_sjs1` (JS) instead of `fish.genius::lorem`.
- **New package & object:** replace `import fish.genius.lorem.SampleText` with `import genius.lorem.LoremIpsum`, and update call sites accordingly.
- **Scala 3 only:** consumers must be on Scala 3 (3.8.x); the 2.13 artifact is discontinued.
- **Locale → Language:** code using `SampleText.locale` / `SampleText.locales` must migrate to `LoremIpsum.language` / `LoremIpsum.languages` (`genius.i18n.Language`).

## Module Changes

- **`shared/`** — new home for the `LoremIpsum` generator, `SampleImages`, all `genius.lorem.sources.*` data, JSON resources, and the cross-platform test suite.
- **`jvm/`** — JVM `ResourceLoader` that reads name lists from classpath resources.
- **`js/`** — Scala.js `ResourceLoader` and the `genius-lorem_sjs1` artifact.
- **Removed** — the legacy `src/main` / `src/test` tree under `fish.genius.lorem`, along with the sbt `project/` build definitions.

## Compatibility

- **Scala:** 3.8.3 (built with Mill 1.1.5)
- **Scala.js:** 1.21.0 (artifact suffix `_sjs1`)
- **Key dependencies:**
  - `dev.zio::zio-json` 0.9.2
  - `dev.zio::zio-test` / `zio-test-sbt` 2.1.26 (test)
  - `fish.genius::genius-i18n` 1.0.0
  - JS polyfills: `io.github.cquiroz::scala-java-time` (+ tzdb) 2.6.0, `org.scala-js::scalajs-java-securerandom` 1.0.0
- **Not compatible with 1.x** — see Breaking Changes for migration steps.
