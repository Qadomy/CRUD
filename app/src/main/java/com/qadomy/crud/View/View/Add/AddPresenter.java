package com.qadomy.crud.View.View.Add;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qadomy.crud.View.API.ApiClient;
import com.qadomy.crud.View.API.ApiInterface;
import com.qadomy.crud.View.Model.Note;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPresenter {

    private static final String tag = AddPresenter.class.getSimpleName();

    private AddView view;
    private ApiInterface apiInterface;

    // constructor for view
    public AddPresenter(AddView view) {
        this.view = view;
    }

    // method for save the note in server
    void saveNote(String title, String note, int color) {
        Log.d(tag, "saveNote");

        view.showProgress();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Note> call = apiInterface.saveNote(title, note, color);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
                view.hideProgress();
                Log.d(tag, "onResponse");

                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();
                    if (success) {
                        Log.d(tag, "success yes");
                        view.onRequestSuccess(response.body().getMessage());

                    } else {
                        Log.d(tag, "success no");
                        view.onRequestError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Note> call, @NonNull Throwable t) {
                Log.d(tag, "onFailure");
                Log.d(tag, t.getLocalizedMessage());

                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());
            }
        });
    }

    // method for update the note from server
    void updateNote(int id, String title, String note, int color) {
        Log.d(tag, "updateNote");

        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Note> call = apiInterface.updateNote(id, title, note, color);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
                Log.d(tag, "updateNote onResponse");

                view.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();
                    if (success) {
                        Log.d(tag, "onResponse success");
                        view.onRequestSuccess(response.body().getMessage());

                    } else {
                        Log.d(tag, "onResponse failed");
                        view.onRequestError(response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Note> call, @NonNull Throwable t) {
                Log.d(tag, "updateNote onFailure");
                Log.d(tag, t.getLocalizedMessage());

                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());
            }
        });
    }

    // method for delete the note from server
    void deleteNote(int id){
        Log.d(tag, "deleteNote");

        view.showProgress();
        ApiInterface  apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Note> call = apiInterface.deleteNote( id);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
                Log.d(tag, "onResponse");

                if (response.isSuccessful()&&response.body()!=null){
                    Boolean success = response.body().getSuccess();
                    if (success){
                        Log.d(tag, "onResponse: success");

                        view.onRequestSuccess(response.body().getMessage());

                    }else{
                        Log.d(tag, "onResponse: failed");

                        view.onRequestError(response.body().getMessage());
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<Note> call, @NonNull Throwable t) {
                Log.d(tag, "onFailure");
                Log.d(tag, t.getLocalizedMessage());

                view.hideProgress();
                view.onRequestSuccess(t.getLocalizedMessage());
            }
        });
    }
}
