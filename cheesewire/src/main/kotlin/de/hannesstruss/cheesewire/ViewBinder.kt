package de.hannesstruss.cheesewire

import android.support.annotation.IdRes
import android.view.View
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

typealias Initializer<T> = (KProperty<*>) -> T

/**
 * Provides property delegates that bind Android views. The views are resolved lazily
 * by calling [findView], and cached until [reset] is called.
 *
 * Also provides a generic lazy property that is cached until [reset] is called. This can
 * be used to cache things derived from the views, such as RxBinding Observables:
 *
 *     val views = ViewBinder { findViewById(it) }
 *     val button by views.bind(R.id.button)
 *     val buttonClicks by views.lazy { button.clicks() }
 */
open class ViewBinder(private val findView: (id: Int) -> View?) {
  private val lazies = mutableSetOf<ResettableLazy<*>>()

  /** Binds a single view */
  @Suppress("UNCHECKED_CAST")
  fun <T : View> bind(@IdRes id: Int): ReadOnlyProperty<Any, T> {
    return this.lazy { prop ->
      findView(id) as T? ?: viewNotFound(id, prop)
    }
  }

  /** Binds a single optional view */
  @Suppress("UNCHECKED_CAST")
  fun <T : View> bindOptional(@IdRes id: Int): ReadOnlyProperty<Any, T?> {
    return this.lazy {
      findView(id) as T?
    }
  }

  /** Binds a list of views, all of which are required to be present */
  @Suppress("UNCHECKED_CAST")
  fun <T : View> bindAll(vararg ids: Int): ReadOnlyProperty<Any, List<T>> {
    return this.lazy { prop ->
      ids.map { id -> findView(id) as T? ?: viewNotFound(id, prop) }
    }
  }

  /**
   * Binds a list of views, of which some may be not present.
   * Non-present views will be omitted from the list.
   */
  @Suppress("UNCHECKED_CAST")
  fun <T : View> bindSome(vararg ids: Int): ReadOnlyProperty<Any, List<T>> {
    return this.lazy {
      ids.map { id -> findView(id) as T? }.filterNotNull()
    }
  }

  /**
   * Generic lazy property delegate that will be reset when [ViewBinder.reset] is called.
   */
  fun <T> lazy(initializer: Initializer<T>): ReadOnlyProperty<Any, T> {
    val lazy = ResettableLazy(initializer)
    lazies.add(lazy)
    return lazy
  }

  /** Resets the view bindings */
  fun reset() {
    lazies.forEach { it.reset() }
  }

  private fun viewNotFound(id: Int, prop: KProperty<*>): Nothing {
    throw IllegalStateException("View with id '$id' for property '${prop.name}' not found")
  }
}
