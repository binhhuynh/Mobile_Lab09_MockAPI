package com.se.mobile_lab09_mockapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText etSearch;
    private Button btnSearch, btnAddEmp;
    private RecyclerView recyclerView;
    private List<Employee> employees;
    private CustomAdapter adapter;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch = findViewById(R.id.etSearch);

        btnAddEmp = findViewById(R.id.btnAddEmp);
        btnSearch = findViewById(R.id.btnSearch);

        recyclerView = findViewById(R.id.recyclerView);

        gson = new Gson();

        adapter = new CustomAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAddEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddEmployeeActivity.class));
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chưa xử lí chức năng này
                getEmployee();
                recyclerView.setAdapter(adapter);
            }
        });
    }

    public void getEmployee() {
        employees = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://60ad9c2b80a61f0017331458.mockapi.io/api/employees/" + etSearch.getText().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Employee employee = gson.fromJson(response.toString(), Employee.class);
                employees.add(employee);
                adapter.notifyDataSetChanged();
            }
        }, error -> Toast.makeText(this, "Error by get JsonObject...", Toast.LENGTH_SHORT).show());

        queue.add(jsonObjectRequest);
    }
}