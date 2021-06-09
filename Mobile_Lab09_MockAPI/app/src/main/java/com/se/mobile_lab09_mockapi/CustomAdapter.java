package com.se.mobile_lab09_mockapi;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<Employee> employeeList;
    private Activity context;

    public CustomAdapter(Activity context) {
        this.context = context;
        getEmployees();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Employee emp = employeeList.get(holder.getAdapterPosition());

                int ID = Integer.parseInt(emp.getId());

                String fullName = emp.getFullName();
                String gender = emp.getGender();
                double salary = emp.getSalary();
                String position = emp.getPosition();

                Dialog dialog = new Dialog(context);

                dialog.setContentView(R.layout.dialog_update);

                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow().setLayout(width, height);

                dialog.show();

                EditText editTextFullName = dialog.findViewById(R.id.etdFullName);
                EditText editTextGender = dialog.findViewById(R.id.etdGender);
                EditText editTextSalary = dialog.findViewById(R.id.etdSalary);
                EditText editTextPosition = dialog.findViewById(R.id.etdPosition);

                editTextFullName.setText(fullName);
                editTextGender.setText(gender);
                editTextSalary.setText(String.valueOf(salary));
                editTextPosition.setText(String.valueOf(position));

                Button btnUpdate = dialog.findViewById(R.id.btn_update);

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        String uFullName = editTextFullName.getText().toString().trim();
                        String uGender = editTextGender.getText().toString().trim();
                        double uSalary = Double.parseDouble(editTextSalary.getText().toString().trim());
                        String uPosition = editTextPosition.getText().toString().trim();

                        RequestQueue queue = Volley.newRequestQueue(context);
                        String url = "https://60ad9c2b80a61f0017331458.mockapi.io/api/employees/" + String.valueOf(ID);

                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, "Can't updated", Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("fullName", uFullName);
                                params.put("gender", uGender);
                                params.put("salary", String.valueOf(uSalary));
                                params.put("position", String.valueOf(uPosition));

                                return params;
                            }
                        };

                        queue.add(stringRequest);

                        notifyDataSetChanged();
                    }
                });
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

    public void getEmployees() {
        employeeList = new ArrayList<>();
        Gson gson = new Gson();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://60ad9c2b80a61f0017331458.mockapi.io/api/employees";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject object = (JSONObject) response.get(i);
                        employeeList.add(gson.fromJson(String.valueOf(object), Employee.class));
                    }
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> Toast.makeText(context, "Error with JSON Array Object", Toast.LENGTH_SHORT).show());

        queue.add(jsonArrayRequest);
    }
}
