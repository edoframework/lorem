package dev.taihei.mihon

/**
 * The list a given name was drawn from. It is closed on purpose: it says which of a language's two
 * given-name lists, and which form of a gendered family name (Russian Иванов and Иванова), a
 * [[Person]] has. Where a culture has unisex names, such a name is on both lists.
 */
enum Gender:
  case Female, Male
