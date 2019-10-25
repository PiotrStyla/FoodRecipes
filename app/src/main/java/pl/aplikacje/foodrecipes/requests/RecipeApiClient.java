package pl.aplikacje.foodrecipes.requests;

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

import pl.aplikacje.foodrecipes.AppExecutors;
import pl.aplikacje.foodrecipes.models.Recipe;
import pl.aplikacje.foodrecipes.requests.responses.RecipeSearchResponse;
import pl.aplikacje.foodrecipes.util.Constants;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static pl.aplikacje.foodrecipes.util.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetriveRecipesRunable mRetriveRecipesRunable;

    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();

    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

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
    public void cancelRequest(){
        if(mRetriveRecipesRunable != null){
            mRetriveRecipesRunable.cancelRequest();
        }
    }
}
