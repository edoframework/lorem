# Release 4.0.2

**Release Date:** 2026-10-23

Taihei Framework lorem 4.0.2 changes one thing: it depends on `dev.taihei::taihei-i18n` 4.0.1 instead of 4.0.0.

taihei-i18n up to 4.0.0 accepts a language tag of any length, and parsing a very long one can exhaust the stack ([GHSA-45w8-x6c8-w4hg](https://github.com/taiheiframework/i18n/security/advisories/GHSA-45w8-x6c8-w4hg)). taihei-i18n 4.0.1 refuses a tag over 255 characters. taihei-lorem 4.0.0 and 4.0.1 pull in the affected taihei-i18n 4.0.0, so a project that depends on lorem gets it unless it adds taihei-i18n 4.0.1 itself.

lorem never parses a language tag: it only uses the `Language` values taihei-i18n defines. The risk is in your own code, if it calls `Language.parse` or decodes a `Language` from input it does not control while taihei-i18n 4.0.0 is on the classpath.

## Upgrading

Change the version of `dev.taihei::taihei-lorem` to 4.0.2. Nothing else changes: the public API and the generated data are the same as in 4.0.1. If you added `dev.taihei::taihei-i18n:4.0.1` only to override lorem's dependency, you can remove it.

## Changelog

### Changed
- `dev.taihei::taihei-i18n` from 4.0.0 to 4.0.1, on the JVM and on Scala.js.
- The README's stack lists taihei-i18n 4.0.1.

### Added
- A test, run on the JVM and on Scala.js, that fails when lorem resolves a taihei-i18n that accepts a language tag of 256 characters.

## Module Changes

- `lorem.jvm` (`dev.taihei:taihei-lorem_3`): depends on `taihei-i18n_3` 4.0.1.
- `lorem.js` (`dev.taihei:taihei-lorem_sjs1_3`): depends on `taihei-i18n_sjs1_3` 4.0.1.

## Compatibility

- **Scala:** 3.9.0
- **Scala.js:** 1.22.0
- **zio-json:** 1.1.1
- **taihei-i18n:** 4.0.1
- **scala-java-time (Scala.js):** 2.8.0
- **scalajs-java-securerandom (Scala.js):** 1.0.0
- **ZIO Test (tests only):** 2.1.27

4.0.2 is binary-compatible with 4.0.1, as checked by MiMa.
