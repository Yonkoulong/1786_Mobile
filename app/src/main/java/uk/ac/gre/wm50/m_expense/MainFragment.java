package uk.ac.gre.wm50.m_expense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
//import android.widget.SearchView;

import java.util.ArrayList;

import uk.ac.gre.wm50.m_expense.databinding.FragmentMainBinding;
import uk.ac.gre.wm50.m_expense.model.Trip;
import uk.ac.gre.wm50.m_expense.sqlite.TripDao;

public class MainFragment extends Fragment implements TripListAdapter.ListTripListener {

    private FragmentMainBinding binding;
    private TripListAdapter adapter;
    ArrayList<Trip> listItem;
    private SearchView searchTrip;
    TripDao tripDao;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AppCompatActivity aca = (AppCompatActivity) getActivity();
        aca.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        binding = FragmentMainBinding.inflate(inflater, container, false);
        tripDao = new TripDao(getContext());

        searchTrip = binding.searchTripName;
        searchTrip.clearFocus();
        searchTrip.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tripDao.search(newText);
                return true;
            }
        });

        RecyclerView rv = binding.recyclerView;
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(
                getContext(),
                (new LinearLayoutManager(getContext())).getOrientation())
        );

       tripDao.tripList.observe(
                getViewLifecycleOwner(),
                tripList -> {
                    adapter = new TripListAdapter(tripList, this);
                    binding.recyclerView.setAdapter(adapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
        );

        binding.fabAddTrip.setOnClickListener(v -> this.onItemClick(null));

        return binding.getRoot();
    }

    @Override
    public void onItemClick(String tripId) {
        Bundle bundle = new Bundle();
        if (tripId != null) {
            Trip trip = tripDao.getById(tripId);
            bundle.putString("id", trip.getId());
            bundle.putString("nameOfTheTrip", trip.getNameOfTheTrip());
            bundle.putString("destination", trip.getDestination());
            bundle.putString("dateOfTheTrip", trip.getDateOfTheTrip());
            bundle.putString("requiresRiskAssessment", trip.getRequiresRiskAssessment());
            bundle.putString("description", trip.getDescription());
            System.out.println("Trip: "+ bundle);

        }
        Navigation.findNavController(getView()).navigate(R.id.editorTripFragment, bundle);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(this.getClass().getName(), "onResume");

        tripDao.getAll();
    }

}