package com.wit.venues.activities

import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.firebase.auth.FirebaseAuth
import com.wit.venues.R
import com.wit.venues.models.User
import com.wit.venues.views.venue.VenueView
import com.wit.venues.views.venuelist.VenueListView
import com.wit.venues.views.login.LoginView
import com.wit.venues.views.settings.SettingsView
import kotlinx.android.synthetic.main.activity_venue_list.*
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity


class NavDrawer : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var drawerLayout: DrawerLayout
    var user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)
        // user_email.setText(user!!.email)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)




        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_list, R.id.nav_add, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_drawer, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun navigationListener(user: User?) {
        nav_view_settings.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_list -> {
                    startActivityForResult(intentFor<VenueListView>().putExtra("User", user), 0)
                    true
                }
                R.id.nav_settings -> {
                    startActivityForResult(intentFor<SettingsView>().putExtra("User", user), 0)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_add -> {
                    startActivityForResult(intentFor<VenueView>().putExtra("User", user), 0)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    startActivity<LoginView>()
                    true
                }
                else -> {
                    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    false
                }
            }
        }
    }
}
