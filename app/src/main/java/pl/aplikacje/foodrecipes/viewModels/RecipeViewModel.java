package pl.aplikacje.foodrecipes.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import pl.aplikacje.foodrecipes.models.Recipe;
import pl.aplikacje.foodrecipes.repositories.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private String mRecipeId;
    private Boolean mDidRetriveRecipe;


    public RecipeViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mDidRetriveRecipe = false;
    }

    public LiveData<Recipe> getRecipe(){ return mRecipeRepository.getRecipe(); }

    public LiveData<Boolean> isRequestRecipeTimedOut() { return mRecipeRepository.isRequestRecipeTimedOut(); }

    public void searchRecipeById(String recipeId){
        mRecipeId = recipeId;
        mRecipeRepository.searchRecipeById(recipeId);
    }

    public String getmRecipeId() {
        return mRecipeId;
    }

    public void setRetrivedRecipe(boolean retrivedRecipe){
        mDidRetriveRecipe = retrivedRecipe;
    }

    public boolean didRetrivedRecipe(){
        return mDidRetriveRecipe;
    }
}
