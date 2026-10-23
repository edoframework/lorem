# Release 4.0.3

**Release Date:** 2026-10-23

Taihei Framework lorem 4.0.3 changes one thing: it depends on `dev.taihei::taihei-i18n` 4.0.2 instead of 4.0.1.

taihei-i18n up to 4.0.1 reads a `Message` of any size from JSON, and decoding or formatting one that came from an untrusted source can use unbounded memory and CPU ([GHSA-6h29-v54g-8337](https://github.com/taiheiframework/i18n/security/advisories/GHSA-6h29-v54g-8337)). taihei-i18n 4.0.2 bounds both: the JSON decoder of `Message` refuses more than 1000 templates, more than 1000 arguments, or a template, argument or argument name longer than 65536 characters, and formatting one template stops at a million characters. taihei-lorem 4.0.0 to 4.0.2 pull in an affected taihei-i18n, so a project that depends on lorem gets it unless it adds taihei-i18n 4.0.2 itself.

lorem never decodes or formats a `Message`: it only uses the `Language` values taihei-i18n defines. The risk is in your own code, if it decodes `Message` JSON from a source it does not control, or formats such a message, while an affected taihei-i18n is on the classpath.

## Upgrading

Change the version of `dev.taihei::taihei-lorem` to 4.0.3. Nothing else changes: the public API and the generated data are the same as in 4.0.2. If you added `dev.taihei::taihei-i18n:4.0.1` only to override lorem's dependency, you can remove it.

If you cannot move to lorem 4.0.3 yet, add `dev.taihei::taihei-i18n:4.0.2` to your own dependencies: it is binary compatible with the 4.0.0 and 4.0.1 that older lorem versions pull in, and your build resolves the newer one.

## Changelog

### Changed
- `dev.taihei::taihei-i18n` from 4.0.1 to 4.0.2, on the JVM and on Scala.js.
- The README's coordinates name 4.0.3 and its stack lists taihei-i18n 4.0.2. Its migration section says which lorem versions pull in a taihei-i18n affected by which advisory.

### Added
- A test, run on the JVM and on Scala.js, that fails when lorem resolves a taihei-i18n that decodes a `Message` with a template of 65537 characters.

## Module Changes

- `lorem.jvm` (`dev.taihei:taihei-lorem_3`): depends on `taihei-i18n_3` 4.0.2.
- `lorem.js` (`dev.taihei:taihei-lorem_sjs1_3`): depends on `taihei-i18n_sjs1_3` 4.0.2.

## Compatibility

- **Scala:** 3.9.0
- **Scala.js:** 1.22.0
- **zio-json:** 1.1.1
- **taihei-i18n:** 4.0.2
- **scala-java-time (Scala.js):** 2.8.0
- **scalajs-java-securerandom (Scala.js):** 1.0.0
- **ZIO Test (tests only):** 2.1.27

4.0.3 is binary-compatible with 4.0.2, as checked by MiMa.
