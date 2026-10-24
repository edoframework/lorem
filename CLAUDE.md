# CLAUDE.md

Guidance for Claude Code in this repository: **Taihei Mihon** (`dev.taihei::taihei-mihon`), a
cross-platform fake-data and Lorem Ipsum generator for Scala 3 on the JVM and Scala.js, the
successor of taihei-lorem 4.x. Part of the Taihei Framework; the family's rules come from the
parent folder's `CLAUDE.md` when this repo is checked out under `~/Projects/Taihei`.

## Commands

```bash
make validate          # repo-guard, scalafix, scalafmt, compile, test and MiMa, JVM and Scala.js
make repo-guard        # Maven Central only: no other repository, every dev.taihei pin on Central
make mima              # binary compatibility with the newest 5.x release tag on Maven Central
make test              # ./mill __.test.testForked (JS tests run under Node)
make publish-dry-run   # the bundle Maven Central would receive, written to out/
make release-prepare TYPE=patch|minor|major   # then review RELEASE_x_y_z.md
make release-finalize  # GitHub Packages and the read-back: only with the maintainer's yes
make release-github    # after the push: the bundle on an immutable GitHub Release
```

Releases follow ADR 0003: one signed bundle, uploaded unchanged to this repository's GitHub
Packages registry, read back, tagged and pushed, kept on an immutable GitHub Release, and taken
to Maven Central by the next release train in nakama. A security fix (`EMBARGO=1`) goes to
Central first with `make publish`. The publish token lives in `~/.taihei-packages-publish.env`,
the read token in Coursier's credentials file; never read or print either.

## Layout

- `shared/src/dev/taihei/mihon/`: `LoremIpsum` (a class, created by `seeded` or `secure`),
  `SampleImages`, and the internal word lists in `sources/`.
- `shared/resources/dev/taihei/mihon/sources/`: the three JSON name lists, the only copy. The
  JVM reads them from the classpath; for Scala.js the build generates `EmbeddedLists` from them.
- `jvm/src/…` and `js/src/…`: the platform `ResourceLoader`.
- `shared/test/src/…`: zio-test specs, run on both platforms. `SeededSpec` pins the values of
  seed 42 and `NameListsSpec` the lists: a change to a list or a draw updates them.
- `docs/skill/`: the API reference for the library's Claude Code skill.

## Rules

- Whatever is in `shared/` compiles on Scala.js: no JVM-only API there.
- No global generator and no wall clock: every draw goes through the instance's `Random`, using
  only `nextInt`, `nextInt(n)`, `nextLong`, `nextBoolean` and `nextDouble`, so a seed gives the
  same values on both platforms.
- The public API is `LoremIpsum` and `SampleImages`; everything else is `private[mihon]`. It
  follows semantic versioning; a break is a major release with a migration section in the README.
- Every dependency comes from Maven Central under an Apache-compatible licence; the build needs no
  credentials. taihei-tsuji is pinned in `V.taiheiTsuji` and released first.
- Data in the lists is generic (common names, places, MIME types): nothing taken from a client or a
  person.
- Work is tracked in Jira project TAIHEI; commits are `TAIHEI-12: subject` with a body that explains the
  change to a reader without access to the ticket.
