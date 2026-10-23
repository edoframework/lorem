# Taihei Framework lorem

A composable, cross-platform **fake-data / Lorem Ipsum generator** for Scala 3:
filler text, names, emails, addresses, dates, files, ids, and sample images.
Cross-compiled for the **JVM and Scala.js**.

Part of the [Taihei Framework](https://taihei.dev), the open-source Scala 3
libraries from Innovenso. The package is `dev.taihei.lorem`.

## Modules

Published on Maven Central under `dev.taihei`:

| Artifact            | Platform | Notes                                          |
| ------------------- | -------- | ---------------------------------------------- |
| `taihei-lorem`      | JVM      | Name lists loaded from classpath resources.    |
| `taihei-lorem_sjs1` | Scala.js | Name lists embedded; runs in the browser/Node. |

```scala
mvn"dev.taihei::taihei-lorem:4.0.1"       // Mill, JVM
mvn"dev.taihei::taihei-lorem::4.0.1"      // Mill, Scala.js
"dev.taihei" %% "taihei-lorem" % "4.0.1"  // sbt, JVM
"dev.taihei" %%% "taihei-lorem" % "4.0.1" // sbt, Scala.js (sbt-scalajs or sbt-crossproject)
```

`shared/` holds the generator and data; `jvm/` and `js/` carry only the
platform-specific resource loader. Depends on
[taihei-i18n](https://github.com/taiheiframework/i18n) for `Language`.

## Stack

- Mill 1.3.1
- Scala 3.9.0 (LTS) · Scala.js 1.22.0
- ZIO 2.1.27 (`zio-json` 1.1, `zio-test`)
- `dev.taihei::taihei-i18n` 4.0.1 · scala-java-time 2.8 and scalajs-java-securerandom (JS)

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

Publishing reads the Central Portal user token and the release key from
`~/.edo-release.env` (a name from the Edo Framework days, kept because the
maintainers' workstations write it for every library; `RELEASE_ENV=…`
points elsewhere) or the environment (`MILL_SONATYPE_USERNAME`,
`MILL_SONATYPE_PASSWORD`, `MILL_PGP_SECRET_BASE64`, `MILL_PGP_PASSPHRASE`).
Releases are signed by the Taihei Framework release key,
fingerprint `5026 B299 6DDB 697E C463  0BCF 14A7 33E2 A507 702F`.

## Coming from edo-lorem 3.0.0 or genius-lorem

4.0.0 has the API of `com.edoframework::edo-lorem:3.0.0` under the library's new name:

- the dependency is `dev.taihei::taihei-lorem`;
- the package is `dev.taihei.lorem`: replace `com.edoframework.lorem` in your imports;
- `LoremIpsum.language` and `LoremIpsum.languages` return `dev.taihei.i18n.Language`
  (taihei-i18n 4.0.0).

Coming from `fish.genius::genius-lorem:2.0.0`, replace `genius.lorem` and `genius.i18n`
instead. 3.0.0 also moved to Scala 3.9 (LTS), so your build needs Scala 3.9 or later, to
`zio-json` 1.1 instead of 0.9, and to the Apache License 2.0.

## Quick start

```scala
import dev.taihei.lorem.LoremIpsum

LoremIpsum.name          // "Olivia Hernandez"
LoremIpsum.email         // "olivia.hernandez@brightwavehub.example"
LoremIpsum.paragraph     // a paragraph of lorem filler
LoremIpsum.address       // "lorem ipsum dolor 42, 1234 Berlin"
LoremIpsum.city          // "Tokyo"
LoremIpsum.localDate     // a random java.time.LocalDate
LoremIpsum.id            // a UUID-shaped identifier
```

Every call returns a fresh random value (backed by `java.security.SecureRandom`),
except `today`, `yesterday` and `tomorrow`, which follow the UTC date. E-mail
addresses and URLs use the reserved `.example` top-level domain, so they never
reach a real host. Highlights by category:

- **Text** — `word`, `words`, `sentence`, `paragraph(s)`, `htmlParagraphs`, `title`, `slug`, `password`
- **People** — `firstName`, `lastName`, `name`
- **Internet** — `email`, `url`, `phone`
- **Geography** — `street`, `city`, `zipCode`, `address`, `country`
- **Dates** — `localDate`, `today`, `yesterday`, `tomorrow`, `futureDate`, `pastDate`
- **Files** — `fileExtension`, `fileExtensionWithoutDot`, `mimeType`
- **Ids / misc** — `id`, `character`, `bool`, `int(bound)` and `long(bound)` (in `[1, bound]`), `double(bound)` (in `[0, bound)`), `times(bound, f)` (one to `bound` items)
- **Images** — `SampleImages.landscape`, `SampleImages.portrait` (base64 PNGs)

City and country names are international (native scripts). `LoremIpsum.language`
returns a random `dev.taihei.i18n.Language`.

## Testing

```scala
import dev.taihei.lorem.LoremIpsum
import zio.test.*

assertTrue(
  LoremIpsum.email.contains("@"),
  LoremIpsum.name.contains(" "), // last names may have spaces: "De La Cruz"
)
```

The suite runs on both platforms (`make test`); JS tests execute under Node.

## License

Apache License 2.0: see [LICENSE](LICENSE) and [NOTICE](NOTICE). The NOTICE records
the licence history: `fish.genius::genius-lorem` 1.0.0 to 1.1.0 were published under
the GPL-3.0 and 2.0.0 under the MIT License; the copyright holder relicensed that code
under the Apache License 2.0, from 3.0.0 on.
