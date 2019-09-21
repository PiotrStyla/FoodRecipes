package pl.aplikacje.foodrecipes.requests;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

//All that is constructing a kind of delivery chain to transfer the Recipes from outside by Api gate
//to repository - to viewModel - to Activity
//The gradient provoking that move is the change rejestrated by observers

import pl.aplikacje.foodrecipes.models.Recipe;

public class RecipeApiClient {

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;

    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient(){
        mRecipes = new MutableLiveData<>();

    }

    public MutableLiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }
}
