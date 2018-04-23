package de.hannesstruss.cheesewire.conductor

import android.view.View
import com.bluelinelabs.conductor.Controller
import de.hannesstruss.cheesewire.ViewBinder

private val findView: (Controller) -> (Int) -> View? = { controller ->
  { id ->
    val root = controller.view ?: throw IllegalStateException("Controller doesn't have a view")
    root.findViewById(id)
  }
}

/** A [ViewBinder] that binds views in Conductor controllers */
class ConductorViewBinder(controller: Controller) : ViewBinder(findView(controller)) {
  init {
    val lifecycleListener = object : Controller.LifecycleListener() {
      override fun postDestroyView(controller: Controller) {
        reset()
      }
    }
    controller.addLifecycleListener(lifecycleListener)
  }
}
