package app.ui.fragments;

import static app.constants.Gender.FEMALE_GENDER;
import static app.constants.Gender.FEMALE_GENDER_VALUES;
import static app.constants.Gender.MALE_GENDER;
import static app.constants.Gender.MALE_GENDER_VALUES;
import static app.constants.Gender.UNKNOWN_GENDER;
import static app.constants.Messages.SERVER_TIMEOUT;
import static app.constants.PokemonConstants.NORMAL_VALUE;
import static app.constants.PokemonConstants.SHINY_VALUE;
import static app.constants.StringConstants.FIRESTORE_REFERENCE_TEMPLATE;
import static app.constants.StringConstants.GENDER_PLACEHOLDER;
import static app.constants.StringConstants.POKEDEX_NUMBER_PLACEHOLDER;
import static app.constants.StringConstants.SHINY_PLACEHOLDER;
import static app.constants.collection_fields.Pokemon.ABILITY;
import static app.constants.collection_fields.Pokemon.EVS;
import static app.constants.collection_fields.Pokemon.GENDER;
import static app.constants.collection_fields.Pokemon.IVS;
import static app.constants.collection_fields.Pokemon.LEVEL;
import static app.constants.collection_fields.Pokemon.MOVE1;
import static app.constants.collection_fields.Pokemon.MOVE2;
import static app.constants.collection_fields.Pokemon.MOVE3;
import static app.constants.collection_fields.Pokemon.MOVE4;
import static app.constants.collection_fields.Pokemon.NAME;
import static app.constants.collection_fields.Pokemon.NATURE;
import static app.constants.collection_fields.Pokemon.POKEDEX_NUMBER;
import static app.constants.collection_fields.Pokemon.SHINY;
import static app.constants.collection_fields.Pokemon.UID;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentCollectionBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app.storages.Storage;
import app.async_tasks.database.GetPokemonList;
import app.async_tasks.database.ICallbackContext;
import app.data_objects.Ability;
import app.data_objects.Move;
import app.data_objects.Nature;
import app.data_objects.Pokemon;
import app.layout_adapters.PokemonConfigurationAdapter;
import app.ui.activities.MainActivity;

public class PokemonCollection extends UtilityFragment implements ICallbackContext {
    private FragmentCollectionBinding binding;
    private List<Pokemon> pokemonList;
    private GetPokemonList getPokemonListTask;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        binding.fCGridview.setOnItemClickListener(this::seePokemonDetails);
        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        binding.fCAddOptionsButton.setOnClickListener(this::addOptions);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Storage.setPokemonCollectionFragment(this);
        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
//        ((MainActivity) requireActivity()).setToolbarMenuVisible();
        getPokemonListTask = new GetPokemonList(this);
        getPokemonList();
    }

    private void seePokemonDetails(AdapterView<?> adapterView, View view, int position, long id) {
        Storage.setSelectedPokemon(pokemonList.get((int)id));
        navigateTo(R.id.action_collection_to_details);
    }

    private void addOptions(View view) {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_add_options);
        Window window = dialog.getWindow();
        window.setDimAmount(0);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.x = binding.fCAddOptionsButton.getWidth() / 2 + 100;
        layoutParams.y = 30;

        window.setAttributes(layoutParams);
        dialog.show();
        //dialog.findViewById(R.id.dao_write).setOnClickListener(view1 -> addPokemonManually(dialog));
        //dialog.findViewById(R.id.dao_scan).setOnClickListener(view1 -> scanQRCode(dialog));
    }

    private void addPokemonManually(Dialog dialog) {
        navigateTo(R.id.action_collection_to_add);
        dialog.dismiss();
    }

