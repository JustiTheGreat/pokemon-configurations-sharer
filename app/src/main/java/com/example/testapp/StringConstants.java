package com.example.testapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public interface StringConstants {
    String LOGIN_SUCCESS = "You successfully logged into your account!";
    String LOGIN_PROBLEMS = "Login problems!";
    String REGISTER_SUCCESS = "You successfully created a new account!";
    String REGISTER_PROBLEMS = "Register problems!";
    String INSERT_SUCCESS = "You added a new pokemon!";
    String INSERT_PROBLEMS = "Insert problems!";
    String SERVER_IS_OFFLINE = "Server is offline!";

    String LOGIN_LINK = "http://192.168.0.11/login.php";
    String REGISTER_LINK = "http://192.168.0.11/register.php";
    String GET_POKEMON_FROM_DATABASE_LINK = "http://192.168.0.11/get_pokemon.php";
    String INSERT_POKEMON_LINK = "http://192.168.0.11/insert_pokemon.php";

    String ALL_POKEMON_LINK = "https://pokemondb.net/pokedex/all";
    String ALL_ABILITIES_LINK = "https://pokemondb.net/ability";
    String POKEMON_OFFICIAL_ART_LINK = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/?.png";
    String POKEMON_PAGE_LINK = "https://pokemondb.net/pokedex/?";
    String POKEMON_SPRITE_LINK = "https://img.pokemondb.net/sprites/sword-shield/icon/?.png";
    String POKEMON_MOVES_LINK = "https://pokemondb.net/pokedex/?/moves/7";
    String MOVES_LINK = "https://pokemondb.net/move/all";
    String MOVE_LINK = "https://pokemondb.net/move/?";
    String MOVE_CATEGORY_LINK = "https://img.pokemondb.net/images/icons/move-?.png";


    default String encodeStrings(String[] tags, String[] values) throws UnsupportedEncodingException, IllegalArgumentException {
        if (tags.length != values.length) {
            throw new IllegalArgumentException();
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < tags.length; i++) {
            s.append("&").append(URLEncoder.encode(tags[i], "UTF-8")).append("=").append(URLEncoder.encode(values[i], "UTF-8"));
        }
        return s.substring(1);
    }
}
