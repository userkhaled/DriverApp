package com.eeducationgo.markoupdrivers.util;

public interface BaseView {
    void showProgress();
    void hideProgress();
    void showErrorMessage(String message);
    void showSuccessMessage(String message);
}
