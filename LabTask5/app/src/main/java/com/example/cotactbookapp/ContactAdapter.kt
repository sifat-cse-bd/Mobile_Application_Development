package com.example.cotactbookapp

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.Locale

class ContactAdapter(context: Context, var resource: Int, var contactList : MutableList<Contact>)
    : ArrayAdapter<Contact>(context, resource, contactList){

    private val colors = arrayOf(
        Color.parseColor("#8E44AD"), // Deep Purple
        Color.parseColor("#E74C3C"), // Crimson Red
        Color.parseColor("#27AE60"), // Emerald Green
        Color.parseColor("#3498DB"), // Bright Blue
        Color.parseColor("#F39C12"), // Orange
        Color.parseColor("#9B59B6"), // Amethyst
        Color.parseColor("#1ABC9C"), // Turquoise
        Color.parseColor("#E67E22"), // Carrot
        Color.parseColor("#34495E"), // Wet Asphalt
        Color.parseColor("#16A085")  // Green Sea
    )

    class ViewHolder {
        lateinit var tvAvatar: TextView
        lateinit var tvName: TextView
        lateinit var tvPhone: TextView
    }

    override fun getCount(): Int {
        return contactList.count()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false)
            holder = ViewHolder()
            holder.tvAvatar = view.findViewById(R.id.tvAvatar)
            holder.tvName = view.findViewById(R.id.tvName)
            holder.tvPhone = view.findViewById(R.id.tvPhone)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val contact = contactList[position]
        val avatarText = if (contact.name.isNotEmpty()) {
            contact.name.substring(0, 1).uppercase(Locale.getDefault())
        } else {
            "?"
        }
        holder.tvAvatar.text = avatarText
        holder.tvName.text = contact.name
        holder.tvPhone.text = contact.phone

        // Set circular background color based on first letter
        val firstChar = if (contact.name.isNotEmpty()) contact.name[0].uppercaseChar() else '?'
        val colorIndex = if (firstChar.isLetter()) (firstChar - 'A') % colors.size else 0
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.OVAL
        drawable.setColor(colors[colorIndex])
        holder.tvAvatar.background = drawable

        return view
    }
}