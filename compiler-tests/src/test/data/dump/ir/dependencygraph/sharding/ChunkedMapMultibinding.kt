// STATEMENTS_PER_MULTIBIND_FUN: 2

/*
 * This test verifies that map multibinding chunking generates private helper methods.
 * With STATEMENTS_PER_MULTIBIND_FUN=2 and 5 map entries, the put() calls should be
 * split into 3 helper methods: contributeMapBindings(2), contributeMapBindings2(2),
 * contributeMapBindings3(1).
 *
 * Expected: The buildMap lambda calls helper methods instead of inline put() calls
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
