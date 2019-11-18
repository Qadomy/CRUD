package com.qadomy.crud.View.View.MainView;

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
import com.qadomy.crud.View.API.ApiClient;
import com.qadomy.crud.View.API.ApiInterface;
import com.qadomy.crud.View.Model.Note;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String tag = MainActivity.class.getSimpleName();
    private EditText title, note;
    private ProgressDialog progressDialog;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(tag, "onCreate");

        title = findViewById(R.id.title);
        note = findViewById(R.id.note);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait .....");


    }

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
                int tColor = -2184710;

                // check if empty
                if (tTitel.isEmpty()) {
                    title.setText("Please type a title !!");
                    title.setTextColor(R.color.redColor);

                } else if (tNote.isEmpty()) {
                    note.setText("Please type a note !!");
                    note.setTextColor(R.color.redColor);
                }
                // save
                saveNote(tTitel, tNote, tColor);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // method for save the note in server
    private void saveNote(String title, String note, int color) {
        Log.d(tag, "saveNote");

        progressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Note> call = apiInterface.saveNote(title, note, color);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
                progressDialog.dismiss();
                Log.d(tag, "onResponse");

                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();
                    if (success) {
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish(); // back to main activity

                    } else {
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<Note> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.d(tag, "onFailure");

                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
