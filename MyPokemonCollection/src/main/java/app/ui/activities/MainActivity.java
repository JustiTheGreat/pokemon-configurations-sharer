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

import java.util.Objects;

import app.ui.fragments.AddPokemon;
import app.ui.fragments.GeneralisedFragment;
import app.ui.fragments.PokemonCollection;
import app.ui.fragments.PokemonDetails;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set activity action bar with nav controller
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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    public void setLogoutVisibility(boolean visible) {
        if (menu != null) menu.setGroupVisible(0, visible);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            int id = item.getItemId();
            if (id == R.id.m_log_out) {
                menu.setGroupVisible(0, false);

                GeneralisedFragment<?> currentFragment = (GeneralisedFragment<?>)
                        Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main))
                        .getChildFragmentManager().getFragments().get(0);
                FirebaseAuth.getInstance().signOut();
                if (currentFragment instanceof PokemonCollection) {
                    currentFragment.navigateTo(R.id.action_collection_to_login);
                } else if (currentFragment instanceof PokemonDetails) {
                    currentFragment.navigateTo(R.id.action_details_to_login);
                } else if (currentFragment instanceof AddPokemon) {
                    currentFragment.navigateTo(R.id.action_add_to_login);
                }
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



