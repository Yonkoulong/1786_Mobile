package uk.ac.gre.wm50.m_expense;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.gre.wm50.m_expense.databinding.ListItemBinding;
import uk.ac.gre.wm50.m_expense.model.Trip;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {

    public interface ListTripListener {
        void onItemClick(String id);
    }

    public class TripViewHolder extends RecyclerView.ViewHolder {

        private final ListItemBinding itemViewBinding;

        public TripViewHolder(View itemView) {
            super(itemView);
            itemViewBinding = ListItemBinding.bind(itemView);
        }

        public void bindData(Trip tData) {
            itemViewBinding.name.setText(tData.getNameOfTheTrip());
            itemViewBinding.getRoot()
                    .setOnClickListener(v -> listener.onItemClick(tData.getId()));
        }
    }

    private List<Trip> tripList;
    private ListTripListener listener;

    public TripListAdapter(List<Trip> tripList, ListTripListener listener) {
        this.tripList = tripList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip rData = tripList.get(position);
        holder.bindData(rData);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}
