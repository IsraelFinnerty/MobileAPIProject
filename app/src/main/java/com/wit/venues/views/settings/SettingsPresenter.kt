package com.wit.venues.views.settings

import android.content.ContentValues
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.wit.venues.R
import com.wit.venues.views.BasePresenter
import com.wit.venues.views.BaseView
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class SettingsPresenter(view: BaseView) : BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var user = FirebaseAuth.getInstance().currentUser
    val venues = app.venues.findAll()
    var totalVenues = 0
    var venuesVisited = 0
    var favs = 0
    var recentVisited =  LocalDate.of(1900, 10, 31)
    var recentVenue = ""

    init {
        view.settingsEmail.setText(user!!.email)

        for (venue in venues) {
            totalVenues++
            if (venue.visited) {
                venuesVisited++
                view.statsRecent.setVisibility(View.VISIBLE)
                var currentVisited = LocalDate.of(
                    venue.dateVisitedYear,
                    venue.dateVisitedMonth + 1,
                    venue.dateVisitedDay
                )
                if (currentVisited.isAfter(recentVisited)) {
                    recentVisited = currentVisited
                    recentVenue = venue.name
                }
            }
            if (venue.fav) favs++
           }

        view.statsTotal.setText("Total Venues: $totalVenues")
        view.statsVisited.setText("Venues Visited: $venuesVisited")
        view.statsRecent.setText(
            "Most Recent Visit: $recentVenue ${
                recentVisited.format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                )
            }"
        )
        view.statsFavs.setText("Favourites: $favs")
    }


    fun doClickListener() {
        val updateEmail = view?.updateEmail!!.text.toString()
        val confirmEmail = view?.confirmEmail!!.text.toString()

        if (isEmailValid(updateEmail) == false || isEmailValid(confirmEmail) == false)
        {
            view?.toast(R.string.email_invalid)
        }
        else if (updateEmail != confirmEmail) {
            view?.toast("Email Mismatch")
        }
        else if(updateEmail == null){
            view?.toast("Enter an Email Address")
        }
        else if(confirmEmail == null){
           view?.toast("No Verify Email Address Entered")
        }
        else {
            doUpdateEmail(updateEmail)
            view?.toast("Email Updated")
        }
    }


    fun doUpdateEmail(email: String) {
        user!!.updateEmail(email)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d(ContentValues.TAG, "Email address updated")
                }
            }
    }

    fun doSendPasswordReset(){
        if (user!!.email != null) {
            auth.sendPasswordResetEmail(user!!.email!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        view?.toast("Password Reset Email Sent")
                    }
                }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        return EMAIL_REGEX.toRegex().matches(email);
    }
}