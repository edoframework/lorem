# internals — randomness, deps, cross-platform & build

Package `dev.taihei.lorem`.

## Randomness

All generators draw from a `java.security.SecureRandom`. Consequences:

- **No seeding** — output is not reproducible across runs. Don't assert exact
  values in tests; assert on shape (`email.contains("@")`, two-part `name`, etc.).
- `int(bound)` / `long(bound)` return values in `[1, bound]` — **never 0**.
- `double(bound)` is in `[0, bound)`.

## The taihei-i18n dependency

`LoremIpsum.languages` / `LoremIpsum.language` return `dev.taihei.i18n.Language`
values — the single tie to the [taihei-i18n](https://github.com/taiheiframework/i18n)
library. The build depends on `dev.taihei::taihei-i18n` (JVM) /
`dev.taihei::taihei-i18n_sjs1` (JS) from Maven Central, pinned in `V.taiheiI18n` in
`build.mill`.

## Cross-platform name lists (ResourceLoader)

Most word lists are embedded Scala (`sources/*.scala`). Three larger lists —
male names, female names, surnames — are JSON, loaded through a per-platform
`ResourceLoader`:

| Platform | How the JSON is provided |
| --- | --- |
| **JVM** (`jvm/src`) | `getClass.getResourceAsStream(...)` reads `shared/resources/dev/taihei/lorem/sources/*.json` from the classpath |
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
shared/src/dev/taihei/lorem/                LoremIpsum, SampleImages, sources/* (data)
shared/resources/dev/taihei/lorem/sources/  male-names.json, female-names.json, surnames.json
jvm/src/dev/taihei/lorem/sources/           ResourceLoader (classpath)
js/src/dev/taihei/lorem/sources/            ResourceLoader (embedded)
shared/test/src/dev/taihei/lorem/           LoremIpsumSpec (runs on both platforms)
```

- `make compile` / `make test` cover both platforms (`./mill __.compile`,
  `./mill __.test.testForked` → `lorem.jvm` and `lorem.js`; JS tests run on Node).
- Single-platform: `./mill lorem.jvm.test` or `./mill lorem.js.test`.
- Released to Maven Central from the git tag (`make release-prepare TYPE=…` →
  `make release-finalize`), the same as every Taihei Framework library.
