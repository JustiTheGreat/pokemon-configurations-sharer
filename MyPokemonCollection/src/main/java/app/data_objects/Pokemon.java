package app.data_objects;

import static app.constants.PokemonDatabaseFields.ABILITY;
import static app.constants.PokemonDatabaseFields.EVS;
import static app.constants.PokemonDatabaseFields.GENDER;
import static app.constants.PokemonDatabaseFields.IVS;
import static app.constants.PokemonDatabaseFields.LEVEL;
import static app.constants.PokemonDatabaseFields.MOVES;
import static app.constants.PokemonDatabaseFields.NAME;
import static app.constants.PokemonDatabaseFields.NATURE;
import static app.constants.PokemonDatabaseFields.POKEDEX_NUMBER;
import static app.constants.PokemonDatabaseFields.SHINY;
import static app.constants.PokemonDatabaseFields.USER_ID;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@RequiresApi(api = Build.VERSION_CODES.R)
public class Pokemon {
    @Getter
    @Setter
    private String ID;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private long pokedexNumber;
    @Getter
    @Setter
    private String species;
    @Getter
    @Setter
    private String gender;
    @Getter
    @Setter
    private boolean shiny;
    @Getter
    @Setter
    private long level;
    @Getter
    @Setter
    private Ability ability;
    @Getter
    @Setter
    private Nature nature;
    @Getter
    @Setter
    private List<Long> IVs;
    @Getter
    @Setter
    private List<Long> EVs;
    @Getter
    @Setter
    private List<Move> moves;
    @Getter
    @Setter
    private List<Type> types;
    @Getter
    @Setter
    private List<Long> baseStats;
    @Getter
    @Setter
    private Bitmap image;
    @Getter
    @Setter
    private Bitmap sprite;
    @Getter
    @Setter
    private String userId;

    public Pokemon(String ID, String name, long pokedexNumber, String species, String gender, boolean shiny, long level, Ability ability,
                   Nature nature, List<Long> IVs, List<Long> EVs, List<Move> moves, List<Type> types,
                   List<Long> baseStats, Bitmap image, Bitmap sprite, String userId) {
        this.ID = ID;
        this.name = name;
        this.pokedexNumber = pokedexNumber;
        this.species = species;
        this.gender = gender;
        this.shiny = shiny;
        this.level = level;
        this.ability = ability;
        this.nature = nature;
        this.IVs = IVs;
        this.EVs = EVs;
        this.moves = moves;
        this.types = types;
        this.baseStats = baseStats;
        this.image = image;
        this.sprite = sprite;
        this.userId = userId;
    }

    public Pokemon(long pokedexNumber, String species, String gender, List<Type> types, List<Long> baseStats, Bitmap sprite) {
        this.pokedexNumber = pokedexNumber;
        this.species = species;
        this.gender = gender;
        this.shiny = false;
        this.level = -1;
        this.types = types;
        this.baseStats = baseStats;
        this.sprite = sprite;
    }

    public void setData(Pokemon pokemon) {
        this.ID = pokemon.ID;
        this.name = pokemon.name;
        this.pokedexNumber = pokemon.pokedexNumber;
        this.species = pokemon.species;
        this.gender = pokemon.gender;
        this.shiny = pokemon.shiny;
        this.level = pokemon.level;
        this.ability = pokemon.ability;
        this.nature = pokemon.nature;
        this.IVs = new ArrayList<>(pokemon.IVs);
        this.EVs = new ArrayList<>(pokemon.EVs);
        this.moves.addAll(pokemon.moves);
        this.types = pokemon.types;
        this.baseStats = pokemon.baseStats;
        this.image = pokemon.image;
        this.sprite = pokemon.sprite;
        this.userId = pokemon.userId;
    }

    public Map<String, Object> getDatabaseMapObject() {
        Map<String, Object> mapObject = new HashMap<>();
        mapObject.put(ABILITY, ability.getName());
        mapObject.put(EVS, EVs);
        mapObject.put(GENDER, gender);
        mapObject.put(IVS, IVs);
        mapObject.put(LEVEL, level);
        mapObject.put(MOVES, moves.stream().map(Move::getName).collect(Collectors.toList()));
        mapObject.put(NAME, name);
        mapObject.put(NATURE, nature.getName());
        mapObject.put(POKEDEX_NUMBER, pokedexNumber);
        mapObject.put(SHINY, shiny);
        mapObject.put(USER_ID, userId);
        return mapObject;
    }

    public String toStringOfTransmissibleData() {
        StringBuilder s = new StringBuilder(""
                + name + ";"
                + pokedexNumber + ";"
                + gender + ";"
                + shiny + ";"
                + level + ";"
                + ability.getName() + ";"
                + nature.getName() + ";"
        );
        for (Long iv : IVs) s.append(iv).append(";");
        s.replace(s.length() - 1, s.length(), "");
        s.append(";");
        for (Long ev : EVs) s.append(ev).append(";");
        s.replace(s.length() - 1, s.length(), "");
        s.append(";");
        for (Move move : moves) s.append(move.getName()).append(";");
        s.replace(s.length() - 1, s.length(), "");
        s.append(";");
        return s.toString();
    }

    public static Pokemon fromStringOfTransmissibleData(String pokemonString) {
        String[] s = pokemonString.split(";");
        Pokemon pokemon = new Pokemon(
                null,
                s[0],
                Long.parseLong(s[1]),
                null,
                s[2],
                Boolean.parseBoolean(s[3]),
                Long.parseLong(s[4]),
                new Ability(s[5]),
                Nature.getNature(s[6]),
                Arrays.stream(s[7].split(":")).map(Long::parseLong).collect(Collectors.toList()),
                Arrays.stream(s[8].split(":")).map(Long::parseLong).collect(Collectors.toList()),
                Arrays.stream(s[9].split(":")).map(Move::new).collect(Collectors.toList()),
                null,
                null,
                null,
                null,
                null
        );
        return pokemon;
    }
}