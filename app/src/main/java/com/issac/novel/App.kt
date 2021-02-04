package com.issac.novel

import android.app.Application
import com.issac.novel.db.NovelDataBase
import timber.log.Timber


class App: Application() {

    override fun onCreate() {
        super.onCreate()

        NovelDataBase.create(this.applicationContext)

        Timber.plant(Timber.DebugTree())
    }
}
