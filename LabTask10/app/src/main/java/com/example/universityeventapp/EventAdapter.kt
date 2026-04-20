package com.example.universityeventapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(
    private var list: List<Event>,
    private val onClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = list[position]

        holder.view.findViewById<TextView>(R.id.title).text = event.title
        holder.view.setOnClickListener { onClick(event) }
    }

    fun filter(newList: List<Event>) {
        list = newList
        notifyDataSetChanged()
    }
}