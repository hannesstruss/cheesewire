package de.hannesstruss.cheesewire.sample

import com.bluelinelabs.conductor.Controller
import de.hannesstruss.cheesewire.conductor.ConductorViewBinder

abstract class BaseController : Controller() {
  protected val views = ConductorViewBinder(this)
}
