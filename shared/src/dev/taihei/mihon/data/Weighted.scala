package dev.taihei.mihon.data

import java.util.Random

// A list of values with integer weights, drawn with one `nextInt(total)` and a binary search over
// the running totals. Integer arithmetic only, so a seed draws the same value on the JVM and on
// Scala.js; the data files guarantee positive weights whose total fits in an Int.
final private[mihon] class Weighted[A] private (values: IndexedSeq[A], running: Array[Int]):

  def size: Int = values.size

  def all: IndexedSeq[A] = values

  def draw(random: Random): A =
    val r  = random.nextInt(running(running.length - 1))
    // The first index whose running total exceeds r.
    var lo = 0
    var hi = running.length - 1
    while lo < hi do
      val mid = (lo + hi) >>> 1
      if running(mid) > r then hi = mid else lo = mid + 1
    values(lo)

end Weighted

private[mihon] object Weighted:

  /**
   * Throws when the list is empty or a weight is not positive: the data is checked by the build,
   * so either is a packaging bug.
   */
  def apply[A](entries: Seq[(A, Int)]): Weighted[A] =
    if entries.isEmpty then throw new IllegalStateException("an empty weighted list")
    val running = new Array[Int](entries.size)
    var total   = 0L
    entries.zipWithIndex.foreach { case ((_, w), i) =>
      if w <= 0 then throw new IllegalStateException(s"a weight that is not positive: $w")
      total += w
      if total > Int.MaxValue then
        throw new IllegalStateException("weights add up beyond Int.MaxValue")
      running(i) = total.toInt
    }
    new Weighted(entries.map(_._1).toIndexedSeq, running)

  /** Every value equally likely. */
  def uniform[A](values: Seq[A]): Weighted[A] = apply(values.map(_ -> 1))

end Weighted
