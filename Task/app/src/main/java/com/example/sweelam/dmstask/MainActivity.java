package com.example.sweelam.dmstask;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.sweelam.dmstask.Adapters.PaganationAdapter;
import com.example.sweelam.dmstask.Data.API.APIClient;
import com.example.sweelam.dmstask.Data.API.APIInterface;
import com.example.sweelam.dmstask.Models.Module;
import com.example.sweelam.dmstask.Room.WordViewModel;
import com.example.sweelam.dmstask.Utils.Constant;
import com.example.sweelam.dmstask.Utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    APIInterface api;
    List<Module> list;

    PaganationAdapter adapter;


    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;


    Call<List<Module>> call;
    private static final String TAG = "MainActivity";


    private WordViewModel mWordViewModel;

    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        api = APIClient.getClient().create(APIInterface.class);
        callApi(currentPage);
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        if (isInternetAvailable()) {
            loadFirstPage();

        } else
            Livedata();

        recyclerAddScrollListener();


    }


    private void Livedata() {

        mWordViewModel.getAllWords().observe(this, new Observer<List<Module>>() {
            @Override
            public void onChanged(@Nullable final List<Module> words) {
                // Update the cached copy of the words in the adapter.
                //adapter.setWords(words);
                if (words.size() != 0) {
                    progressBar.setVisibility(View.GONE);


                    adapter.addAll(words);

                }


            }
        });
    }

    private void callApi(int curPage) {
        Log.e(TAG, "CallAPi" + curPage);
        call = api.getTopRatedMovies(curPage, Constant.per_page, Constant.ACESSTOKEN);

    }

    private void recyclerAddScrollListener() {
        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }


    private void init() {
        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        list = new ArrayList<>();

        adapter = new PaganationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);

        swipeRefresh();
    }

    private void swipeRefresh() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isInternetAvailable()) {
                    loadFirstPage();

                }
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private List<Module> fetchResults(Response<List<Module>> response) {
        list = response.body();
        return list;
    }


    private void loadFirstPage() {
        currentPage = 1;
        Log.e(TAG, "loadFirstPage: " + currentPage);
        call = api.getTopRatedMovies(currentPage, Constant.per_page, Constant.ACESSTOKEN);

        call.enqueue(new Callback<List<Module>>() {
            @Override
            public void onResponse(Call<List<Module>> call, Response<List<Module>> response) {

                Log.e(TAG, "Respinse Code: " + response.code());
                Log.e(TAG, "Respinse Message: " + response.message());

                adapter.removeALl();
                mWordViewModel.deleteAll();
                swipeContainer.setRefreshing(false);
                try {


                    List<Module> results = fetchResults(response);

                    for (int x = 0; x < results.size(); x++) {

                        mWordViewModel.insert(results.get(x));
                    }

                    Log.e("RESPONSER ", "responseMessage= " + response.message());
                    Log.e("RESPONSER ", "responseCode= " + response.code());

                    progressBar.setVisibility(View.GONE);
                    if (results != null)
                        adapter.addAll(results);

                    if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                } catch (Exception e) {
                }
            }


            @Override
            public void onFailure(Call<List<Module>> call, Throwable t) {
                String message = t.getMessage();
                Log.e("failure", message);
            }

        });
    }

    private void loadNextPage() {
        Log.e(TAG, "loadNextPage: " + currentPage);

        callApi(currentPage);
        call.enqueue(new Callback<List<Module>>() {
            @Override
            public void onResponse(Call<List<Module>> call, Response<List<Module>> response) {
                adapter.removeLoadingFooter();
                isLoading = false;
                try {
                    List<Module> results = fetchResults(response);

                    for (int x = 0; x < results.size(); x++) {

                        mWordViewModel.insert(results.get(x));
                    }
              /*  if(results!=null)
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;*/
                    if (results.size() != 0) {
                        adapter.addAll(results);
                        adapter.addLoadingFooter();
                    } else isLastPage = true;
                } catch (Exception e){}
            }

            @Override
            public void onFailure(Call<List<Module>> call, Throwable t) {
                // handle failure
            }
        });
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
