package com.example.ecommerceapp

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private var list: List<Product>,
    private var isGrid: Boolean,
    private val onCartClick: (Product) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setViewType(grid: Boolean) {
        isGrid = grid
        notifyDataSetChanged()
    }

    fun updateList(newList: List<Product>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGrid) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            if (viewType == 1) R.layout.item_product_grid else R.layout.item_product_list,
            parent, false
        )
        return if (viewType == 1) GridViewHolder(view) else ListViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = list[position]
        if (holder is ListViewHolder) holder.bind(product)
        if (holder is GridViewHolder) holder.bind(product)
    }

    fun remove(position: Int) {
        val temp = list.toMutableList()
        temp.removeAt(position)
        list = temp
        notifyItemRemoved(position)
    }

    fun swap(from: Int, to: Int) {
        val temp = list.toMutableList()
        val item = temp.removeAt(from)
        temp.add(to, item)
        list = temp
        notifyItemMoved(from, to)
    }

    inner class ListViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val name = v.findViewById<TextView>(R.id.name)
        private val price = v.findViewById<TextView>(R.id.price)
        private val btn = v.findViewById<Button>(R.id.cartBtn)

        fun bind(p: Product) {
            name.text = p.name
            price.text = "$${p.price}"
            btn.setOnClickListener { onCartClick(p) }
        }
    }

    inner class GridViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val name = v.findViewById<TextView>(R.id.name)
        private val price = v.findViewById<TextView>(R.id.price)
        private val btn = v.findViewById<ImageButton>(R.id.cartBtn)

        fun bind(p: Product) {
            name.text = p.name
            price.text = "$${p.price}"
            btn.setOnClickListener { onCartClick(p) }
        }
    }
}