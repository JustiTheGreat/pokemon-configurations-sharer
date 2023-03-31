//package app.async_tasks;
//
//import android.os.AsyncTask;
//import android.os.Build;
//
//import androidx.annotation.RequiresApi;
//import androidx.fragment.app.Fragment;
//
//import app.constants.PokemonConstants;
//import app.constants.StringConstants;
//import app.data_objects.SpeciesRow;
//import app.ui.fragments.AddPokemon;
//
//import java.util.ArrayList;
//
//public class GetAllSpecies extends AsyncTask implements PokemonConstants, StringConstants {
//    private Fragment fragment;
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    protected Object doInBackground(Object[] objects) {
//        this.fragment = (Fragment) objects[0];
//        return TaskHelper.getAllSpecies();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    @Override
//    protected void onPostExecute(Object object) {
//        if (isCancelled()) return;
//        ((AddPokemon) fragment).setAllSpeciesData((ArrayList<SpeciesRow>) object);
//    }
//}
