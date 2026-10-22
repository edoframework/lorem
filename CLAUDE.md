# CLAUDE.md

Guidance for Claude Code in this repository: **Taihei Framework lorem** (`dev.taihei::taihei-lorem`),
a cross-platform fake-data and Lorem Ipsum generator for Scala 3 on the JVM and Scala.js. Part of
the Taihei Framework; the family's rules come from the parent folder's `CLAUDE.md` when this repo is
checked out under `~/Projects/Taihei`.

## Commands

```bash
make validate          # scalafix, scalafmt, compile, test and MiMa, JVM and Scala.js
make mima              # binary compatibility with the newest release tag only
make test              # ./mill __.test.testForked (JS tests run under Node)
make publish-dry-run   # the bundle Maven Central would receive, written to out/
make release-prepare TYPE=patch|minor|major   # then review RELEASE_x_y_z.md
make release-finalize  # publishes to Maven Central: only with the maintainer's yes
```

## Layout

- `shared/src/dev/taihei/lorem/`: `LoremIpsum`, `SampleImages`, and the word lists in
  `sources/`.
- `shared/resources/dev/taihei/lorem/sources/`: the three JSON name lists, read from the
  classpath on the JVM.
- `jvm/src/…` and `js/src/…`: the platform `ResourceLoader`; the JS one embeds the same JSON, so
  a change to a list is made in both places.
- `shared/test/src/…`: zio-test specs, run on both platforms.
- `docs/skill/`: the API reference for the library's Claude Code skill.

## Rules

- Whatever is in `shared/` compiles on Scala.js: no JVM-only API there.
- The public API follows semantic versioning; a break is a major release with a migration section
  in the README.
- Every dependency comes from Maven Central under an Apache-compatible licence; the build needs no
  credentials. taihei-i18n is pinned in `V.taiheiI18n` and released first.
- Data in the lists is generic (common names, places, MIME types): nothing taken from a client or a
  person.
- Work is tracked in Jira project TAIHEI; commits are `TAIHEI-12: subject` with a body that explains the
  change to a reader without access to the ticket.
