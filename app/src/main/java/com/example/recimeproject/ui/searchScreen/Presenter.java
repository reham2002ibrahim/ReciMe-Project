package com.example.recimeproject.ui.searchScreen;
import com.example.recimeproject.DataLayer.model.CategoriesResponse;
import com.example.recimeproject.DataLayer.repo.Repository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Presenter implements PresenterInterface{
    private final SearchInterface view;
    private final Repository repository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public Presenter(SearchInterface view, Repository repository) {
        this.view = view;
        this.repository = repository;
    }


    @Override
    public void searchCategories() {
        Disposable disposable = repository.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> view.showSearchResults(categories),
                        throwable -> view.showError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

}
