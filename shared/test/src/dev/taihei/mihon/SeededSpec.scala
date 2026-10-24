package dev.taihei.mihon

import java.time.*
import java.util.UUID

import zio.test.*

object SeededSpec extends ZIOSpecDefault:

  private val noon  = Instant.parse("2026-10-24T12:00:00Z")
  private val clock = Clock.fixed(noon, ZoneOffset.UTC)

  // A run of draws, so a test compares whole sequences and not one value.
  private def run(lorem: LoremIpsum): List[String] =
    List(lorem.name, lorem.word, lorem.city, lorem.id, lorem.double(10.0).toString, lorem.futureDate.toString)

  def spec = suite("SeededSpec")(
    // The values of seed 42 in this version. They are the same on the JVM and on Scala.js, which
    // this test proves by running on both. A change to a word list or to how a generator draws
    // changes them: update them in that change, and say so in the release notes.
    test("seed 42 and a fixed clock give these values, on every platform") {
      val lorem = LoremIpsum.seeded(42L, clock)
      assertTrue(
        lorem.name == "Teddy Lyons",
        lorem.word == "quas",
        lorem.city == "Wien",
        lorem.id == "f12bbb4b_46ee_4d83_b52c_856caa616abe",
        lorem.double(10.0) == 0.9132461105841794,
        lorem.futureDate == LocalDate.of(2036, 6, 14),
        lorem.pastDate == LocalDate.of(2021, 7, 3),
        lorem.email == "delia.gates@quantumcloudhub.example",
      )
    },
    test("the same seed gives the same values") {
      assertTrue(run(LoremIpsum.seeded(7L, clock)) == run(LoremIpsum.seeded(7L, clock)))
    },
    test("another seed gives other values") {
      assertTrue(run(LoremIpsum.seeded(7L, clock)) != run(LoremIpsum.seeded(8L, clock)))
    },
    test("secure generators do not repeat each other") {
      assertTrue(run(LoremIpsum.secure(clock)) != run(LoremIpsum.secure(clock)))
    },
    test("today is the clock's date in the clock's time zone") {
      // 23:30 UTC is already the next day two hours east.
      val late = Instant.parse("2026-10-24T23:30:00Z")
      val utc  = LoremIpsum.seeded(1L, Clock.fixed(late, ZoneOffset.UTC))
      val east = LoremIpsum.secure(Clock.fixed(late, ZoneId.of("+02:00")))
      assertTrue(
        utc.today == LocalDate.of(2026, 10, 24),
        utc.yesterday == LocalDate.of(2026, 10, 23),
        utc.tomorrow == LocalDate.of(2026, 10, 25),
        east.today == LocalDate.of(2026, 10, 25),
      )
    },
    test("future and past dates are one to a thousand weeks from the clock's today") {
      val lorem = LoremIpsum.seeded(3L, clock)
      val today = LocalDate.of(2026, 10, 24)
      val weeks = (1 to 500).flatMap { _ =>
        Seq(
          java.time.temporal.ChronoUnit.WEEKS.between(today, lorem.futureDate),
          java.time.temporal.ChronoUnit.WEEKS.between(lorem.pastDate, today),
        )
      }
      assertTrue(weeks.nonEmpty, weeks.forall(w => w >= 1 && w <= 1000))
    },
    test("id is a version-4 UUID of the IETF variant, with _ for -") {
      val lorem = LoremIpsum.seeded(5L, clock)
      val uuids = (1 to 200).map(_ => UUID.fromString(lorem.id.replace("_", "-")))
      assertTrue(uuids.nonEmpty, uuids.forall(u => u.version == 4 && u.variant == 2), uuids.toSet.size == 200)
    },
    test("double stays below its bound and refuses one that is not positive and finite") {
      val lorem                  = LoremIpsum.seeded(9L, clock)
      def refused(bound: Double) = scala.util.Try(lorem.double(bound)).isFailure
      assertTrue(
        (1 to 1000).map(_ => lorem.double(0.5)).forall(d => d >= 0.0 && d < 0.5),
        refused(0.0),
        refused(-1.0),
        refused(Double.NaN),
        refused(Double.PositiveInfinity),
      )
    },
  )

end SeededSpec
