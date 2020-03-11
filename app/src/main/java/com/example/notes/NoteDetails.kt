package com.example.notes

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_note_details.*

class NoteDetails : AppCompatActivity() {


    var db:SQLiteDatabase? = null
    var cursor:Cursor? = null
    var noteId:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        val myNotesDatabaseHelper = MyNotesSQLLightOpenHelper(this)
        db = myNotesDatabaseHelper.writableDatabase

        noteId = intent.extras?.get("NOTE_ID").toString().toInt()

        if(noteId!=0){
            cursor = db!!.query("NOTES", arrayOf("TITLE","DESCRIPTION"),
                "_id=?", arrayOf(noteId.toString()), null,null,null)

            if(cursor!!.moveToFirst()){
                editTextTitle.setText(cursor!!.getString(0))
                editTextDescription.setText(cursor!!.getString(1))

            }

        }
    }




    override fun onOptionsItemSelected(item: MenuItem?): Boolean {


        if(item!!.itemId == R.id.save_note) {
            //inserting new note

            val newNoteValues = ContentValues()

            if (editTextTitle.text.isEmpty() == true) {
                newNoteValues.put("TITLE", "Untitled")
            } else {
                newNoteValues.put("TITLE", editTextTitle.text.toString())
            }

            newNoteValues.put("DESCRIPTION", editTextDescription.text.toString())

            if (noteId == 0) {
                insertNote(newNoteValues)
            } else {
                updateNote(newNoteValues)
            }
        }
        else if(item!!.itemId == R.id.delete_note){

            deleteNote()
        }
            return super.onOptionsItemSelected(item)
        }

    fun deleteNote(){

        var dialog:AlertDialog
        val builder = AlertDialog.Builder(this)

        //title for alert dialog
        builder.setTitle("Deleting note")
        //message for alert dialog
        builder.setMessage("Are you sure you want to delete '${editTextTitle.text}'?")
        //yes button
        val dialogClickListener = DialogInterface.OnClickListener{_, which ->
            if (which == DialogInterface.BUTTON_POSITIVE)
            {
                db!!.delete("NOTES", "_id=?", arrayOf(noteId.toString()))
                Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show()

                //destroy current activity
                finish()
            }
        }
        builder.setPositiveButton("YES", dialogClickListener)

        //cancel button
        builder.setNegativeButton("CANCEL", dialogClickListener)

        //initialize alert dialog
        dialog = builder.create()

        //display alert dialog
        dialog.show()


    }
    fun updateNote(noteValues: ContentValues){
        db!!.update("NOTES",noteValues, "_id=?", arrayOf(noteId.toString()))
        Toast.makeText(this,"Note Updated!", Toast.LENGTH_LONG).show()
    }

    private fun insertNote(newNoteValues: ContentValues) {


        db!!.insert("NOTES", null, newNoteValues)

        Toast.makeText(this, "NOTE SAVED", Toast.LENGTH_SHORT).show()

        //clearing text fields
        editTextTitle.setText("")
        editTextDescription.setText("")
        //set focus back to title
        editTextTitle.requestFocus()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_details_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onDestroy() {
        super.onDestroy()


        db!!.close()
    }

}
