package be.student.pxl.kookboek;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    private Recipe recipe;
    KookboekDBHelper kookboekDbHelper;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kookboekDbHelper = new KookboekDBHelper(this.getActivity());

        if (getArguments().containsKey("recipeId")) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Cursor res = kookboekDbHelper.getRecipeById(getArguments().getInt("recipeId"));
            while (res.moveToNext()) {
                recipe = new Recipe(res.getInt(0), res.getString(1), res.getBlob(2), res.getString(3), res.getInt(4),
                        res.getString(6), res.getString(7));
            }

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

        // Show the dummy content as text in a TextView.
        if (recipe != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(recipe.getPicture(), 0, recipe.getPicture().length);
            ((ImageView) rootView.findViewById(R.id.recipe_photo)).setImageBitmap(bitmap);

            ((TextView) rootView.findViewById(R.id.description)).setText(recipe.getDescription());

            ((TextView) rootView.findViewById(R.id.numberOfPersons)).setText("INGREDIËNTEN - " + recipe.getNumberOfPersons() + " PERSONEN");
            ((TextView) rootView.findViewById(R.id.time)).setText(recipe.getCookingTime());

            Cursor cursorI = kookboekDbHelper.getRecipeIngredients(recipe.getId());
            while (cursorI.moveToNext()) {
                recipe.addIngredient(new Ingredient(cursorI.getString(2), cursorI.getString(3)));
            }
            String ingredients = "";
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                ingredients += recipe.getIngredients().get(i).getAmount() + " " + recipe.getIngredients().get(i).getDescription() + "\n";
            }
            ((TextView) rootView.findViewById(R.id.ingredients)).setText(ingredients);

            Cursor cursorS = kookboekDbHelper.getRecipeSteps(recipe.getId());
            while (cursorS.moveToNext()) {
                recipe.addStep(cursorS.getString(2));
            }
            String steps = "";
            for (int i = 0; i < recipe.getSteps().size(); i++) {
                steps += (i + 1) + ". " + recipe.getSteps().get(i) + "\n";
            }
            ((TextView) rootView.findViewById(R.id.steps)).setText(steps);

            ((TextView) rootView.findViewById(R.id.comment)).setText(recipe.getCommentary());
        }

        return rootView;
    }
}
