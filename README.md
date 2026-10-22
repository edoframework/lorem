# Edo Framework lorem

A composable, cross-platform **fake-data / Lorem Ipsum generator** for Scala 3:
filler text, names, emails, addresses, dates, files, ids, and sample images.
Cross-compiled for the **JVM and Scala.js**.

Part of the [Edo Framework](https://edoframework.com), the open-source Scala 3
libraries from Innovenso. The package is `com.edoframework.lorem`.

## Modules

Published on Maven Central under `com.edoframework`:

| Artifact          | Platform | Notes                                          |
| ----------------- | -------- | ---------------------------------------------- |
| `edo-lorem`       | JVM      | Name lists loaded from classpath resources.    |
| `edo-lorem_sjs1`  | Scala.js | Name lists embedded; runs in the browser/Node. |

```scala
mvn"com.edoframework::edo-lorem:3.0.0"      // Mill, JVM
mvn"com.edoframework::edo-lorem::3.0.0"     // Mill, Scala.js
"com.edoframework" %%% "edo-lorem" % "3.0.0" // sbt
```

`shared/` holds the generator and data; `jvm/` and `js/` carry only the
platform-specific resource loader. Depends on
[edo-i18n](https://github.com/edoframework/i18n) for `Language`.

## Stack

- Mill 1.3.1
- Scala 3.9.0 (LTS) · Scala.js 1.22.0
- ZIO 2.1.27 (`zio-json` 1.1, `zio-test`)
- `com.edoframework::edo-i18n` 3.0.0 · scala-java-time 2.8 and scalajs-java-securerandom (JS)

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
`~/.edo-release.env` or the environment (`MILL_SONATYPE_USERNAME`,
`MILL_SONATYPE_PASSWORD`, `MILL_PGP_SECRET_BASE64`, `MILL_PGP_PASSPHRASE`).
Releases are signed by `Edo Framework Releases <releases@edoframework.com>`,
fingerprint `5026 B299 6DDB 697E C463  0BCF 14A7 33E2 A507 702F`.

## Migrating from genius-lorem

3.0.0 is the API of `fish.genius::genius-lorem:2.0.0` under its new name:

- the dependency is `com.edoframework::edo-lorem`;
- the package is `com.edoframework.lorem`: replace `genius.lorem` in your imports;
- `LoremIpsum.language` and `LoremIpsum.languages` return `com.edoframework.i18n.Language`
  (edo-i18n 3.0.0) instead of `genius.i18n.Language`;
- it is compiled with Scala 3.9 (LTS), so your build needs Scala 3.9 or later;
- it brings `zio-json` 1.1 instead of 0.9;
- the licence is Apache 2.0.

## Quick start

```scala
import com.edoframework.lorem.LoremIpsum

LoremIpsum.name          // "Olivia Hernandez"
LoremIpsum.email         // "olivia.hernandez@brightwavehub.com"
LoremIpsum.paragraph     // a paragraph of lorem filler
LoremIpsum.address       // "lorem ipsum dolor 42, 1234 Berlin"
LoremIpsum.city          // "Tokyo"
LoremIpsum.localDate     // a random java.time.LocalDate
LoremIpsum.id            // a UUID-shaped identifier
```

Every call returns a fresh random value (backed by `java.security.SecureRandom`).
Highlights by category:

- **Text** — `word`, `words`, `sentence`, `paragraph(s)`, `htmlParagraphs`, `title`, `slug`, `password`
- **People** — `firstName`, `lastName`, `name`
- **Internet** — `email`, `url`, `phone`
- **Geography** — `street`, `city`, `zipCode`, `address`, `country`
- **Dates** — `localDate`, `today`, `yesterday`, `tomorrow`, `futureDate`, `pastDate`
- **Files** — `fileExtension`, `fileExtensionWithoutDot`, `mimeType`
- **Ids / misc** — `id`, `character`, `bool`, `int(bound)`, `long(bound)`, `double(bound)`, `times(n)(f)`
- **Images** — `SampleImages.landscape`, `SampleImages.portrait` (base64 PNGs)

City and country names are international (native scripts). `LoremIpsum.language`
returns a random `com.edoframework.i18n.Language`.

## Testing

```scala
import com.edoframework.lorem.LoremIpsum
import zio.test.*

assertTrue(
  LoremIpsum.email.contains("@"),
  LoremIpsum.name.split(" ").length == 2,
)
```

The suite runs on both platforms (`make test`); JS tests execute under Node.

## License

Apache License 2.0: see [LICENSE](LICENSE) and [NOTICE](NOTICE). Versions up to and
including 2.0.0 (`fish.genius::genius-lorem`) remain available under the MIT License.
