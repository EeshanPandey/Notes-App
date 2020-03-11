package com.example.notes

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SimpleCursorAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var db: SQLiteDatabase? = null
    var cursor:Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)

        //open the noteDetailsActivity
        fabAddNote.setOnClickListener{
            openNoteDetails(0)
        }

        ListViewNotes.setOnItemClickListener { parent, view, position, id ->
            openNoteDetails(id)

        }



    }
    fun openNoteDetails(noteId:Long)  {
        val intent = Intent(this, NoteDetails::class.java)
        intent.putExtra("NOTE_ID", noteId)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        var objToCreateDB = MyNotesSQLLightOpenHelper(this)//create database and notes table
        db = objToCreateDB.readableDatabase  //access to the database

        //read data
        cursor = db!!.query("NOTES", arrayOf("_id","title"),
            null, null, null, null, null)


        //adapter
        val listAdapter = SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
            cursor, arrayOf("title"), intArrayOf(android.R.id.text1),0)

        //set adapter view
        ListViewNotes.adapter = listAdapter
    }
    override fun onDestroy() {
        super.onDestroy()
        
        cursor!!.close()
        db!!.close()
    }
}
