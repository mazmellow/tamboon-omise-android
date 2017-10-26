package com.mazmellow.testomise.presenter;

public interface Presenter<V> {
    void attachView(V view);
    void detachView();
}
