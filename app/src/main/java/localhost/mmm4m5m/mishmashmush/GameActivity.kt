package localhost.mmm4m5m.mishmashmush

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //??? old navigation - fix 'up' after 'back'
        NavigationUI.setupActionBarWithNavController(this, findNavController(R.id.nav_host_fragment))

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //??? old navigation - finish if first fragment
        if (item.itemId == android.R.id.home) { finish(); return true }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        //??? old navigation - finish if first fragment
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }
}
