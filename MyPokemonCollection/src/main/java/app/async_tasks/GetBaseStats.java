//package app.async_tasks;
//
//import android.os.AsyncTask;
//import android.os.Build;
//
//import androidx.annotation.RequiresApi;
//import androidx.fragment.app.Fragment;
//
//import app.ui.fragments.AddPokemon;
//
//import java.util.ArrayList;
//
//public class GetBaseStats extends AsyncTask {
//    private Fragment fragment;
//    private ArrayList<Integer> baseStats;
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    protected Object doInBackground(Object[] objects) {
//        fragment = (Fragment) objects[0];
//        baseStats = TaskHelper.getPokemonBaseStats((String) objects[1]);
//        return null;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    @Override
//    protected void onPostExecute(Object object) {
//        if (isCancelled()) return;
//        ((AddPokemon) fragment).setBaseStats(baseStats);
//    }
//}