package com.se.mobile_lab09_mockapi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<Employee> employeeList;
    private Activity context;

    public CustomAdapter(List<Employee> employeeList, Activity context) {
        this.employeeList = employeeList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        Employee employee = employeeList.get(position);

        holder.textViewFullName.setText(employee.getFullName());
        holder.textViewGender.setText(employee.getGender());
        holder.textViewSalary.setText(String.valueOf(employee.getSalary()));
        holder.textViewPosition.setText(employee.getPosition());

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Employee e = employeeList.get(holder.getAdapterPosition());

                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "https://60ad9c2b80a61f0017331458.mockapi.io/api/employees/" + e.getId();

                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(context, "Cannot deleted", Toast.LENGTH_SHORT).show());

                queue.add(stringRequest);

                employeeList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, employeeList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewFullName, textViewGender, textViewSalary, textViewPosition;
        private Button btnEdit, btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewFullName = itemView.findViewById(R.id.tvFullName);
            textViewGender = itemView.findViewById(R.id.tvGender);
            textViewSalary = itemView.findViewById(R.id.tvSalary);
            textViewPosition = itemView.findViewById(R.id.tvPosition);

            btnEdit = itemView.findViewById(R.id.ibEdit);
            btnRemove = itemView.findViewById(R.id.ibRemove);
        }
    }
}
