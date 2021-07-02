package localhost.mmm4m5m.mishmashmush

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import localhost.mmm4m5m.mishmashmush.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_App_NoActionBar)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setSupportActionBar(findViewById(R.id.toolbar))
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.navHostFragmentMain)
        navController.addOnDestinationChangedListener(::onDestinationChangedListener)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        binding.navigationView.menu.findItem(R.id.testOldDividerDrawer).isVisible = PROJECT_TEST
        binding.navigationView.menu.findItem(R.id.gameActivityDrawer)  .isVisible = PROJECT_TEST
        binding.navigationView.menu.findItem(R.id.aboutActivityDrawer) .isVisible = PROJECT_TEST
        //binding.navigationView.setNavigationItemSelectedListener(::onNavigationItemSelectedListener) // used by NavController
        binding.navigationView.menu.findItem(R.id.aboutDrawer)        .setOnMenuItemClickListener(::onMenuItemClickListener)
        binding.navigationView.menu.findItem(R.id.gameQuestionsDrawer).setOnMenuItemClickListener(::onMenuItemClickListener)
        binding.navigationView.menu.findItem(R.id.gameActivityDrawer) .setOnMenuItemClickListener(::onMenuItemClickListener)
        binding.navigationView.menu.findItem(R.id.aboutActivityDrawer).setOnMenuItemClickListener(::onMenuItemClickListener)

        //??? todo use fab
        binding.floatingActionButton.setOnClickListener { view ->
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
        menu.findItem(R.id.testOldDividerMenu).isVisible = PROJECT_TEST
        menu.findItem(R.id.gameActivityMenu)  .isVisible = PROJECT_TEST
        menu.findItem(R.id.aboutActivityMenu) .isVisible = PROJECT_TEST
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //PROJECT_TEST_TOAST(this, navController.currentBackStackEntry?.destination.toString())
        when {
            item.itemId == R.id.aboutMenu         -> navController.navigate(R.id.aboutFragmentNav)
            //NavigationUI.onNavDestinationSelected(item, navController) -> {}
            item.itemId == R.id.gameQuestionsMenu -> navController.navigate(R.id.gameTitleFragmentNav)
            item.itemId == R.id.gameActivityMenu  -> startActivity(Intent(this, GameActivity::class.java))
            item.itemId == R.id.aboutActivityMenu -> startActivity(Intent(this, AboutActivity::class.java))
            else -> return super.onOptionsItemSelected(item)
        }
        binding.drawerLayout.close()
        return true
    }

//    private fun onNavigationItemSelectedListener(item: MenuItem): Boolean {
//        when {
//            item.itemId == R.id.aboutDrawer         -> navController.navigate(R.id.aboutFragmentNav)
//            //NavigationUI.onNavDestinationSelected(item, navController) -> {} // like NavController
//            item.itemId == R.id.gameQuestionsDrawer -> navController.navigate(R.id.gameTitleFragmentNav)
//            item.itemId == R.id.gameActivityDrawer  -> startActivity(Intent(this, GameActivity::class.java))
//            item.itemId == R.id.aboutActivityDrawer -> startActivity(Intent(this, AboutActivity::class.java))
//            else -> return false
//        }
//        binding.drawerLayout.close()
//        return true
//    }

    private fun onMenuItemClickListener(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.aboutDrawer         -> navController.navigate(R.id.aboutFragmentNav)
            R.id.gameQuestionsDrawer -> navController.navigate(R.id.gameTitleFragmentNav)
            R.id.gameActivityDrawer  -> startActivity(Intent(this, GameActivity::class.java))
            R.id.aboutActivityDrawer -> startActivity(Intent(this, AboutActivity::class.java))
            else -> return false
        }
        binding.drawerLayout.close()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        //return navController.navigateUp()
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    private fun onDestinationChangedListener(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        //PROJECT_TEST_TOAST(this, destination.id)
        when (destination.id) {
            R.id.diceFragmentNav -> binding.floatingActionButton.show()
            else -> binding.floatingActionButton.hide()
        }
    }

    override fun onResume() {
        super.onResume() //??? todo test pause/resume
        navController.addOnDestinationChangedListener(::onDestinationChangedListener)
    }

    override fun onPause() {
        navController.removeOnDestinationChangedListener(::onDestinationChangedListener)
        super.onPause() //??? todo test pause/resume
    }
}
