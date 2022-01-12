package com.example.testapp;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.testapp.constants.FragmentConstants;
import com.example.testapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements FragmentConstants {

    private AppBarConfiguration appBarConfiguration;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        binding.toolbar.getMenu().setGroupVisible(0,false);
        return true;
    }

    public void setToolbarMenuVisible(){
        binding.toolbar.getMenu().setGroupVisible(0,true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.m_log_out) {
            switch(Storage.getCurrentFragmentType()){
                case COLLECTION:
                    binding.toolbar.getMenu().setGroupVisible(0,false);
                    Storage.setCurrentFragmentType(OTHER);
                    NavHostFragment
                            .findNavController(Storage.getPokemonCollectionFragment())
                            .navigate(R.id.action_collection_to_login);
                    break;
                case DETAILS:
                    binding.toolbar.getMenu().setGroupVisible(0,false);
                    Storage.setCurrentFragmentType(OTHER);
                    NavHostFragment
                            .findNavController(Storage.getPokemonDetailsFragment())
                            .navigate(R.id.action_details_to_login);
                    break;
                case ADD:
                    binding.toolbar.getMenu().setGroupVisible(0,false);
                    Storage.setCurrentFragmentType(OTHER);
                    NavHostFragment
                            .findNavController(Storage.getAddPokemonFragment())
                            .navigate(R.id.action_add_to_login);
                    break;
                default:
            }
            return true;
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



