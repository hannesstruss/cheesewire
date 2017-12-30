package de.hannesstruss.cheesewire

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class ResettableLazy<out T>(
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
