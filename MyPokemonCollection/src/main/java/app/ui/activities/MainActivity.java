package app.ui.activities;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.ActivityMainBinding;

import app.storages.Storage;
import app.ui.fragments.AddPokemon;
import app.ui.fragments.PokemonCollection;
import app.ui.fragments.PokemonDetails;
import app.ui.fragments.UtilityFragment;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //todo
        //set activity action bar with controller
        setSupportActionBar(binding.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //set background
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(this, R.color.analogous_1),
                        ContextCompat.getColor(this, R.color.analogous_2),
                        ContextCompat.getColor(this, R.color.analogous_3),
                        ContextCompat.getColor(this, R.color.analogous_4),
                        //ContextCompat.getColor(this, R.color.analogous_5)
                });
        getWindow().getDecorView().setBackground(gradientDrawable);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        binding.toolbar.getMenu().setGroupVisible(0, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.m_log_out) {
            binding.toolbar.getMenu().setGroupVisible(0, false);
            UtilityFragment currentFragment = Storage.getCurrentFragment();
            FirebaseAuth.getInstance().signOut();
            if (currentFragment instanceof PokemonCollection) {
                currentFragment.navigateTo(R.id.action_collection_to_login);
            } else if (currentFragment instanceof PokemonDetails) {
                currentFragment.navigateTo(R.id.action_details_to_login);
            } else if (currentFragment instanceof AddPokemon) {
                currentFragment.navigateTo(R.id.action_add_to_login);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}



