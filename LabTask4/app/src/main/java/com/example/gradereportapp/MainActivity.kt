package com.example.gradereportapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var tableLayout: TableLayout
    lateinit var edtSubName: EditText
    lateinit var edtObtainMark: EditText
    lateinit var edtTotalMark: EditText
    lateinit var tvSummary: TextView
    lateinit var tvGPA: TextView
    lateinit var btnAddSub: Button
    lateinit var btnPrintReport: Button

    data class Subject(val subName: String, val obtainMark: Int, val totalMark: Int, val grade: String)
    val subjects = mutableListOf<Subject>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize all UI components
        tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        edtSubName = findViewById<EditText>(R.id.edtSubName)
        edtObtainMark = findViewById<EditText>(R.id.edtObtainMark)
        edtTotalMark = findViewById<EditText>(R.id.edtTotalMark)
        tvSummary = findViewById<TextView>(R.id.tvSummary)
        tvGPA = findViewById<TextView>(R.id.tvGPA)
        btnAddSub = findViewById<Button>(R.id.btnAddSubject)
        btnPrintReport = findViewById<Button>(R.id.btnPrintReport)

        btnAddSub.setOnClickListener {
            addSubject()
        }

        btnPrintReport.setOnClickListener {
            printReport()
        }
    }

    private fun addSubject() {
        val txtSubName = edtSubName.text.toString()
        val txtObtainMark = edtObtainMark.text.toString().toIntOrNull()
        val txtTotalMark = edtTotalMark.text.toString().toIntOrNull()


        if (!fieldValidation(txtSubName, txtObtainMark, txtTotalMark)) return

        // Now safe to calculate grade
        val percent: Int = (txtObtainMark!! * 100) / txtTotalMark!!
        val grade: String = getGrade(percent)

        val subject: Subject = Subject(txtSubName, txtObtainMark, txtTotalMark, grade)

        subjects.add(subject)
        addRowToTable(subject)
        clearInput()

        updateSummaryAndGPA()
    }

    private fun addRowToTable(subject: Subject) {
        // Create table row
        val tableRow = TableRow(this)
        val tv1 = TextView(this).apply {
            text = subject.subName
            setPadding(10, 10, 10, 10)
            textSize = 14f
        }
        val tv2 = TextView(this).apply {
            text = subject.obtainMark.toString()
            setPadding(10, 10, 10, 10)
            textSize = 14f
        }
        val tv3 = TextView(this).apply {
            text = subject.totalMark.toString()
            setPadding(10, 10, 10, 10)
            textSize = 14f
        }
        val tv4 = TextView(this).apply {
            text = subject.grade
            setPadding(10, 10, 10, 10)
            textSize = 14f
            setTypeface(null, Typeface.BOLD)
        }

        tableRow.addView(tv1)
        tableRow.addView(tv2)
        tableRow.addView(tv3)
        tableRow.addView(tv4)

        // Highlight entire row based on pass/fail
        val rowColor = if (subject.grade == "F") {
            // Light red for failed
            Color.parseColor("#FFEBEE")
        } else {
            // Light green for passed
            Color.parseColor("#E8F5E9")
        }
        tableRow.setBackgroundColor(rowColor)

        // Color the grade text as well
        if (subject.grade == "F") {
            tv4.setTextColor(Color.RED)
        } else {
            tv4.setTextColor(Color.GREEN)
        }

        tableLayout.addView(tableRow)
    }

    private fun updateSummaryAndGPA() {
        val totalSubjects = subjects.size
        val passed = subjects.count { it.grade != "F" }
        val failed = totalSubjects - passed

        tvSummary.text = "Total Subjects: $totalSubjects | Passed: $passed | Failed: $failed"

        val gpa = if (totalSubjects > 0) {
            subjects.map { calculateGPA(it.grade) }.average()
        } else 0.0

        tvGPA.text = "GPA: %.2f".format(gpa)
    }

    private fun calculateGPA(grade: String): Double {
        return getPoint(grade)
    }

    private fun fieldValidation(
        txtSubName: String,
        txtObtainMark: Int?,
        txtTotalMark: Int?
    ): Boolean {
        if (txtSubName.isEmpty()) {
            Toast.makeText(this, "Subject name is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (txtObtainMark == null) {
            Toast.makeText(this, "Obtain mark is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (txtTotalMark == null) {
            Toast.makeText(this, "Total mark is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (txtObtainMark > txtTotalMark) {
            Toast.makeText(
                this,
                "Obtain mark cannot be greater than total mark",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (txtObtainMark < 0 || txtTotalMark <= 0) {
            Toast.makeText(
                this,
                "Obtain mark and total mark cannot be negative",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun getGrade(p: Int): String {
        return when (p) {
            in 90..100 -> "A+"
            in 80..89 -> "A"
            in 70..79 -> "B+"
            in 60..69 -> "B"
            in 50..59 -> "C"
            in 40..49 -> "D"
            else -> "F"
        }
    }

    private fun getPoint(g: String): Double {
        return when (g) {
            "A+" -> 4.00
            "A" -> 3.70
            "B+" -> 3.30
            "B" -> 3.00
            "C" -> 2.00
            "D" -> 1.00
            else -> 0.00
        }
    }

    private fun clearInput() {
        // After adding row clear the edit text
        edtSubName.text.clear()
        edtObtainMark.text.clear()
        edtTotalMark.text.clear()
    }

    private fun printReport() {
        if (subjects.isEmpty()) {
            Toast.makeText(this, "No subjects to print", Toast.LENGTH_SHORT).show()
            return
        }

        val reportText = buildReportString()

        // Create intent to share the report
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, reportText)
            putExtra(Intent.EXTRA_SUBJECT, "Grade Report")
        }

        startActivity(Intent.createChooser(shareIntent, "Share Grade Report"))
    }

    private fun buildReportString(): String {
        val totalSubjects = subjects.size
        val passed = subjects.count { it.grade != "F" }
        val failed = totalSubjects - passed
        val gpa = if (totalSubjects > 0) {
            subjects.map { calculateGPA(it.grade) }.average()
        } else 0.0

        val reportBuilder = StringBuilder()
        reportBuilder.append("===== GRADE REPORT =====\n\n")

        reportBuilder.append("Subject\t\tObtained\tTotal\t\tGrade\n")
        reportBuilder.append("-".repeat(60)).append("\n")

        for (subject in subjects) {
            reportBuilder.append("${subject.subName}\t\t${subject.obtainMark}\t\t${subject.totalMark}\t\t${subject.grade}\n")
        }

        reportBuilder.append("-".repeat(60)).append("\n")
        reportBuilder.append("Total Subjects: $totalSubjects\n")
        reportBuilder.append("Passed: $passed\n")
        reportBuilder.append("Failed: $failed\n")
        reportBuilder.append("GPA: %.2f\n".format(gpa))

        return reportBuilder.toString()
    }
}