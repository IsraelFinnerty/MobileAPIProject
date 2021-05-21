package com.wit.venues.views.venue

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wit.venues.R
import com.wit.venues.helpers.*
import com.wit.venues.models.VenueModel
import com.wit.venues.models.Location
import com.wit.venues.models.firebase.VenueFireStore
import com.wit.venues.views.BasePresenter
import com.wit.venues.views.BaseView
import com.wit.venues.views.VIEW
import kotlinx.android.synthetic.main.activity_venue.*
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VenuePresenter(view: BaseView) : BasePresenter(view), Callback<List<VenueModel>>{
    var venue = VenueModel()
    val IMAGE_REQUEST1 = 1
    val IMAGE_REQUEST2 = 2
    val IMAGE_REQUEST3 = 3
    val IMAGE_REQUEST4 = 4
    val LOCATION_REQUEST = 5
    var edit = false
    var locationManualyChanged = false
    var map: GoogleMap? = null
    var locationService: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()
    var fireStore: VenueFireStore? = null


    init {
        if (app.venues is VenueFireStore) {
            fireStore = app.venues as VenueFireStore
        }


        if (view.intent.hasExtra("venue_edit")) {
            edit = true
            venue = view.intent.extras?.getParcelable<VenueModel>("venue_edit")!!
            view.showVenue(venue)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }

    fun doAddOrSave(
        name: String,
        description: String,
        notes: String,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        venue.name = name
        venue.description = description
        venue.notes = notes
        venue.dateVisitedYear = year
        venue.dateVisitedMonth = month
        venue.dateVisitedDay = dayOfMonth
        if (venue.name.isNotEmpty()) {
            doAsync {
                if (edit) {
                    app.venues.update(venue)
                } else {
                    app.venues.create(venue)
                }
                uiThread {
                    view!!.intent.removeExtra("Fav")
                    fireStore!!.fetchVenues {
                        view?.navigateTo(VIEW.LIST)
                    }

                }
            }
        } else {
            view?.toast(R.string.enter_title)
        }

    }


    fun cacheVenue(
        venueTitle: String,
        description: String,
        notes: String,
        dayOfMonth: Int,
        month: Int,
        year: Int,
        visited: Boolean,
        favourite: Boolean,
        rating: Float
    )
    {
        venue.name = venueTitle
        venue.description = description
        venue.notes = notes
        venue.dateVisitedDay = dayOfMonth
        venue.dateVisitedMonth = month
        venue.dateVisitedYear = year
        venue.visited

    }

    fun doSelectImage1() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST1)
        }
    }

    fun doSelectImage2() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST2)
        }
    }

    fun doSelectImage3() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST3)
        }
    }

    fun doSelectImage4() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST4)
        }
    }

    fun doSetLocation() {
        locationManualyChanged = true;
        view?.navigateTo(
            VIEW.LOCATION,
            LOCATION_REQUEST,
            "location",
            Location(venue.lat, venue.lng, venue.zoom)
        )
    }

    fun doDelete() {
        doAsync {
            app.venues.delete(venue)
            uiThread {
                view?.navigateTo(VIEW.LIST)
            }
        }
    }

    fun doCheckVisited(isChecked: Boolean) {
        if (isChecked) {
            view!!.date_visited.setVisibility(View.VISIBLE)

            view!!.date_title.setVisibility(View.VISIBLE)
            venue.visited = true
        } else {
            view!!.date_visited.setVisibility(View.GONE)
            view!!.date_title.setVisibility(View.GONE)
            venue.visited = false
        }
    }

    fun doCheckFav(isChecked: Boolean) {
        venue.fav = isChecked
    }

    fun doCheckRating(rating: Float) {
        venue.rating = rating
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST1 -> {
                if (data != null) {
                    venue.image1 = data.data.toString()
                    view?.showVenue(venue)
                }
            }
            IMAGE_REQUEST2 -> {
                if (data != null) {
                    venue.image2 = data.data.toString()
                    view?.showVenue(venue)
                }
            }

            IMAGE_REQUEST3 -> {
                if (data != null) {
                    venue.image3 = data.data.toString()
                    view?.showVenue(venue)
                }
            }

            IMAGE_REQUEST4 -> {
                if (data != null) {
                    venue.image4 = data.data.toString()
                    view?.showVenue(venue)
                }
            }

            LOCATION_REQUEST -> {

                    val location = data.extras?.getParcelable<Location>("location")!!
                    venue.lat = location.lat
                    venue.lng = location.lng
                    venue.zoom = location.zoom
                    locationUpdate(venue.lat, venue.lng)

            }
        }
    }

    override fun doRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(venue.lat, venue.lng)
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(venue.lat, venue.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        venue.lat = lat
        venue.lng = lng
        venue.zoom = 8f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options =
            MarkerOptions().title(venue.name).position(LatLng(venue.lat, venue.lng))
        map?.addMarker(options)
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(venue.lat, venue.lng),
                venue.zoom
            )
        )
        view?.showVenue(venue)
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    if (!locationManualyChanged) {
                        locationUpdate(l.latitude, l.longitude)
                    }
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
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


