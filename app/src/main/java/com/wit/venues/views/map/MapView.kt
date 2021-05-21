package com.wit.venues.views.map

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.wit.venues.R
import com.wit.venues.models.Location
import com.wit.venues.views.BaseView
import kotlinx.android.synthetic.main.activity_map.*

class MapView : BaseView(), GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener  {


    lateinit var presenter: MapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        presenter = initPresenter(MapPresenter(this)) as MapPresenter

        toolbarMap.title = title
        setSupportActionBar(toolbarMap)

        mapEdit.onCreate(savedInstanceState)
        mapEdit.getMapAsync {
            it.setOnMarkerDragListener(this)
            it.setOnMarkerClickListener(this)
            presenter.doConfigureMap(it)
        }


        }

    override fun showLocation(location: Location) {
        lat.setText("%.6f".format(location.lat))
        lng.setText("%.6f".format(location.lng))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit_location, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_save -> {
                presenter.doSave()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onMarkerDragStart(marker: Marker) {}

    override fun onMarkerDrag(marker: Marker) {
        lat.setText("%.6f".format(marker.position.latitude))
        lng.setText("%.6f".format(marker.position.longitude))
    }

    override fun onMarkerDragEnd(marker: Marker) {
        presenter.doUpdateLocation(marker.position.latitude, marker.position.longitude)
    }

    override fun onBackPressed() {
        presenter.doSave()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doUpdateMarker(marker)
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        mapEdit.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapEdit.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapEdit.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapEdit.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapEdit.onSaveInstanceState(outState)
    }


}