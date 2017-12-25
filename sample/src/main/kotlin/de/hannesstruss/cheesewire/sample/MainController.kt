package de.hannesstruss.cheesewire.sample

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.bluelinelabs.conductor.Controller
import de.hannesstruss.cheesewire.conductor.ConductorViewBinder

@SuppressLint("SetTextI18n")
class MainController : Controller() {
  private val views = ConductorViewBinder(this)

  private val someLazy: String by views.lazy { "${Math.random()}" }

  private val text1: TextView by views.bind(R.id.text1)
  private val text2: TextView? by views.bindOptional(R.id.text2)
  private val nonExistingText3: TextView? by views.bindOptional(R.id.does_not_exist)

  private val buttons: List<Button> by views.bindAll(R.id.btn1, R.id.btn2, R.id.btn3)
  private val optionals: List<TextView> by views.bindSome(R.id.text_some1, R.id.text_some2, R.id.does_not_exist)

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    return inflater.inflate(R.layout.controller_main, container, false)
  }

  override fun onAttach(view: View) {
    super.onAttach(view)
    text1.text = "Nice! $someLazy"
    text2?.text = "Hello! $someLazy"
    nonExistingText3?.text = "Cool! $someLazy"

    buttons.forEach {
      it.text = "Button! $someLazy"
    }

    optionals.forEach {
      it.text = "Optional! $someLazy"
    }
  }
}
