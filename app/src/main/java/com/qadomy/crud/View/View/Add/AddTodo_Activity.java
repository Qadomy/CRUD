package com.qadomy.crud.View.View.Add;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.qadomy.crud.R;
import com.thebluealliance.spectrum.SpectrumPalette;

public class AddTodo_Activity extends AppCompatActivity implements AddView {

    private static final String tag = AddTodo_Activity.class.getSimpleName();
    private EditText title, note;
    private ProgressDialog progressDialog;
    private SpectrumPalette palette;

    private AddPresenter presenter;
    private int color, iColor, iId;
    private String iTitle, iNote;

    private Menu actionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        Log.d(tag, "onCreate");

        title = (EditText) findViewById(R.id.tTitle);
        note = (EditText) findViewById(R.id.tNote);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait .....");

        palette = (SpectrumPalette) findViewById(R.id.palette);
        // when selected a new color
        palette.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int clr) {
                color = clr;
            }
        });


        // init AddPresenter
        presenter = new AddPresenter(this);

        // get intent from Home activity
        getIntentFromHomeActivity();
        setDataFromIntentExtra();

    }// end of onCreate


    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);

        actionMenu = menu;

        if (iId != 0) {
            actionMenu.findItem(R.id.save).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(true);
            actionMenu.findItem(R.id.edit).setVisible(true);

        } else {
            actionMenu.findItem(R.id.save).setVisible(true);
            actionMenu.findItem(R.id.update).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(false);
            actionMenu.findItem(R.id.edit).setVisible(false);
        }
        return true;
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String tTitel = title.getText().toString().trim();
        String tNote = note.getText().toString().trim();
        int tColor = this.color;

        switch (item.getItemId()) {
            case R.id.save:
                Log.d(tag, "save menu");

                // check if empty
                if (tTitel.isEmpty()) {
                    title.setError("Please type a title !!");

                } else if (tNote.isEmpty()) {
                    note.setError("Please type a note !!");

                } else {
                    // save
                    presenter.saveNote(tTitel, tNote, tColor);
                }

                return true;

            case R.id.edit:

                editMode();
                actionMenu.findItem(R.id.save).setVisible(false);
                actionMenu.findItem(R.id.update).setVisible(true);
                actionMenu.findItem(R.id.delete).setVisible(false);
                actionMenu.findItem(R.id.edit).setVisible(false);

                return true;

            case R.id.delete:

                // display an Alert Dialog to confirm the delete of note
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
                alertdialog.setTitle("Confirm!");
                alertdialog.setMessage("Are you sure want to delete?");
                alertdialog.setNegativeButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    presenter.deleteNote(iId);
                });
                alertdialog.setPositiveButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });
                alertdialog.show();

                return true;

            case R.id.update:

                // check if empty
                if (tTitel.isEmpty()) {
                    title.setError("Please type a title !!");

                } else if (tNote.isEmpty()) {
                    note.setError("Please type a note !!");

                } else {
                    // update
                    presenter.updateNote(iId, tTitel, tNote, tColor);
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // methods implements from Add View interface
    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onRequestSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRequestError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // method for get the data from home activity
    private void getIntentFromHomeActivity() {
        Log.d(tag, "getIntentFromHomeActivity");

        Intent intent = getIntent();
        iId = intent.getIntExtra("id", 0);
        iColor = intent.getIntExtra("color", 0);
        iTitle = intent.getStringExtra("title");
        iNote = intent.getStringExtra("note");
    }

    // method for set the data comes from another activity in Add activity to edit or delete
    private void setDataFromIntentExtra() {
        if (iId != 0) {
            title.setText(iTitle);
            note.setText(iNote);
            palette.setSelectedColor(iColor);

            getSupportActionBar().setTitle("Update Notes");
            readMode();
        } else {
            // set the default color in the setup
            palette.setSelectedColor(getResources().getColor(R.color.white));
            color = getResources().getColor(R.color.white);
            editMode();
        }
    }

    // method for read note mode
    private void readMode() {
        title.setFocusableInTouchMode(false);
        note.setFocusableInTouchMode(false);
        title.setFocusable(false);
        note.setFocusable(false);
        palette.setEnabled(false);
    }

    //method for edit note mode
    private void editMode() {
        title.setFocusableInTouchMode(true);
        note.setFocusableInTouchMode(true);
        palette.setEnabled(true);
    }
}
