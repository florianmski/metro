// STATEMENTS_PER_MULTIBIND_FUN: 2
// ENABLE_GRAPH_SHARDING: true
// KEYS_PER_GRAPH_SHARD: 3

/*
 * This test verifies that multibinding chunking works correctly when combined
 * with graph sharding. Map entries may be contributed from bindings in different
 * shards, and the chunked helper methods must correctly access cross-shard properties.
 *
 * Validation: All 5 map entries present, including those from cross-shard bindings
 */

@SingleIn(AppScope::class) @Inject class Dep1
@SingleIn(AppScope::class) @Inject class Dep2
@SingleIn(AppScope::class) @Inject class Dep3

@DependencyGraph(scope = AppScope::class)
interface TestGraph {
  @Provides @IntoMap @StringKey("a") fun provideA(d: Dep1): String = "a:${d::class.simpleName}"
  @Provides @IntoMap @StringKey("b") fun provideB(d: Dep2): String = "b:${d::class.simpleName}"
  @Provides @IntoMap @StringKey("c") fun provideC(d: Dep3): String = "c:${d::class.simpleName}"
  @Provides @IntoMap @StringKey("d") fun provideD(): String = "d"
  @Provides @IntoMap @StringKey("e") fun provideE(): String = "e"

  val entries: Map<String, String>
}

fun box(): String {
  val graph = createGraph<TestGraph>()
  val map = graph.entries
  return when {
    map.size != 5 -> "FAIL: wrong map size ${map.size}"
    map["a"] != "a:Dep1" -> "FAIL: wrong value for 'a': ${map["a"]}"
    map["b"] != "b:Dep2" -> "FAIL: wrong value for 'b': ${map["b"]}"
    map["c"] != "c:Dep3" -> "FAIL: wrong value for 'c': ${map["c"]}"
    map["d"] != "d" -> "FAIL: wrong value for 'd': ${map["d"]}"
    map["e"] != "e" -> "FAIL: wrong value for 'e': ${map["e"]}"
    else -> "OK"
  }
}
