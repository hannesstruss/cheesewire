package de.hannesstruss.cheesewire.conductor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.bluelinelabs.conductor.Controller

class TestController : Controller() {
  companion object {
    private const val child1Id = 1
  }

  val views = ConductorViewBinder(this)

  val child1: TextView by views.bind(child1Id)

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    val context = container.context

    val root = FrameLayout(context)

    val child1 = TextView(context)
    child1.id = child1Id
    root.addView(child1)

    return root
  }
}
