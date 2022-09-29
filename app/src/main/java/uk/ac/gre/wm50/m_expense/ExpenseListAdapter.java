package uk.ac.gre.wm50.m_expense;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.gre.wm50.m_expense.databinding.ListItemBinding;
import uk.ac.gre.wm50.m_expense.model.Expense;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder> {

    public interface ListExpenseListener {
        void onItemClick(String id);
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {

        private final ListItemBinding itemViewBinding;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            itemViewBinding = ListItemBinding.bind(itemView);
        }

        public void bindData(Expense tData) {
            itemViewBinding.name.setText(tData.getTypeOfExpense());
            itemViewBinding.getRoot()
                    .setOnClickListener(v -> listener.onItemClick(tData.getId()));
        }
    }

    private List<Expense> expenseList;
    private ExpenseListAdapter.ListExpenseListener listener;

    public ExpenseListAdapter(List<Expense> expenseList, ExpenseListAdapter.ListExpenseListener listener) {
        this.expenseList = expenseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseListAdapter.ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ExpenseListAdapter.ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseListAdapter.ExpenseViewHolder holder, int position) {
        Expense eData = expenseList.get(position);
        holder.bindData(eData);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }
}
