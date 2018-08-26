package be.student.pxl.kookboek;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import be.student.pxl.kookboek.Data.KookboekContract;
import be.student.pxl.kookboek.Data.KookboekDBHelper;
import be.student.pxl.kookboek.Entities.Ingredient;
import be.student.pxl.kookboek.Entities.Recipe;
import be.student.pxl.kookboek.dummy.DummyContent;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    //public static final int ARG_ITEM_ID = 0;

    /**
     * The dummy content this fragment is presenting.
     */
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Recipe recipe;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("recipeId")) {
            recipe = getRecipeById(getArguments().getLong("recipeId"));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(recipe.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        if (recipe != null) {
//            ((ImageView) rootView.findViewById(R.id.recipe_photo)).setImageURI(Uri.parse(recipe.getPicture()));
            StorageReference storageRef  = storage.getReferenceFromUrl(recipe.getPicture());
            Glide.with(this.getActivity())
                    .using(new FirebaseImageLoader())
                    .load(storageRef)
                    .into((ImageView) rootView.findViewById(R.id.recipe_photo));

            ((TextView) rootView.findViewById(R.id.description)).setText(recipe.getDescription());

            ((TextView) rootView.findViewById(R.id.numberOfPersons)).setText("INGREDIËNTEN - " + recipe.getNumberOfPersons() + " PERSONEN");

            ((TextView) rootView.findViewById(R.id.time)).setText(recipe.getCookingTime());

            ((TextView) rootView.findViewById(R.id.ingredients)).setText(getIngredientsByRecipeId(recipe.getId()));

            ((TextView) rootView.findViewById(R.id.steps)).setText(getStepsByRecipeId(recipe.getId()));

            ((TextView) rootView.findViewById(R.id.comment)).setText(recipe.getCommentary());
        }

        return rootView;
    }

    private Recipe getRecipeById(long recipeId) {
        ContentResolver resolver = this.getActivity().getContentResolver();
        String[] projection = new String[]{KookboekContract.RecipeEntry._ID, KookboekContract.RecipeEntry.COLUMN_TITLE,
                KookboekContract.RecipeEntry.COLUMN_PICTURE, KookboekContract.RecipeEntry.COLUMN_NUMBER_OF_PERSONS,
                KookboekContract.RecipeEntry.COLUMN_COOKING_TIME, KookboekContract.RecipeEntry.COLUMN_DESCRIPTION,
                KookboekContract.RecipeEntry.COLUMN_COMMENTARY};
        String[] selectionArgs = new String[]{Long.toString(recipeId)};
        String selection = KookboekContract.RecipeEntry.TABLE_NAME + "." +
                KookboekContract.RecipeEntry._ID + " = ?";
        Cursor cursor = resolver.query(KookboekContract.RecipeEntry.buildRecipeUri(recipeId),
                projection,
                selection,
                selectionArgs,
                null);
        Recipe recipe = new Recipe();

        while (cursor.moveToNext()) {
            recipe.setId(cursor.getLong(0));
            recipe.setTitle(cursor.getString(1));
            recipe.setPicture(cursor.getString(2));
            recipe.setNumberOfPersons(cursor.getInt(3));
            recipe.setCookingTime(cursor.getString(4));
            recipe.setDescription(cursor.getString(5));
            recipe.setCommentary(cursor.getString(6));
        }

        return recipe;
    }

    private String getIngredientsByRecipeId(long recipeId) {
        ContentResolver resolver = this.getActivity().getContentResolver();
        String[] projection = new String[]{KookboekContract.IngredientEntry.COLUMN_AMOUNT, KookboekContract.IngredientEntry.COLUMN_DESCRIPTION};
        String[] selectionArgs = new String[]{Long.toString(recipeId)};
        String selection = KookboekContract.IngredientEntry.COLUMN_RECIPE_ID + " = ?";
        Cursor cursor = resolver.query(KookboekContract.IngredientEntry.buildIngredientRecipe(recipeId),
                projection,
                selection,
                selectionArgs,
                null);
        String ingredients = "";

        while (cursor.moveToNext()) {
            ingredients += cursor.getString(0) + " " + cursor.getString(1) + " \n";
        }

        return ingredients;
    }

    private String getStepsByRecipeId(long recipeId) {
        ContentResolver resolver = this.getActivity().getContentResolver();
        String[] projection = new String[]{KookboekContract.StepEntry.TABLE_NAME + "." + KookboekContract.StepEntry.COLUMN_DESCRIPTION};
        String[] selectionArgs = new String[]{Long.toString(recipeId)};
        String selection = KookboekContract.StepEntry.TABLE_NAME + "." +
                KookboekContract.StepEntry.COLUMN_RECIPE_ID + " = ?";
        Cursor cursor = resolver.query(KookboekContract.StepEntry.buildStepRecipe(recipeId),
                projection,
                selection,
                selectionArgs,
                null);

        String steps = "";

        while (cursor.moveToNext()) {
            steps += cursor.getString(0);
        }

        return steps;
    }
}
