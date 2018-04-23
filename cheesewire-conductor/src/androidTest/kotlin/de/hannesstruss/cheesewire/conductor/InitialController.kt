package de.hannesstruss.cheesewire.conductor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bluelinelabs.conductor.Controller

class InitialController : Controller() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    return FrameLayout(container.context)
  }
}
