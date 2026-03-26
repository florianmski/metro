// STATEMENTS_PER_MULTIBIND_FUN: 2

/*
 * This test verifies that map multibinding chunking works correctly.
 * With STATEMENTS_PER_MULTIBIND_FUN=2 and 5 map entries, the put() calls
 * should be split across 3 private helper methods (2+2+1).
 *
 * Validation: All 5 map entries are present and have correct values
 */

@DependencyGraph
interface TestGraph {
  @Provides @IntoMap @StringKey("a") fun provideA(): String = "valueA"
  @Provides @IntoMap @StringKey("b") fun provideB(): String = "valueB"
  @Provides @IntoMap @StringKey("c") fun provideC(): String = "valueC"
  @Provides @IntoMap @StringKey("d") fun provideD(): String = "valueD"
  @Provides @IntoMap @StringKey("e") fun provideE(): String = "valueE"

  val entries: Map<String, String>
}

fun box(): String {
  val graph = createGraph<TestGraph>()
  val map = graph.entries
  return when {
    map.size != 5 -> "FAIL: wrong map size ${map.size}"
    map["a"] != "valueA" -> "FAIL: wrong value for 'a': ${map["a"]}"
    map["b"] != "valueB" -> "FAIL: wrong value for 'b': ${map["b"]}"
    map["c"] != "valueC" -> "FAIL: wrong value for 'c': ${map["c"]}"
    map["d"] != "valueD" -> "FAIL: wrong value for 'd': ${map["d"]}"
    map["e"] != "valueE" -> "FAIL: wrong value for 'e': ${map["e"]}"
    else -> "OK"
  }
}
