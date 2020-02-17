package mb.com.onthego

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var editTextValue: EditText
    lateinit var spinner: Spinner
    lateinit var dateButton: Button
    lateinit var submitButton: Button

    // [START declare_database_ref]
    private lateinit var database: DatabaseReference
    // [END declare_database_ref]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "Firebase connection successful", Toast.LENGTH_LONG).show()

        // Code for spinner
        // access the items of the list
        val categories = resources.getStringArray(R.array.CategoryArray)
        //acess the spinner
        spinner = findViewById<Spinner>(R.id.category)
        if (spinner!=null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, categories)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selected = spinner.selectedItem.toString()
                    //Toast.makeText(this@MainActivity, selected + " "
                   // + "" + categories[position], Toast.LENGTH_SHORT).show()
                }
            }
        }

        editTextValue = findViewById(R.id.value)
        dateButton = findViewById(R.id.dateBtn)
        submitButton = findViewById(R.id.submitBtn)

        submitButton.setOnClickListener {

            saveEntry()
        }

        // [START initialize_database_ref]
        //database = FirebaseDatabase.getInstance().reference
        // [END initialize_database_ref]

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickDataPicker(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val txt: String = """${monthOfYear + 1} / $dayOfMonth / $year"""
            dateButton.text = txt

        }, year, month, day)

        dpd.show()

    }

    private fun saveEntry(){

        // store the value for user entered expense
        val expense = editTextValue.text.toString()

        if (expense.isEmpty()){
            editTextValue.error = "Please enter the expense in USD"
            Toast.makeText(this, "Please enter a number for expense", Toast.LENGTH_SHORT).show()
            return
        }

        // store the category that user picked
        val categoryPicked = spinner.selectedItem.toString()


        // store the date that user picked
        val date = dateButton.text.toString()

        if (date.isNullOrEmpty()){
            dateButton.error = "Please select a date for your entry"
            Toast.makeText(this, "Please pick a date", Toast.LENGTH_SHORT).show()
            return
        }

        /** We now create a reference of the Firebase database */

        val ref = FirebaseDatabase.getInstance().getReference("Entries")

        // create a key (which is the ID) for each entry that we will log
        val entryID = ref.push().key

        // create an instance of the Entry.kt class object
        val entry = Entry(entryID, expense, categoryPicked, date)

        // add the entry to the database under the unique key(ID), display message if entry added
        if (entryID != null) {
            ref.child(entryID).setValue(entry).addOnCompleteListener {
                Toast.makeText(applicationContext, "Expense added successfully", Toast.LENGTH_LONG).show()
            }
        }


    }

    }




