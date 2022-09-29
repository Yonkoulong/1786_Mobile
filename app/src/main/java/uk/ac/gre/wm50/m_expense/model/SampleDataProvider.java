package uk.ac.gre.wm50.m_expense.model;

import java.util.ArrayList;
import java.util.List;

public class SampleDataProvider {
    public static List<Trip> getTrips() {
        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip("1", "Conference", "Tokyo", "18/09/2022 11:00:00", "Yes", "The trip come to Tokyo in Japan"));
        trips.add(new Trip("2", "Client meeting", "London", "19/09/2022 10:00:00", "No", "The trip come to Tokyo in London"));
        trips.add(new Trip("3", "Contract Sign", "Turkey", "20/09/2022 09:00:00", "Yes", "The trip come to Tokyo in Turkey"));
        return trips;
    }
}
