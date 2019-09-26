package pl.aplikacje.foodrecipes.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import pl.aplikacje.foodrecipes.models.Recipe;
import pl.aplikacje.foodrecipes.repositories.RecipeRepository;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();

    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String querry, int pageNumber) {
        mRecipeRepository.searchRecipesApi(querry, pageNumber);
    }
}
