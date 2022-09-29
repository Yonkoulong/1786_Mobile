package uk.ac.gre.wm50.m_expense;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

import uk.ac.gre.wm50.m_expense.model.Trip;

public class EditorTripViewModel extends ViewModel {

    MutableLiveData<Trip> trip = new MutableLiveData<>();

    public void getTripById(String id) {
        if (id == null || id.equals( Constants.NEW_TRIP_ID)) {
            trip.setValue(new Trip());
            return;
        }
    }

    public void updateTrip(Trip updateTrip) {
        Map<String, Object> tMap = updateTrip.getMapWithoutId();

        //choc den db
        //update cac keu
    }
}