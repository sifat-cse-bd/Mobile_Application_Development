package com.example.universityeventapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class SeatAdapter(
    private val context: Context,
    private val list: List<Seat>,
    private val onClick: (Seat) -> Unit
) : BaseAdapter() {

    override fun getCount() = list.size
    override fun getItem(p0: Int) = list[p0]
    override fun getItemId(p0: Int) = p0.toLong()

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_seat, null)
        val seat = list[pos]

        val v = view.findViewById<View>(R.id.seatView)

        when (seat.status) {
            0 -> v.setBackgroundColor(Color.GREEN)
            1 -> v.setBackgroundColor(Color.RED)
            2 -> v.setBackgroundColor(Color.BLUE)
        }

        view.setOnClickListener {
            if (seat.status != 1) {
                onClick(seat)
                notifyDataSetChanged()
            }
        }

        return view
    }
}