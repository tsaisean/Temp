package com.nytimes.android.sample.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nytimes.android.external.store.base.Fetcher;
import com.nytimes.android.external.store.base.impl.BarCode;
import com.nytimes.android.external.store.base.impl.Store;
import com.nytimes.android.external.store.base.impl.StoreBuilder;
import com.nytimes.android.sample.R;
import com.nytimes.android.sample.data.model.RedditData;
import com.nytimes.android.sample.reddit.PostAdapter;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class StoreActivity extends AppCompatActivity {

    private static final String TAG = StoreActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
//    Cache<BarCode, Observable<Integer>> cache = CacheBuilder.newBuilder()
//            .maximumSize(100)
//            .expireAfterAccess(3000, TimeUnit.MILLISECONDS)
//            .build();
    private int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        postAdapter = new PostAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.postRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(postAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");


        loadPosts();
    }

    public void loadPosts() {
        Log.d(TAG, "[loadPosts] start");

        BarCode awwRequest = new BarCode(RedditData.class.getSimpleName(), "aww");

        Log.d(TAG, "[loadPosts] Call provideRedditStore get with key \"aww\"");

        provideRedditStore()
                .get(awwRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "[onCompleted]");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer posts) {
                        Log.d(TAG, "[onNext] get " + posts);
                    }
                });
    }

//    private void showPosts(List<Post> posts) {
//        postAdapter.setPosts(posts);
//        makeText(StoreActivity.this,
//                "Loaded " + posts.size() + " posts",
//                Toast.LENGTH_SHORT)
//                .show();
//    }

//    private Observable<Post> sanitizeData(RedditData redditData) {
//        return rx.Observable.from(redditData.data().children())
//                .map(Children::data);
//    }

    private Store<Integer, BarCode> provideRedditStore() {
        return StoreBuilder.<Integer>barcode()
                .fetcher(new Fetcher<Integer, BarCode>() {
                    @Nonnull
                    @Override
                    public Observable<Integer> fetch(@Nonnull BarCode barCode) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int current_i = StoreActivity.this.i++;
                        Log.d(TAG, "[fetch] return " + current_i);
                        return Observable.just(current_i);
                    }
                })
                .open();
    }

//    private Api provideRetrofit() {
//        return new Retrofit.Builder()
//                .baseUrl("http://reddit.com/")
//                .addConverterFactory(GsonConverterFactory.create(provideGson()))
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .validateEagerly(BuildConfig.DEBUG)  // Fail early: check Retrofit configuration at creation time in Debug build.
//                .build()
//                .create(Api.class);
//    }

//    Gson provideGson() {
//        return new GsonBuilder()
//                .registerTypeAdapterFactory(new GsonAdaptersModel())
//                .create();
//    }
}
