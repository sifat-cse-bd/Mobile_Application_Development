package com.example.studentregistrationapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    lateinit var etId: EditText
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var etAge: EditText

    lateinit var radioGroup: RadioGroup

    lateinit var cbFootball: CheckBox
    lateinit var cbCricket: CheckBox
    lateinit var cbBasketball: CheckBox
    lateinit var cbBadminton: CheckBox

    lateinit var spinner: Spinner
    lateinit var btnDate: Button
    lateinit var btnSubmit: Button
    lateinit var btnReset: Button

    var selectedDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Find Views
        etId = findViewById(R.id.etId)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etAge = findViewById(R.id.etAge)

        radioGroup = findViewById(R.id.radioGroup)

        cbFootball = findViewById(R.id.cbFootball)
        cbCricket = findViewById(R.id.cbCricket)
        cbBasketball = findViewById(R.id.cbBasketball)
        cbBadminton = findViewById(R.id.cbBadminton)

        spinner = findViewById(R.id.spinnerCountry)
        btnDate = findViewById(R.id.btnDate)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnReset = findViewById(R.id.btnReset)

        // Spinner Data
        val countries = arrayOf("Bangladesh", "India", "USA", "UK", "Canada")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countries)
        spinner.adapter = adapter

        // Date Picker
        btnDate.setOnClickListener {
            val cal = Calendar.getInstance()

            val dp = DatePickerDialog(
                this,
                { _, year, month, day ->
                    selectedDate = "$day/${month + 1}/$year"
                    btnDate.text = selectedDate
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dp.show()
        }

        // Submit
        btnSubmit.setOnClickListener {

            val id = etId.text.toString()
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val age = etAge.text.toString()

            val genderId = radioGroup.checkedRadioButtonId
            val gender = if (genderId != -1) findViewById<RadioButton>(genderId).text.toString() else ""

            val sports = mutableListOf<String>()
            if (cbFootball.isChecked) sports.add("Football")
            if (cbCricket.isChecked) sports.add("Cricket")
            if (cbBasketball.isChecked) sports.add("Basketball")
            if (cbBadminton.isChecked) sports.add("Badminton")

            val country = spinner.selectedItem.toString()

            // Validation
            if (id.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty() ||
                age.isEmpty() || gender.isEmpty() || selectedDate.isEmpty() ||
                !email.contains("@") || age.toInt() <= 0
            ) {
                Toast.makeText(this, "Please complete all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val message = """
                ID: $id
                Name: $name
                Email: $email
                Gender: $gender
                Sports: ${sports.joinToString()}
                Country: $country
                DOB: $selectedDate
            """.trimIndent()

            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }


        btnReset.setOnClickListener {

            etId.text.clear()
            etName.text.clear()
            etEmail.text.clear()
            etPassword.text.clear()
            etAge.text.clear()

            radioGroup.clearCheck()

            cbFootball.isChecked = false
            cbCricket.isChecked = false
            cbBasketball.isChecked = false
            cbBadminton.isChecked = false

            spinner.setSelection(0)

            selectedDate = ""
            btnDate.text = "Select Date"
        }
    }
}






