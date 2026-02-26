// STATEMENTS_PER_MULTIBIND_FUN: 2

/*
 * This test verifies that set multibinding chunking works correctly.
 * With STATEMENTS_PER_MULTIBIND_FUN=2 and 5 set elements, the add() calls
 * should be split across 3 private helper methods (2+2+1).
 *
 * Validation: All 5 set elements are present
 */

@DependencyGraph
interface TestGraph {
  @Provides @IntoSet fun provideA(): String = "a"
  @Provides @IntoSet fun provideB(): String = "b"
  @Provides @IntoSet fun provideC(): String = "c"
  @Provides @IntoSet fun provideD(): String = "d"
  @Provides @IntoSet fun provideE(): String = "e"

  val elements: Set<String>
}

fun box(): String {
  val graph = createGraph<TestGraph>()
  val set = graph.elements
  return when {
    set.size != 5 -> "FAIL: wrong set size ${set.size}"
    "a" !in set -> "FAIL: missing 'a'"
    "b" !in set -> "FAIL: missing 'b'"
    "c" !in set -> "FAIL: missing 'c'"
    "d" !in set -> "FAIL: missing 'd'"
    "e" !in set -> "FAIL: missing 'e'"
    else -> "OK"
  }
}
