package com.example.todolist

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

class TodoRealmAdapter(realmResult: OrderedRealmCollection<TodoRealm>) : RealmBaseAdapter<TodoRealm>(realmResult) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vh : ViewHolder
        val view : View

        if (convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_todo, parent, false) // Make new view since there's no value store in convertView

            vh = ViewHolder(view)
            view.tag = vh  // store ViewHolder object in 'view' as type of tag which can hold any object.
        } else {
            view = convertView // use already made view (convertView)
            vh = view.tag as ViewHolder // get already made ViewHolder object from tag property, and change its type to 'ViewHolder"
        }

        if (adapterData != null) {
            val item = adapterData!![position]
            vh.contentTextView.text = item.content.toString()
            vh.datesTextView.text = DateFormat.format("yyyy/MM/dd", item.dates)
        }
        return view // this view will be reused after.
    }

    override fun getItemId(position: Int): Long {
        // if adapterView has a Realm data,
        if (adapterData != null) {
                return adapterData!![position].PK_ID
            }
        return super.getItemId(position)
    }
}

class ViewHolder(view: View) {
    // 전달밭은  view에서 itemContentTextView와 itemDateTextView의 아이디를 가진 텍스트뷰들의 참조를 저
    val contentTextView : TextView = view.findViewById(R.id.itemCotentTextView)
    val datesTextView : TextView = view.findViewById(R.id.itemDateTextView)
}