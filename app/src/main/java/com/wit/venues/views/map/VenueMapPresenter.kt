package com.wit.venues.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.wit.venues.models.VenueModel
import com.wit.venues.views.BasePresenter
import com.wit.venues.views.BaseView

class VenueMapPresenter(view: BaseView) : BasePresenter(view) {

    fun doPopulateMap(map: GoogleMap, venues: List<VenueModel>) {
        map.uiSettings.setZoomControlsEnabled(true)
        venues.forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as Long
        val venue = app.venues.findById(tag)
        if (venue != null) view?.showVenue(venue)
    }

    fun loadVenues() {
        view?.showVenues(app.venues.findAll())
    }
}