package com.example.pokectcollection.rule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * [MainCoroutineRule] JUnit rule for setting a main coroutine dispatcher for tests.
 *
 * This rule allows for the use of a [TestDispatcher] in place of the standard main dispatcher during tests,
 * providing a controlled environment for coroutine execution and testing.
 *
 * @property dispatcher the [TestDispatcher] to be used as the main dispatcher in tests. Defaults to [StandardTestDispatcher].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineRule(
    private val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    /**
     * Sets the main dispatcher to the provided [TestDispatcher] before each test.
     *
     * @param description the description of the test.
     */
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    /**
     * Resets the main dispatcher to the original dispatcher after each test.
     *
     * @param description the description of the test.
     */
    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
