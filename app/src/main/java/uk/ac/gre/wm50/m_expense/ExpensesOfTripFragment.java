package uk.ac.gre.wm50.m_expense;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.gre.wm50.m_expense.databinding.FragmentExpensesOfTripBinding;
import uk.ac.gre.wm50.m_expense.model.Expense;
import uk.ac.gre.wm50.m_expense.sqlite.ExpenseDao;

public class ExpensesOfTripFragment extends Fragment implements ExpenseListAdapter.ListExpenseListener {

    private FragmentExpensesOfTripBinding binding;
    private ExpenseListAdapter adapter;
    ExpenseDao expenseDao;

    public static ExpensesOfTripFragment newInstance() {
        return new ExpensesOfTripFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        AppCompatActivity app = (AppCompatActivity)getActivity();
        ActionBar ab = app.getSupportActionBar();

        ab.setHomeButtonEnabled(true);

        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        setHasOptionsMenu(true);


        binding = FragmentExpensesOfTripBinding.inflate(inflater, container, false);

        expenseDao = new ExpenseDao(getContext(), getArguments().getString("tripId"));

        RecyclerView rve = binding.recyclerViewExpense;
        rve.setHasFixedSize(true);
        rve.addItemDecoration(new DividerItemDecoration(
                getContext(),
                (new LinearLayoutManager(getContext())).getOrientation())
        );

        expenseDao.expenseList.observe(
                getViewLifecycleOwner(),
                expenseList -> {
                    System.out.println(expenseList);
                    adapter = new ExpenseListAdapter(expenseList, this);
                    binding.recyclerViewExpense.setAdapter(adapter);
                    binding.recyclerViewExpense.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
        );

        binding.fabAddExpense.setOnClickListener(v -> this.onItemClick(null));
        expenseDao.getAll();
        return binding.getRoot();
    }

    @Override
    public void onItemClick(String expenseId) {
        Bundle bundle = new Bundle();
        if (expenseId != null) {
            Expense expense = expenseDao.getById(expenseId);
            bundle.putString("id", expense.getId());
            bundle.putString("tripId", getArguments().getString("tripId"));
            bundle.putString("typeOfExpense", expense.getTypeOfExpense());
            bundle.putDouble("amountOfTheExpense", expense.getAmountOfTheExpense());
            bundle.putString("timeOfTheExpense", expense.getTimeOfTheExpense());
            bundle.putString("additionalComments", expense.getAdditionalComments());
        }
        bundle.putString("tripId", getArguments().getString("tripId"));
        Navigation.findNavController(getView()).navigate(R.id.editorExpenseFragment, bundle);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return Navigation.findNavController(getView()).navigateUp();
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(this.getClass().getName(), "onResume");

        expenseDao.getAll();
    }
}