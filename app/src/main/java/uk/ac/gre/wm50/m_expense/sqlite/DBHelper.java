package uk.ac.gre.wm50.m_expense.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "MExpense";
    public static final String TRIP = "trip";
    private static final String EXPENSE = "expense";
    public  static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String trip = "CREATE TABLE TRIP (" +
                "id integer primary key autoincrement, " +
                "nameOfTheTrip text not null, " +
                "destination text not null, " +
                "dateOfTheTrip text not null, " +
                "requiresRiskAssessment text not null, " +
                "description text not null)";

        String expense = "CREATE TABLE EXPENSE (" +
                "id integer primary key autoincrement, " +
                "idTrip text not null, " +
                "typeOfExpense text not null, " +
                "amountOfTheExpense real not null, " +
                "timeOfTheExpense text not null, " +
                "additionalComments text not null, " +
                "foreign key(idTrip) REFERENCES TRIP (id))";
        db.execSQL(trip);
        db.execSQL(expense);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TRIP);
        db.execSQL("DROP TABLE IF EXISTS "+EXPENSE);
        onCreate(db);
    }

}
