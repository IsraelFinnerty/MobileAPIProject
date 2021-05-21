package com.wit.venues.views.login


import com.google.firebase.auth.FirebaseAuth
import com.wit.venues.helpers.serviceAvailableMessage
import com.wit.venues.models.VenueModel
import com.wit.venues.models.firebase.VenueFireStore
import com.wit.venues.views.BasePresenter
import com.wit.venues.views.BaseView
import com.wit.venues.views.VIEW
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class doLoginPresenter(view: BaseView) : BasePresenter(view), Callback<List<VenueModel>> {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    var fireStore: VenueFireStore? = null

    init {
        if (app.venues is VenueFireStore) {
            fireStore = app.venues as VenueFireStore
        }
    }


    fun doLogin(email: String, password: String) {
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                if (fireStore != null) {
                    view?.navigateTo(VIEW.LIST)
                    /*   fireStore!!.fetchVenues {
                        view?.hideProgress()



                    }
                } else {
                    view?.hideProgress()
                    view?.navigateTo(VIEW.LIST)
                }
                 */

                } else {
                    view?.hideProgress()
                    view?.toast("Log in Failed: ${task.exception?.message}")
                }
            }
        }
    }

    fun doSignUp(email: String, password: String) {
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(view!!) { task ->
                if (task.isSuccessful) {
                    fireStore!!.seed()
                    fireStore!!.fetchVenues {
                        view?.hideProgress()
                        view?.navigateTo(VIEW.LIST)
                    }
                } else {
                    view?.toast("Sign Up Failed: ${task.exception?.message}")
                }
                view?.hideProgress()
            }
    }

    fun doSendPasswordReset(email: String) {
        if (email != null) {
            auth.sendPasswordResetEmail(email!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        view?.toast("Password Reset Email Sent")
                    } else {
                        view?.toast("Email Address Not Found")
                    }
                }
        }
    }


    override fun onResponse(
        call: Call<List<VenueModel>>,
        response: Response<List<VenueModel>>
    ) {
        //   serviceAvailableMessage(activity!!)
        // info("Retrofit JSON = $response.raw()")
        app.apiList = response.body() as ArrayList<VenueModel>


    }

    override fun onFailure(call: Call<List<VenueModel>>, t: Throwable) {
        // info("Retrofit Error : $t.message")
        // serviceUnavailableMessage(activity!!)
        // hideLoader(loader)
    }

}


