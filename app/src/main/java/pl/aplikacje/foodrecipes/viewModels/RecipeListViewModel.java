package pl.aplikacje.foodrecipes.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import pl.aplikacje.foodrecipes.models.Recipe;
import pl.aplikacje.foodrecipes.repositories.RecipeRepository;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipes;
    private boolean mIsPerformingQuerry;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mIsPerformingQuerry = false;

    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String querry, int pageNumber) {
        mIsViewingRecipes = true;
        mIsPerformingQuerry = true;
        mRecipeRepository.searchRecipesApi(querry, pageNumber);
    }

    public boolean ismIsViewingRecipes(){
        return  mIsViewingRecipes;
    }

    public void setIsViewingRecipes (boolean isViewingRecipes){
        mIsViewingRecipes = isViewingRecipes;
    }

    public void setIsPerformingQuerry( Boolean isPerformingQuerry){

        mIsPerformingQuerry = isPerformingQuerry;

    }

    public boolean isPerformingQuerry(){
        return mIsPerformingQuerry;
    }

    public boolean onBackPressed(){

        if(mIsPerformingQuerry){
            //cancell querry
            mRecipeRepository.cancelRequest();
            mIsPerformingQuerry = false;

        }
        if(ismIsViewingRecipes()){
            mIsViewingRecipes = false;
            return false;
        }
        return true;
    }
}
