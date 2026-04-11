package com.example.cotactbookapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.Locale

class ContactAdapter(val context: Context, var resource: Int, var contactList : MutableList<Contact>)
    : ArrayAdapter<Contact>(context, resource, contactList){
    override fun getCount(): Int {
        return contactList.count()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)

        val tvAvatar = view.findViewById<TextView>(R.id.tvAvatar)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvPhone = view.findViewById<TextView>(R.id.tvPhone)


        val contact = contactList[position]
        val avatarText = if (contact.name.isNotEmpty()) {
            contact.name.substring(0, 1).uppercase(Locale.getDefault())
        } else {
            "?"
        }
        tvAvatar.text = avatarText
        tvName.text = contact.name
        tvPhone.text = contact.phone
        return view
    }
}