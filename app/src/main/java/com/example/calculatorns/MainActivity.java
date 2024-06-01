package com.example.calculatorns;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/*public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}*/
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNumber;
    private Spinner spinnerFrom, spinnerTo;
    private TextView textViewResult;
    private Button buttonConvert;

    private final String[] bases = {"2", "8", "10", "16"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNumber = findViewById(R.id.editTextNumber);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        textViewResult = findViewById(R.id.textViewResult);
        buttonConvert = findViewById(R.id.buttonConvert);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bases);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertNumber();
            }
        });
    }

    private void convertNumber() {
        String numberInput = editTextNumber.getText().toString().toUpperCase();
        String fromBaseStr = (String) spinnerFrom.getSelectedItem();
        String toBaseStr = (String) spinnerTo.getSelectedItem();

        if (TextUtils.isEmpty(numberInput)) {
            textViewResult.setText("Error");
            return;
        }

        int fromBase = Integer.parseInt(fromBaseStr);
        int toBase = Integer.parseInt(toBaseStr);

        try {
            int numberInDecimal = Integer.parseInt(numberInput, fromBase);
            String result = Integer.toString(numberInDecimal, toBase).toUpperCase();
            textViewResult.setText(result);
        } catch (NumberFormatException e) {
            textViewResult.setText("Error");
        }
    }
}