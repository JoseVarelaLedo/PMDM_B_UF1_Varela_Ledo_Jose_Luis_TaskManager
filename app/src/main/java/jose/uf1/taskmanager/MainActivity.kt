package jose.uf1.taskmanager

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            handleBottomNavigation(menuItem)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleSideNavigation(menuItem)
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun handleBottomNavigation(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.choiceFragment -> {
                navController.navigate(R.id.choiceFragment)
                return true
            }
            R.id.newTaskFragment -> {
                navController.navigate(R.id.newTaskFragment)
                return true
            }
            R.id.newNoteFragment -> {
                navController.navigate(R.id.newNoteFragment)
                return true
            }
            else -> return false
        }
    }
    private fun handleSideNavigation(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.choiceFragment -> {
                navController.navigate(R.id.choiceFragment)

            }
            R.id.newTaskFragment -> {
                navController.navigate(R.id.newTaskFragment)

            }
            R.id.newNoteFragment -> {
                navController.navigate(R.id.newNoteFragment)
            }
            R.id.tasksFragment -> {
                navController.navigate(R.id.tasksFragment)
            }
            R.id.notesFragment -> {
                navController.navigate(R.id.notesFragment)
            }
        }
        drawerLayout.closeDrawers()
    }
}