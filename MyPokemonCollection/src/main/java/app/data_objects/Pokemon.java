package app.data_objects;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import app.constants.PokemonConstants;
import lombok.Getter;
import lombok.Setter;

@RequiresApi(api = Build.VERSION_CODES.R)
public class Pokemon implements Serializable {
    @Getter @Setter private String ID;
    @Getter @Setter private String name;
    @Getter @Setter private long pokedexNumber;
    @Getter @Setter private String species;
    @Getter @Setter private String gender;
    @Getter @Setter private boolean shiny;
    @Getter @Setter private long level;
    @Getter @Setter private Ability ability;
    @Getter @Setter private Nature nature;
    @Getter @Setter private List<Long> IVs;
    @Getter @Setter private List<Long> EVs;
    @Getter @Setter private List<Move> moves;
    @Getter @Setter private List<Type> types;
    @Getter @Setter private List<Long> baseStats;
    @Getter @Setter private Bitmap image;
    @Getter @Setter private Bitmap sprite;

    public Pokemon(String ID, String name, long pokedexNumber, String species, String gender,
                   boolean shiny, long level, Ability ability, Nature nature, List<Long> IVs,
                   List<Long> EVs, List<Move> moves, List<Type> types, List<Long> baseStats,
                   Bitmap image, Bitmap sprite) {
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
        for (Long iv : IVs) s.append(";").append(iv);
        for (Long ev : EVs) s.append(";").append(ev);
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
                pokedexNumber,
                species,
                gender,
                shiny,
                level,
                ability,
                nature,
                new ArrayList<>(IVs),
                new ArrayList<>(EVs),
                new ArrayList<>(moves),
                types,
                baseStats,
                image,
                sprite
        );
    }
}