package com.example.cotactbookapp

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    lateinit var svContact: SearchView
    lateinit var lvContact: ListView
    lateinit var fabAdd: FloatingActionButton
    lateinit var tvEmpty: TextView
    private val contactList = mutableListOf<Contact>()
    private val displayList = mutableListOf<Contact>()
    lateinit var adapter:ContactAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Initialization of fields
        svContact = findViewById<SearchView>(R.id.svContact)
        lvContact = findViewById<ListView>(R.id.lvContact)
        fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        tvEmpty = findViewById<TextView>(R.id.tvEmpty)

        //Default value of contact list
        contactList.add(Contact("Faruk Ahmed Sifat", "01713386933", "23-51221-1@student.aiub.edu"))

        //Add contacts to the adapter
        adapter = ContactAdapter(this, R.layout.item_contact, displayList)
        lvContact.adapter = adapter

        fabAdd.setOnClickListener {
            showContactDialog()
        }

        svContact.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterContacts(newText ?: "")
                checkEmptyStatus()
                return true
            }
        })
    }
    private fun showContactDialog()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add new contact")
        builder.setIcon(R.drawable.contact)

        //Layout
        val layout = LinearLayout(this).apply{orientation = LinearLayout.VERTICAL; setPadding(60, 40, 60, 0)}


        val edtName = EditText(this).apply{hint = "Enter name"; inputType = InputType.TYPE_CLASS_TEXT}
        val edtPhone = EditText(this).apply{hint = "Enter phone number"; inputType = InputType.TYPE_CLASS_PHONE}
        val edtEmail = EditText(this).apply{hint = "Enter email"; inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS} //optional
        layout.addView(edtName)
        layout.addView(edtPhone)
        layout.addView(edtEmail)
        builder.setView(layout)

        // add button under the layout
        builder.setPositiveButton("Add") { _,_ ->
            if(edtName.text.isEmpty() || edtPhone.text.isEmpty())
            {
                // Show error message if name or phone is empty
                AlertDialog.Builder(this)
                    .setTitle("Validation Error")
                    .setMessage("Name and Phone number are required!")
                    .setPositiveButton("OK", null)
                    .show()
            }
            else
            {
                val name = edtName.text.toString()
                val phone = edtPhone.text.toString()
                val email = edtEmail.text.toString()
                contactList.add(Contact(name, phone, email))
                filterContacts()
                checkEmptyStatus()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun filterContacts(query:String? = null)
    {
        displayList.clear()
        if(query.isNullOrBlank())
        {
            displayList.addAll(contactList)

        }else{
            displayList.addAll(contactList.filter{it.name.contains(query, ignoreCase = true)})
        }
        adapter.notifyDataSetChanged()
    }

    private fun checkEmptyStatus()
    {
        if(displayList.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            lvContact.visibility = View.GONE
        } else
        {
            tvEmpty.visibility = View.GONE
            lvContact.visibility = View.VISIBLE
        }
    }

}