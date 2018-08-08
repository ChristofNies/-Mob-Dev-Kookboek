package be.student.pxl.kookboek;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import be.student.pxl.kookboek.Data.KookboekDBHelper;

public class MainActivity extends AppCompatActivity {
    KookboekDBHelper kookboekDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

       kookboekDBHelper = new KookboekDBHelper(this);
    }

    public void addRecipe(View view) {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        startActivity(intent);
    }

    public void goToMyRecipes(View view) {
        Intent intent = new Intent(this, MyRecipesActivity.class);
        startActivity(intent);
    }
}
