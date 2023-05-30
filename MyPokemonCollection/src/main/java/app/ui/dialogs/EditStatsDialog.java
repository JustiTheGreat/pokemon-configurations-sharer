package app.ui.dialogs;

import static app.data_objects.Nature.NATURES;

import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.mypokemoncollection.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import app.ui.fragments.ICallbackContext;
import app.data_objects.Nature;
import app.data_objects.Pokemon;
import app.stats_calculators.IStatsCalculator;
import app.stats_calculators.StatsCalculator;
import app.ui.fragments.AddPokemon;

public class EditStatsDialog extends GeneralisedDialog {
    private final Pokemon pokemon;
    private EditText levelET;
    private Spinner natureSpinner;
    private final List<EditText> ivsETs = new ArrayList<>();
    private final List<EditText> evsETs = new ArrayList<>();
    private final List<TextView> totalTVs = new ArrayList<>();

    public EditStatsDialog(ICallbackContext context, Pokemon pokemon) {
        super(context);
        this.pokemon = pokemon;
    }

    @Override
    protected void create() {
        dialog = new android.app.Dialog(((AddPokemon) callbackContext).getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_stats);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void setupFunctionality() {
        //create TV references
        levelET = dialog.findViewById(R.id.d_stats_level);
        natureSpinner = dialog.findViewById(R.id.d_stats_nature);
        ivsETs.clear();
        ivsETs.add(dialog.findViewById(R.id.d_stats_ivhp));
        ivsETs.add(dialog.findViewById(R.id.d_stats_ivattack));
        ivsETs.add(dialog.findViewById(R.id.d_stats_ivdefense));
        ivsETs.add(dialog.findViewById(R.id.d_stats_ivspecialattack));
        ivsETs.add(dialog.findViewById(R.id.d_stats_ivspecialdefense));
        ivsETs.add(dialog.findViewById(R.id.d_stats_ivspeed));
        evsETs.clear();
        evsETs.add(dialog.findViewById(R.id.d_stats_evhp));
        evsETs.add(dialog.findViewById(R.id.d_stats_evattack));
        evsETs.add(dialog.findViewById(R.id.d_stats_evdefense));
        evsETs.add(dialog.findViewById(R.id.d_stats_evspecialattack));
        evsETs.add(dialog.findViewById(R.id.d_stats_evspecialdefense));
        evsETs.add(dialog.findViewById(R.id.d_stats_evspeed));
        totalTVs.clear();
        totalTVs.add(dialog.findViewById(R.id.d_stats_tothp));
        totalTVs.add(dialog.findViewById(R.id.d_stats_totattack));
        totalTVs.add(dialog.findViewById(R.id.d_stats_totdefense));
        totalTVs.add(dialog.findViewById(R.id.d_stats_totspecialattack));
        totalTVs.add(dialog.findViewById(R.id.d_stats_totspecialdefense));
        totalTVs.add(dialog.findViewById(R.id.d_stats_totspeed));

        //set default data in TV
        levelET.setText(String.valueOf(pokemon.getLevel()));
        natureSpinner.setAdapter(new ArrayAdapter<>(((AddPokemon) callbackContext).getActivity(),
                android.R.layout.simple_spinner_item,
                NATURES.stream().map(Nature::getName).collect(Collectors.toList())));
        Nature N = Nature.getNature(pokemon.getNature().getName());
        natureSpinner.setSelection(NATURES.indexOf(N));
        ivsETs.forEach(ivET -> ivET.setText(String.valueOf(pokemon.getIVs().get(ivsETs.indexOf(ivET)))));
        evsETs.forEach(evET -> evET.setText(String.valueOf(pokemon.getEVs().get(evsETs.indexOf(evET)))));
        setTotalStatsTVs();

        //add listeners to TV
        addOnTextChangedListener(levelET);
        natureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setTotalStatsTVs();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ivsETs.forEach(this::addOnTextChangedListener);
        evsETs.forEach(this::addOnTextChangedListener);

        //add listener to button
        Button button = dialog.findViewById(R.id.d_stats_button);
        button.setOnClickListener(v -> {
            if (levelET != null && natureSpinner != null && !ivsETs.isEmpty() && !evsETs.isEmpty()
                    && !totalTVs.isEmpty())
                callbackContext.callback(this, null);
            else callbackContext.timedOut(this);
            dialog.dismiss();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void setTotalStatsTVs() {
        long level = getInputtedLevel();
        Nature nature = getInputtedNature();
        List<Long> ivs = getInputtedIVs();
        List<Long> evs = getInputtedEVs();
        assert nature != null;
        IStatsCalculator statsCalculator = new StatsCalculator();
        List<Long> resultedStats = statsCalculator.calculateStats(pokemon.getBaseStats(), ivs, evs, level, nature);
        totalTVs.forEach(totalTV -> totalTV.setText(String.valueOf(resultedStats.get(totalTVs.indexOf(totalTV)))));
    }

    private void addOnTextChangedListener(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String correctValue = s.toString();
                if (correctValue.equals("")) {
                    editText.removeTextChangedListener(this);
                    editText.setText("0");
                    editText.setSelection(editText.getText().length());
                    editText.addTextChangedListener(this);
                } else {
                    correctValue = correctValue
                            .replace(",", "")
                            .replace(".", "")
                            .replace("-", "")
                            .replace(" ", "");
                    if (correctValue.startsWith("0") && correctValue.length() != 1) {
                        correctValue = correctValue.replace("0", "");
                    }
                    int limit = (editText.equals(levelET) ? 100 : (ivsETs.contains(editText) ? 31 : (evsETs.contains(editText) ? 252 : 0)));
                    if (Integer.parseInt(correctValue) > limit) {
                        correctValue = "" + limit;
                    }
                    editText.removeTextChangedListener(this);
                    editText.setText(correctValue);
                    editText.setSelection(editText.getText().length());
                    editText.addTextChangedListener(this);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(""))
                    setTotalStatsTVs();
            }
        });
    }

    public void setViewVisible(int resourceId) {
        dialog.findViewById(resourceId).setVisibility(View.VISIBLE);
    }

    public long getInputtedLevel() {
        if (levelET == null) return -1;
        return Long.parseLong(levelET.getText().toString());
    }

    public Nature getInputtedNature() {
        if (levelET == null) return null;
        return Nature.getNature(natureSpinner.getSelectedItem().toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Long> getInputtedIVs() {
        return ivsETs.stream().map(ivET -> Long.parseLong(ivET.getText().toString().equals("") ? "0" : ivET.getText().toString())).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Long> getInputtedEVs() {
        return evsETs.stream().map(evET -> Long.parseLong(evET.getText().toString().equals("") ? "0" : evET.getText().toString())).collect(Collectors.toList());
    }
}
