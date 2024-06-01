package com.example.calculatorns;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNumber;
    private Spinner spinnerFrom, spinnerTo;
    private TextView textViewResult;
    private Button buttonConvert, buttonHistory;
    private DatabaseHelper dbHelper;
    private List<String> bases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.bases = new ArrayList<>();
        for (int i = 2; i <= 16; i++)
            this.bases.add(String.valueOf(i));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupSpinners();
        setListeners();
    }

    /**
     * Инициализация представлений.
     */
    private void initViews() {
        editTextNumber = findViewById(R.id.editTextNumber);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        textViewResult = findViewById(R.id.textViewResult);
        buttonConvert = findViewById(R.id.buttonConvert);
        buttonHistory = findViewById(R.id.buttonHistory);
        dbHelper = new DatabaseHelper(this);
    }

    /**
     * Настройка спиннеров.
     */
    private void setupSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bases);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Установить значение по умолчанию для spinnerFrom на 10
        spinnerFrom.setSelection(adapter.getPosition("10"));
    }

    /**
     * Установка слушателей для кнопок.
     */
    private void setListeners() {
        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertNumber();
            }
        });

        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Конвертация числа.
     */
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
            BigInteger numberInDecimal = new BigInteger(numberInput, fromBase);
            String result = numberInDecimal.toString(toBase).toUpperCase();
            textViewResult.setText(result);
            dbHelper.insertHistory(numberInput, fromBase, result, toBase);
        } catch (NumberFormatException e) {
            textViewResult.setText("Error");
        }
    }
}
