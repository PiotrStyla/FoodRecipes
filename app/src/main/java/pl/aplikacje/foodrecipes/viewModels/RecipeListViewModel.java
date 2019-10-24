package pl.aplikacje.foodrecipes.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import pl.aplikacje.foodrecipes.models.Recipe;
import pl.aplikacje.foodrecipes.repositories.RecipeRepository;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipes;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();

    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String querry, int pageNumber) {
        mIsViewingRecipes = true;
        mRecipeRepository.searchRecipesApi(querry, pageNumber);
    }

    public boolean ismIsViewingRecipes(){
        return  mIsViewingRecipes;
    }

    public void setIsViewingRecipes (boolean isViewingRecipes){
        mIsViewingRecipes = isViewingRecipes;
    }

    public boolean onBackPressed(){
        if(ismIsViewingRecipes()){
            mIsViewingRecipes = false;
            return false;
        }
        return true;
    }
}
