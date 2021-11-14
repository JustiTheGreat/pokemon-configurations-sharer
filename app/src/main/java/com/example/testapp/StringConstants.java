package com.example.testapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public interface StringConstants {
    String LOGIN_SUCCESS = "You logged into your account!";
    String REGISTER_SUCCESS = "You successfully created a new account!";
    String SERVER_IS_OFFLINE = "Server is offline!";
    String LOGIN_PROBLEMS = "Login problems!";
    String LOGIN_LINK = "http://192.168.0.11/login.php";
    String REGISTER_LINK = "http://192.168.0.11/register.php";
    String GET_POKEMON_FROM_DATABASE_LINK = "http://192.168.0.11/get_pokemon.php";
    String ALL_POKEMON_LINK = "https://pokemondb.net/pokedex/all";
    String ALL_ABILITIES_LINK = "https://pokemondb.net/ability";

    default String encodeStrings(String[] tags, String[] values) throws UnsupportedEncodingException, IllegalArgumentException {
        if (tags.length != values.length) {
            throw new IllegalArgumentException();
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < tags.length; i++) {
            if (i == 0) {
                s.append(URLEncoder.encode(tags[i], "UTF-8")).append("=").append(URLEncoder.encode(values[i], "UTF-8"));
            } else {
                s.append("&").append(URLEncoder.encode(tags[i], "UTF-8")).append("=").append(URLEncoder.encode(values[i], "UTF-8"));
            }
        }
        return s.toString();
    }

    static String getPokemonImageLink(String s) throws NullPointerException {
        if (s == null) throw new NullPointerException();
        return "https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + s + ".png";
    }

    static String getPokemonPageLink(String s) throws NullPointerException {
        if (s == null) throw new NullPointerException();
        return "https://pokemondb.net/pokedex/" + s;
    }

    static String getPokemonIconLink(String s) throws NullPointerException {
        if (s == null) throw new NullPointerException();
        return "https://img.pokemondb.net/sprites/sword-shield/icon/" + s + ".png";
    }

    static String getPokemonMovesLink(String s) throws NullPointerException {
        if (s == null) throw new NullPointerException();
        return "https://pokemondb.net/pokedex/" + s + "/moves/7";
    }

    static String getPokemonMoveLink(String s) throws NullPointerException {
        if (s == null) throw new NullPointerException();
        return "https://pokemondb.net/move/" + s;
    }
}
