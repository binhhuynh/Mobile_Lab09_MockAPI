package com.se.mobile_lab09_mockapi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddEmployeeActivity extends AppCompatActivity {
    private EditText editTextName, editTextGender, editTextSalary, editTextPosition;
    private Button btnAdd, btnCancel, btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        editTextName = findViewById(R.id.etFullName);
        editTextGender = findViewById(R.id.etGender);
        editTextSalary = findViewById(R.id.etSalary);
        editTextPosition = findViewById(R.id.etPosition);

        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        btnReturn = findViewById(R.id.btnReturn);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Employee employee = new Employee();
                employee.setFullName(editTextName.getText().toString());
                employee.setGender(editTextGender.getText().toString());
                employee.setSalary(Double.parseDouble(editTextSalary.getText().toString()));
                employee.setPosition(editTextPosition.getText().toString());
                addEmployee(employee);
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddEmployeeActivity.this, MainActivity.class));
            }
        });
    }

    public void addEmployee(Employee employee) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://60ad9c2b80a61f0017331458.mockapi.io/api/employees";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddEmployeeActivity.this, "Created", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddEmployeeActivity.this, "Cannot created", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("fullName", employee.getFullName());
                params.put("gender", employee.getGender());
                params.put("salary", String.valueOf(employee.getSalary()));
                params.put("position", employee.getPosition());

                return params;
            }
        };

        queue.add(stringRequest);
    }
}