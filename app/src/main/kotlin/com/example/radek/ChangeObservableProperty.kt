package com.example.radek

import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

abstract class ChangeObservableProperty<T>(initialValue: T) : ObservableProperty<T>(initialValue) {
    override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean {
        if(oldValue == null && newValue == null) return false
        if(oldValue == null) return true
        if(newValue == null) return true
        if(oldValue == newValue) return false
        return true
    }
}
