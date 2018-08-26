package be.student.pxl.kookboek;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import be.student.pxl.kookboek.Data.KookboekContract;
import be.student.pxl.kookboek.Data.KookboekDBHelper;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        populateTags();
    }

    public void addRecipe(View view) {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        startActivity(intent);
    }

    public void goToMyRecipes(View view) {
        Intent intent = new Intent(this, RecipeListActivity.class);
        startActivity(intent);
    }

    public void populateTags() {
        ContentResolver resolver = getContentResolver();
        String[] projection = new String[]{KookboekContract.TagEntry.COLUMN_NAME};
        Cursor cursor = resolver.query(KookboekContract.TagEntry.CONTENT_URI, projection, null, null, null);

        if (cursor.getCount() == 0) {
            ContentValues tagValues = new ContentValues();
            String[] tags = {"Hoofdgerecht", "Voorgerecht", "Dessert", "Vis", "Vlees", "Veggie", "Soep", "Gezond", "Snack"};

            for (String t : tags) {
                tagValues.put(KookboekContract.TagEntry.COLUMN_NAME, t);
                resolver.insert(KookboekContract.TagEntry.CONTENT_URI, tagValues);
            }
        }
    }


}
