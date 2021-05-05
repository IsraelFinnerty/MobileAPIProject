package com.wit.venues.views

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.wit.venues.main.MainApp
import com.wit.venues.models.VenueModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BasePresenter(var view: BaseView?): Callback<List<VenueModel>> {

    var app: MainApp =  view?.application as MainApp

    open fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.venues.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

    open fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

    }

    open fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    }

    open fun onDestroy() {
        view = null
    }

    override fun onFailure(call: Call<List<VenueModel>>, t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResponse(
        call: Call<List<VenueModel>>,
        response: Response<List<VenueModel>>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}