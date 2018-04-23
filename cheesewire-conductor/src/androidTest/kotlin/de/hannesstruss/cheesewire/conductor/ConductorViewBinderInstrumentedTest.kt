package de.hannesstruss.cheesewire.conductor

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.bluelinelabs.conductor.RouterTransaction
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConductorViewBinderInstrumentedTest {
  @get:Rule val activityTestRule = ActivityTestRule(ConductorViewBinderActivity::class.java)
  @get:Rule val thrown = ExpectedException.none()

  val activity get() = activityTestRule.activity

  @Test fun findsViews() {
    main { pushController() }

    assertThat(activity.controller.child1.parent).isEqualTo(activity.controller.view)
  }

  @Test fun resets() {
    main {
      pushController()
      val childBeforePop = activity.controller.child1
      popController()
      activity.controller = TestController()
      pushController()
      val childAfterPop = activity.controller.child1

      assertThat(childBeforePop).isNotEqualTo(childAfterPop)
    }
  }

  @Test fun throwsIfControllerHasNoView() {
    thrown.expect(IllegalStateException::class.java)
    thrown.expectMessage("Controller doesn't have a view")

    activity.controller.child1
  }

  private fun pushController() {
    activity.router.pushController(RouterTransaction.with(activity.controller))
  }

  private fun popController() {
    activity.router.popController(activity.controller)
  }

  private fun main(block: () -> Unit) {
    InstrumentationRegistry.getInstrumentation().runOnMainSync(block)
  }
}
