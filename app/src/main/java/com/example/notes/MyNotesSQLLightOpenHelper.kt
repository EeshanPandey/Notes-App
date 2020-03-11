package com.example.notes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.AccessControlContext

class MyNotesSQLLightOpenHelper(context:Context): SQLiteOpenHelper(context,"MyNotesDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

            db?.execSQL(
                "CREATE TABLE notes("
                        + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "title TEXT,"
                        + "description TEXT)"
            )


    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }

}