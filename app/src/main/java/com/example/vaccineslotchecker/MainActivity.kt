package com.example.vaccineslotchecker

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var Pincode: String
    private lateinit var centerRecyclerView: RecyclerView
    private lateinit var centerRecyclerAdapter: CenterAdapter
    private lateinit var centerList: List<centerModal>
    private lateinit var progressBar: ProgressBar

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        centerList = ArrayList<centerModal>()
        progressBar = findViewById(R.id.progress)
        centerRecyclerView = findViewById(R.id.centerRecyclerView)
        askPincode()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun askPincode() {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        val pincodeText = EditText(this)
        builder.setTitle("Pincode").setMessage("Enter Your Pincode").setView(pincodeText)
        builder.setPositiveButton(
            "OK"
        ) {
                dialog, which ->
            Pincode = pincodeText.text.toString()
            if (Pincode.length != 6) {
                Toast.makeText(this@MainActivity, "Please Enter Valid pin Code", Toast.LENGTH_LONG)
                    .show()
            } else {
                (centerList as ArrayList<centerModal>).clear()
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val datePicker = DatePickerDialog(
                    this, { view, year, month, dayOfMonth ->
                        val date: String = """ $dayOfMonth - ${month + 1} - $year """
                        progressBar.visibility = View.VISIBLE
                        getAppointments(Pincode, date)
                    },
                    year,
                    month,
                    day
                )
                datePicker.show()
            }

        }
        builder.create().show()
    }

    private fun getAppointments(pincode: String, date: String) {
        val url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=$pincode&date=$date"
        val queue = Volley.newRequestQueue(this@MainActivity)
        val jsonObject = JsonObjectRequest(Request.Method.GET,url,null, { it->
            Log.d("TAG","Success Response")
            progressBar.visibility = View.GONE
            try {
                val centerArray = it.getJSONArray("centers")
                if (centerArray.length().equals(0)) {
                    Toast.makeText(this@MainActivity, "No content found", Toast.LENGTH_LONG).show()
                }
                else
                {
                    for(i in 0 until centerArray.length())
                    {
                        val centerObject = centerArray.getJSONObject(i)
                        val centername : String = centerObject.getString("name")
                        val centeraddress:String = centerObject.getString("address")
                        val centerfromtime:String = centerObject.getString("from")
                        Log.d("centerFrom",centerfromtime)
                        val centertotime : String = centerObject.getString("to")
                        val feetype : String = centerObject.getString("fee_type")
                        val sessionObj = centerObject.getJSONArray("sessions").getJSONObject(0)
                        val agelimit  : Int = sessionObj.getInt("min_age_limit")
                        val vaccinename : String = sessionObj.getString("vaccine")
                        val available : Int = sessionObj.getInt("available_capacity")
                        val center = centerModal(
                            centername,centeraddress,centerfromtime,centertotime,feetype,agelimit,vaccinename,available)
                        centerList = centerList + center
                    }
                    centerRecyclerAdapter = CenterAdapter(centerList)
                    centerRecyclerView.layoutManager = LinearLayoutManager(this)
                    centerRecyclerView.adapter = centerRecyclerAdapter
                    centerRecyclerAdapter.notifyDataSetChanged()
                }
            }
            catch (e:JSONException)
            {
                e.printStackTrace()
            }
        }, { error->
            Log.d("tag","Response is $error")
            Toast.makeText(this@MainActivity,"Failed to get Data",Toast.LENGTH_LONG).show()
        }
        )
        queue.add(jsonObject)
    }
}
