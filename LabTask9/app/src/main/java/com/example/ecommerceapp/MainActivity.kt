package com.example.ecommerceapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ProductAdapter
    private var isGrid = false
    private val productList = mutableListOf<Product>()
    private val cartList = mutableListOf<Product>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var toggleView: ImageView
    private lateinit var chipGroup: ChipGroup
    private lateinit var cartCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        toggleView = findViewById(R.id.toggleView)
        chipGroup = findViewById(R.id.chipGroup)
        cartCount = findViewById(R.id.cartCount)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main) ?: window.decorView.rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadData()

        adapter = ProductAdapter(productList, isGrid,
            onCartClick = {
                it.inCart = !it.inCart
                if (it.inCart) cartList.add(it) else cartList.remove(it)
                updateCartBadge()
            })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        toggleView.setOnClickListener {
            isGrid = !isGrid
            adapter.setViewType(isGrid)
            recyclerView.layoutManager =
                if (isGrid) GridLayoutManager(this, 2)
                else LinearLayoutManager(this)
        }

        setupChips()
        setupSwipeDrag()
    }

    private fun loadData() {
        productList.add(Product(1, "Phone", 500.0, 4.5f, "Electronics", R.drawable.phone))
        productList.add(Product(2, "Shirt", 30.0, 4.0f, "Clothing", R.drawable.shirt))
        productList.add(Product(3, "Book", 10.0, 4.2f, "Books", R.drawable.book))
        productList.add(Product(4, "Burger", 5.0, 4.8f, "Food", R.drawable.burger))
        productList.add(Product(5, "Toy Car", 15.0, 4.1f, "Toys", R.drawable.toys_car))
    }

    private fun setupChips() {
        val categories = listOf("All", "Electronics", "Clothing", "Books", "Food", "Toys")
        categories.forEach { category ->
            val chip = Chip(this)
            chip.text = category
            chip.setOnClickListener { filter(category) }
            chipGroup.addView(chip)
        }
    }

    private fun filter(category: String) {
        if (category == "All") adapter.updateList(productList)
        else adapter.updateList(productList.filter { it.category == category })
    }

    private fun setupSwipeDrag() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                adapter.swap(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.remove(viewHolder.adapterPosition)
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
    }

    private fun updateCartBadge() {
        cartCount.text = cartList.size.toString()
    }
}