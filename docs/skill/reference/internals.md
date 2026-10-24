# internals: seeds, the clock, dependencies, cross-platform and build

Package `dev.taihei.mihon`.

## Randomness and seeds

A `LoremIpsum` draws every value from the `java.util.Random` it was created with; there is no
global generator.

- `LoremIpsum.seeded(seed)` uses `new java.util.Random(seed)`. Every draw goes through the methods
  whose algorithm `java.util.Random` specifies and Scala.js implements alike (`nextInt`,
  `nextInt(n)`, `nextLong`, `nextBoolean`, `nextDouble`), so a seed gives the same values on the
  JVM and on Scala.js. The test `SeededSpec` pins the values of seed 42 and runs on both.
- The values for a seed hold for one Mihon version on one taihei-tsuji version. A new word, a
  changed list or a new language in taihei-tsuji changes them, and that is allowed in a minor
  release of either.
- `LoremIpsum.secure()` uses a `java.security.SecureRandom`; values do not repeat.
- One instance is thread-safe, but threads drawing from it in turn make it non-reproducible.
- `int(bound)` / `long(bound)` return values in `[1, bound]`, each equally likely, never 0.
  `double(bound)` is `nextDouble() * bound`, in `[0, bound)`.
- `id` builds a version-4 UUID from two `nextLong` draws.

## The clock

Both factories take a `java.time.Clock`, `Clock.systemUTC()` by default. `today` is
`LocalDate.now(clock)`, so it is the clock's date in the clock's zone; `yesterday`, `tomorrow`,
`futureDate` and `pastDate` are relative to it. In a test, pass `Clock.fixed(...)`.

## The taihei-tsuji dependency

`LoremIpsum.language` returns a `dev.taihei.tsuji.Language`, one of `Language.values`: the
single tie to [Taihei Tsuji](https://github.com/taiheiframework/tsuji). The build depends on
`dev.taihei::taihei-tsuji` from Maven Central, pinned in `V.taiheiTsuji` in `build.mill`.

## Cross-platform name lists

`ResourceLoader`, `SampleWordCollection` and the objects in `dev.taihei.mihon.sources` are
`private[mihon]`. Use `LoremIpsum`.

Most word lists are Scala (`sources/*.scala`). Three larger lists (male names, female names,
surnames) are JSON in `shared/resources/dev/taihei/mihon/sources/`, the one copy of them:

| Platform | How the JSON is provided |
| --- | --- |
| **JVM** (`jvm/src`) | `ResourceLoader` reads the files from the classpath |
| **Scala.js** (`js/src`) | the build generates `EmbeddedLists` from the same files (`EmbeddedLists` in `build.mill`), and `ResourceLoader` reads it |

The generator refuses a file in that folder that is not a non-empty JSON array of non-empty
strings. Both loaders throw on a missing list: it is build-time data, so a failure is a packaging
bug. `NameListsSpec` pins each list's size and hash on both platforms; a change to a list updates
it there.

## Build and layout

```
shared/src/dev/taihei/mihon/                LoremIpsum, SampleImages, sources/* (data)
shared/resources/dev/taihei/mihon/sources/  male-names.json, female-names.json, surnames.json
jvm/src/dev/taihei/mihon/sources/           ResourceLoader (classpath)
js/src/dev/taihei/mihon/sources/            ResourceLoader (generated EmbeddedLists)
shared/test/src/dev/taihei/mihon/           LoremIpsumSpec, SeededSpec, NameListsSpec (both platforms)
```

- `make compile` / `make test` cover both platforms (`./mill __.compile`,
  `./mill __.test.testForked`, for `mihon.jvm` and `mihon.js`; JS tests run on Node).
- Single platform: `./mill mihon.jvm.test` or `./mill mihon.js.test`.
- Released to Maven Central from the git tag (`make release-prepare TYPE=…`, then
  `make release-finalize`), the same as every Taihei Framework library. MiMa compares with the
  newest 5.x release; 5.0.0 is the first under this name.
