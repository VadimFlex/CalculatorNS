package com.example.calculatorns;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HistoryActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listViewHistory;
    private FloatingActionButton fabClearHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewHistory = findViewById(R.id.listViewHistory);
        fabClearHistory = findViewById(R.id.fabClearHistory);
        dbHelper = new DatabaseHelper(this);

        loadHistory();

        fabClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearHistory();
            }
        });
    }

    private void loadHistory() {
        Cursor cursor = dbHelper.getAllHistory();
        String[] from = {DatabaseHelper.COLUMN_INPUT, DatabaseHelper.COLUMN_FROM_BASE, DatabaseHelper.COLUMN_RESULT, DatabaseHelper.COLUMN_TO_BASE};
        int[] to = {R.id.textViewInput, R.id.textViewFromBase, R.id.textViewResult, R.id.textViewToBase};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.history_item, cursor, from, to, 0);
        listViewHistory.setAdapter(adapter);
    }

    private void clearHistory() {
        dbHelper.clearHistory();
        loadHistory();
        Toast.makeText(this, "История очищена", Toast.LENGTH_SHORT).show();
    }
}
