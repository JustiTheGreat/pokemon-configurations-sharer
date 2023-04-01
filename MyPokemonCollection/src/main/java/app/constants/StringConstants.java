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
    String POKEMON_SPRITE_LINK = "https://www.serebii.net/pokedex-sv/icon/new/?.png";
    String POKEMON_MOVES_LINK = "https://pokemondb.net/pokedex/?/moves/7";
    String MOVES_LINK = "https://pokemondb.net/move/all";
    String MOVE_LINK = "https://pokemondb.net/move/?";
    String MOVE_CATEGORY_LINK = "https://img.pokemondb.net/images/icons/move-?.png";
}
