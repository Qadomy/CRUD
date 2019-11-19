package com.qadomy.crud.View.View.Home;

import com.qadomy.crud.View.Model.Note;

import java.util.List;

public interface HomeView {

    void showLoading();

    void hideLoading();

    void onGetResult(List<Note> notes);

    void onErrorLoading(String message);
}
