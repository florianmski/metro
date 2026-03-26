// STATEMENTS_PER_MULTIBIND_FUN: 2

/*
 * This test verifies that set multibinding chunking generates private helper methods.
 * With STATEMENTS_PER_MULTIBIND_FUN=2 and 5 set elements, the add() calls should be
 * split into 3 helper methods: contributeSetBindings(2), contributeSetBindings2(2),
 * contributeSetBindings3(1).
 *
 * Expected: The buildSet lambda calls helper methods instead of inline add() calls
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
