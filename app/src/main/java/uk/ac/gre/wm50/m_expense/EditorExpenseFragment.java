package uk.ac.gre.wm50.m_expense;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.ac.gre.wm50.m_expense.databinding.FragmentEditorExpenseBinding;
import uk.ac.gre.wm50.m_expense.databinding.FragmentEditorTripBinding;
import uk.ac.gre.wm50.m_expense.model.Expense;
import uk.ac.gre.wm50.m_expense.sqlite.ExpenseDao;
import uk.ac.gre.wm50.m_expense.sqlite.TripDao;

public class EditorExpenseFragment extends Fragment {

    private EditorExpenseViewModel mViewModel;
    private FragmentEditorExpenseBinding binding;
    private ExpenseDao expenseDao;

    String tripId;
    String expenseId;
    String typeExpense;
    double amountExpense;
    String dateExpense;
    String commentsExpense;

    String[] listTypeOfExpense = {"Lunch", "BreakFast", "Dinner"};
    AutoCompleteTextView autoCompleteTypeExpenses;
    ArrayAdapter<String> adapterTypeExpenses;

    EditText date_time_in;
    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd YYY HH:mm:ss");

    public static EditorExpenseFragment newInstance() {
        return new EditorExpenseFragment();
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

        binding= FragmentEditorExpenseBinding.inflate(inflater,container,false);

        //get Arguments from expenseFragment
        expenseId = getArguments().getString("id");
        tripId = getArguments().getString("tripId");
        typeExpense = getArguments().getString("typeOfExpense");
        amountExpense = getArguments().getDouble("amountOfTheExpense");
        dateExpense = getArguments().getString("timeOfTheExpense");
        commentsExpense = getArguments().getString("additionalComments");

        expenseDao = new ExpenseDao(getContext(), tripId);

        //get autoComplete by id
        autoCompleteTypeExpenses = binding.autoCompleteTypeExpenses;

        //Create array adapter
        adapterTypeExpenses = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, listTypeOfExpense);

        date_time_in = binding.editDateTime;
        date_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date_time_in);
            }
        });

        //OnCLick Save and Add expense to trip
        Button buttonSave = binding.save;
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveAndReturn();
            }
        });

        expenseDao.expense.observe(
                getViewLifecycleOwner(),
                e -> {
                    binding.autoCompleteTypeExpenses.setText(typeExpense);
                    binding.amountOfTheExpense.setText(String.valueOf(amountExpense));
                    binding.editDateTime.setText(dateExpense);
                    binding.additionalComments.setText(commentsExpense);

                    //set item view to autoComplete
                    autoCompleteTypeExpenses.setAdapter(adapterTypeExpenses);

                    //data come back
                    //refresh option menu
                    //this will trigger onPrepareOptionsMenu() below
                    requireActivity().invalidateOptionsMenu();
                }
        );

        expenseDao.getExpenseById(expenseId);
        return binding.getRoot();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Expense e = expenseDao.expense.getValue();
        if(e != null && e.getId() == Constants.NEW_EXPENSE_ID) {
            //add new expense
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
                return deleteAndReturn(expenseId);
            default: return super.onOptionsItemSelected(item);
        }
    }

    private boolean deleteAndReturn(String id) {
        Log.i(this.getClass().getName(), "delete and return");
        expenseDao.delete(id);
        Navigation.findNavController(getView()).navigateUp();
        return true;
    }

    private boolean saveAndReturn() {
        String typeExpense = binding.autoCompleteTypeExpenses.getText().toString();
        double amountExpense = Double.parseDouble(binding.amountOfTheExpense.getText().toString());
        String dateOfTheExpense = binding.editDateTime.getText().toString();
        String commentsExpense = binding.additionalComments.getText().toString();

        Expense updateExpense = new Expense(expenseId != null ? expenseId: Constants.NEW_EXPENSE_ID, tripId ,typeExpense, amountExpense, dateOfTheExpense, commentsExpense);

        if(expenseId == Constants.NEW_EXPENSE_ID) {
            expenseDao.insert(updateExpense);
        } else {
            expenseDao.update(updateExpense);
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

}