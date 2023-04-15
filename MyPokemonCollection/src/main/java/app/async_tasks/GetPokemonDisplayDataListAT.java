package app.async_tasks;

import static app.constants.StringConstants.ALL_POKEMON_LINK;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import app.data_objects.Pokemon;
import app.data_objects.Type;
import app.ui.fragments.ICallbackContext;

public class GetPokemonDisplayDataListAT extends AsyncTask<List<Pokemon>, String, List<Pokemon>> {
    private final ICallbackContext callbackContext;

    public GetPokemonDisplayDataListAT(ICallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected List<Pokemon> doInBackground(List<Pokemon>... params) {
        List<Pokemon> pokemonList = params[0];

        List<Thread> threads = new ArrayList<>();
        pokemonList.forEach(pokemon -> threads.add(new Thread(() -> getPokemonDisplayData(pokemon))));
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return pokemonList;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void getPokemonDisplayData(Pokemon pokemon) {
        Document doc = null;
        try {
            doc = Jsoup.connect(ALL_POKEMON_LINK).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert doc != null;

        Elements elements = doc.getElementsByClass("infocard-cell-data");
        for (Element e : elements) {
            long value = Long.parseLong(e.text().replaceFirst("^0+(?!$)", ""));
            if (value == pokemon.getPokedexNumber()) {
                Element parent = e.parent().parent();
                String species = parent.getElementsByClass("ent-name").text();
                List<Type> types = parent.getElementsByClass("type-icon").stream()
                        .map(Element::text)
                        .map(Type::getType)
                        .collect(Collectors.toList());
                pokemon.setSpecies(species);
                pokemon.setTypes(types);
                break;
            }
        }
    }

    @Override
    protected void onPostExecute(List<Pokemon> result) {
        if (isCancelled()) return;
        if (result == null) callbackContext.timedOut(this);
        else callbackContext.callback(this, result);
    }
}
