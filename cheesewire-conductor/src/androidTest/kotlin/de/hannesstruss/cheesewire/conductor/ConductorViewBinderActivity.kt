package de.hannesstruss.cheesewire.conductor

import android.app.Activity
import android.os.Bundle
import android.widget.FrameLayout
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction

class ConductorViewBinderActivity : Activity() {
  lateinit var router: Router
  lateinit var controller: TestController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val container = FrameLayout(this)
    setContentView(container)

    router = Conductor.attachRouter(this, container, savedInstanceState)
    router.pushController(RouterTransaction.with(InitialController()))
    controller = TestController()
  }
}
