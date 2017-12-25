package de.hannesstruss.cheesewire

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.widget.TextView
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import java.util.concurrent.atomic.AtomicInteger

@RunWith(AndroidJUnit4::class)
class CheeseWireInstrumentedTest {
  @get:Rule val activityTestRule = ActivityTestRule(CheeseWireTestActivity::class.java)
  @get:Rule val thrown = ExpectedException.none()

  val activity get() = activityTestRule.activity

  val timesSearchedView = AtomicInteger(0)
  val findView: (Int) -> View? = { id: Int ->
    timesSearchedView.incrementAndGet()
    activity.findViewById(id)
  }
  val viewBinder = ViewBinder(findView)

  val child1: TextView by viewBinder.bind(CheeseWireTestActivity.child1Id)
  val child2: TextView? by viewBinder.bindOptional(CheeseWireTestActivity.child2Id)

  val throwingNonExistingChild: TextView by viewBinder.bind(CheeseWireTestActivity.doesNotExistId)
  val nonExistingChild: TextView? by viewBinder.bindOptional(CheeseWireTestActivity.doesNotExistId)

  val allExistingChildren: List<TextView> by viewBinder.bindAll(
      CheeseWireTestActivity.child1Id, CheeseWireTestActivity.child2Id)
  val someMissingChildren: List<TextView> by viewBinder.bindAll(
      CheeseWireTestActivity.child1Id, CheeseWireTestActivity.doesNotExistId)
  val someExistingChildren: List<TextView> by viewBinder.bindSome(
      CheeseWireTestActivity.child1Id, CheeseWireTestActivity.doesNotExistId)

  @Before fun setUp() {
  }

  @Test fun lazilyFindsViews() {
    main {
      child1.text = "Hello!"
      child1.text = "Nice!"
    }

    assertThat(timesSearchedView.get()).isEqualTo(1)
  }

  @Test fun findsOptionalViews() {
    main {
      assertThat(child2).isNotNull()
      assertThat(child2?.id).isEqualTo(CheeseWireTestActivity.child2Id)
    }
  }

  @Test fun throwsWhenRequiredViewDoesNotExist() {
    thrown.expect(IllegalStateException::class.java)
    thrown.expectMessage("View with id '${CheeseWireTestActivity.doesNotExistId}' " +
        "for property 'throwingNonExistingChild' not found")

    throwingNonExistingChild.text = "Boom!"
  }

  @Test fun findsList() {
    main {
      assertThat(allExistingChildren).hasSize(2)
      assertThat(allExistingChildren[0].id).isEqualTo(CheeseWireTestActivity.child1Id)
      assertThat(allExistingChildren[1].id).isEqualTo(CheeseWireTestActivity.child2Id)
    }
  }

  @Test fun throwsWhenSomeRequiredViewsInListDontExist() {
    thrown.expect(IllegalStateException::class.java)
    thrown.expectMessage("View with id '${CheeseWireTestActivity.doesNotExistId}' " +
        "for property 'someMissingChildren' not found")
    assertThat(someMissingChildren.size).isEqualTo(1)
  }

  @Test fun findsOptionalList() {
    main {
      assertThat(someExistingChildren).hasSize(1)
      assertThat(someExistingChildren[0].id).isEqualTo(CheeseWireTestActivity.child1Id)
    }
  }

  @Test fun nonExistingViewIsNull() {
    main {
      assertThat(nonExistingChild).isNull()
    }
  }

  @Test fun resets() {
    main {
      child1.text = "Hello!"
      viewBinder.reset()
      child1.text = "Nice!"
    }

    assertThat(timesSearchedView.get()).isEqualTo(2)
  }

  private fun main(block: () -> Unit) {
    InstrumentationRegistry.getInstrumentation().runOnMainSync(block)
  }
}
