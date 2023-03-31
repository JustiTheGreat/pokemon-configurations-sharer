package app.data_objects;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import app.constants.PokemonConstants;
import lombok.Getter;
import lombok.Setter;

@RequiresApi(api = Build.VERSION_CODES.R)
public class Pokemon implements PokemonConstants {
    @Getter @Setter private String ID;
    @Getter @Setter private String name;
    @Getter @Setter private String species;
    @Getter @Setter private String gender;
    @Getter @Setter private int level;
    @Getter @Setter private Ability ability;
    @Getter @Setter private Nature nature;
    @Getter @Setter private List<Integer> IVs;
    @Getter @Setter private List<Integer> EVs;
    @Getter @Setter private List<Move> moves;
    @Getter private final List<Type> types;
    @Getter private final List<Integer> baseStats;
    @Getter private final Bitmap officialArt;
    @Getter private final Bitmap sprite;

    public Pokemon(String ID, String name, String species, String gender, int level, Ability ability,
                   Nature nature, List<Integer> IVs, List<Integer> EVs, List<Move> moves,
                   List<Type> types, List<Integer> baseStats, Bitmap officialArt, Bitmap sprite) {
        this.ID = ID;
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.level = level;
        this.ability = ability;
        this.nature = nature;
        this.IVs = IVs;
        this.EVs = EVs;
        this.moves = moves;
        this.types = types;
        this.baseStats = baseStats;
        this.officialArt = officialArt;
        this.sprite = sprite;
    }

    public String toStringOfTransmissibleData() {
        StringBuilder s = new StringBuilder(""
                + name + ";"
                + species + ";"
                + gender + ";"
                + level + ";"
                + ability.getName() + ";"
                + nature.getName()
        );
        for (Integer iv : IVs) s.append(";").append(iv);
        for (Integer ev : EVs) s.append(";").append(ev);
        for (Move move : moves) {
            s.append(";");
            if (move != null) s.append(move.getName());
        }
        s.append(";");
        return s.toString();
    }

    public Pokemon copy(){
        return new Pokemon(
                ID,
                name,
                species,
                gender,
                level,
                ability,
                nature,
                new ArrayList<Integer>(){{addAll(IVs);}},
                new ArrayList<Integer>(){{addAll(EVs);}},
                new ArrayList<Move>(){{addAll(moves);}},
                types,
                baseStats,
                officialArt,
                sprite
        );
    }
}