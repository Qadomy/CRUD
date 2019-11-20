package com.qadomy.crud.View.View.Add;

public interface AddView {

    void showProgress();

    void hideProgress();

    void onRequestSuccess(String message);

    void onRequestError(String message);
}
