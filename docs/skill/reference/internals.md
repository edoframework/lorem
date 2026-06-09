# internals — randomness, deps, cross-platform & build

Package `genius.lorem`.

## Randomness

All generators draw from a `java.security.SecureRandom`. Consequences:

- **No seeding** — output is not reproducible across runs. Don't assert exact
  values in tests; assert on shape (`email.contains("@")`, two-part `name`, etc.).
- `int(bound)` / `long(bound)` return values in `[1, bound]` — **never 0**.
- `double(bound)` is in `[0, bound)`.

## The genius-i18n dependency

`LoremIpsum.languages` / `LoremIpsum.language` return `genius.i18n.Language`
values — the single tie to the [`genius-i18n`](https://github.com/genius-fish/i18n)
library. The build depends on `fish.genius::genius-i18n` (JVM) /
`fish.genius::genius-i18n_sjs1` (JS), pinned in the `V.geniusI18n` version in
`build.mill`.

Resolution note: the build adds the **local ivy repository** to its resolvers, so
`make publishLocal` in the `i18n` repo makes the dependency available locally. For
CI and external consumers, `genius-i18n` must be published to `repo.genius.fish`.

## Cross-platform name lists (ResourceLoader)

Most word lists are embedded Scala (`sources/*.scala`). Three larger lists —
male names, female names, surnames — are JSON, loaded through a per-platform
`ResourceLoader`:

| Platform | How the JSON is provided |
| --- | --- |
| **JVM** (`jvm/src`) | `getClass.getResourceAsStream(...)` reads `shared/resources/genius/lorem/sources/*.json` from the classpath |
| **Scala.js** (`js/src`) | the JSON is embedded as string literals in the JS `ResourceLoader` (the browser can't read the filesystem) |

Both fail fast (throw) on a missing/malformed resource — this is build-time data,
so a failure means a packaging bug, not a runtime condition. If you add a new
JSON-backed list, add the file under `shared/resources/...` **and** embed it in
the JS `ResourceLoader`.

## Cross-platform rules

`shared/src` must compile on Scala.js. It uses `java.time` (via scala-java-time)
and `java.security.SecureRandom` (via scalajs-java-securerandom) — both already in
the `js` module's deps. Anything genuinely platform-specific goes in `jvm/src` or
`js/src` as a matching object (as `ResourceLoader` does).

## Build & layout

```
shared/src/genius/lorem/           LoremIpsum, SampleImages, sources/* (data)
shared/resources/genius/lorem/sources/  male-names.json, female-names.json, surnames.json
jvm/src/genius/lorem/sources/      ResourceLoader (classpath)
js/src/genius/lorem/sources/       ResourceLoader (embedded)
shared/test/src/genius/lorem/      LoremIpsumSpec (runs on both platforms)
```

- `make compile` / `make test` cover both platforms (`./mill __.compile`,
  `./mill __.test.testForked` → `lorem.jvm` and `lorem.js`; JS tests run on Node).
- Single-platform: `./mill lorem.jvm.test` or `./mill lorem.js.test`.
- Released via the mill-release flow (`make release-prepare TYPE=…` →
  `make release-finalize`), same as the other Genius Fish libraries.
