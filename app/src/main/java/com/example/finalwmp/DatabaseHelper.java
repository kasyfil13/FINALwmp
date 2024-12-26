package com.example.finalwmp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {
    private static final String DATABASE_NAME = "student_db";
    private static final int DATABASE_VERSION = 2; // Incremented version to force onUpgrade

    public static final String TABLE_STUDENTS = "students";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public static final String TABLE_SUBJECTS = "subjects";
    public static final String COLUMN_SUBJECT_ID = "subject_id";
    public static final String COLUMN_SUBJECT_NAME = "name";
    public static final String COLUMN_SUBJECT_CREDITS = "credits";

    public DatabaseHelper(android.content.Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create students table
        String createStudentsTable = "CREATE TABLE " + TABLE_STUDENTS + "("
                + COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createStudentsTable);

        // Create subjects table
        String createSubjectsTable = "CREATE TABLE " + TABLE_SUBJECTS + "("
                + COLUMN_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SUBJECT_NAME + " TEXT, "
                + COLUMN_SUBJECT_CREDITS + " INTEGER)";
        db.execSQL(createSubjectsTable);

        // Insert some sample subjects
        populateSubjectsTable(db);
    }

    private void populateSubjectsTable(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " (" + COLUMN_SUBJECT_NAME + ", " + COLUMN_SUBJECT_CREDITS + ") VALUES " +
                "('Mathematics', 3), " +
                "('Physics', 4), " +
                "('Chemistry', 4), " +
                "('Biology', 3), " +
                "('History', 2), " +
                "('Geography', 3), " +
                "('English Literature', 3), " +
                "('Computer Science', 5), " +
                "('Art', 2), " +
                "('Philosophy', 2);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        onCreate(db);
    }

    public boolean registerStudent(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_STUDENTS, null, values);
        return result != -1;
    }

    public boolean loginStudent(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_STUDENTS,
                new String[]{COLUMN_STUDENT_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null
        );
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    public Cursor getSubjects() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_SUBJECTS, null, null, null, null, null, null);
    }

    public Cursor getSubjectById(int subjectId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_SUBJECTS, null, COLUMN_SUBJECT_ID + "=?", new String[]{String.valueOf(subjectId)}, null, null, null);
    }

    public void enrollSubject(int studentId, int subjectId) {
        // Implement enrollment logic if needed
    }
}
