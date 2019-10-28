package pl.aplikacje.foodrecipes.repositories;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import pl.aplikacje.foodrecipes.models.Recipe;
import pl.aplikacje.foodrecipes.requests.RecipeApiClient;

public class RecipeRepository {

    private static RecipeRepository instance;
    private static RecipeApiClient mRecipeApiClient;
    private String mQuerry;
    private int mPageNumber;


    public static RecipeRepository getInstance() {

        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }


    private RecipeRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return mRecipeApiClient.getRecipes();
    }

    public void searchRecipesApi(String querry, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mQuerry = querry;
        mPageNumber = pageNumber;
        mRecipeApiClient.searchRecipesApi(querry, pageNumber);

    }

    public void searchNextPage(){
        searchRecipesApi(mQuerry, mPageNumber+1);
    }

    public void  cancelRequest(){
        mRecipeApiClient.cancelRequest();

    }
}
