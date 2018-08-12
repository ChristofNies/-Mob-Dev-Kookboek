package be.student.pxl.kookboek;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import be.student.pxl.kookboek.Data.KookboekDBHelper;
import be.student.pxl.kookboek.Entities.Ingredient;
import be.student.pxl.kookboek.Entities.Recipe;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    KookboekDBHelper kookboekDBHelper;

    ImageView imageUpload;
    EditText title, time, quantity, description, comment, ingredient1, step;
    TextView addTag;
    Button save;

    Recipe recipe;

    ImageButton addIngredient, addStep;
    LinearLayout ingredientLayout, stepLayout, tagLayout;

    List<String> tagList = new ArrayList<>();
    List<Integer> recipeTags = new ArrayList<>();
    CharSequence[] tagSeq;
    boolean[] checkedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        kookboekDBHelper = new KookboekDBHelper(this);
        Cursor getTags = kookboekDBHelper.getAllTags();

        while(getTags.moveToNext()) {
            tagList.add(getTags.getString(1));
        }
        checkedItems = new boolean[tagList.size()];
        tagSeq = tagList.toArray(new CharSequence[tagList.size()]);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(R.string.add_recipes);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        title = (EditText) findViewById(R.id.title);
        time = (EditText) findViewById(R.id.time);
        quantity = (EditText) findViewById(R.id.quantity);
        description = (EditText) findViewById(R.id.description);
        comment = (EditText) findViewById(R.id.comment);
        ingredient1 = (EditText) findViewById(R.id.ingredient1);
        step = (EditText) findViewById(R.id.step);
        addTag = (TextView) findViewById(R.id.addTag);
        addTag.setOnClickListener(this);
        imageUpload = (ImageView) findViewById(R.id.imageView);
        imageUpload.setOnClickListener(this);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);

        ingredientLayout = (LinearLayout) findViewById(R.id.ingredientLayout);
        stepLayout = (LinearLayout) findViewById(R.id.stepLayout);
        addIngredient = (ImageButton) findViewById(R.id.addIngredient);
        addIngredient.setOnClickListener(this);
        addStep = (ImageButton) findViewById(R.id.addStep);
        addStep.setOnClickListener(this);

        tagLayout = (LinearLayout) findViewById(R.id.tagLayout);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        final LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newView = layoutInflater.inflate(R.layout.edittext_to_add, null);

        switch (view.getId()) {
            case R.id.imageView:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;

            case R.id.addIngredient:
                EditText ingredientText = newView.findViewById(R.id.newTextBox);
                ingredientText.setHint("ingredient");
                ingredientLayout.addView(newView);
                break;

            case R.id.addStep:
                EditText stepText = newView.findViewById(R.id.newTextBox);
                stepText.setHint("stappenplan");
                stepLayout.addView(newView);
                break;

            case R.id.save:
                // make a byte array of the image
                // TODO: if image upload = vectordrawable than use standard picture
                Bitmap image = ((BitmapDrawable) imageUpload.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArrayImage = byteArrayOutputStream.toByteArray();

                recipe = new Recipe();
                // make the recipe
                recipe.setTitle(title.getText().toString());
                recipe.setNumberOfPersons(Integer.parseInt(quantity.getText().toString()));
                recipe.setCookingTime(time.getText().toString());
                recipe.setDescription(description.getText().toString());
                recipe.setCommentary(comment.getText().toString());
                recipe.setPicture(byteArrayImage);
                for (int i = 0; i < recipeTags.size(); i++) {
                    if (recipeTags.get(i) != null)
                        recipe.addTagId(recipeTags.get(i));
                }
                Ingredient ingredient = new Ingredient(ingredient1.getText().toString().substring(0,
                        ingredient1.getText().toString().indexOf(" ")),
                        ingredient1.getText().toString().substring(ingredient1.getText().toString().indexOf(" ") + 1,
                                ingredient1.getText().toString().length()));
                recipe.addIngredient(ingredient);
                for (int i = 0; i < ingredientLayout.getChildCount(); i++) {
                        View viewI = (View) ingredientLayout.getChildAt(i);
                        EditText editTextI = (EditText) viewI.findViewById(R.id.newTextBox);
                        ingredient = new Ingredient(editTextI.getText().toString().substring(0,
                            editTextI.getText().toString().indexOf(" ")),
                            editTextI.getText().toString().substring(editTextI.getText().toString().indexOf(" ") + 1,
                                    editTextI.getText().toString().length()));
                        recipe.addIngredient(ingredient);
                }
                recipe.addStep(step.getText().toString());
                for (int i = 0; i < stepLayout.getChildCount(); i++) {
                    View viewS = (View) stepLayout.getChildAt(i);
                    EditText editTextS = (EditText) viewS.findViewById(R.id.newTextBox);
                    recipe.addStep(editTextS.getText().toString());
                }

                // add to db
                boolean isInserted = kookboekDBHelper.addRecipe(recipe);

                // toast message if it worked or not
                if (isInserted) {
                    Toast.makeText(AddRecipeActivity.this, "Recept toegevoegd", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddRecipeActivity.this, "Er is iets foutgelopen", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.addTag:
                // TODO: Make tags disappear when unchecked, unchecked stil become checked after new click...
                final AlertDialog dialog;
                final List<String> selectedTags = new ArrayList<String>();;
                final List<Integer> selectedTagsIndex = new ArrayList<Integer>();;

                for (int i = 0; i < tagList.size(); i++) {
                    if (recipeTags.contains(tagList.get(i))) {
                        checkedItems[i] = true;
                    } else {
                        checkedItems[i] = false;
                    }
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Kies je tags voor dit recept");
                builder.setMultiChoiceItems(tagSeq, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedIndex, boolean isChecked) {
                        if (isChecked) {
                            selectedTags.add(tagList.get(selectedIndex).toString());
                            selectedTagsIndex.add(selectedIndex);
                        } else if (selectedTags.contains(selectedIndex)) {
                            selectedTags.remove(Integer.valueOf(selectedIndex));
                            selectedTagsIndex.remove(Integer.valueOf(selectedIndex));
                        }
                    }
                })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                for (int i = 0; i < selectedTags.size(); i++) {
                                    recipeTags = selectedTagsIndex;
                                    View textView = layoutInflater.inflate(R.layout.tag, null);
                                    TextView tagTextView = textView.findViewById(R.id.newTag);
                                    tagTextView.setText(selectedTags.get(i).toString());
                                    tagLayout.addView(textView);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                dialog = builder.create();
                dialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageUpload.setImageURI(selectedImage);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_addRecipe) {
            // Handle the camera action
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
}


