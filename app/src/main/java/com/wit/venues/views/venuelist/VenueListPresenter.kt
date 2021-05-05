package com.wit.venues.views.venuelist



import android.view.View
import androidx.appcompat.app.AlertDialog

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.wit.venues.helpers.*
import com.wit.venues.models.VenueModel
import com.wit.venues.models.VenueModelAPI
import com.wit.venues.views.BasePresenter
import com.wit.venues.views.BaseView
import com.wit.venues.views.VIEW
import kotlinx.android.synthetic.main.activity_venue_list.*
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VenueListPresenter(view: BaseView) : BasePresenter(view), Callback<List<VenueModel>>, AnkoLogger {

    var favCheck = false
   // lateinit var loader : AlertDialog


    init {
        if (view.intent.hasExtra("Fav")) {
            favCheck = true
        }

    }

    fun loadVenues() {
        doAsync {
            val venues = app.venues.findAll()
            var favVenues = mutableListOf<VenueModel>()
            uiThread {
                if (favCheck) {
                    for (element in venues) {
                        if (element.fav == true) {
                            favVenues.add(element)
                        }
                    }
                    view?.showVenues(favVenues)
                    if (favVenues.isEmpty()) {
                        view!!.none.setVisibility(View.VISIBLE)
                        view!!.none.setText("No Favourites")
                    }
                } else {
                    view?.showVenues(venues)
                    if (venues.isEmpty()) {
                        view!!.none.setVisibility(View.VISIBLE)
                        view!!.none.setText("No Venues")
                    }
                }
            }
        }
    }



    fun doAddVenue() {
        view?.navigateTo(VIEW.VENUE)

    }


    fun doEditVenue(venue: VenueModel) {
        view?.navigateTo(VIEW.VENUE, 0, "venue_edit", venue)
    }

    fun doShowSettings() {
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doFav() {
        view?.navigateTo(VIEW.FAVS, 0, "Fav")
    }

    fun doMap() {
        view?.navigateTo(VIEW.MAP)
    }


    fun doSwipeHandler() {

        val swipeDeleteHandler = object : SwipeToDeleteCallback(view!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var venue =   view?.findById(viewHolder.itemView.tag as Long)
                swipeDelete(venue!!)

            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(view!!.recyclerView)


        val swipeEditHandler = object : SwipeToEditCallback(view!!) {
             override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               var venue =   view?.findById(viewHolder.itemView.tag as Long)
               doEditVenue(venue!!)
                        }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(view!!.recyclerView)

    }

    fun doFindById(id: Long): VenueModel? {
        return app.venues.findById(id)
    }

    fun swipeDelete(venue: VenueModel) {
        doAsync {
            app.venues.delete(venue)
            uiThread {
                if (favCheck) doFav()
                else view?.navigateTo(VIEW.LIST)
            }
        }
    }

    fun getAllVenues() {
       // showLoader(loader, "Downloading Venues List")
        var callGetAll = app.venueService.getall()
        callGetAll.enqueue(this)
        view?.showVenuesAPI(app.apiList)


    }

    override fun onResponse(call: Call<List<VenueModel>>,
                            response: Response<List<VenueModel>>
    ) {
        //serviceAvailableMessage(activity!!)
        info("Retrofit JSON = $response.raw()")
        app.apiList = response.body() as ArrayList<VenueModelAPI>
        updateUI()
      //  hideLoader(loader)
    }

    override fun onFailure(call: Call<List<VenueModel>>, t: Throwable) {
        info("Retrofit Error : $t.message")
       // serviceUnavailableMessage(activity!!)
     //   hideLoader(loader)
    }

   fun updateUI() {
       /* totalDonated = app.venuesStore.findAll().sumBy { it.amount }
        progressBar.progress = totalDonated
        totalSoFar.text = format("$ $totalDonated") */
    }

}
