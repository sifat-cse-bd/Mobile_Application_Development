package com.example.photogalleryapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var adapter: PhotoAdapter
    private lateinit var selectionToolbar: LinearLayout
    private lateinit var selectionCountText: TextView
    private lateinit var deleteBtn: Button
    private lateinit var shareBtn: Button
    private lateinit var fab: FloatingActionButton

    private val allPhotos = mutableListOf(
        Photo(1,  R.drawable.nature1,   "Forest Path",     "Nature"),
        Photo(2,  R.drawable.nature2,   "Mountain Lake",   "Nature"),
        Photo(3,  R.drawable.nature3,   "Sunset Valley",   "Nature"),
        Photo(4,  R.drawable.nature4,   "Beach Sunset",    "Nature"),
        Photo(4,  R.drawable.city1,     "City Lights",     "City"),
        Photo(5,  R.drawable.city2,     "Downtown Skyline","City"),
        Photo(6,  R.drawable.city3,     "Night Bridge",    "City"),
        Photo(6,  R.drawable.city4,     "City Streets",    "City"),
        Photo(7,  R.drawable.animals1,   "Golden Retriever","Animals"),
        Photo(8,  R.drawable.animals2,   "Lazy Cat",        "Animals"),
        Photo(9,  R.drawable.animals3,   "Wild Fox",        "Animals"),
        Photo(10, R.drawable.animals4,   "Cute Dog",        "Animals"),
        Photo(10, R.drawable.food1,     "Sushi Platter",   "Food"),
        Photo(11, R.drawable.food2,     "Pizza Night",     "Food"),
        Photo(12, R.drawable.food3,     "Pasta Meal",      "Food"),
        Photo(13, R.drawable.food4,     "Taco Day",        "Food"),
        Photo(12, R.drawable.travel1,   "Santorini",       "Travel"),
        Photo(13, R.drawable.travel2,   "Tokyo Streets",   "Travel"),
        Photo(14, R.drawable.travel3,   "Venice Beach",    "Travel"),
        Photo(15, R.drawable.travel4,   "Paris Cafe",      "Travel")
    )

    private val displayedPhotos = mutableListOf<Photo>()
    private var currentCategory = "All"

    // Random drawable IDs for FAB simulation
    private val randomPool = listOf(
        R.drawable.nature1, R.drawable.city1, R.drawable.animals1,
        R.drawable.food1, R.drawable.travel1
    )
    private var nextFabId = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridView          = findViewById(R.id.gridView)
        selectionToolbar  = findViewById(R.id.selectionToolbar)
        selectionCountText= findViewById(R.id.selectionCountText)
        deleteBtn         = findViewById(R.id.deleteBtn)
        shareBtn          = findViewById(R.id.shareBtn)
        fab               = findViewById(R.id.fab)

        displayedPhotos.addAll(allPhotos)
        adapter = PhotoAdapter(this, displayedPhotos)
        gridView.adapter = adapter

        setupCategoryTabs()
        setupGridListeners()
        setupToolbarButtons()

        fab.setOnClickListener { addRandomPhoto() }
    }

    private fun setupCategoryTabs() {
        val categories = listOf("All", "Nature", "City", "Animals", "Food", "Travel")
        val tabContainer: LinearLayout = findViewById(R.id.tabContainer)

        categories.forEach { category ->
            val btn = Button(this).apply {
                text = category
                isAllCaps = false
                setBackgroundResource(
                    if (category == "All") R.drawable.tab_selected else R.drawable.tab_unselected
                )
                setPadding(40, 16, 40, 16)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 0, 8, 0)
                layoutParams = params

                setOnClickListener {
                    filterByCategory(category)
                    // Update button states
                    for (i in 0 until tabContainer.childCount) {
                        val child = tabContainer.getChildAt(i) as? Button
                        child?.setBackgroundResource(R.drawable.tab_unselected)
                    }
                    setBackgroundResource(R.drawable.tab_selected)
                }
            }
            tabContainer.addView(btn)
        }
    }

    private fun filterByCategory(category: String) {
        exitSelectionMode()
        currentCategory = category
        displayedPhotos.clear()
        displayedPhotos.addAll(
            if (category == "All") allPhotos else allPhotos.filter { it.category == category }
        )
        adapter.notifyDataSetChanged()
    }


    private fun setupGridListeners() {
        // Normal tap
        gridView.setOnItemClickListener { _, _, position, _ ->
            val photo = adapter.getItem(position)
            if (adapter.isSelectionMode) {
                photo.isSelected = !photo.isSelected
                updateSelectionToolbar()
                adapter.notifyDataSetChanged()
            } else {
                val intent = Intent(this, FullscreenActivity::class.java)
                intent.putExtra("resourceId", photo.resourceId)
                intent.putExtra("title", photo.title)
                startActivity(intent)
            }
        }


        gridView.setOnItemLongClickListener { _, _, position, _ ->
            if (!adapter.isSelectionMode) {
                adapter.isSelectionMode = true
                selectionToolbar.visibility = View.VISIBLE
            }
            val photo = adapter.getItem(position)
            photo.isSelected = true
            updateSelectionToolbar()
            adapter.notifyDataSetChanged()
            true
        }
    }


    private fun setupToolbarButtons() {
        deleteBtn.setOnClickListener {
            val count = adapter.removeSelectedPhotos()
            // Also remove from master list
            allPhotos.removeAll { it.isSelected }
            Toast.makeText(this, "$count photos deleted", Toast.LENGTH_SHORT).show()
            exitSelectionMode()
        }

        shareBtn.setOnClickListener {
            val count = adapter.getSelectedCount()
            Toast.makeText(this, "Sharing $count photos…", Toast.LENGTH_SHORT).show()
            exitSelectionMode()
        }
    }

    private fun updateSelectionToolbar() {
        val count = adapter.getSelectedCount()
        selectionCountText.text = "$count selected"
        if (count == 0) exitSelectionMode()
    }

    private fun exitSelectionMode() {
        adapter.clearSelections()
        selectionToolbar.visibility = View.GONE
    }


    private fun addRandomPhoto() {
        val resId = randomPool.random()
        val newPhoto = Photo(
            id         = nextFabId++,
            resourceId = resId,
            title      = "New Photo $nextFabId",
            category   = currentCategory.let { if (it == "All") "Nature" else it }
        )
        allPhotos.add(newPhoto)
        if (currentCategory == "All" || newPhoto.category == currentCategory) {
            displayedPhotos.add(newPhoto)
            adapter.notifyDataSetChanged()
        }
        Toast.makeText(this, "Photo added!", Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (adapter.isSelectionMode) {
            exitSelectionMode()
        } else {
            super.onBackPressed()
        }
    }
}































