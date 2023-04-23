package app.ui.fragments;

import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;
import static app.constants.Gender.FEMALE_GENDER;
import static app.constants.Gender.MALE_GENDER;
import static app.constants.Gender.UNKNOWN_GENDER;
import static app.constants.Messages.EMPTY;
import static app.constants.Messages.MOVE_ALREADY_ADDED;
import static app.constants.Messages.PLEASE_INPUT_A_NAME;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentAddPokemonBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.async_tasks.GetAllPokemonSpeciesDataAT;
import app.async_tasks.GetPokemonAbilitiesAT;
import app.async_tasks.GetPokemonMovesAT;
import app.data_objects.Ability;
import app.data_objects.Move;
import app.data_objects.Nature;
import app.data_objects.Pokemon;
import app.dialogs.AddMoveDialog;
import app.dialogs.ChooseAbilityDialog;
import app.dialogs.ChooseSpeciesDialog;
import app.dialogs.EditStatsDialog;
import app.firebase.GetPokemonHomeArtDB;
import app.firebase.InsertPokemonDB;
import app.firebase.UpdatePokemonDB;
import app.layout_adapters.MoveItemAdapterForAddEdit;
import app.stats_calculators.IStatsCalculator;
import app.stats_calculators.StatsCalculator;
import app.storages.Storage;
import lombok.Getter;

