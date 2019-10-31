package pl.aplikacje.foodrecipes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import pl.aplikacje.foodrecipes.models.Recipe;

public class RecipeActivity extends BaseActivity {

    //UI Components
    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeRank;
    private LinearLayout mRecipeIngredientsContainer;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeRank = findViewById(R.id.recipe_social_score);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);


        getIncomingIntent();
    }

    private void  getIncomingIntent(){

        if(getIntent().hasExtra("recipe")){
            Recipe recipe = getIntent().getParcelableExtra("recipe");
        }
    }
}
