---
name: taihei-mihon
description: How to use Taihei Mihon (dev.taihei::taihei-mihon, formerly taihei-lorem, edo-lorem and genius-lorem), Innovenso's cross-platform fake-data and Lorem Ipsum generator for Scala 3 (JVM and Scala.js). Apply when writing or reviewing Scala that needs test or seed data (filler text, fake names, emails, addresses, dates, files, ids, sample images) through dev.taihei.mihon.LoremIpsum, or when migrating from dev.taihei.lorem. Covers the seeded and secure generators, the clock, the generator catalog, sample images, the taihei-tsuji dependency and the cross-platform name lists.
---

# Taihei Mihon (formerly taihei-lorem)

A fake-data and Lorem Ipsum generator for Scala 3, cross-compiled for the **JVM and Scala.js**.
One class, `LoremIpsum`, with a flat catalog of generators for filler text, people, internet,
geography, dates, files and ids, plus `SampleImages`.

The package is `dev.taihei.mihon`. Artifacts on Maven Central: `dev.taihei::taihei-mihon`,
published as `taihei-mihon_3` (JVM) and `taihei-mihon_sjs1_3` (Scala.js), from 5.0.0, compiled
with Scala 3.9 (LTS), Apache 2.0. It depends on `dev.taihei::taihei-tsuji` 5 for `Language`.
Source: <https://github.com/taiheiframework/mihon>. Up to 4.x it was `dev.taihei::taihei-lorem`
in package `dev.taihei.lorem`, with `LoremIpsum` as an object.

## When to use this skill

Read this whenever you write or review Scala that imports `dev.taihei.mihon.*`, or code on
`dev.taihei.lorem.*` that moves to 5.x: test fixtures, seed data, mock-up content.

## The model

`LoremIpsum` is a final class. Create a generator with one of two factories; there is no global
instance:

```scala
import dev.taihei.mihon.LoremIpsum

val lorem = LoremIpsum.secure()     // SecureRandom: values do not repeat
val fixed = LoremIpsum.seeded(42L)  // java.util.Random(42): the same values on every run

lorem.name        // "Olivia Hernandez"
lorem.email       // "olivia.hernandez@brightwavehub.example"
lorem.paragraph   // a paragraph of lorem filler
lorem.localDate   // a random java.time.LocalDate
```

Both factories take a `java.time.Clock` last, `Clock.systemUTC()` by default; `today`,
`yesterday`, `tomorrow`, `futureDate` and `pastDate` follow it.

## Reference index

- **`reference/generators.md`**: the full `LoremIpsum` catalog by category and `SampleImages`,
  with exact signatures.
- **`reference/internals.md`**: seeds and reproducibility, the clock, the taihei-tsuji
  dependency, the cross-platform name lists and the build layout.

## Top gotchas

- The package is **`dev.taihei.mihon`**; `dev.taihei.lorem` (4.x), `com.edoframework.lorem`
  (3.0.0) and `genius.lorem` are the old names. `LoremIpsum.name` on the companion no longer
  compiles: create a generator first.
- A seed repeats **within one Mihon version** only: a minor release may change the values for a
  seed. Pin values in tests only next to a pinned version; otherwise assert on shape
  (`email.contains("@")`).
- A seeded generator shared across threads stays thread-safe but stops being reproducible.
- `LoremIpsum.languages` is gone: use `Language.values` from `dev.taihei.tsuji`.
- The word lists (`sources`), `SampleWordCollection` and `ResourceLoader` are internal.
- Several generators are Belgium/EU flavoured (`phone` is `+32…`, `zipCode` 4 digits); city and
  country names are international.
- `password` is fake data, not a credential: from a seeded generator it is predictable.
