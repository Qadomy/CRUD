package com.qadomy.crud.View.View.Home;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qadomy.crud.View.API.ApiClient;
import com.qadomy.crud.View.API.ApiInterface;
import com.qadomy.crud.View.Model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter {

    private static final String tag = HomePresenter.class.getSimpleName();
    private HomeView view;

    // interface constructor
    public HomePresenter(HomeView view) {
        this.view = view;
    }

    void getData() {
        Log.d(tag, "getData");
        view.showLoading();

        //request to server
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Note>> call = apiInterface.getNotes();
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(@NonNull Call<List<Note>> call, @NonNull Response<List<Note>> response) {
                Log.d(tag, "onResponse");
                view.hideLoading();

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(tag, "onResponse successfully");

                    view.onGetResult(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Note>> call, @NonNull Throwable t) {
                Log.d(tag, "onFailure");
                Log.d(tag, t.getLocalizedMessage());

                view.hideLoading();
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }
}
