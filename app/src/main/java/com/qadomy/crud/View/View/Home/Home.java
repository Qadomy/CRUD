package com.qadomy.crud.View.View.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
    private FloatingActionButton floatingActionButton;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

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
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "onClickFloatingActionButton");

                Intent intent = new Intent(Home.this, AddTodo_Activity.class);
                startActivity(intent);
            }
        });

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        refreshLayout.setOnRefreshListener(
                () -> presenter.getData()
        );

        itemClickListener = ((view, position) -> {
            String title = note.get(position).getTitle();
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
        });
    }


    // methods when implement the Home View interface
    @Override
    public void showLoading() {
        Log.d(tag, "showLoading");

        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        Log.d(tag, "hideLoading");

        refreshLayout.setRefreshing(false);
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

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
