package pl.aplikacje.foodrecipes.requests;

import pl.aplikacje.foodrecipes.util.Consstants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder().baseUrl(Consstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static RecipeApi recipeApi = retrofit.create(RecipeApi.class);

    public RecipeApi getRecipeApi (){
        return recipeApi;
    }


}
