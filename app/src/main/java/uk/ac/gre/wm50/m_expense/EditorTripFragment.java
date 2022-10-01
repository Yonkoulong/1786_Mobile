package uk.ac.gre.wm50.m_expense;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.ac.gre.wm50.m_expense.databinding.FragmentEditorTripBinding;
import uk.ac.gre.wm50.m_expense.model.Trip;
import uk.ac.gre.wm50.m_expense.sqlite.TripDao;

public class EditorTripFragment extends Fragment {

    private EditorTripViewModel mViewModel;
    private FragmentEditorTripBinding binding;
    private TripDao tripDao;

    String tripId;
    String tripName;
    String destinationTrip;
    String dateTrip;
    String riskAssessmentTrip;
    String descriptionTrip;

    String[] listTripName = {"Conference","Client meeting", "Holiday", "Visiting", "Discovery", "Interview"};
    String[] listTripDestination = {"Manchester United", "Arsenal", "Tottenham Hotspur", "Liverpool", "Chelsea", "Manchester City"};

    AutoCompleteTextView autoCompleteTripName;
    AutoCompleteTextView autoCompleteDestination;

    ArrayAdapter<String> adapterTripName;
    ArrayAdapter<String> adapterDestination;

    EditText date_time_in;
    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd YYY HH:mm:ss");

    RadioGroup radioGroup;
    int radioId;
    public static EditorTripFragment newInstance() {
        return new EditorTripFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AppCompatActivity app = (AppCompatActivity)getActivity();
        ActionBar ab = app.getSupportActionBar();

        ab.setHomeButtonEnabled(true);

        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        setHasOptionsMenu(true);

        binding= FragmentEditorTripBinding.inflate(inflater,container,false);
        tripDao = new TripDao(getContext());

        //get Arguments from main fragment
        tripId = getArguments().getString("id");
        tripName = getArguments().getString("nameOfTheTrip");
        destinationTrip = getArguments().getString("destination");
        dateTrip = getArguments().getString("dateOfTheTrip");
        riskAssessmentTrip = getArguments().getString("requiresRiskAssessment");
        descriptionTrip = getArguments().getString("description");

        //get autoComplete by id
        autoCompleteTripName = binding.autoCompleteTripName;
        autoCompleteDestination = binding.autoCompleteDestination;

        //create array adapter
        adapterTripName = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, listTripName);
        adapterDestination = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, listTripDestination);

        //set item view to autoComplete

        //get radioButton id
        radioGroup = binding.riskAssessment;

        date_time_in = binding.editDateTime;
        date_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date_time_in);
            }
        });

        //handle onChange radio button
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_yes:
                    case R.id.radio_no:
                        radioId = checkedId;
                        break;
                }
            }
        });

        //OnClick Save and Add Trip
        Button buttonSave = binding.save;
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(this.validate() == true) {
                    saveAndReturn();
                }
            }

            private boolean validate() {
                TextInputLayout inputLayoutTripName = binding.tripName;
                TextInputLayout inputLayoutDestination = binding.tripDestination;

                AutoCompleteTextView textViewTripName = binding.autoCompleteTripName;
                AutoCompleteTextView textViewDestination = binding.autoCompleteDestination;

                EditText dateTimeEditText = binding.editDateTime;
                EditText descriptionEditText = binding.description;

                boolean isValidated = true;

                if(textViewTripName.getText().toString().isEmpty()) {
                    inputLayoutTripName.setError("You must select a trip name");
                    isValidated = false;
                } else {
                    inputLayoutTripName.setErrorEnabled(false);
                }

                if(textViewDestination.getText().toString().isEmpty()) {
                    inputLayoutDestination.setError("You must select a destination");
                    isValidated = false;
                } else {
                    inputLayoutDestination.setErrorEnabled(false);
                }

//                if(dateTimeEditText.getText().toString().isEmpty()) {
//                    dateTimeEditText.setError("You must select date and time");
//                    isValidated = false;
//                }

                return isValidated;
            }
        });

        //OnClick navigate to expense list of trip
        Button buttonExpenseList = binding.navigateToExpense;
        buttonExpenseList.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToExpenseList();
            }
        }));

        tripDao.trip.observe(
                getViewLifecycleOwner(),
                t -> {
                    binding.autoCompleteTripName.setText(tripName);
                    binding.autoCompleteDestination.setText(destinationTrip);
                    binding.riskAssessment.check(checkRadioId(riskAssessmentTrip));
                    binding.editDateTime.setText(dateTrip);

                    binding.description.setText(descriptionTrip);
                    autoCompleteTripName.setAdapter(adapterTripName);
                    autoCompleteDestination.setAdapter(adapterDestination);

                    //data come back
                    //refresh option menu
                    //this will trigger onPrepareOptionsMenu() below
                    requireActivity().invalidateOptionsMenu();
                }
        );

       tripDao.getTripById(tripId);
        return binding.getRoot();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Trip t = tripDao.trip.getValue();
        if(t != null && t.getId() == Constants.NEW_TRIP_ID) {
            //add new Trip
            menu.findItem(R.id.action_delete).setVisible(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return Navigation.findNavController(getView()).navigateUp();
            case R.id.action_delete:
                return deleteAndReturn(tripId);
            default: return super.onOptionsItemSelected(item);
        }
    }

    private boolean deleteAndReturn(String id) {
        Log.i(this.getClass().getName(), "delete and return");
        tripDao.delete(id);
        Navigation.findNavController(getView()).navigateUp();
        return true;
    }

    private boolean saveAndReturn() {
        String nameOfTheTrip = binding.autoCompleteTripName.getText().toString();
        String destinationOfTheTrip = binding.autoCompleteDestination.getText().toString();
        String dateOfTheTrip = binding.editDateTime.getText().toString();
        String riskAssessmentTrip = String.valueOf(radioId);
        String descriptionTrip = binding.description.getText().toString();

        Trip updateTrip
                = new Trip(tripId != null ? tripId: Constants.NEW_TRIP_ID,nameOfTheTrip,destinationOfTheTrip, dateOfTheTrip,riskAssessmentTrip, descriptionTrip);
        if(tripId == Constants.NEW_TRIP_ID){
            tripDao.insert(updateTrip);
        }
        else {
            tripDao.update(updateTrip);
        }

        Navigation.findNavController(getView()).navigateUp();
        return true;
    }

    private void showDateTimeDialog(EditText date_time_in) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(requireContext(), timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void navigateToExpenseList() {
        Bundle bundle = new Bundle();
        if (tripId != null) {
            bundle.putString("tripId", tripId);
            Navigation.findNavController(getView()).navigate(R.id.expensesOfTripFragment, bundle);
        };
        return;
    }

    private int checkRadioId(String radioId) {
        if(radioId == null || radioId.equals("true")) {
            radioId = "0";
        }
        return Integer.parseInt(radioId);
    }
}




