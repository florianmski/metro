// STATEMENTS_PER_MULTIBIND_FUN: 2
// ENABLE_GRAPH_SHARDING: true
// KEYS_PER_GRAPH_SHARD: 3

/*
 * This test verifies that multibinding chunking works with graph sharding.
 * Helper methods must correctly access binding properties across shard boundaries.
 *
 * Expected: Chunked helper methods with cross-shard property access
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
