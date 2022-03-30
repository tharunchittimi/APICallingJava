package com.example.apicalling.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apicalling.JobsDataItems;
import com.example.apicalling.R;
import com.example.apicalling.ResultJsonData;

import java.util.ArrayList;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {
    private ArrayList<JobsDataItems> jobsDataItems;
   public Context applicationContext;

    public JobsAdapter(ArrayList<JobsDataItems> jobsDataItems,  Context applicationContext) {
        this.jobsDataItems = jobsDataItems;
        this.applicationContext=applicationContext;
    }

    @NonNull
    @Override
    public JobsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jobs_rv, parent, false));
    }

    @Override
    public  void onBindViewHolder(@NonNull final JobsAdapter.ViewHolder holder,final int position) {
        JobsDataItems objJobsDataItems = jobsDataItems.get(position);
        holder.Role.setText(objJobsDataItems.role);
        holder.Company.setText(objJobsDataItems.company);
        holder.City.setText(objJobsDataItems.city);
        holder.Days.setText(objJobsDataItems.days);
        Glide.with(holder.Poster).load(objJobsDataItems.poster).into(holder.Poster);
        holder.Description.setText(objJobsDataItems.description);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                applicationContext=v.getContext();
                Intent intent = new Intent(applicationContext, ResultJsonData.class);
                intent.putExtra(ResultJsonData.EXTRA_ROLE,jobsDataItems.get(holder.getAdapterPosition()).role);
                intent.putExtra(ResultJsonData.EXTRA_COMPANY,jobsDataItems.get(holder.getAdapterPosition()).company);
                intent.putExtra(ResultJsonData.EXTRA_CITY,jobsDataItems.get(holder.getAdapterPosition()).city);
                intent.putExtra(ResultJsonData.EXTRA_DAYS,jobsDataItems.get(holder.getAdapterPosition()).days);
                intent.putExtra(ResultJsonData.EXTRA_POSTER,jobsDataItems.get(holder.getAdapterPosition()).poster);
                intent.putExtra(ResultJsonData.EXTRA_DESCRIPTION,jobsDataItems.get(holder.getAdapterPosition()).description);
                intent.putExtra(ResultJsonData.EXTRA_APPLY,jobsDataItems.get(holder.getAdapterPosition()).apply);
                intent.putExtra(ResultJsonData.EXTRA_COMPANY_URL,jobsDataItems.get(holder.getAdapterPosition()).company_url);
                applicationContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return jobsDataItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Role;
        TextView Company;
        TextView City;
        TextView Days;
        ImageView Poster;
        TextView Description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Role=itemView.findViewById(R.id.textView2);
            Company=itemView.findViewById(R.id.textView3);
            City=itemView.findViewById(R.id.textView6);
            Days=itemView.findViewById(R.id.textView7);
            Poster=itemView.findViewById(R.id.imageLogo);
            Description=itemView.findViewById(R.id.textView9);
        }
    }

    public void updateList(ArrayList<JobsDataItems> jobsDataItems){
        this.jobsDataItems=jobsDataItems;
    }

}
