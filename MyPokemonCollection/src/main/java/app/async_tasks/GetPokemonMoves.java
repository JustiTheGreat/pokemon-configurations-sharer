//package app.async_tasks;
//
//import android.os.AsyncTask;
//import android.os.Build;
//
//import androidx.annotation.RequiresApi;
//import androidx.fragment.app.Fragment;
//
//import app.ui.fragments.AddPokemon;
//import app.data_objects.Move;
//
//import java.util.ArrayList;
//
//public class GetPokemonMoves extends AsyncTask {
//    private Fragment fragment;
//    private ArrayList<Move> moves;
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    @Override
//    protected Object doInBackground(Object[] objects) {
//        fragment = (Fragment) objects[0];
//        moves = TaskHelper.getPokemonMoves((String) objects[1]);
//        return null;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    @Override
//    protected void onPostExecute(Object o) {
//        if (isCancelled()) return;
//        ((AddPokemon) fragment).setMoves(moves);
//    }
//}