/*

///
package com.example.photogalleryapp

import android.os.Bundle
import android.provider.ContactsContract
import android.widget.GridView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.sync.Mutex
import java.util.Collections.addAll

class MainActivity : AppCompatActivity() {
    lateinit var btnAll: MaterialButton
    lateinit var btnNature: MaterialButton
    lateinit var btnCity: MaterialButton
    lateinit var btnAnimals: MaterialButton
    lateinit var btnFood: MaterialButton
    lateinit var btnTravel: MaterialButton

    lateinit var gvPhotos: GridView
    lateinit var adapter: PhotoAdapter
    var allPhotos: MutableList<Photo> = mutableListOf()
    var filteredPhotos: MutableList<Photo> = mutableListOf()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //initialize views
        btnAll = findViewById(R.id.btnAll)
        btnNature = findViewById(R.id.btnNature)
        btnCity = findViewById(R.id.btnCity)
        btnAnimals = findViewById(R.id.btnAnimals)
        btnFood = findViewById(R.id.btnFood)
        btnTravel = findViewById(R.id.btnTravel)
        gvPhotos = findViewById(R.id.gvPhotos)

        // Load data
        loadPhotos()

        // Initial display
        filterPhotos()
        populateGridView()

        // all photos show
        btnAll.setOnClickListener {
            filterPhotos()
            populateGridView()
        }

        //nature photos show
        btnNature.setOnClickListener {
            filterPhotos("nature")
            populateGridView()
        }

        //city photos show
        btnCity.setOnClickListener {
            filterPhotos("city")
            populateGridView()
        }

        //animals photos show
        btnAnimals.setOnClickListener {
            filterPhotos("animals")
            populateGridView()
        }

        //food photos show
        btnFood.setOnClickListener {
            filterPhotos("food")
            populateGridView()
        }

        //travel photos show
        btnTravel.setOnClickListener {
            filterPhotos("travel")
            populateGridView()

        }


    }

    fun populateGridView()
    {
        adapter = PhotoAdapter(this, filteredPhotos)
        gvPhotos.adapter = adapter
    }

    private fun filterPhotos(category:String = "all")
    {
        filteredPhotos.clear()
        if(category == "all")
        {
            filteredPhotos.addAll(allPhotos)
        }
        else {
            filteredPhotos.addAll(allPhotos.filter { it.category == category })
        }

    }

    private fun loadPhotos()
    {
        //load photos
        val photoList = mutableListOf<Photo>(
            Photo(1, R.drawable.nature1, "Nature1", "nature"),
            Photo(2, R.drawable.nature2, "Nature2", "nature"),
            Photo(3, R.drawable.nature3, "Nature3", "nature"),
            Photo(4, R.drawable.nature4, "Nature4", "nature"),
            Photo(4, R.drawable.city1, "City1", "city"),
            Photo(5, R.drawable.city2, "City2", "city"),
            Photo(6, R.drawable.city3, "City3", "city"),
            Photo(7, R.drawable.city4, "City4", "city"),
            Photo(8, R.drawable.animals1, "Animals1", "animals"),
            Photo(9, R.drawable.animals2, "Animals2", "animals"),
            Photo(10, R.drawable.animals3, "Animals3", "animals"),
            Photo(11, R.drawable.animals4, "Animals4", "animals"),
            Photo(12, R.drawable.food1, "Food1", "food"),
            Photo(13, R.drawable.food2, "Food2", "food"),
            Photo(14, R.drawable.food3, "Food3", "food"),
            Photo(15, R.drawable.food4, "Food4", "food"),
            Photo(16, R.drawable.travel1, "Travel1", "travel"),
            Photo(17, R.drawable.travel2, "Travel2", "travel"),
            Photo(18, R.drawable.travel3, "Travel3", "travel"),
            Photo(19, R.drawable.travel4, "Travel4", "travel")
        )

        allPhotos.addAll( photoList)

    }
}

*/