//    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
//            result -> {
//                if (result != null) {
//                    if (result.getContents() == null) {
//                        Toast.makeText(this.getActivity(), "Scan cancelled!", Toast.LENGTH_LONG).show();
//                    } else {
//                        binding.fCGridview.setEnabled(false);
//                        binding.fCAddOptionsButton.setEnabled(false);
//                        Toast.makeText(this.getActivity(), "Scan successful!", Toast.LENGTH_LONG).show();
//                        new CreatePokemon().execute(this, result.getContents());
//                    }
//                }
//            });
//
//    private void scanQRCode(Dialog dialog) {
//        ScanOptions options = new ScanOptions();
//        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
//        options.setOrientationLocked(false);
//        options.setPrompt("Scan a QR code");
//        options.setBeepEnabled(false);
//        barcodeLauncher.launch(options);
//        dialog.dismiss();
//    }

    public void afterANewPokemonWasCreatedInScan() {
        navigateTo(R.id.action_collection_to_add);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (getPokemonListTask.getStatus() != AsyncTask.Status.FINISHED) {
            getPokemonListTask.cancel(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void callback(Object caller, Object result) {
        if (caller instanceof GetPokemonList) {
            pokemonList = (List<Pokemon>) result;
            BaseAdapter baseAdapter = new PokemonConfigurationAdapter(this.getActivity(), new ArrayList<>(pokemonList));
            binding.fCGridview.setAdapter(baseAdapter);
            binding.fCProgressbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void timedOut() {
        toast(SERVER_TIMEOUT);
        binding.fCProgressbar.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void getPokemonList() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            timedOut();
            return;
        }
        String user_id = currentUser.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pokemon")
                .whereEqualTo(UID, user_id)
                .get()
                .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) timedOut();
                            else {
                                List<Pokemon> pokemonList = new ArrayList<>();
                                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                                for (DocumentSnapshot document : documents) {
                                    Pokemon pokemon = new Pokemon(
                                            document.getId(),
                                            document.getString(NAME),
                                            document.getLong(POKEDEX_NUMBER),
                                            null,
                                            document.getString(GENDER),
                                            document.getBoolean(SHINY),
                                            document.getLong(LEVEL),
                                            new Ability(document.getString(ABILITY)),
                                            Nature.getNature(document.getString(NATURE)),
                                            new ArrayList<>(((Map<String, Long>) document.get(IVS)).values()),
                                            new ArrayList<>(((Map<String, Long>) document.get(EVS)).values()),
                                            Arrays.asList(
                                                    new Move(document.getString(MOVE1)),
                                                    new Move(document.getString(MOVE2)),
                                                    new Move(document.getString(MOVE3)),
                                                    new Move(document.getString(MOVE4))
                                            ),
                                            null,
                                            null,
                                            null,
                                            null
                                    );

                                    StorageReference storageReference = getImageStorageReference(pokemon.getPokedexNumber(), pokemon.getGender(), pokemon.isShiny());
                                    final long ONE_MEGABYTE = 1024 * 1024;
                                    storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> pokemon.setImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));

                                    pokemonList.add(pokemon);
                                }
                                getPokemonListTask.execute(pokemonList);
                            }
                        }
                );
    }

    private StorageReference getImageStorageReference(long pokedexNumber, String gender, boolean isShiny) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        String s = FIRESTORE_REFERENCE_TEMPLATE
                .replace(POKEDEX_NUMBER_PLACEHOLDER, String.format("%04d", pokedexNumber))
                .replace(SHINY_PLACEHOLDER, isShiny ? SHINY_VALUE : NORMAL_VALUE);
        StorageReference pathReference = null;
        switch (gender) {
            case MALE_GENDER:
                for (String genderValue : MALE_GENDER_VALUES) {
                    String temp = s.replace(GENDER_PLACEHOLDER, genderValue);
                    pathReference = storageReference.child(temp);
                    try {
                        pathReference.getDownloadUrl();
                        break;
                    } catch (Exception ignored) {
                    }
                }
                break;
            case FEMALE_GENDER:
                for (String genderValue : FEMALE_GENDER_VALUES) {
                    String temp = s.replace(GENDER_PLACEHOLDER, genderValue);
                    pathReference = storageReference.child(temp);
                    try {
                        pathReference.getDownloadUrl();
                        break;
                    } catch (Exception ignored) {
                    }
                }
                break;
            case UNKNOWN_GENDER:
                String temp = s.replace(GENDER_PLACEHOLDER, gender);
                pathReference = storageReference.child(temp);
                break;
        }
        return pathReference;
    }
}
