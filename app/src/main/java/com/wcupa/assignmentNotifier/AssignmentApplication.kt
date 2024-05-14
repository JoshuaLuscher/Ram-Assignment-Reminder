package com.wcupa.assignmentNotifier

import android.app.Application
import com.wcupa.assignmentNotifier.data.AppContainer
import com.wcupa.assignmentNotifier.data.AppDataContainer

class AssignmentApplication : Application() {
     //AppContainer instance used by the rest of classes to obtain dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
