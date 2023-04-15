//package app.async_tasks.web_scraping;
//
//import static app.constants.StringConstants.ALL_ABILITIES_LINK;
//import static app.constants.StringConstants.ALL_POKEMON_LINK;
//
//import android.os.AsyncTask;
//import android.os.Build;
//
//import androidx.annotation.RequiresApi;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import app.async_tasks.TaskHelper;
//import app.data_objects.Ability;
//import app.data_objects.Pokemon;
//import app.data_objects.Type;
//import app.ui.fragments.ICallbackContext;
//
//public class GetPokemonListAT extends AsyncTask<List<Pokemon>, String, List<Pokemon>> {
//    private final ICallbackContext callbackContext;
//
//    public GetPokemonListAT(ICallbackContext callbackContext) {
//        this.callbackContext = callbackContext;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    @Override
//    protected List<Pokemon> doInBackground(List<Pokemon>... params) {
//        List<Pokemon> pokemonList = params[0];
//
//        List<Thread> threads = new ArrayList<>();
//        pokemonList.forEach(pokemon -> threads.add(new Thread(() -> {
//            getPokemonData(pokemon);
//            pokemon.setAbility(getPokemonAbility(pokemon.getAbility().getName()));
//            pokemon.setMoves(pokemon.getMoves().stream().map(move->TaskHelper.getMove(move.getName())).collect(Collectors.toList()));
//        })));
//
//        threads.forEach(Thread::start);
//        threads.forEach(thread -> {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        return pokemonList;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    public static void getPokemonData(Pokemon pokemon) {
//        Document doc = null;
//        try {
//            doc = Jsoup.connect(ALL_POKEMON_LINK).get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        assert doc != null;
//
//        Elements elements = doc.getElementsByClass("infocard-cell-data");
//        for (Element e : elements) {
//            long value = Long.parseLong(e.text().replaceFirst("^0+(?!$)", ""));
//            if (value == pokemon.getPokedexNumber()) {
//                Element parent = e.parent().parent();
//                String species = parent.getElementsByClass("ent-name").text();
//                List<Type> types = parent.getElementsByClass("type-icon").stream()
//                        .map(Element::text)
//                        .map(Type::getType)
//                        .collect(Collectors.toList());
//                List<Long> baseStats = parent.getElementsByClass("cell-num").stream()
//                        .map(bs -> Long.parseLong(bs.text()))
//                        .collect(Collectors.toList());
//                baseStats.remove(0);
//                baseStats.remove(0);
//                pokemon.setSpecies(species);
//                pokemon.setTypes(types);
//                pokemon.setBaseStats(baseStats);
//                break;
//            }
//        }
//    }
//
//    public static Ability getPokemonAbility(String s) {
//        Document doc = null;
//        try {
//            doc = Jsoup.connect(ALL_ABILITIES_LINK).get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        assert doc != null;
//        Elements elements = doc.getElementsByClass("ent-name");
//        for (Element e : elements) {
//            if (e.text().equals(s)) {
//                return new Ability(
//                        e.text(),
//                        e.parent().parent().getElementsByClass("cell-med-text").get(0).text()
//                );
//            }
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(List<Pokemon> result) {
//        if (isCancelled()) return;
//        if (result == null) callbackContext.timedOut(this);
//        else callbackContext.callback(this, result);
//    }
//}