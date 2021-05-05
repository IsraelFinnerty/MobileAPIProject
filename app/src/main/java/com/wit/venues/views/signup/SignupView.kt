package com.wit.venues.views.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wit.venues.R
import kotlinx.android.synthetic.main.activity_venue.toolbarAdd
import kotlinx.android.synthetic.main.activity_signup.*


class SignupView: AppCompatActivity() {


    lateinit var presenter: SignupPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        presenter = SignupPresenter(this)


        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)


        btnNewSignup.setOnClickListener() {
           presenter.doNewSignup()
        }
    }



}