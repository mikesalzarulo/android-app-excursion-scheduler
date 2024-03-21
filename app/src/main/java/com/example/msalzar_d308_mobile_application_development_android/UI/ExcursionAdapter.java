package com.example.msalzar_d308_mobile_application_development_android.UI;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msalzar_d308_mobile_application_development_android.R;
import com.example.msalzar_d308_mobile_application_development_android.entities.Excursion;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    class ExcursionViewHolder extends RecyclerView.ViewHolder{
        private final TextView excursionItemView;
        private final TextView excursionItemView2;
        private ExcursionViewHolder(View itemView){
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView2);
            excursionItemView2 = itemView.findViewById(R.id.textView3);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Excursion current = mExcursions.get(position);
                Intent intent=new Intent(context,ExcursionDetails.class);
                intent.putExtra("id", current.getExcursionID());
                intent.putExtra("name", current.getExcursionName());
                intent.putExtra("date", current.getExcursionDate());
                intent.putExtra("vacationID", current.getVacationID());
                intent.putExtra("start", startDate);
                intent.putExtra("end", endDate);
                context.startActivity(intent);
            });
        }
    }

    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;
    private final String startDate;
    private final String endDate;
    public ExcursionAdapter(Context context, String startDate, String endDate){
        mInflater = LayoutInflater.from(context);
        this.context=context;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list_item,parent,false);
        return new ExcursionViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if(mExcursions!=null){
            Excursion current = mExcursions.get(position);
            String name = current.getExcursionName();
            int vacationID = current.getVacationID();
            holder.excursionItemView.setText(name);
            holder.excursionItemView2.setText(Integer.toString(vacationID));
        }
        else{
            holder.excursionItemView.setText("No excursion name.");
            holder.excursionItemView2.setText("No vacation id.");
        }
    }
    public void setExcursions(List<Excursion> excursions){
        mExcursions = excursions;
        notifyDataSetChanged();
        int size = mExcursions.size();
        Log.d("ExcursionAdapter", "Size of mExcursions: " + size);
    }

    @Override
    public int getItemCount() {
        return mExcursions.size();
    }
}
