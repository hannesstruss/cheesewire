package de.hannesstruss.cheesewire

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

class CheeseWireTestActivity : Activity() {
  companion object {
    const val child1Id = 1
    const val child2Id = 2
    const val doesNotExistId = 999
  }

  lateinit var root: ViewGroup

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    root = FrameLayout(this)

    val child1 = TextView(this)
    child1.id = child1Id
    root.addView(child1)

    val child2 = TextView(this)
    child2.id = child2Id
    root.addView(child2)

    setContentView(root)
  }
}
