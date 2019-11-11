package pl.aplikacje.foodrecipes.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.List;

import pl.aplikacje.foodrecipes.models.Recipe;
import pl.aplikacje.foodrecipes.requests.RecipeApiClient;

public class RecipeRepository {

    private static RecipeRepository instance;
    private static RecipeApiClient mRecipeApiClient;
    private String mQuerry;
    private int mPageNumber;
    private MutableLiveData <Boolean> mIsQuerryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();


    public static RecipeRepository getInstance() {

        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }


    private RecipeRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();
        initMediators();
    }

    private void initMediators(){
        LiveData<List<Recipe>> recipeListApiSource = mRecipeApiClient.getRecipes();
        mRecipes.addSource(recipeListApiSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes != null){
                    mRecipes.setValue(recipes);
                    doneQuerry(recipes);
                }
                else{
                    //search database casshe
                    doneQuerry(null);
                }
            }
        });
    }

    public void doneQuerry(List<Recipe> list){
        if(list !=null){
            if(list.size()<30){
                mIsQuerryExhausted.setValue(true);
            }
        }
        else {
            mIsQuerryExhausted.setValue(true);
        }

    }

    public LiveData<Boolean> isQuerryExhausted(){
        return  mIsQuerryExhausted;
    }


    public LiveData<List<Recipe>> getRecipes() { return mRecipes; }
    public LiveData<Recipe> getRecipe() { return mRecipeApiClient.getRecipe(); }
    public LiveData<Boolean> isRequestRecipeTimedOut() { return mRecipeApiClient.isRequestRecipeTimedOut(); }



    public void searchRecipeById(String recipeId){ mRecipeApiClient.searchRecipeById(recipeId);
    }



    public void searchRecipesApi(String querry, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mQuerry = querry;
        mPageNumber = pageNumber;
        mIsQuerryExhausted.setValue(false);
        mRecipeApiClient.searchRecipesApi(querry, pageNumber);

    }

    public void searchNextPage(){
        searchRecipesApi(mQuerry, mPageNumber+1);
    }

    public void  cancelRequest(){
        mRecipeApiClient.cancelRequest();

    }
}
