package com.wit.venues.views.signup

import com.wit.venues.R
import com.wit.venues.main.MainApp
import com.wit.venues.models.User
import com.wit.venues.views.venuelist.VenueListView
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class SignupPresenter(var view: SignupView) {

    lateinit var app: MainApp
    var user = User()

    init {
        app = view.application as MainApp
    }


    fun doNewSignup() {
       // var emailUsed = app.users.findUserByEmail(view.signup_email.text.toString())
        user.name = view.signup_name.text.toString()
        user.year = view.signup_year.text.toString().toInt()
        user.email = view.signup_email.text.toString()
        user.password = view.signup_password.text.toString()
        if (user.name.isEmpty()) view.toast(view.getString(R.string.enter_name))
        else if (user.email.isEmpty()) view.toast(view.getString(R.string.enter_email))
        else if (user.password.isEmpty()) view.toast(view.getString(R.string.enter_password))
        else if (user.password.length < 8) view.toast(view.getString(R.string.short_password))
        else if (user.year == 0) view.toast(view.getString(R.string.enter_year))
       // else if (emailUsed != null) view.toast(view.getString(R.string.email_used))
        else if (isEmailValid(user.email) == false) view.toast(view.getString(R.string.email_invalid))
        else {
        //    app.users.createUser(user.copy())
            view.startActivityForResult(view.intentFor<VenueListView>().putExtra("User", user), 0)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        return EMAIL_REGEX.toRegex().matches(email);
    }
}