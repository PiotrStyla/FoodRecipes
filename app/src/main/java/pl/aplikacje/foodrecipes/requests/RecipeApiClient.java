package pl.aplikacje.foodrecipes.requests;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//All that is constructing a kind of delivery chain to transfer the Recipes from outside by Api gate
//to repository - to viewModel - to Activity
//The gradient provoking that move is the change rejestrated by observers

import pl.aplikacje.foodrecipes.AppExecutors;
import pl.aplikacje.foodrecipes.models.Recipe;

import static pl.aplikacje.foodrecipes.util.Constants.NETWORK_TIMEOUT;

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

    public void serchRecipesApi(){

        final Future handler = AppExecutors.getInstance().networkIO().submit(new Runnable() {
            @Override
            public void run() {

                //retrive data from rest Api
       //           mRecipes.postValue();
            }
        });

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let the user know it is timeout
                handler.cancel(true)
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);


    }
}
