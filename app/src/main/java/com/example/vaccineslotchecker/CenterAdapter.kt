package com.example.vaccineslotchecker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CenterAdapter(private val centerList : List<centerModal>): RecyclerView.Adapter<CenterAdapter.CenterViewHolder>() {
    class CenterViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
        val centerName       : TextView   = view.findViewById(R.id.txtCenterName)
        val centerAddress    : TextView   = view.findViewById(R.id.txtCenterAddress)
        val centerTiming     : TextView   = view.findViewById(R.id.txtCenterTiming)
        val vaccineName      : TextView   = view.findViewById(R.id.txtVaccineName)
        val ageLimit         : TextView   = view.findViewById(R.id.txtAgeLimit)
        val feeType          : TextView   = view.findViewById(R.id.txtFeeType)
        val availability     : TextView   = view.findViewById(R.id.txtVaccineAvailability)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_center,parent,false)
        return CenterViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val currentItem  = centerList[position]
        holder.centerName.text      = "Center Name : ${currentItem.centerName}"
        holder.centerAddress.text   = "Center Address : ${currentItem.centerAddress}"
        holder.centerTiming.text    = "From : ${currentItem.centerFromTime}   To : ${currentItem.centerToTime}"
        holder.vaccineName.text     =  "Vaccine Name : ${currentItem.vaccineName}"
        holder.ageLimit.text        =  "Age Limit "+currentItem.ageLimit.toString()
        holder.feeType.text         =  "FeeType : ${currentItem.fee_type}"
        holder.availability.text    =  "AvailabaleCapacity : ${currentItem.availableCapacity.toString()}"
    }

    override fun getItemCount(): Int {
        return centerList.size
    }

}