package com.example.todolist

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {
    override fun onCreate() {
        // this onCreate() method called before the activity is created.
        super.onCreate()
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder().name("Todolist.realm").build()
        //Set specific configuration for realm: line above changes the name of the realm file from
        //"default.realm" to "Todolist.realm".
        Realm.setDefaultConfiguration(realmConfig)
        //set customized configuration as default configuration.
    }
}