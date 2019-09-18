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

    }


    private void subscribeObservers(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {

            }
        });
    }

    private void testRetrofitRequest() {
        final RecipeApi recipeApi = ServiceGenerator.getRecipeApi();

        Call<RecipeSearchResponse> responseCall = recipeApi.searchRecipe(Constants.API_KEY,
                "chicken breast",
                "1");


        responseCall.enqueue(new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
                Log.d(TAG, "onResponse: server response" + response.toString());
                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: " + response.body().toString());
                    List<Recipe> recipes = new ArrayList<>(response.body().getRecipes());
                    for (Recipe recipe : recipes) {
                        Log.d(TAG, "onResponse: " + recipe.getTitle());
                    }
                } else {
                    try {
                        Log.d(TAG, "onResponse: " + response.errorBody().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {

            }
        });


    }
}
