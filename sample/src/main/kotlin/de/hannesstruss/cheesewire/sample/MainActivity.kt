package de.hannesstruss.cheesewire.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction

class MainActivity : AppCompatActivity() {
  private lateinit var router: Router

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val root = findViewById<ViewGroup>(R.id.root)
    router = Conductor.attachRouter(this, root, savedInstanceState)
    if (!router.hasRootController()) {
      router.pushController(RouterTransaction.with(MainController()))
    }
  }
}
