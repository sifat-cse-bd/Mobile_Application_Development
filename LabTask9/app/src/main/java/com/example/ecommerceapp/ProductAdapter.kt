package com.example.ecommerceapp

import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.analytics.ecommerce.Product

class ProductAdapter(
    private var list: List<Product>,
    private var isGrid: Boolean,
    private val onCartClick: (Product) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
}