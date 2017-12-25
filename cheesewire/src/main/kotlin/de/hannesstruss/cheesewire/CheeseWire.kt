package de.hannesstruss.cheesewire

import android.support.annotation.IdRes
import android.view.View
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

typealias Initializer<T> = (KProperty<*>) -> T

open class ViewBinder(private val findView: (id: Int) -> View?) {
  private val lazies = mutableSetOf<ResettableLazy<*>>()

  @Suppress("UNCHECKED_CAST")
  fun <T : View> bind(@IdRes id: Int): ReadOnlyProperty<Any, T> {
    return this.lazy { prop ->
      findView(id) as T? ?: viewNotFound(id, prop)
    }
  }

  @Suppress("UNCHECKED_CAST")
  fun <T : View> bindOptional(@IdRes id: Int): ReadOnlyProperty<Any, T?> {
    return this.lazy {
      findView(id) as T?
    }
  }

  @Suppress("UNCHECKED_CAST")
  fun <T : View> bindAll(vararg ids: Int): ReadOnlyProperty<Any, List<T>> {
    return this.lazy { prop ->
      ids.map { id -> findView(id) as T? ?: viewNotFound(id, prop) }
    }
  }

  @Suppress("UNCHECKED_CAST")
  fun <T : View> bindSome(vararg ids: Int): ReadOnlyProperty<Any, List<T>> {
    return this.lazy {
      ids.map { id -> findView(id) as T? }.filterNotNull()
    }
  }

  fun <T> lazy(initializer: Initializer<T>): ReadOnlyProperty<Any, T> {
    val lazy = ResettableLazy(initializer)
    lazies.add(lazy)
    return lazy
  }

  fun reset() {
    lazies.forEach { it.reset() }
  }
}

private fun viewNotFound(id: Int, prop: KProperty<*>): Nothing {
  throw IllegalStateException("View with id '$id' for property '${prop.name}' not found")
}

private class ResettableLazy<out T>(
    private val initializer: Initializer<T>
) : ReadOnlyProperty<Any, T> {

  private object EMPTY

  private var value: Any? = EMPTY

  override fun getValue(thisRef: Any, property: KProperty<*>): T {
    if (value == EMPTY) {
      value = initializer(property)
    }

    @Suppress("UNCHECKED_CAST")
    return value as T
  }

  fun reset() {
    value = EMPTY
  }
}
