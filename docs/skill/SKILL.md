---
name: library-genius-lorem
description: How to use genius-lorem — Genius Fish's composable, cross-platform fake-data / Lorem Ipsum generator for Scala 3 (JVM + Scala.js). Apply when writing or reviewing Scala that needs randomized test/seed data — filler text, fake names, emails, addresses, dates, files, ids, or sample images — via the genius.lorem.LoremIpsum API. Covers the generator catalog, sample images, randomness, the genius-i18n dependency, and the cross-platform resource model.
---

# genius-lorem

A composable, cross-platform fake-data / Lorem Ipsum generator for Scala 3,
cross-compiled for the **JVM and Scala.js**. One object, `LoremIpsum`, with a
flat catalog of generators for filler text, people, internet, geography, dates,
files, ids, and sample images.

The package is `genius.lorem` (note: **not** `fish.genius.lorem` — extracted
verbatim from the platform). Artifacts: `fish.genius::genius-lorem` (JVM) and
`fish.genius::genius-lorem_sjs1` (Scala.js). It depends on
`fish.genius::genius-i18n` for the `Language` enum. Source:
<https://github.com/genius-fish/lorem>.

## When to use this skill

Read this whenever you write or review Scala that imports `genius.lorem.*` —
generating randomized test fixtures, seed data, or UI mockup content. It's the
fake-data faucet: names, emails, addresses, lorem text, dates, mime types, ids,
and base64 sample images.

## The model

This is **not** a DSL — it's a single object, `LoremIpsum`, whose methods each
return a fresh random value on every call (backed by
`java.security.SecureRandom`; there is no seed control, so output is not
reproducible). Data comes from embedded word lists plus three JSON name lists
loaded per platform (classpath on the JVM, embedded on JS).

```scala
import genius.lorem.LoremIpsum

LoremIpsum.name        // "Olivia Hernandez"
LoremIpsum.email       // "olivia.hernandez@brightwavehub.com"
LoremIpsum.paragraph   // a paragraph of lorem filler
LoremIpsum.localDate   // a random java.time.LocalDate
```

`LoremIpsum.language: genius.i18n.Language` returns a random language;
`LoremIpsum.languages` lists them — this is the one tie to the genius-i18n
library.

## Reference index

- **`reference/generators.md`** — the full `LoremIpsum` catalog grouped by
  category (text, people, internet, geography, dates, files, ids, primitives) and
  `SampleImages`, with exact signatures.
- **`reference/internals.md`** — randomness, the `genius-i18n` dependency, the
  cross-platform `ResourceLoader` / name-list model, and the build/test layout.

## Top gotchas

- The package is **`genius.lorem`**, not `fish.genius.lorem`.
- Every method call returns a **new random value**; there is **no seeding**, so
  don't rely on lorem data for deterministic test assertions — assert on shape
  (e.g. `email.contains("@")`), not exact values.
- It's cross-platform: `shared/` code must compile on Scala.js. `java.time` and
  `SecureRandom` are provided there by scala-java-time and
  scalajs-java-securerandom (already wired into the `js` deps).
- The JVM build needs the name-list JSON on the classpath (`shared/resources/`);
  the JS build embeds them in its `ResourceLoader`.
- Several generators are locale-flavored Belgium/EU (`phone` → `+32…`, `zipCode`
  → 4 digits); city/country names are international.
