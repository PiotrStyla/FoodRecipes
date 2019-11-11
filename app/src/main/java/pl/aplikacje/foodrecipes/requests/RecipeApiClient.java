package pl.aplikacje.foodrecipes.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//All that is constructing a kind of delivery chain to transfer the Recipes from outside by Api gate
//to repository - to viewModel - to Activity
//The gradient provoking that move is the change rejestrated by observers

import okio.Timeout;
import pl.aplikacje.foodrecipes.AppExecutors;
import pl.aplikacje.foodrecipes.models.Recipe;
import pl.aplikacje.foodrecipes.requests.responses.RecipeReponse;
import pl.aplikacje.foodrecipes.requests.responses.RecipeSearchResponse;
import pl.aplikacje.foodrecipes.util.Constants;
import retrofit2.Call;
import retrofit2.Response;

import static pl.aplikacje.foodrecipes.util.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetriveRecipesRunable mRetriveRecipesRunable;
    private MutableLiveData<Recipe> mRecipe;
    private RetriveRecipeRunable mRetriveRecipeRunable;
    private MutableLiveData<Boolean> mRecipeRequestTimeout = new MutableLiveData<>();

    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
        mRecipe = new MutableLiveData<Recipe>();

    }

    public LiveData<List<Recipe>> getRecipes() { return mRecipes; }

    public LiveData<Recipe> getRecipe() { return mRecipe; }

    public LiveData<Boolean> isRequestRecipeTimedOut() { return mRecipeRequestTimeout; }



    public void searchRecipesApi(String querry, int pageNumber) {

        if (mRetriveRecipesRunable != null) {
            mRetriveRecipesRunable = null;
        }
        mRetriveRecipesRunable = new RetriveRecipesRunable(querry, pageNumber);

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetriveRecipesRunable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let the user know it is timeout
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);

    }

    public void searchRecipeById(String recipeId){
        if(mRetriveRecipeRunable != null){
            mRetriveRecipeRunable = null;
        }
        mRetriveRecipeRunable = new RetriveRecipeRunable(recipeId);

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetriveRecipeRunable);

        mRecipeRequestTimeout.setValue(false);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let the user know it is timeout
                mRecipeRequestTimeout.postValue(true);
                handler.cancel(true);

            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetriveRecipesRunable implements Runnable {

        private String querry;
        private int pageNumber;
        boolean cancelRequest;

        private RetriveRecipesRunable(String querry, int pageNumber) {
            this.querry = querry;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try {
                Response response = getRecipes(querry, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }

                if (response.code() == 200) {
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse) response.body()).getRecipes());
                    if (pageNumber == 1) {
                        mRecipes.postValue(list);
                    } else {
                        List<Recipe> currentRecipies = mRecipes.getValue();
                        currentRecipies.addAll(list);
                        mRecipes.postValue(currentRecipies);
                    }
                } else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mRecipes.postValue(null);
                }

            } catch (
                    IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }

        }

        private Call<RecipeSearchResponse> getRecipes(String querry, int pageNumber) {

            return ServiceGenerator.getRecipeApi().searchRecipe(
                    Constants.API_KEY,
                    querry,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest() {
            Log.d(TAG, "setCancelRequest: cancelingRequest");
            cancelRequest = true;
        }


    }

    private class RetriveRecipeRunable implements Runnable {

        private String recipeId;
        boolean cancelRequest;

        private RetriveRecipeRunable(String recipeId) {
            this.recipeId = recipeId;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try {
                Response response = getRecipe(recipeId).execute();
                if (cancelRequest) {
                    return;
                }

                if (response.code() == 200) {
                    Recipe recipe = ((RecipeReponse)response.body()).getRecipe();
                    mRecipe.postValue(recipe);
                } else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mRecipes.postValue(null);
                }

            } catch (
                    IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }

        }

        private Call<RecipeReponse> getRecipe(String recipeId) {

            return ServiceGenerator.getRecipeApi().getRecipe(
                    Constants.API_KEY,
                    recipeId
            );
        }

        private void cancelRequest() {
            Log.d(TAG, "setCancelRequest: cancelingRequest");
            cancelRequest = true;
        }


    }
    public void cancelRequest(){
        if(mRetriveRecipesRunable != null){
            mRetriveRecipesRunable.cancelRequest();
        }
        if(mRetriveRecipeRunable != null){
            mRetriveRecipeRunable.cancelRequest();
        }
    }
}
