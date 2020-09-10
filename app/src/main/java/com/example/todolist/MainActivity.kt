package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import splitties.activities.start

class MainActivity : AppCompatActivity() {

    val realm = Realm.getDefaultInstance() // get realm instance for use in MainActivity.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        //call the realm query and get all data in query, then sort it in descending order.
        val realmResult = realm.where<TodoRealm>().findAll().sort("dates", Sort.DESCENDING)
        val adapter = TodoRealmAdapter(realmResult)
        listView.adapter = adapter

        //when item data has been changed, apply the change to adapter.
        realmResult.addChangeListener { _ -> adapter.notifyDataSetChanged() }
        listView.setOnItemClickListener { parent, view, position, PK_ID ->

            val idIntent = Intent(this, EditActivity::class.java)
            idIntent.putExtra("segueId", PK_ID)
            startActivity(idIntent)
        }


        fab.setOnClickListener {
            start<EditActivity>()
            // add new todoItem which means, no value segued
        }
        /*findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
         */
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