@RequiresApi(api = Build.VERSION_CODES.R)
public class AddPokemon extends UtilityFragment {
    private GetAllPokemonSpeciesDataAT getAllSpeciesTask;
    private GetPokemonAbilitiesAT getAbilitiesTask;
    private GetPokemonMovesAT getPokemonMovesTask;
    private FragmentAddPokemonBinding binding;
    private MoveItemAdapterForAddEdit movesAdapter;
    private List<Pokemon> allSpeciesData;
    private List<Ability> allAbilitiesData;
    @Getter
    private List<Move> allMovesData;
    private final long DEFAULT_POKEMON_LEVEL = 1;
    private final Nature DEFAULT_POKEMON_NATURE = Nature.getDefaultNature();
    private final List<Long> DEFAULT_POKEMON_IVS = Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L);
    private final List<Long> DEFAULT_POKEMON_EVS = Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L);
    private final Pokemon pokemon = Pokemon.newPokemon()
            .pokedexNumber(-1)
            .gender(UNKNOWN_GENDER)
            .level(DEFAULT_POKEMON_LEVEL)
            .nature(DEFAULT_POKEMON_NATURE)
            .ivs(DEFAULT_POKEMON_IVS)
            .evs(DEFAULT_POKEMON_EVS)
            .moves(new ArrayList<>());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddPokemonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fAPSpeciesButton.setEnabled(false);
        binding.fAPAbilitiesButton.setEnabled(false);
        binding.fAPStatsButton.setEnabled(false);
        binding.fAPAddMove.setEnabled(false);

        movesAdapter = new MoveItemAdapterForAddEdit(getContext(), pokemon.getMoves(), this);
        binding.fAPMoves.setAdapter(movesAdapter);

        if (Storage.getCopyOfSelectedPokemon() != null) {
            pokemon.setData(Storage.getCopyOfSelectedPokemon());

            setSpeciesInfo(pokemon, true);
            binding.fAPSpeciesButton.setText(R.string.empty);
            setAbilityInfo(pokemon.getAbility(), true);
            setStatsInfo(null, true);
            movesAdapter.notifyDataSetChanged();
            if (pokemon.getMoves().size() != 4) binding.fAPAddMove.setVisibility(View.VISIBLE);
        } else {
            getAllSpeciesTask = new GetAllPokemonSpeciesDataAT(this);
            getAllSpeciesTask.execute();

            binding.fAPChosenSpecies.setVisibility(View.GONE);
            binding.fAPChosenAbility.setVisibility(View.GONE);
            binding.fAPChosenStats.setVisibility(View.GONE);
            binding.fAPMoves.setVisibility(View.GONE);
            binding.fAPAddMove.setVisibility(View.VISIBLE);

            binding.fAPFinalize.setEnabled(false);

            binding.fAPSpeciesButton.setOnClickListener(v -> {
                app.dialogs.Dialog dialog = new ChooseSpeciesDialog(this, R.layout.dialog_search, allSpeciesData, pokemon);
                dialog.loadDialog();
            });
        }

        recalculateWeights();

        binding.fAPGender.setOnClickListener(v -> changeGender());
        binding.fAPAbilitiesButton.setOnClickListener(v -> {
            app.dialogs.Dialog dialog = new ChooseAbilityDialog(this, R.layout.dialog_search, allAbilitiesData, pokemon.getAbility());
            dialog.loadDialog();
        });
        binding.fAPStatsButton.setOnClickListener(v -> {
            app.dialogs.Dialog dialog = new EditStatsDialog(this, R.layout.dialog_stats, pokemon);
            dialog.loadDialog();
        });
        binding.fAPAddMove.setOnClickListener(v -> {
            app.dialogs.Dialog dialog = new AddMoveDialog(this, R.layout.dialog_search, allMovesData, -1);
            dialog.loadDialog();
        });
        binding.fAPFinalize.setOnClickListener(v -> nameDialog());
    }

    public void setAddMoveButtonVisibility(boolean value) {
        if (value) binding.fAPAddMove.setVisibility(View.VISIBLE);
        else binding.fAPAddMove.setVisibility(View.GONE);
    }

    public void recalculateWeights() {
        int species_weight = 10, ability_weight = 6, stats_weight = 16, move_weight = 11, add_move_weight = 11;
        int space_weight = 100 - species_weight - ability_weight - stats_weight - 4 * move_weight + add_move_weight;
        if (binding.fAPChosenSpecies.getVisibility() == View.VISIBLE)
            space_weight += species_weight;
        if (binding.fAPChosenAbility.getVisibility() == View.VISIBLE)
            space_weight += ability_weight;
        if (binding.fAPChosenStats.getVisibility() == View.VISIBLE)
            space_weight += stats_weight;

        if (binding.fAPMoves.getVisibility() == View.VISIBLE) {
            space_weight += pokemon.getMoves().size() * move_weight;
            if (pokemon.getMoves().size() == 4) space_weight -= add_move_weight;

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.fAPMoves.getLayoutParams();
            params.weight = 100 - pokemon.getMoves().size() * move_weight;
            binding.fAPMoves.setLayoutParams(params);
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.fAPSpace.getLayoutParams();
        params.weight = space_weight;
        binding.fAPSpace.setLayoutParams(params);
    }

    private void changeGender() {
        if (pokemon.getGender().equals(UNKNOWN_GENDER)) return;
        if (pokemon.getGender().equals(MALE_GENDER)) pokemon.setGender(FEMALE_GENDER);
        else if (pokemon.getGender().equals(FEMALE_GENDER)) pokemon.setGender(MALE_GENDER);
        binding.fAPGender.setText(pokemon.getGender());
    }

    public void seeIfFinalizingIsPossible() {
        binding.fAPFinalize.setEnabled(binding.fAPChosenAbility.getVisibility() == View.VISIBLE
                && binding.fAPChosenStats.getVisibility() == View.VISIBLE
                && pokemon.getMoves().size() > 0);
    }

    private void nameDialog() {
        Dialog dialog = createDialog(R.layout.dialog_add_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 80 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        EditText name = dialog.findViewById(R.id.d_addname_name);
        if (pokemon.getID() != null) name.setText(pokemon.getName());
        dialog.findViewById(R.id.d_addname_button).setOnClickListener(v -> {
            if (name.getText().toString().trim().equals(EMPTY)) {
                toast(PLEASE_INPUT_A_NAME);
                return;
            }
            pokemon.setName(name.getText().toString().trim());

            if (pokemon.getID() == null) {
                String user_id = getAuthenticatedUserId();
                pokemon.setUserId(user_id);

                disableActivityTouchInput();
                new InsertPokemonDB(this, pokemon).execute();
            } else {
                disableActivityTouchInput();
                new UpdatePokemonDB(this, pokemon).execute();
            }
            dialog.dismiss();
        });
    }

    private void cancelTasks() {
        if (getAllSpeciesTask != null && getAllSpeciesTask.getStatus() != AsyncTask.Status.FINISHED)
            getAllSpeciesTask.cancel(true);
        if (getAbilitiesTask != null && getAbilitiesTask.getStatus() != AsyncTask.Status.FINISHED)
            getAbilitiesTask.cancel(true);
        if (getPokemonMovesTask != null && getPokemonMovesTask.getStatus() != AsyncTask.Status.FINISHED)
            getPokemonMovesTask.cancel(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        cancelTasks();
    }

    @Override
    public void callback(Object caller, Object result) {
        if (caller instanceof GetAllPokemonSpeciesDataAT) {
            allSpeciesData = (List<Pokemon>) result;
            binding.fAPSpeciesButton.setEnabled(true);
        } else if (caller instanceof GetPokemonAbilitiesAT) {
            allAbilitiesData = (List<Ability>) result;
            binding.fAPAbilitiesButton.setEnabled(true);
        } else if (caller instanceof GetPokemonMovesAT) {
            allMovesData = (List<Move>) result;
            binding.fAPAddMove.setEnabled(true);
        } else if (caller instanceof ChooseSpeciesDialog) {
            setSpeciesInfo((Pokemon) result, false);
        } else if (caller instanceof ChooseAbilityDialog) {
            setAbilityInfo((Ability) result, false);
        } else if (caller instanceof EditStatsDialog) {
            setStatsInfo((EditStatsDialog) caller, false);
        } else if (caller instanceof AddMoveDialog) {
            setMoveInfo(((AddMoveDialog) caller).getMoveIndex(), (Move) result);
        } else if (caller instanceof InsertPokemonDB) {
            Pokemon pokemon = (Pokemon) result;
            Storage.setCopyOfSelectedPokemon(pokemon);
            new GetPokemonHomeArtDB(this, pokemon).execute();
        } else if (caller instanceof GetPokemonHomeArtDB) {
            Storage.getPokemonList().add((Pokemon) result);
            enableActivityTouchInput();
            navigateTo(R.id.action_add_to_collection);
        } else if (caller instanceof UpdatePokemonDB) {
            Pokemon p = (Pokemon) result;
            List<Pokemon> pokemonList = Storage.getPokemonList();
            for (Pokemon pokemon : pokemonList) {
                if (pokemon.getID().equals(p.getID())) {
                    pokemonList.set(pokemonList.indexOf(pokemon), p);
                    break;
                }
            }
            enableActivityTouchInput();
            navigateTo(R.id.action_add_to_collection);
        }
    }

    private void setSpeciesInfo(Pokemon species, boolean loadFromLocalData) {
        if (!loadFromLocalData) {
            binding.fAPAbilitiesButton.setEnabled(false);
            binding.fAPStatsButton.setEnabled(false);
            binding.fAPMoves.setEnabled(false);
            binding.fAPFinalize.setEnabled(false);
            cancelTasks();
        }

        getAbilitiesTask = new GetPokemonAbilitiesAT(this);
        getAbilitiesTask.execute(species.getPokedexNumber());
        binding.fAPStatsButton.setEnabled(true);
        getPokemonMovesTask = new GetPokemonMovesAT(this);
        getPokemonMovesTask.execute(species.getPokedexNumber());

        if (!loadFromLocalData) {
            pokemon.setPokedexNumber(species.getPokedexNumber());
            pokemon.setSprite(species.getSprite());
            pokemon.setSpecies(species.getSpecies());
            pokemon.setGender(species.getGender());
            pokemon.setTypes(species.getTypes());
            pokemon.setBaseStats(species.getBaseStats());
        }

        binding.fAPSprite.setImageBitmap(species.getSprite());
        binding.fAPSpecies.setText(species.getSpecies());
        binding.fAPGender.setText(species.getGender());

        binding.fAPType1.setText(pokemon.getTypes().get(0).getName().toUpperCase());
        binding.fAPType1.setBackgroundResource(pokemon.getTypes().get(0).getColor());
        if (pokemon.getTypes().size() == 2) {
            binding.fAPType2.setText(pokemon.getTypes().get(1).getName().toUpperCase());
            binding.fAPType2.setBackgroundResource(pokemon.getTypes().get(1).getColor());
            binding.fAPType2.setVisibility(View.VISIBLE);
        } else {
            binding.fAPType2.setVisibility(View.GONE);
        }

        if (!loadFromLocalData) {
            pokemon.getMoves().clear();
            movesAdapter.notifyDataSetChanged();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.fAPMoves.getLayoutParams();
            params.weight = 0;
            binding.fAPMoves.setLayoutParams(params);
            binding.fAPMoves.setVisibility(View.GONE);
            binding.fAPAddMove.setVisibility(View.VISIBLE);

            binding.fAPChosenSpecies.setVisibility(View.VISIBLE);
            binding.fAPChosenAbility.setVisibility(View.GONE);
            binding.fAPChosenStats.setVisibility(View.GONE);

            recalculateWeights();

            binding.fAPSpeciesButton.setText(R.string.change_species);
            binding.fAPAbilitiesButton.setText(R.string.choose_ability);
            binding.fAPStatsButton.setText(R.string.choose_stats_related);
        }
    }

    private void setAbilityInfo(Ability ability, boolean loadFromLocalData) {
        pokemon.setAbility(ability);
        binding.fAPAbilityName.setText(pokemon.getAbility().getName());
        binding.fAPAbilityDescription.setText(pokemon.getAbility().getDescription());
        if (!loadFromLocalData) {
            binding.fAPAbilitiesButton.setText(R.string.change_ability);
            if (binding.fAPChosenAbility.getVisibility() == View.GONE) {
                binding.fAPChosenAbility.setVisibility(View.VISIBLE);
                recalculateWeights();
            }
            seeIfFinalizingIsPossible();
        }
    }

    private void setStatsInfo(EditStatsDialog dialog, boolean loadFromLocalData) {
        if (!loadFromLocalData) {
            long s = dialog.getInputtedEVs().stream().mapToLong(f -> f).sum();
            if (s > 510) {
                dialog.setViewVisible(R.id.d_stats_error_message);
                return;
            }
            dialog.setTotalStatsTVs();
            pokemon.setLevel(dialog.getInputtedLevel());
            pokemon.setNature(dialog.getInputtedNature());
            pokemon.setIVs(dialog.getInputtedIVs());
            pokemon.setEVs(dialog.getInputtedEVs());
        }

        binding.fAPLevel.setText(String.valueOf(pokemon.getLevel()));
        binding.fAPNature.setText(pokemon.getNature().getName());
        IStatsCalculator calculator = new StatsCalculator();
        List<Long> resultedStats = calculator.calculateStats(pokemon.getBaseStats(), pokemon.getIVs(),
                pokemon.getEVs(), pokemon.getLevel(), pokemon.getNature());
        binding.fAPHp.setText("" + resultedStats.get(0));
        binding.fAPAttack.setText("" + resultedStats.get(1));
        binding.fAPDefense.setText("" + resultedStats.get(2));
        binding.fAPSpecialAttack.setText("" + resultedStats.get(3));
        binding.fAPSpecialDefense.setText("" + resultedStats.get(4));
        binding.fAPSpeed.setText("" + resultedStats.get(5));
        binding.fAPStatsButton.setText(R.string.change_stats_related);

        if (!loadFromLocalData) {
            if (binding.fAPChosenStats.getVisibility() == View.GONE) {
                binding.fAPChosenStats.setVisibility(View.VISIBLE);
                recalculateWeights();
            }
            seeIfFinalizingIsPossible();
        }
    }

    private void setMoveInfo(int index, Move move) {
        boolean moveAlreadyExists = false;
        for (Move m : pokemon.getMoves()) {
            if (move.getName().equals(m.getName())) {
                toast(MOVE_ALREADY_ADDED);
                moveAlreadyExists = true;
                break;
            }
        }
        if (!moveAlreadyExists) {
            if (index == -1) pokemon.getMoves().add(move);
            else pokemon.getMoves().set(index, move);
            movesAdapter.notifyDataSetChanged();

            if (pokemon.getMoves().size() == 1) binding.fAPMoves.setVisibility(View.VISIBLE);
            if (pokemon.getMoves().size() == 4) binding.fAPAddMove.setVisibility(View.GONE);

            recalculateWeights();
        }
        seeIfFinalizingIsPossible();
    }

    @Override
    public void timedOut(Object caller) {
        if(caller instanceof InsertPokemonDB || caller instanceof GetPokemonHomeArtDB || caller instanceof UpdatePokemonDB) {
            enableActivityTouchInput();
            toast(CONNECTION_TIMEOUT);
            navigateTo(R.id.action_add_to_collection);
        }
    }
}
