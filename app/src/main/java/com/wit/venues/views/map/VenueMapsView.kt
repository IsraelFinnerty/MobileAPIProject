package com.wit.venues.views.map

import android.os.Bundle
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_venue_maps.*
import com.wit.venues.R

import com.wit.venues.models.VenueModel
import com.wit.venues.views.BaseView
import kotlinx.android.synthetic.main.activity_venue_maps.toolbar

class VenueMapsView : BaseView(), GoogleMap.OnMarkerClickListener {

    lateinit var presenter: VenueMapPresenter
    lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_maps)
        presenter = initPresenter (VenueMapPresenter(this)) as VenueMapPresenter

        drawerLayout = findViewById(R.id.drawer_layout_map)
        toolbar.title = title
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        setSupportActionBar(toolbar)


        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        val check = drawerLayout.isDrawerOpen(GravityCompat.START)
        toolbar.setNavigationOnClickListener {
            if (!check) drawerLayout.openDrawer(GravityCompat.START)
            else  drawerLayout.closeDrawer(GravityCompat.START)
        }

        nav_view_maps.setNavigationItemSelectedListener { menuItem -> navDrawer(menuItem) }

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            presenter.loadVenues()
        }

        bottom_navigation_map.setOnNavigationItemSelectedListener { item ->  bottomNavigation(item)  }
    }

    override fun showVenue(venue: VenueModel) {
        currentTitle.text = venue.name
        currentDescription.text = venue.description
        Glide.with(currentImage).load(venue.image1).into(currentImage);
    }

    override fun showVenues(venues: List<VenueModel>) {
        presenter.doPopulateMap(map, venues)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}