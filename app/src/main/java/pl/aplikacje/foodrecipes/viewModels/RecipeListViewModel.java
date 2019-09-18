package pl.aplikacje.foodrecipes.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import pl.aplikacje.foodrecipes.models.Recipe;

public class RecipeListViewModel extends ViewModel {


    private MutableLiveData<List<Recipe>> mRecipes = new MutableLiveData<>();

    public RecipeListViewModel() {

    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }
}
