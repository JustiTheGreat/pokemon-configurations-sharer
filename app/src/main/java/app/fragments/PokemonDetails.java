package app.fragments;

import static app.constants.PokemonConstants.NUMBER_OF_STATS;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import app.MainActivity;
import app.constants.PokemonConstants;
import com.example.testapp.R;
import app.Storage;
import app.async_tasks.database.DeleteTask;
import app.data_objects.Move;
import app.data_objects.Pokemon;
import com.example.testapp.databinding.FragmentPokemonDetailsBinding;
import app.layout_adapters.MoveItemAdapterForDetails;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class PokemonDetails extends Fragment {
    private Pokemon pokemon;
    private FragmentPokemonDetailsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Storage.setPokemonDetailsFragment(this);
        ((MainActivity) requireActivity()).setToolbarMenuVisible();

        if(!Storage.pokemonIsSelectedForDetails()){
            System.err.println("Call on PokemonDetails with null pokemon in storage");
            System.exit(-1);
        }
        pokemon = Storage.getSelectedPokemonForDetails();
        setPageInfo();
        //adapting layouts when open clicking one
        {
            LinearLayout ability = binding.fPDAbility;
            LinearLayout hideAbility = binding.fPDHideAbility;
            LinearLayout stats = binding.fPDStats;
            LinearLayout hideStats = binding.fPDHideStats;
            ListView moves = binding.fPDMoves;
            LinearLayout hideMoves = binding.fPDHideMoves;
            LinearLayout space = binding.fPDSpace;

            int ability_weight = 5, stats_weight = 20, moves_weight = 32;
            int space_weight = 100 - ability_weight - stats_weight - moves_weight;
            ability.setVisibility(View.GONE);
            stats.setVisibility(View.GONE);
            moves.setVisibility(View.GONE);
            {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                params.weight = space_weight;
                space.setLayoutParams(params);
            }
            hideAbility.setOnClickListener(v -> {
                if (ability.getVisibility() == View.GONE) {
                    ability.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                    params.weight = params.weight + ability_weight;
                    space.setLayoutParams(params);
                } else {
                    ability.setVisibility(View.GONE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                    params.weight = params.weight - ability_weight;
                    space.setLayoutParams(params);
                }
            });
            hideStats.setOnClickListener(v -> {
                if (stats.getVisibility() == View.GONE) {
                    stats.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                    params.weight = params.weight + stats_weight;
                    space.setLayoutParams(params);
                } else {
                    stats.setVisibility(View.GONE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                    params.weight = params.weight - stats_weight;
                    space.setLayoutParams(params);
                }
            });
            hideMoves.setOnClickListener(v -> {
                if (moves.getVisibility() == View.GONE) {
                    moves.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                    params.weight = params.weight + moves_weight;
                    space.setLayoutParams(params);
                } else {
                    moves.setVisibility(View.GONE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                    params.weight = params.weight - moves_weight;
                    space.setLayoutParams(params);
                }
            });
        }
        //end of adapting layouts
        binding.fPDDelete.setOnClickListener(v -> deletePokemon());
        binding.fPDQrCode.setOnClickListener(v -> showQRCode());
        binding.fPDEdit.setOnClickListener(v -> editPokemon());
    }

    private void deletePokemon() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.show();

        dialog.findViewById(R.id.d_delete_yes).setOnClickListener(v -> {
            new DeleteTask().execute(this, pokemon);
            dialog.dismiss();
        });
        dialog.findViewById(R.id.d_delete_no).setOnClickListener(v -> dialog.dismiss());
    }

    private void showQRCode() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_qr);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels / 2;
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(pokemon.toStringOfTransmisibleData(), BarcodeFormat.QR_CODE, width, width);
            ImageView imageViewQrCode = dialog.findViewById(R.id.d_qr_image);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void editPokemon() {
        Storage.setSelectedPokemonForAdd(pokemon);
        NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_details_to_add);
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setPageInfo() {
        binding.fPDName.setText(pokemon.getName());
        binding.fPDImage.setImageBitmap(pokemon.getOfficialArt());

        String text = pokemon.getSpecies()
                + " " + pokemon.getGender()
                + " Lv. " + pokemon.getLevel();
        binding.fPDSpecies.setText(text);
        binding.fPDNature.setText(pokemon.getNature().getName());

        binding.fPDType1.setText(pokemon.getTypes().get(0).getName().toUpperCase());
        binding.fPDType1.setBackgroundResource(pokemon.getTypes().get(0).getColor());
        if (pokemon.getTypes().size() == 1) {
            binding.fPDType2.setVisibility(View.GONE);
        } else {
            binding.fPDType2.setText(pokemon.getTypes().get(1).getName().toUpperCase());
            binding.fPDType2.setBackgroundResource(pokemon.getTypes().get(1).getColor());
        }

        binding.fPDAbilityName.setText(pokemon.getAbility().getName());
        binding.fPDAbilityDescription.setText(pokemon.getAbility().getDescription());

        ArrayList<ArrayList<TextView>> statsViews = new ArrayList<ArrayList<TextView>>() {{
            add(
                    new ArrayList<TextView>() {{
                        add(binding.fPDTotalHp);
                        add(binding.fPDTotalAttack);
                        add(binding.fPDTotalDefense);
                        add(binding.fPDTotalSpecialAttack);
                        add(binding.fPDTotalSpecialDefense);
                        add(binding.fPDTotalSpeed);
                    }}
            );
            add(
                    new ArrayList<TextView>() {{
                        add(binding.fPDBaseHp);
                        add(binding.fPDBaseAttack);
                        add(binding.fPDBaseDefense);
                        add(binding.fPDBaseSpecialAttack);
                        add(binding.fPDBaseSpecialDefense);
                        add(binding.fPDBaseSpeed);
                    }}
            );
            add(
                    new ArrayList<TextView>() {{
                        add(binding.fPDIvsHp);
                        add(binding.fPDIvsAttack);
                        add(binding.fPDIvsDefense);
                        add(binding.fPDIvsSpecialAttack);
                        add(binding.fPDIvsSpecialDefense);
                        add(binding.fPDIvsSpeed);
                    }}
            );
            add(
                    new ArrayList<TextView>() {{
                        add(binding.fPDEvsHp);
                        add(binding.fPDEvsAttack);
                        add(binding.fPDEvsDefense);
                        add(binding.fPDEvsSpecialAttack);
                        add(binding.fPDEvsSpecialDefense);
                        add(binding.fPDEvsSpeed);
                    }}
            );
        }};

        for (int i = 0; i < NUMBER_OF_STATS; i++) {
            statsViews.get(1).get(i).setText(String.valueOf(pokemon.getBaseStats().get(i)));
            statsViews.get(2).get(i).setText(String.valueOf(pokemon.getIVs().get(i)));
            statsViews.get(3).get(i).setText(String.valueOf(pokemon.getEVs().get(i)));
        }
        ArrayList<Integer> totalStats = PokemonConstants.calculateStats(
                pokemon.getBaseStats(),
                pokemon.getIVs(),
                pokemon.getEVs(),
                pokemon.getLevel(),
                pokemon.getNature());
        for (int i = 0; i < NUMBER_OF_STATS; i++) {
            statsViews.get(0).get(i).setText(String.valueOf(totalStats.get(i)));
        }

        ArrayList<Move> moves = new ArrayList<>();
        pokemon.getMoves().forEach(move->{if(move!=null)moves.add(move);});
        MoveItemAdapterForDetails moveItemsAdapter = new MoveItemAdapterForDetails(this.getContext(), moves);
        binding.fPDMoves.setAdapter(moveItemsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
