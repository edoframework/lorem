# generators — the LoremIpsum catalog

Package `genius.lorem`. `LoremIpsum` is a single object; every member is a
generator returning a fresh random value per call.

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
| `password: String` | 30 random characters |
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
| `email: String` | `first.last@domain.tld` (name parts sanitized) |
| `url: String` | `https://domain.tld` |
| `phone: String` | `+32` followed by 9 digits |

## Geography

| Member | Returns |
| --- | --- |
| `street: String` | ~3 words |
| `city: String` | an international city name |
| `zipCode: String` | 4 numeric digits |
| `address: String` | `"word word word 42, 1234 City"` |
| `country: String` | an international country name |

## Dates (`java.time.LocalDate`)

| Member | Returns |
| --- | --- |
| `localDate` | a random date (year in [1, 3000]) |
| `today` / `yesterday` / `tomorrow` | current date (UTC) ± 1 day |
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
| `id: String` | a UUID-shaped id (hyphens → underscores) |
| `bool: Boolean` | random boolean |
| `int(bound: Int): Int` | random int in `[1, bound]` (never 0) |
| `long(bound: Int): Long` | random long in `[1, bound]` |
| `double(bound: Double): Double` | random double in `[0, bound)` |
| `times[A](bound: Int, f: Int => A): List[A]` | apply `f` `bound` times → list |
| `languages: Array[Language]` / `language: Language` | the genius-i18n languages |

## Sample images

`SampleImages` holds two base64-encoded PNGs for UI mockups/seed data:

| Member | Returns |
| --- | --- |
| `SampleImages.landscape: String` | base64 landscape PNG |
| `SampleImages.portrait: String` | base64 portrait PNG |

## Usage

```scala
import genius.lorem.LoremIpsum

// a fake person + contact
val person  = LoremIpsum.name                 // "Olivia Hernandez"
val email   = LoremIpsum.email                // "olivia.hernandez@brightwavehub.com"
val address = LoremIpsum.address              // "lorem ipsum dolor 42, 3421 Berlin"

// filler content
val heading = LoremIpsum.title
val body    = LoremIpsum.htmlParagraphs       // <p>…</p><p>…</p>…

// a batch of test rows
val rows = LoremIpsum.times(10, _ => (LoremIpsum.name, LoremIpsum.localDate))
```
