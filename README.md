# Taihei Mihon

Mihon (見本) means sample or specimen. Taihei Mihon is a **fake-data and Lorem Ipsum generator**
for Scala 3: filler text, names, emails, addresses, dates, files, ids and sample images, from a
seed when you want the same values on every run. Cross-compiled for the **JVM and Scala.js**.

Part of the [Taihei Framework](https://taihei.dev), the open-source Scala 3
libraries from Innovenso. The package is `dev.taihei.mihon`. Up to 4.x the library was called
taihei-lorem: see [Coming from taihei-lorem 4.x](#coming-from-taihei-lorem-4x).

## Modules

Published on Maven Central under `dev.taihei`:

| Artifact            | Platform | Notes                                                |
| ------------------- | -------- | ---------------------------------------------------- |
| `taihei-mihon`      | JVM      | Name lists read from the classpath.                  |
| `taihei-mihon_sjs1` | Scala.js | Name lists compiled in; runs in the browser or Node. |

```scala
mvn"dev.taihei::taihei-mihon:5.0.0"       // Mill, JVM
mvn"dev.taihei::taihei-mihon::5.0.0"      // Mill, Scala.js
"dev.taihei" %% "taihei-mihon" % "5.0.0"  // sbt, JVM
"dev.taihei" %%% "taihei-mihon" % "5.0.0" // sbt, Scala.js (sbt-scalajs or sbt-crossproject)
```

`shared/` holds the generator and the word lists; `jvm/` and `js/` carry only the
platform-specific loader of the name lists. Depends on
[Taihei Tsuji](https://github.com/taiheiframework/tsuji) for `Language`.

5.0.0 is the first release under the name Taihei Mihon, and a major release: see
[Coming from taihei-lorem 4.x](#coming-from-taihei-lorem-4x). The 4.x line keeps its name,
`dev.taihei::taihei-lorem`, and receives security fixes for 12 months after the release of
Taihei Mihon 5.0.0.

## Stack

- Mill 1.3.1
- Scala 3.9.0 (LTS) · Scala.js 1.22.0
- ZIO 2.1.27 (`zio-json` 1.1, `zio-test`)
- `dev.taihei::taihei-tsuji` 5.0.0 · scala-java-time 2.8 and scalajs-java-securerandom (JS)

Everything resolves from Maven Central; the build needs no credentials.

## Build

```sh
make compile        # ./mill __.compile           (JVM + JS)
make test           # ./mill __.test.testForked    (JVM + JS; JS runs on Node)
make fmt            # ./mill __.reformat
make fix            # ./mill __.fix                 (scalafix)
make validate       # fix + fmt + compile + test + mima
make mima           # binary compatibility with the newest release tag
make publishLocal   # to ~/.ivy2/local (a SNAPSHOT only)
```

## Releasing

The version is the git tag: an exact `v`-tag on a clean tree is the release,
anything else a SNAPSHOT of the last tag.

```sh
make release-prepare TYPE=patch|minor|major   # validate, write RELEASE_x_y_z.md
make release-finalize                          # commit the notes, tag locally,
                                               # check Central, publish to it
git push origin main && git push origin vX.Y.Z # only after the publish succeeded
make publish-dry-run                           # the signed bundle, written to out/
```

Both artifacts go to Central as one bundle. A release publishes only the commit
`release-prepare` validated, and refuses when a file git does not track is in a
source or resource folder, or a stray build file is in the tree.

Publishing reads the Central Portal user token and the release key from
`~/.edo-release.env` (a name from the Edo Framework days, kept because the
maintainers' workstations write it for every library; `RELEASE_ENV=…`
points elsewhere) or the environment (`MILL_SONATYPE_USERNAME`,
`MILL_SONATYPE_PASSWORD`, `MILL_PGP_SECRET_BASE64`, `MILL_PGP_PASSPHRASE`).
Releases are signed by the Taihei Framework release key,
fingerprint `5026 B299 6DDB 697E C463  0BCF 14A7 33E2 A507 702F`.

## Quick start

```scala
import dev.taihei.mihon.LoremIpsum

val lorem = LoremIpsum.secure()

lorem.name          // "Olivia Hernandez"
lorem.email         // "olivia.hernandez@brightwavehub.example"
lorem.paragraph     // a paragraph of lorem filler
lorem.address       // "lorem ipsum dolor 42, 1234 Berlin"
lorem.city          // "Tokyo"
lorem.localDate     // a random java.time.LocalDate
lorem.id            // a UUID-shaped identifier
```

A generator draws every value from its own random number generator and reads
today's date from its own clock. Create one of two kinds:

- `LoremIpsum.secure()` draws from a `java.security.SecureRandom`: the values do
  not repeat from run to run.
- `LoremIpsum.seeded(seed)` draws from a `java.util.Random` with that seed: the
  same seed, clock and Mihon version give the same values, in the same order, on
  the JVM and on Scala.js. A minor release may change the values for a seed (a new
  word, a changed list), so assert on the values of a seed only within one version.

Both take a `java.time.Clock` as their last parameter, `Clock.systemUTC()` by
default. `today`, `yesterday`, `tomorrow`, `futureDate` and `pastDate` follow it:

```scala
import java.time.{Clock, Instant, ZoneOffset}

val clock = Clock.fixed(Instant.parse("2026-10-24T12:00:00Z"), ZoneOffset.UTC)
val fixed = LoremIpsum.seeded(42L, clock)

fixed.today       // 2026-10-24
fixed.futureDate  // the same date on every run
```

One generator may be shared across threads; it stays thread-safe, but the order in
which threads draw then decides the values, so they are no longer reproducible.
E-mail addresses and URLs use the reserved `.example` top-level domain, so they
never reach a real host. Highlights by category:

- **Text**: `word`, `words`, `sentence`, `sentences`, `paragraph`, `paragraphs`, `htmlParagraphs`, `title`, `description`, `slug`, `password`
- **People**: `firstName`, `lastName`, `name`
- **Internet**: `email`, `url`, `phone`
- **Geography**: `street`, `city`, `zipCode`, `address`, `country`
- **Dates**: `localDate`, `today`, `yesterday`, `tomorrow`, `futureDate`, `pastDate`
- **Files**: `fileExtension`, `fileExtensionWithoutDot`, `mimeType`
- **Ids and primitives**: `id`, `character`, `bool`, `int(bound)` and `long(bound)` (in `[1, bound]`), `double(bound)` (in `[0, bound)`), `times(bound, f)` (one to `bound` items), `language`
- **Images**: `SampleImages.landscape`, `SampleImages.portrait` (base64 PNGs, constants)

City and country names are international (native scripts). `language` returns a
random `dev.taihei.tsuji.Language`.

## Testing

```scala
import dev.taihei.mihon.LoremIpsum
import zio.test.*

val lorem = LoremIpsum.seeded(42L)

assertTrue(
  lorem.email.contains("@"),
  lorem.name.contains(" "), // last names may have spaces: "De La Cruz"
)
```

The suite runs on both platforms (`make test`); JS tests execute under Node.

## Coming from taihei-lorem 4.x

Taihei Mihon 5.0.0 is the successor of `dev.taihei::taihei-lorem` 4.x under a new name. The
coordinates and the package change with it:

- the dependency is `dev.taihei::taihei-mihon`;
- the package is `dev.taihei.mihon`, where it was `dev.taihei.lorem`;
- `language` returns a `dev.taihei.tsuji.Language` of
  [Taihei Tsuji](https://github.com/taiheiframework/tsuji) 5, the successor of taihei-i18n.

There is no taihei-lorem 5.0.0 and no relocation from the old coordinates. The two lines are
different artifacts in different packages, so they can be on one classpath side by side.

```sh
# every import of dev.taihei.lorem becomes dev.taihei.mihon
grep -rlZ 'dev\.taihei\.lorem' --include='*.scala' . | xargs -0 perl -pi -e 's/dev\.taihei\.lorem/dev.taihei.mihon/g'
```

`LoremIpsum` is no longer an object with a global generator and the wall clock: create a
generator once and call the same members on it.

| 4.x | 5.x |
| --- | --- |
| `LoremIpsum.name` (any generator) | `val lorem = LoremIpsum.secure()`, then `lorem.name` |
| no way to repeat values | `LoremIpsum.seeded(seed)` |
| `today` is the UTC date by the wall clock | `today` follows the generator's clock, `Clock.systemUTC()` by default |
| `LoremIpsum.languages` | `Language.values` from `dev.taihei.tsuji` |
| `dev.taihei.i18n.Language` | `dev.taihei.tsuji.Language` |
| `dev.taihei.lorem.sources.*` (word lists), `SampleWordCollection`, `ResourceLoader` | internal now: use `LoremIpsum` |
| `sources.StateAbbreviations`, `sources.StateFullNames` | removed; nothing used them |
| `id` from `UUID.randomUUID` | a version-4 UUID drawn from the generator, so a seeded one repeats |

`SampleImages` and the names, ranges and distributions of the other generators are unchanged.

## Coming from edo-lorem 3.0.0 or genius-lorem

`com.edoframework::edo-lorem:3.0.0` and `fish.genius::genius-lorem` had the API of
taihei-lorem 4.x in the packages `com.edoframework.lorem` and `genius.lorem`. Replace either
package with `dev.taihei.mihon` in your imports (and `genius.i18n` or `com.edoframework.i18n`
with `dev.taihei.tsuji`), then follow [Coming from taihei-lorem 4.x](#coming-from-taihei-lorem-4x).
genius-lorem 2.0.0 was on an earlier Scala: Taihei Mihon needs Scala 3.9 or later, `zio-json`
1.1, and is under the Apache License 2.0.

## License

Apache License 2.0: see [LICENSE](LICENSE) and [NOTICE](NOTICE). The NOTICE records
the names the library was published under and its licence history:
`fish.genius::genius-lorem` 1.0.0 to 1.1.0 were published under the GPL-3.0 and 2.0.0
under the MIT License; the copyright holder relicensed that code under the Apache
License 2.0, from 3.0.0 on.
