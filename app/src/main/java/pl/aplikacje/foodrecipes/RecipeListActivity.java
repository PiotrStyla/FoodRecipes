package pl.aplikacje.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.aplikacje.foodrecipes.adapters.OnRecipeListener;
import pl.aplikacje.foodrecipes.adapters.RecipeRecyclerAdapter;
import pl.aplikacje.foodrecipes.models.Recipe;
import pl.aplikacje.foodrecipes.requests.RecipeApi;
import pl.aplikacje.foodrecipes.requests.ServiceGenerator;
import pl.aplikacje.foodrecipes.requests.responses.RecipeSearchResponse;
import pl.aplikacje.foodrecipes.util.Constants;
import pl.aplikacje.foodrecipes.util.VerticalSpcingItemDecorator;
import pl.aplikacje.foodrecipes.viewModels.RecipeListViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        mRecyclerView = findViewById(R.id.recipe_list);


        initRecyclerView();
        subscribeObservers();
        initSearchView();
        if(!mRecipeListViewModel.ismIsViewingRecipes()){
            //display search category
            displaySearchCategories();
        }


    }


    private void subscribeObservers() {
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {

                    mAdapter.setmRecipes(recipes);

                }




            }
        });
    }

    private void initRecyclerView(){

        mAdapter = new RecipeRecyclerAdapter(this);
        VerticalSpcingItemDecorator itemDecorator = new VerticalSpcingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


    private void initSearchView(){
        final SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                mAdapter.displayLoading();
                mRecipeListViewModel.searchRecipesApi(s, 1);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {

        mAdapter.displayLoading();
        mRecipeListViewModel.searchRecipesApi(category, 1);

    }

    private void displaySearchCategories(){
        mRecipeListViewModel.setIsViewingRecipes(false);
        mAdapter.displaySearchCategories();
    }
}
