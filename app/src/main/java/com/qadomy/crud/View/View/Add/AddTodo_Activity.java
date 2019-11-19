package com.qadomy.crud.View.View.Add;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
    private int color;

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
        // set the default color in the setup
        palette.setSelectedColor(getResources().getColor(R.color.white));
        color = getResources().getColor(R.color.white);

        // init AddPresenter
        presenter = new AddPresenter(this);

    }// end of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                Log.d(tag, "save menu");

                String tTitel = title.getText().toString().trim();
                String tNote = note.getText().toString().trim();
                int tColor = this.color;

                // check if empty
                if (tTitel.isEmpty()) {
                    title.setHint("Please type a title !!");

                } else if (tNote.isEmpty()) {
                    note.setHint("Please type a note !!");

                } else {
                    // save
                    presenter.saveNote(tTitel, tNote, tColor);
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
    public void onAddSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onAddError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
