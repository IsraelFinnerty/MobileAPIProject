package com.wit.venues.main

import android.app.Application
import com.wit.api.VenueService
import com.wit.venues.models.VenueModel
import com.wit.venues.models.VenueModelAPI
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.wit.venues.models.VenueStore
import com.wit.venues.models.firebase.VenueFireStore

class MainApp : Application(), AnkoLogger {
  lateinit var venues: VenueStore
  lateinit var venueService: VenueService
  var apiList = ArrayList<VenueModelAPI>()

  override fun onCreate() {
    super.onCreate()
    venueService = VenueService.create()
    info("Venue Service Created")
    venues = VenueFireStore(applicationContext)
    info("Venue App Started")
      }

}