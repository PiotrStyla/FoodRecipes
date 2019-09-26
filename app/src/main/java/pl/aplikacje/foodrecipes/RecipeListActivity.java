package pl.aplikacje.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.aplikacje.foodrecipes.models.Recipe;
import pl.aplikacje.foodrecipes.requests.RecipeApi;
import pl.aplikacje.foodrecipes.requests.ServiceGenerator;
import pl.aplikacje.foodrecipes.requests.responses.RecipeSearchResponse;
import pl.aplikacje.foodrecipes.util.Constants;
import pl.aplikacje.foodrecipes.viewModels.RecipeListViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity {

    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel mRecipeListViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        subscribeObservers();
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRetrofitRequest();
            }
        });

    }


    private void subscribeObservers() {
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {
                    for (Recipe recipe : recipes) {
                        Log.d(TAG, "onChanged: " + recipe.getTitle());
                    }
                }


            }
        });
    }

    private void searchRecipesApi(String querry, int pageNumber) {
        mRecipeListViewModel.searchRecipesApi(querry, pageNumber);
    }

    private void testRetrofitRequest() {
        searchRecipesApi("Chicken Breast", 1);

    }
}
