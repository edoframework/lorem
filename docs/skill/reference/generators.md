# generators: the LoremIpsum catalog

Package `dev.taihei.mihon`. `LoremIpsum` is a final class; every member draws a new value from the
generator's own `java.util.Random` (seeded) or `SecureRandom` (secure).

## Creating a generator

| Member | Returns |
| --- | --- |
| `LoremIpsum.seeded(seed: Long, clock: Clock = Clock.systemUTC()): LoremIpsum` | values that repeat for the same seed, clock and Mihon version, on the JVM and Scala.js |
| `LoremIpsum.secure(clock: Clock = Clock.systemUTC()): LoremIpsum` | values from a `SecureRandom`, which do not repeat |

## Text

| Member | Returns |
| --- | --- |
| `word: String` | one lorem word |
| `words: String` | ~10 words, space-separated |
| `sentence: String` | ~10 words ending in `.` |
| `sentences: String` | ~5 sentences |
| `paragraph: String` | ~6 sentences |
| `paragraphs: String` | ~4 paragraphs |
| `htmlParagraphs: String` | paragraphs wrapped in `<p>…</p>` |
| `title: String` | ~5 capitalized words |
| `description: String` | alias for `sentences` |
| `slug: String` | path-like `word/word/word` |
| `password: String` | 30 random characters (fake data, not a credential) |
| `character: String` | one character (letter/digit/symbol) |

## People

| Member | Returns |
| --- | --- |
| `firstName: String` | a male or female given name (50/50) |
| `lastName: String` | a surname |
| `name: String` | `"FirstName LastName"` |

## Internet

| Member | Returns |
| --- | --- |
| `email: String` | `first.last@name.example` (name parts sanitized; the domain is always under the reserved `.example` TLD) |
| `url: String` | `https://name.example` |
| `phone: String` | `+32` followed by 9 digits, each 0-9 |

## Geography

| Member | Returns |
| --- | --- |
| `street: String` | ~3 words |
| `city: String` | an international city name |
| `zipCode: String` | 4 digits, each 0-9 |
| `address: String` | `"word word word 42, 1234 City"` |
| `country: String` | an international country name |

## Dates (`java.time.LocalDate`)

| Member | Returns |
| --- | --- |
| `localDate` | a random date (year in [1, 3000], any month, day of month in [1, 28]) |
| `today` / `yesterday` / `tomorrow` | the clock's date, in the clock's zone, and ± 1 day |
| `futureDate` / `pastDate` | today ± a random number of weeks |

## Files

| Member | Returns |
| --- | --- |
| `fileExtension: String` | e.g. `.pdf` |
| `fileExtensionWithoutDot: String` | e.g. `pdf` |
| `mimeType: String` | e.g. `application/pdf` |

## Ids & primitives

| Member | Returns |
| --- | --- |
| `id: String` | a version-4 UUID drawn from the generator (hyphens → underscores) |
| `bool: Boolean` | random boolean |
| `int(bound: Int): Int` | random int in `[1, bound]` (never 0) |
| `long(bound: Int): Long` | random long in `[1, bound]`; the bound is an `Int` |
| `double(bound: Double): Double` | random double in `[0, bound)`; refuses a bound that is not positive and finite |
| `times[A](bound: Int, f: Int => A): List[A]` | `f` applied to `1..n` for a random `n` in `[1, bound]` → one to `bound` items |
| `language: Language` | one of `dev.taihei.tsuji.Language.values` |

## Sample images

`SampleImages` is an object with two base64-encoded PNGs (constants, not random) for mock-ups and seed data:

| Member | Returns |
| --- | --- |
| `SampleImages.landscape: String` | base64 landscape PNG, 399 by 310 pixels |
| `SampleImages.portrait: String` | base64 portrait PNG, 160 by 240 pixels |

## Usage

```scala
import dev.taihei.mihon.LoremIpsum

val lorem = LoremIpsum.seeded(42L)

// a fake person + contact
val person  = lorem.name                 // the same name on every run
val email   = lorem.email
val address = lorem.address              // "lorem ipsum dolor 42, 3421 Berlin"

// filler content
val heading = lorem.title
val body    = lorem.htmlParagraphs       // <p>…</p><p>…</p>…

// a batch of test rows
val rows = lorem.times(10, _ => (lorem.name, lorem.localDate))
```
