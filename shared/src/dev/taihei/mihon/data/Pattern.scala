package dev.taihei.mihon.data

import java.util.Random

// Fills the patterns of the data files. In a code pattern (postal codes, phone numbers) `#` is a
// digit 0 to 9, `N` a digit 1 to 9 and `?` an ASCII capital letter; anything else stays as it is.
// Digits are ASCII in every language: localised digits are for a formatter such as Tsuji.
private[mihon] object Pattern:

  def code(pattern: String, random: Random): String =
    val out = new StringBuilder
    pattern.foreach {
      case '#' => out.append(('0' + random.nextInt(10)).toChar)
      case 'N' => out.append(('1' + random.nextInt(9)).toChar)
      case '?' => out.append(('A' + random.nextInt(26)).toChar)
      case c   => out.append(c)
    }
    out.toString

  // `{name}` placeholders, replaced in order of appearance by `value(name)`, which draws as it
  // goes: the order of the draws is the order of the placeholders. An unknown name throws, since
  // the build checks the names.
  def template(pattern: String, value: String => String): String =
    val out = new StringBuilder
    var i   = 0
    while i < pattern.length do
      val open = pattern.indexOf('{', i)
      if open < 0 then
        out.append(pattern.substring(i))
        i = pattern.length
      else
        val close = pattern.indexOf('}', open)
        if close < 0 then throw new IllegalStateException(s"an unclosed placeholder in $pattern")
        out.append(pattern.substring(i, open))
        out.append(value(pattern.substring(open + 1, close)))
        i = close + 1
    out.toString

  end template

end Pattern
