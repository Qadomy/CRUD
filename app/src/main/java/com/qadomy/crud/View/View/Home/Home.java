package com.qadomy.crud.View.View.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qadomy.crud.R;
import com.qadomy.crud.View.Adapter.HomeAdapter;
import com.qadomy.crud.View.Model.Note;
import com.qadomy.crud.View.View.Add.AddTodo_Activity;

import java.util.List;

public class Home extends AppCompatActivity implements HomeView {

    private static final String tag = Home.class.getSimpleName();
    private static final int INTENT_ADD = 100;
    private static final int INTENT_Edit = 200;

    private FloatingActionButton floatingActionButton;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Toast previousToast;

    private HomePresenter presenter;
    private HomeAdapter adapter;
    private HomeAdapter.ItemClickListener itemClickListener;

    private List<Note> note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(tag, "onCreate");

        // init Home presenter
        presenter = new HomePresenter(this);
        presenter.getData();

        // when click on Add button
        floatingActionButton = findViewById(R.id.add);
        floatingActionButton.setOnClickListener(v -> {
            Log.d(tag, "onClickFloatingActionButton");

            Intent intent = new Intent(Home.this, AddTodo_Activity.class);
            startActivityForResult(intent, INTENT_ADD);
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        swipeRefreshLayout.setOnRefreshListener(
                () -> presenter.getData()
        );

        itemClickListener = ((view, position) -> {

            // send the current data to ADD activity to edit it
            int id = note.get(position).getId();
            int color = note.get(position).getColor();
            String title = note.get(position).getTitle();
            String notes = note.get(position).getNote();

            Intent intent = new Intent(this, AddTodo_Activity.class);
            intent.putExtra("id", id);
            intent.putExtra("color", color);
            intent.putExtra("title", title);
            intent.putExtra("note", notes);

            startActivityForResult(intent, INTENT_Edit);
        });
    }// end of onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_ADD && resultCode == RESULT_OK) {
            presenter.getData();
        } else if (requestCode == INTENT_Edit && resultCode == RESULT_OK) {
            presenter.getData();
        }
    }

    // methods when implement the Home View interface
    @Override
    public void showLoading() {
        Log.d(tag, "showLoading");

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
        Log.d(tag, "hideLoading");

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onGetResult(List<Note> notes) {
        Log.d(tag, "onGetResult");

        adapter = new HomeAdapter(this, notes, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        note = notes;
    }

    @Override
    public void onErrorLoading(String message) {
        Log.d(tag, "onErrorLoading");

        showMessage(message);
    }

    // method for display the Toast message
    private void showMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();

        // hide the previous toast message
        if (previousToast != null) {
            previousToast.cancel();
        }
        previousToast = toast;
    }
}
