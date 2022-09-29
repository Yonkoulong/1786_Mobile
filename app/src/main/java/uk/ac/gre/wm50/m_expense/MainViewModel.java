package uk.ac.gre.wm50.m_expense;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gre.wm50.m_expense.model.SampleDataProvider;
import uk.ac.gre.wm50.m_expense.model.Trip;

public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    MutableLiveData<List<Trip>> tripList
            = new MutableLiveData<List<Trip>>();

    { //static code run once
        getTrips();
    }

    public void getTrips() {
        List<Trip> tList = new ArrayList<>();


    }
}