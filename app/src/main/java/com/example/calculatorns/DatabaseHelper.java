package com.example.calculatorns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Класс DatabaseHelper для управления базой данных истории конверсий.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "conversion_history.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_HISTORY = "history";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INPUT = "input";
    public static final String COLUMN_FROM_BASE = "from_base";
    public static final String COLUMN_RESULT = "result";
    public static final String COLUMN_TO_BASE = "to_base";

    /**
     * Конструктор для DatabaseHelper.
     *
     * @param context контекст приложения
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    /**
     * Создает таблицу истории.
     *
     * @param db база данных
     */
    private void createTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_HISTORY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_INPUT + " TEXT, " +
                COLUMN_FROM_BASE + " INTEGER, " + // Изменено на INTEGER
                COLUMN_RESULT + " TEXT, " +
                COLUMN_TO_BASE + " INTEGER)"; // Изменено на INTEGER
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // Создание временной таблицы
            String tempTable = TABLE_HISTORY + "_temp";
            db.execSQL("ALTER TABLE " + TABLE_HISTORY + " RENAME TO " + tempTable);

            // Создание новой таблицы
            createTable(db);

            // Перенос данных из временной таблицы в новую таблицу
            String insertData = "INSERT INTO " + TABLE_HISTORY + " (" +
                    COLUMN_ID + ", " +
                    COLUMN_INPUT + ", " +
                    COLUMN_FROM_BASE + ", " +
                    COLUMN_RESULT + ", " +
                    COLUMN_TO_BASE + ") SELECT " +
                    COLUMN_ID + ", " +
                    COLUMN_INPUT + ", " +
                    COLUMN_FROM_BASE + ", " +
                    COLUMN_RESULT + ", " +
                    COLUMN_TO_BASE + " FROM " + tempTable;
            db.execSQL(insertData);

            // Удаление временной таблицы
            db.execSQL("DROP TABLE IF EXISTS " + tempTable);
        }
    }

    /**
     * Вставляет новую запись в таблицу истории.
     *
     * @param input    входное значение
     * @param fromBase основание исходной системы счисления
     * @param result   результат конверсии
     * @param toBase   основание целевой системы счисления
     */
    public void insertHistory(String input, int fromBase, String result, int toBase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INPUT, input);
        values.put(COLUMN_FROM_BASE, fromBase);
        values.put(COLUMN_RESULT, result);
        values.put(COLUMN_TO_BASE, toBase);
        db.insert(TABLE_HISTORY, null, values);
    }

    /**
     * Возвращает все записи из таблицы истории.
     *
     * @return объект Cursor, содержащий все записи
     */
    public Cursor getAllHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_HISTORY, null, null, null, null, null, COLUMN_ID + " DESC");
    }

    /**
     * Очищает историю операций
     */
    public void clearHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, null, null);
    }

}
