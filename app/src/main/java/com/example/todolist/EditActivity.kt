package com.example.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import splitties.alertdialog.alertDialog
import splitties.alertdialog.okButton
import splitties.alertdialog.onShow
import splitties.alertdialog.positiveButton
import splitties.views.textColorResource
import java.util.*

class EditActivity : AppCompatActivity() {

    val realm = Realm.getDefaultInstance() // get instance for using realm
    val calendar : Calendar = Calendar.getInstance() // get calendar instance It's initialized to the date when the instance is created.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)


        val intentID = intent.getLongExtra("segueId", -1L) // make a validation check; get id value from intent data, if there're no data, set default value(-1L)
        println(intentID)
        if (intentID == -1L) {
            addMode()
        } else {
            updateMode(intentID)
        }

        // If user select the specific date on calendar view, changes are applied to calendar view
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
    }

    private fun addMode() {
        deleteFAB.hide() // hide delete Floating Action Button
        doneFAB.setOnClickListener {
            addTodo()
        }
    }

    private fun updateMode(id : Long) {
        val targetItem = realm.where<TodoRealm>().equalTo("PK_ID", id).findFirst()!!
        todoEditTextBox.setText(targetItem.content)
        calendarView.date = targetItem.dates // Sync calendar view's date and targetItem dates.

        doneFAB.setOnClickListener {
            updateTodo(id)
        }

        deleteFAB.setOnClickListener {
            deleteTodo(id)
        }
    }

    private fun addTodo() {
        // Every tasks except 'read' using Realm should be executed inside of Transaction.
        realm.beginTransaction() // Start transaction with Realm DB

        val newItem = realm.createObject<TodoRealm>(getNextID()) // Create new Item at TodoRealm with PK_ID as value from getNextID()
        newItem.content = todoEditTextBox.text.toString()
        newItem.dates = calendar.timeInMillis // convert time into Long type.

        realm.commitTransaction() // end transaction with Realm DB

        alertDialog(getString(R.string.add_todo_item)) {
            okButton{ finish() }
        }.onShow{
            positiveButton.textColorResource = R.color.colorPrimary
        }.show()
    }

    private fun updateTodo(id : Long) {
        realm.beginTransaction()

        val targetItem = realm.where<TodoRealm>().equalTo("PK_ID", id).findFirst()!!
        // where<REALM>(): Return RealmQuery and executes following task
        // .equalTo("FieldName", value) : find item from Query that has certain value.
        // .findFirst()!! : if there're no items on 'FieldName" column, return very first data.
        targetItem.content = todoEditTextBox.text.toString()
        targetItem.dates = calendar.timeInMillis

        realm.commitTransaction()

        alertDialog(getString(R.string.updage_todo_item)) {
            okButton { finish() }
        }.onShow{
            positiveButton.textColorResource = R.color.colorPrimary
        }.show()
    }

    private fun deleteTodo(id: Long) {
        realm.beginTransaction()
        val subjectItem = realm.where<TodoRealm>().equalTo("PK_ID", id).findFirst()!!
        subjectItem.deleteFromRealm()
        realm.commitTransaction()

        alertDialog(getString(R.string.delete_todo_item)){
            okButton { finish() }
        }.onShow{
            positiveButton.textColorResource = R.color.colorPrimary
        }.show()
    }

    private fun getNextID() : Int {
        val currentMaxID = realm.where<TodoRealm>().max("PK_ID") // get largest ID value from Realm "TodoRealm"
        if (currentMaxID != null) {
            return currentMaxID.toInt() + 1
        }
        return 0
    }

    override fun onDestroy() {
        // onDestroy() method is in charge of close/eliminating activity
        super.onDestroy()
        realm.close() // Realm instance will be closed when the activity is closed.
    }
}