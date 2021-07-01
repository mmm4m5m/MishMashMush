package localhost.mmm4m5m.mishmashmush

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
//import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import localhost.mmm4m5m.mishmashmush.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_App_NoActionBar)
        //setContentView(R.layout.activity_main)
        //??? todo data vs view binding (use data with view binding?)
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_main) // gradle.build/dataBinding; layout.xml/layout
        binding = ActivityMainBinding.inflate(layoutInflater) // gradle.build/viewBinding
        setContentView(binding.root)

        //setSupportActionBar(findViewById(R.id.toolbar))
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navController)
        binding.navigationView.menu.findItem(R.id.test_old_divider).isVisible = PROJECT_TEST
        binding.navigationView.menu.findItem(R.id.action_game)     .isVisible = PROJECT_TEST
        binding.navigationView.menu.findItem(R.id.action_about)    .isVisible = PROJECT_TEST
        binding.navigationView.setNavigationItemSelectedListener(::onNavigationItemSelectedListener)

        //??? todo do something; todo hide it
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG) //???
                .setAction("Action", null).show()
        }

        //??? todo show about for first time
        if (PROJECT_TEST_ABOUT_ACTIVITY) {
            //setContentView(R.layout.activity_about)
            startActivity(Intent(this, AboutActivity::class.java))
        } else if (PROJECT_TEST_GAME_ACTIVITY) {
            startActivity(Intent(this, GameActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.test_old_divider).isVisible = PROJECT_TEST
        menu.findItem(R.id.action_game)     .isVisible = PROJECT_TEST
        menu.findItem(R.id.action_about)    .isVisible = PROJECT_TEST
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        //return findNavController(R.id.nav_host_fragment).navigateUp()
        return NavigationUI.navigateUp(findNavController(R.id.nav_host_fragment), binding.drawerLayout)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, findNavController(R.id.nav_host_fragment))
                || when (item.itemId) {
            R.id.action_game  -> { startActivity(Intent(this, GameActivity::class.java)); true }
            R.id.action_about -> { startActivity(Intent(this, AboutActivity::class.java)); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onNavigationItemSelectedListener(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_game  -> { startActivity(Intent(this, GameActivity::class.java)); true }
            R.id.action_about -> { startActivity(Intent(this, AboutActivity::class.java)); true }
            else -> false
        }
    }
}
