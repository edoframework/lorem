# genius-lorem

A composable, cross-platform **fake-data / Lorem Ipsum generator** for Scala 3 —
filler text, names, emails, addresses, dates, files, ids, and sample images.
Cross-compiled for the **JVM and Scala.js**.

Extracted from the Genius Fish platform as a standalone library; the package is
`genius.lorem`.

## Modules

| Artifact          | Platform | Notes                                          |
| ----------------- | -------- | ---------------------------------------------- |
| `genius-lorem`    | JVM      | Name lists loaded from classpath resources.    |
| `genius-lorem_sjs1` | Scala.js | Name lists embedded; runs in the browser/Node. |

`shared/` holds the generator and data; `jvm/` and `js/` carry only the
platform-specific resource loader. Depends on
[`genius-i18n`](https://github.com/genius-fish/i18n) for the `Language` enum.

## Stack

- Mill 1.1.5 · Scala 3.8.3 · Scala.js 1.21.0
- ZIO 2.1.26 (`zio-json`, `zio-test`)
- `fish.genius::genius-i18n` · scala-java-time + scalajs-java-securerandom (JS)

## Build

```sh
make compile        # ./mill __.compile           (JVM + JS)
make test           # ./mill __.test.testForked    (JVM + JS; JS runs on Node)
make validate       # fix + fmt + compile + test
make publishLocal   # to ~/.ivy2/local
```

> Resolving the `genius-i18n` dependency: the build includes the local ivy
> repository, so `make publishLocal` in the `i18n` repo makes it available here.
> For CI / external consumers, `genius-i18n` must be published to
> `repo.genius.fish`.

## Quick start

```scala
import genius.lorem.LoremIpsum

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
returns a random `genius.i18n.Language`.

## Testing

```scala
import genius.lorem.LoremIpsum
import zio.test.*

assertTrue(
  LoremIpsum.email.contains("@"),
  LoremIpsum.name.split(" ").length == 2,
)
```

The suite runs on both platforms (`make test`); JS tests execute under Node.

## License

MIT — see [LICENSE](LICENSE).
