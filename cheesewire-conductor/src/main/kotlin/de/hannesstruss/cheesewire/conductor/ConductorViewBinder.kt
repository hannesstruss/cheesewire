package de.hannesstruss.cheesewire.conductor

import com.bluelinelabs.conductor.Controller
import de.hannesstruss.cheesewire.ViewBinder

/** A [ViewBinder] that binds views in Conductor controllers */
class ConductorViewBinder(controller: Controller) : ViewBinder({ id -> controller.view?.findViewById(id) }) {
  init {
    val lifecycleListener = object : Controller.LifecycleListener() {
      override fun postDestroyView(controller: Controller) {
        reset()
      }
    }
    controller.addLifecycleListener(lifecycleListener)
  }
}
