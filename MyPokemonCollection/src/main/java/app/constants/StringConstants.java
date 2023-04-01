package app.constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public interface StringConstants {

    String FIRESTORE_REFERENCE_TEMPLATE = "pokemon_home/poke_capture_#_000_%_n_00000000_f_?.png";
    String POKEDEX_NUMBER_PLACEHOLDER = "#";
    String GENDER_PLACEHOLDER = "%";
    String SHINY_PLACEHOLDER = "?";
    String ALL_POKEMON_LINK = "https://pokemondb.net/pokedex/all";
    String ALL_ABILITIES_LINK = "https://pokemondb.net/ability";
    String POKEMON_PAGE_LINK = "https://pokemondb.net/pokedex/?";
    String POKEMON_SPRITE_LINK = "https://img.pokemondb.net/sprites/sword-shield/icon/?.png";
    String POKEMON_MOVES_LINK = "https://pokemondb.net/pokedex/?/moves/7";
    String MOVES_LINK = "https://pokemondb.net/move/all";
    String MOVE_LINK = "https://pokemondb.net/move/?";
    String MOVE_CATEGORY_LINK = "https://img.pokemondb.net/images/icons/move-?.png";


    static String encodeStrings(String[] tags, String[] values)
            throws UnsupportedEncodingException, IllegalArgumentException {
        if (tags.length != values.length) {
            throw new IllegalArgumentException();
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < tags.length; i++) {
            s.append("&").append(URLEncoder.encode(tags[i], "UTF-8")).append("=")
                    .append(URLEncoder.encode(values[i], "UTF-8"));
        }
        return s.substring(1);
    }
}
