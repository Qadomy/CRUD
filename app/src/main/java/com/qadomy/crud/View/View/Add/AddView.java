package com.qadomy.crud.View.View.Add;

public interface AddView {

    void showProgress();

    void hideProgress();

    void onAddSuccess(String message);

    void onAddError(String message);
}
