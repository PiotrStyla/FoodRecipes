package pl.aplikacje.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import pl.aplikacje.foodrecipes.adapters.OnRecipeListener;
import pl.aplikacje.foodrecipes.adapters.RecipeRecyclerAdapter;
import pl.aplikacje.foodrecipes.models.Recipe;
import pl.aplikacje.foodrecipes.util.VerticalSpacingItemDecorator;
import pl.aplikacje.foodrecipes.viewModels.RecipeListViewModel;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        mRecyclerView = findViewById(R.id.recipe_list);
        mSearchView = findViewById(R.id.searchView);


        initRecyclerView();
        subscribeObservers();
        initSearchView();
        if(!mRecipeListViewModel.ismIsViewingRecipes()){
            //display search category
            displaySearchCategories();
        }
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));


    }


    private void subscribeObservers() {
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {

                    if(mRecipeListViewModel.ismIsViewingRecipes()){
                        mRecipeListViewModel.setIsPerformingQuerry(false);
                        mAdapter.setmRecipes(recipes);
                    }

                }




            }
        });

        mRecipeListViewModel.isQuerryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean) Log.d(TAG,"onChanged: the querry is exhausted");
            }
        });
    }

    private void initRecyclerView(){

        mAdapter = new RecipeRecyclerAdapter(this);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!mRecyclerView.canScrollVertically(1)){
                    //search the next page
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });


    }


    private void initSearchView(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                mAdapter.displayLoading();
                mRecipeListViewModel.searchRecipesApi(s, 1);
                mSearchView.clearFocus();

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

        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", mAdapter.getSelectedRecipe(position));
        startActivity(intent);

    }

    @Override
    public void onCategoryClick(String category) {

        mAdapter.displayLoading();
        mRecipeListViewModel.searchRecipesApi(category, 1);
        mSearchView.clearFocus();

    }

    private void displaySearchCategories(){
        mRecipeListViewModel.setIsViewingRecipes(false);
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onBackPressed() {

        if(mRecipeListViewModel.onBackPressed()){
            super.onBackPressed();
        }
        else {
            displaySearchCategories();
        }
    }
    //inflate menu


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_categories){
            displaySearchCategories();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
