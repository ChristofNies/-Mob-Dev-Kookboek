package be.student.pxl.kookboek;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import be.student.pxl.kookboek.Adapter.SimpleItemRecyclerViewAdapter;
import be.student.pxl.kookboek.Data.KookboekContract;
import be.student.pxl.kookboek.Data.KookboekDBHelper;
import be.student.pxl.kookboek.Entities.Recipe;
import be.student.pxl.kookboek.dummy.DummyContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);
        toolbar.setTitle("Mijn recepten");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // 2 manieren van wit maken werken niet... NAAM STAAT BLACK MAAR IS WIT
        // Had ook in styles iets toegevoegd wat neit werkt
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, getAllRecipes(), mTwoPane));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_addRecipe) {
            Intent intent = new Intent(this, AddRecipeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_myRecipes) {

        } else if (id == R.id.nav_Friends) {

        } else if (id == R.id.nav_Mealplanner) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private List<Recipe> getAllRecipes() {
        ContentResolver resolver = getContentResolver();
        // loopt vast als cursor te groot is, oplossing door in de while loop de foto's per recept apart op te halen #goedkope oplossing
        // lost niks op zorg voor kleine formaat foto's
        String[] projection = new String[]{KookboekContract.RecipeEntry._ID, KookboekContract.RecipeEntry.COLUMN_TITLE,
                KookboekContract.RecipeEntry.COLUMN_PICTURE};
        Cursor cursor = resolver.query(KookboekContract.RecipeEntry.CONTENT_URI, projection, null, null, null);
        List<Recipe> recipeList = new ArrayList<>();

        while (cursor.moveToNext()) {
            Recipe recipe = new Recipe();
            recipe.setId(cursor.getLong(0));
            recipe.setTitle(cursor.getString(1));
            recipe.setPicture(cursor.getString(2));
            recipeList.add(recipe);
        }

        return recipeList;
    }
}
