package uk.ac.gre.wm50.m_expense.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import uk.ac.gre.wm50.m_expense.Constants;
import uk.ac.gre.wm50.m_expense.model.Trip;

public class TripDao {
    private SQLiteDatabase db;
    public MutableLiveData<List<Trip>> tripList;
    public MutableLiveData<Trip> trip;
    public DBHelper helper;

    public TripDao(Context context) {
         this.helper = new DBHelper(context);

        db = helper.getWritableDatabase();

        tripList = new MutableLiveData<List<Trip>>();
        {
            tripList.setValue(getAll());
        }
        trip = new MutableLiveData<Trip>();
    }

    @SuppressLint("Range")
    public List<Trip> get(String sql, String ...selectArgs) {
        List<Trip> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectArgs);

        while(cursor.moveToNext()) {
            Trip t = new Trip();
            t.setId(cursor.getString(cursor.getColumnIndex("id")));
            t.setNameOfTheTrip(cursor.getString(cursor.getColumnIndex("nameOfTheTrip")));
            t.setDestination(cursor.getString(cursor.getColumnIndex("destination")));
            t.setDateOfTheTrip(cursor.getString(cursor.getColumnIndex("dateOfTheTrip")));
            t.setRequiresRiskAssessment(cursor.getString(cursor.getColumnIndex("requiresRiskAssessment")));
            t.setDescription(cursor.getString(cursor.getColumnIndex("description")));

            list.add(t);
        }
        return list;
    }
    public List<Trip> getAll() {
        String sql = "SELECT * FROM trip";

        return get(sql);
    }

    public Trip getById(String id) {
        String sql = "SELECT * FROM trip WHERE id = ?";

        List<Trip> list = get(sql, id);
        trip.setValue(list.get(0));
        return list.get(0);
    }

    public void getTripById(String id){
        if (id == null || id.equals(Constants.NEW_TRIP_ID)) {
            trip.setValue(new Trip());
            return;
        }
        getById(id);
    }

    public long insert(Trip t) {
        ContentValues values = new ContentValues();
        values.put("nameOfTheTrip", t.getNameOfTheTrip());
        values.put("destination",t.getDestination());
        values.put("dateOfTheTrip", t.getDateOfTheTrip());
        values.put("requiresRiskAssessment", t.getRequiresRiskAssessment());
        values.put("description", t.getDescription());
        values.put("description", t.getDescription());

    return db.insert("trip", null, values);
    }

    public long update(Trip t) {
        ContentValues values = new ContentValues();
        values.put("nameOfTheTrip", t.getNameOfTheTrip());
        values.put("dateOfTheTrip", t.getDateOfTheTrip());
        values.put("requiresRiskAssessment", t.getRequiresRiskAssessment());
        values.put("description", t.getDescription());

        return db.update("trip", values, "id=?", new String[]{t.getId()});
    }

    public int delete(String id) {
        return db.delete("trip", "id=?", new String[]{id});
    }

    public void search(String text) {
        String query = "Select * from trip WHERE nameOfTheTrip LIKE '%"+text+"%' OR destination LIKE '%"+text+"%' OR dateOfTheTrip LIKE '%"+text+"%'";
        tripList.setValue(get(query));
    }
}
