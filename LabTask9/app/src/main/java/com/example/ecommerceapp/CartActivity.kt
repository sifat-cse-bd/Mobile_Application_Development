package com.example.ecommerceapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val totalPrice = findViewById<TextView>(R.id.totalPrice)

        recyclerView.layoutManager = LinearLayoutManager(this)

        @Suppress("UNCHECKED_CAST")
        val list = intent.getSerializableExtra("cart") as? ArrayList<Product> ?: arrayListOf()

        recyclerView.adapter = ProductAdapter(list, false) {}

        val total = list.sumOf { it.price }
        totalPrice.text = "Total: $$total"
    }
}