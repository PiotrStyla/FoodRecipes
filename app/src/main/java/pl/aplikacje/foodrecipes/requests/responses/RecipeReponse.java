package pl.aplikacje.foodrecipes.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import pl.aplikacje.foodrecipes.models.Recipe;

public class RecipeReponse {

    @SerializedName("recipe")
    @Expose
    private Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        return "RecipeReponse{" +
                "recipe=" + recipe +
                '}';
    }
}








