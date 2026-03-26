// STATEMENTS_PER_MULTIBIND_FUN: 2

/*
 * This test verifies that map multibinding chunking works correctly with the
 * MapFactory/MapProviderFactory path (triggered when map is injected as
 * Provider<Map<K, V>> or Map<K, Provider<V>>).
 * With STATEMENTS_PER_MULTIBIND_FUN=2 and 5 map entries, the put() calls
 * should be split across 3 private helper methods (2+2+1).
 *
 * Validation: All 5 map entries are present via both Provider<Map> and Map<K, Provider<V>>
 */

@DependencyGraph
interface TestGraph {
  @Provides @IntoMap @StringKey("a") fun provideA(): String = "valueA"
  @Provides @IntoMap @StringKey("b") fun provideB(): String = "valueB"
  @Provides @IntoMap @StringKey("c") fun provideC(): String = "valueC"
  @Provides @IntoMap @StringKey("d") fun provideD(): String = "valueD"
  @Provides @IntoMap @StringKey("e") fun provideE(): String = "valueE"

  val providerMap: Provider<Map<String, String>>
  val providerValueMap: Map<String, Provider<String>>
}

fun box(): String {
  val graph = createGraph<TestGraph>()

  // Test Provider<Map<String, String>> (MapFactory path)
  val map = graph.providerMap()
  if (map.size != 5) return "FAIL: wrong providerMap size ${map.size}"
  if (map["a"] != "valueA") return "FAIL: wrong value for 'a': ${map["a"]}"
  if (map["b"] != "valueB") return "FAIL: wrong value for 'b': ${map["b"]}"
  if (map["c"] != "valueC") return "FAIL: wrong value for 'c': ${map["c"]}"
  if (map["d"] != "valueD") return "FAIL: wrong value for 'd': ${map["d"]}"
  if (map["e"] != "valueE") return "FAIL: wrong value for 'e': ${map["e"]}"

  // Test Map<String, Provider<String>> (MapProviderFactory path)
  val pmap = graph.providerValueMap
  if (pmap.size != 5) return "FAIL: wrong providerValueMap size ${pmap.size}"
  if (pmap["a"]?.invoke() != "valueA") return "FAIL: wrong provider value for 'a'"
  if (pmap["b"]?.invoke() != "valueB") return "FAIL: wrong provider value for 'b'"
  if (pmap["c"]?.invoke() != "valueC") return "FAIL: wrong provider value for 'c'"
  if (pmap["d"]?.invoke() != "valueD") return "FAIL: wrong provider value for 'd'"
  if (pmap["e"]?.invoke() != "valueE") return "FAIL: wrong provider value for 'e'"

  return "OK"
}
