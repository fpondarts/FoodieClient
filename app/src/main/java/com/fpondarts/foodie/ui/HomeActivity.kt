package com.fpondarts.foodie.ui

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.Toast
import androidx.lifecycle.Observer
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.AuthRepository
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.services.MyLocationService
import com.fpondarts.foodie.ui.auth2.AuthActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.nav_header_home.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class HomeActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    val repository : UserRepository by instance()

    private lateinit var locationRequest: LocationRequest

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        val token = intent.getStringExtra("token")
        val id = intent.getLongExtra("user_id",-1)

        if (id.equals(-1)){
            Toast.makeText(this,"Error en el login",Toast.LENGTH_LONG).show()
            val newIntent = Intent(this,AuthActivity::class.java)
            startActivity(newIntent)
            finish()
        }

        repository.initUser(token,id)

        repository.currentUser.observe(this, Observer {
            it?.let{
                drawer_user_name?.text = it.name
                drawer_user_email?.text = it.email
            }
        })

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_profile, R.id.myOrdersFragment, R.id.favours_nav_home,R.id.deliveredOrdersFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        // 5 es el índice del item logout
        navView.menu.getItem(5).setOnMenuItemClickListener {
            FirebaseAuth.getInstance().signOut()
            val repository : AuthRepository by instance()
            repository.role = null
            repository.userId = null
            repository.token = null
            val intent = Intent(this,AuthActivity::class.java)
            startActivity(intent)
            finish()
            true
        }

        val headerView = navView.getHeaderView(0)

        fusedLocationProviderClient = FusedLocationProviderClient(this)




        askLocationPermission()

        repository.apiError.observe(this, Observer {
            it?.let {
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            }
        })


    }

    lateinit var settingsRequest : LocationSettingsRequest

    final val REQUEST_CHECK_SETTINGS = 999

    private fun updateLocation() {

        buildLocationRequest()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)


        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {

            onSettingsOk()
       }

        task.addOnFailureListener {
            if (it is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    it.startResolutionForResult(this@HomeActivity,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Toast.makeText(this,sendEx.message,Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS){
            if (resultCode == Activity.RESULT_OK){
                onSettingsOk()
            } else {
                updateLocation()
            }
        }
    }

    private fun onSettingsOk(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,getPendingIntent())

    }

    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(this@HomeActivity, MyLocationService::class.java)
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE_USER)
        return PendingIntent.getBroadcast(this@HomeActivity,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement=10f

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun askLocationPermission(){

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object: PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                    updateLocation()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    askLocationPermission()
                    Toast.makeText(this@HomeActivity,"This permission should be accepted",Toast.LENGTH_LONG).show()
                }

            }).check()

    }

}
