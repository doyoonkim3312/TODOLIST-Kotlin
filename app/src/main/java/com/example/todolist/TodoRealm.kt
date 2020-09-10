package com.example.todolist

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


// Using Realm DB by creating Model class and inherits RealmObject class
// the class below is the 'Model Class' inherits RealmObject() class.
open class TodoRealm (
    @PrimaryKey var PK_ID : Long = 0,
    var content : String = "",
    var dates : Long = 0
) : RealmObject() {

}