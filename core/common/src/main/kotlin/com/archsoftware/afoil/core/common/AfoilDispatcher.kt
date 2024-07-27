package com.archsoftware.afoil.core.common

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val afoilDispatcher: AfoilDispatcher)

enum class AfoilDispatcher {
    Default,
    IO
}