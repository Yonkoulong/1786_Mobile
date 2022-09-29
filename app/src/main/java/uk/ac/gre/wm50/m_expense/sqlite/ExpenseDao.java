package uk.ac.gre.wm50.m_expense.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gre.wm50.m_expense.Constants;
import uk.ac.gre.wm50.m_expense.model.Expense;
import uk.ac.gre.wm50.m_expense.model.Trip;

public class ExpenseDao {
    private SQLiteDatabase db;
    public MutableLiveData<List<Expense>> expenseList;
    public MutableLiveData<Expense> expense;
    String tripId;

    public ExpenseDao(Context context, String tripId) {
        DBHelper helper = new DBHelper(context);

        db = helper.getWritableDatabase();

        this.tripId = tripId;

        expenseList = new MutableLiveData<List<Expense>>();
        {
            expenseList.setValue(getAll());
        }
        expense = new MutableLiveData<Expense>();
    }

    @SuppressLint("Range")
    public List<Expense> get(String sql, String ...selectArgs) {
        List<Expense> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectArgs);

        while(cursor.moveToNext()) {
            Expense e = new Expense();
            e.setId(cursor.getString(cursor.getColumnIndex("id")));
            e.setIdTrip(cursor.getString(cursor.getColumnIndex("idTrip")));
            e.setTypeOfExpense(cursor.getString(cursor.getColumnIndex("typeOfExpense")));
            e.setAmountOfTheExpense(cursor.getDouble(cursor.getColumnIndex("amountOfTheExpense")));
            e.setTimeOfTheExpense(cursor.getString(cursor.getColumnIndex("timeOfTheExpense")));
            e.setAdditionalComments(cursor.getString(cursor.getColumnIndex("additionalComments")));

            list.add(e);
        }
        return list;
    }
    public List<Expense> getAll() {
        String sql = "SELECT * FROM expense WHERE idTrip = ?";

        return get(sql, tripId);
    }

    public Expense getById(String id) {
        String sql = "SELECT * FROM expense WHERE id = ? and idTrip = ?";

        List<Expense> list = get(sql, id, tripId);
        expense.setValue(list.get(0));
        return list.get(0);
    }

    public void getExpenseById(String id){
        if (id == null || id.equals(Constants.NEW_EXPENSE_ID)) {
            expense.setValue(new Expense());
            return;
        }
        getById(id);
    }


    public long insert(Expense e) {
        ContentValues values = new ContentValues();
        values.put("idTrip", e.getIdTrip());
        values.put("typeOfExpense", e.getTypeOfExpense());
        values.put("amountOfTheExpense", e.getAmountOfTheExpense());
        values.put("timeOfTheExpense", e.getTimeOfTheExpense());
        values.put("additionalComments", e.getAdditionalComments());

        return db.insert("expense", null, values);
    }

    public long update(Expense e) {
        ContentValues values = new ContentValues();
        values.put("typeOfExpense", e.getTypeOfExpense());
        values.put("amountOfTheExpense", e.getAmountOfTheExpense());
        values.put("timeOfTheExpense", e.getTimeOfTheExpense());
        values.put("additionalComments", e.getAdditionalComments());

        return db.update("expense", values, "id=?", new String[]{e.getId()});
    }

    public int delete(String id) {
        return db.delete("expense", "id=?", new String[]{id});
    }
}
