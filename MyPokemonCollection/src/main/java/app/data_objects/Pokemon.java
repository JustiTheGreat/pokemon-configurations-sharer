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

@RequiresApi(api = Build.VERSION_CODES.R)
public class Pokemon {

    private String ID;
    private String name;
    private long pokedexNumber;
    private String species;
    private String gender;
    private boolean shiny;
    private long level;
    private Ability ability;
    private Nature nature;
    private List<Long> IVs;
    private List<Long> EVs;
    private List<Move> moves;
    private List<Type> types;
    private List<Long> baseStats;
    private Bitmap image;
    private Bitmap sprite;
    private String userId;

    private Pokemon() {
    }

    public static Pokemon newPokemon() {
        return new Pokemon();
    }

    public static Pokemon clone(Pokemon pokemon) {
        return newPokemon()
                .id(pokemon.ID)
                .name(pokemon.name)
                .pokedexNumber(pokemon.pokedexNumber)
                .species(pokemon.species)
                .gender(pokemon.gender)
                .shiny(pokemon.shiny)
                .level(pokemon.level)
                .ability(pokemon.ability)
                .nature(pokemon.nature)
                .ivs(pokemon.IVs == null ? null : new ArrayList<>(pokemon.IVs))
                .evs(pokemon.EVs == null ? null : new ArrayList<>(pokemon.EVs))
                .moves(pokemon.moves == null ? null : new ArrayList<>(pokemon.moves))
                .types(pokemon.types)
                .baseStats(pokemon.baseStats)
                .image(pokemon.image)
                .sprite(pokemon.sprite)
                .userId(pokemon.userId);
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
        return newPokemon().name(s[0])
                .pokedexNumber(Long.parseLong(s[1]))
                .gender(s[2])
                .shiny(Boolean.parseBoolean(s[3]))
                .level(Long.parseLong(s[4]))
                .ability(new Ability(s[5]))
                .nature(Nature.getNature(s[6]))
                .ivs(Arrays.stream(s[7].split(":")).map(Long::parseLong).collect(Collectors.toList()))
                .evs(Arrays.stream(s[8].split(":")).map(Long::parseLong).collect(Collectors.toList()))
                .moves(Arrays.stream(s[9].split(":")).map(Move::new).collect(Collectors.toList()));
    }

    public Pokemon id(String ID) {
        this.ID = ID;
        return this;
    }

    public Pokemon name(String name) {
        this.name = name;
        return this;
    }

    public Pokemon pokedexNumber(long pokedexNumber) {
        this.pokedexNumber = pokedexNumber;
        return this;
    }

    public Pokemon species(String species) {
        this.species = species;
        return this;
    }

    public Pokemon gender(String gender) {
        this.gender = gender;
        return this;
    }

    public Pokemon shiny(boolean shiny) {
        this.shiny = shiny;
        return this;
    }

    public Pokemon level(long level) {
        this.level = level;
        return this;
    }

    public Pokemon ability(Ability ability) {
        this.ability = ability;
        return this;
    }

    public Pokemon nature(Nature nature) {
        this.nature = nature;
        return this;
    }

    public Pokemon ivs(List<Long> IVs) {
        this.IVs = IVs;
        return this;
    }

    public Pokemon evs(List<Long> EVs) {
        this.EVs = EVs;
        return this;
    }

    public Pokemon moves(List<Move> moves) {
        this.moves = moves;
        return this;
    }

    public Pokemon types(List<Type> types) {
        this.types = types;
        return this;
    }

    public Pokemon baseStats(List<Long> baseStats) {
        this.baseStats = baseStats;
        return this;
    }

    public Pokemon image(Bitmap image) {
        this.image = image;
        return this;
    }

    public Pokemon sprite(Bitmap sprite) {
        this.sprite = sprite;
        return this;
    }

    public Pokemon userId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPokedexNumber() {
        return pokedexNumber;
    }

    public void setPokedexNumber(long pokedexNumber) {
        this.pokedexNumber = pokedexNumber;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isShiny() {
        return shiny;
    }

    public void setShiny(boolean shiny) {
        this.shiny = shiny;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    public List<Long> getIVs() {
        return IVs;
    }

    public void setIVs(List<Long> IVs) {
        this.IVs = IVs;
    }

    public List<Long> getEVs() {
        return EVs;
    }

    public void setEVs(List<Long> EVs) {
        this.EVs = EVs;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public List<Long> getBaseStats() {
        return baseStats;
    }

    public void setBaseStats(List<Long> baseStats) {
        this.baseStats = baseStats;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getSprite() {
        return sprite;
    }

    public void setSprite(Bitmap sprite) {
        this.sprite = sprite;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}