package pl.aplikacje.foodrecipes.repositories;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import pl.aplikacje.foodrecipes.models.Recipe;
import pl.aplikacje.foodrecipes.requests.RecipeApiClient;

public class RecipeRepository {

    private static RecipeRepository instance;
    private static RecipeApiClient mRecipeApiClient;


    public static RecipeRepository getInstance() {

        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }


    private RecipeRepository(){
        mRecipeApiClient = RecipeApiClient.getInstance();
    }

    public MutableLiveData<List<Recipe>> getRecipes(){
        return mRecipeApiClient.getRecipes();
    }
}
