package com.rendal.notebook

import android.app.Application
import com.rendal.notebook.di.modules.AppComponent
import com.rendal.notebook.di.modules.ContextModule
import com.rendal.notebook.di.modules.DaggerAppComponent
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())

        appComponent = DaggerAppComponent.builder().contextModule(ContextModule(this)).build()
    }
}