package pl.aplikacje.foodrecipes.requests;

import pl.aplikacje.foodrecipes.requests.responses.RecipeReponse;
import pl.aplikacje.foodrecipes.requests.responses.RecipeSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    //Search

    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe(
            @Query("key") String key,  //?
            @Query("q") String query,  //&
            @Query("page") String page
    );

    //Get recipe request
    @GET("api/get")
    Call<RecipeReponse> getRecipe(
            @Query("key") String key,
            @Query("rId") String recipe_id
    );


}
