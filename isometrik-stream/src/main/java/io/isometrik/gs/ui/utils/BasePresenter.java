package io.isometrik.gs.ui.utils;

public interface BasePresenter<T> {
  void attachView(T view);

  void detachView();
}
