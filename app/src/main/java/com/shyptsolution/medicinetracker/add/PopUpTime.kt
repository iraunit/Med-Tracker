package com.shyptsolution.medicinetracker.add

import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.shyptsolution.medicinetracker.MainActivity
import com.shyptsolution.medicinetracker.R

class PopUpTime:DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.popuptime,container,false)
        var yes=view.findViewById(R.id.yesbutaon) as Button
        var no=view.findViewById(R.id.nobutton) as Button
        yes.setOnClickListener {

            startActivity(Intent(activity,MainActivity::class.java))
            }
        no.setOnClickListener {
            this.dismiss()
        }
        return view
    }



}