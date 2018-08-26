package be.student.pxl.kookboek;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import be.student.pxl.kookboek.Data.KookboekContract;
import be.student.pxl.kookboek.Data.KookboekDBHelper;
import be.student.pxl.kookboek.Entities.Ingredient;
import be.student.pxl.kookboek.Entities.Recipe;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final int RESULT_LOAD_IMAGE = 1;

    private ImageView imageUpload;
    private TextView addTag;
    private Button save;
    private ImageButton addIngredient, addStep;
    private LinearLayout ingredientLayout, stepLayout, tagLayout;

    private String path;
    private List<String> tagList = new ArrayList<>();
    private List<Integer> recipeTags = new ArrayList<>();
    private CharSequence[] tagSeq;
    private boolean[] checkedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        setTags();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(R.string.add_recipes);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

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
        // 2 manieren van wit maken werken niet... NAAM STAAT BLACK MAAR IS WIT
        // Had ook in styles iets toegevoegd wat neit werkt
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
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
                // TODO: if image upload = vectordrawable than use standard picture
                Bitmap bitmap = ((BitmapDrawable) imageUpload.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArrayImage = byteArrayOutputStream.toByteArray();

                String path = "gerechten/" + UUID.randomUUID() + ".png";
                final StorageReference gerechtenRef = storage.getReference(path);

                gerechtenRef.putBytes(byteArrayImage).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return gerechtenRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            saveRecipe(downloadUri.toString());

                            long recipeId = findRecipeId();
                            saveIngredients(recipeId);
                            saveSteps(recipeId);
                            saveTags(recipeId);

                            Toast.makeText(AddRecipeActivity.this, "Recept toegevoegd", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddRecipeActivity.this, "Recept kon niet opgeslagen worden", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
            Intent intent = new Intent(this, RecipeListActivity.class);
            startActivity(intent);
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

    private void setTags() {
        ContentResolver resolver = getContentResolver();
        String[] projection = new String[]{KookboekContract.TagEntry._ID, KookboekContract.TagEntry.COLUMN_NAME};
        Cursor cursor = resolver.query(KookboekContract.TagEntry.CONTENT_URI, projection, null,null, null);

        while (cursor.moveToNext()) {
            tagList.add(cursor.getString(1));
        }

        checkedItems = new boolean[tagList.size()];
        tagSeq = tagList.toArray(new CharSequence[tagList.size()]);
    }

    private long findRecipeId() {
        long recipeId = -1;
        ContentResolver resolver = getContentResolver();
        String[] projection = new String[]{KookboekContract.RecipeEntry._ID};
        EditText title = (EditText) findViewById(R.id.title);
        String[] selectionArgs = new String[]{title.getText().toString()};
        String selection = KookboekContract.RecipeEntry.TABLE_NAME + "." +
                KookboekContract.RecipeEntry.COLUMN_TITLE + " = ?";
        Cursor cursor = resolver.query(KookboekContract.RecipeEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        while (cursor.moveToNext()) {
            recipeId = cursor.getLong(0);
        }
        return recipeId;
    }

    private void saveRecipe(String downloadUrl) {
        ContentResolver resolver = getContentResolver();
        ContentValues recipeValues = new ContentValues();

        EditText title = (EditText) findViewById(R.id.title);
        EditText quantity = (EditText) findViewById(R.id.quantity);
        EditText time = (EditText) findViewById(R.id.time);
        EditText description = (EditText) findViewById(R.id.description);
        EditText comment = (EditText) findViewById(R.id.comment);

        recipeValues.put(KookboekContract.RecipeEntry.COLUMN_TITLE,
                title.getText().toString());
        recipeValues.put(KookboekContract.RecipeEntry.COLUMN_PICTURE,
                downloadUrl);
        recipeValues.put(KookboekContract.RecipeEntry.COLUMN_NUMBER_OF_PERSONS,
                quantity.getText().toString());
        recipeValues.put(KookboekContract.RecipeEntry.COLUMN_COOKING_TIME,
                time.getText().toString());
        recipeValues.put(KookboekContract.RecipeEntry.COLUMN_DESCRIPTION,
                description.getText().toString());
        recipeValues.put(KookboekContract.RecipeEntry.COLUMN_COMMENTARY,
                comment.getText().toString());

        resolver.insert(KookboekContract.RecipeEntry.CONTENT_URI, recipeValues);
    }

    private void saveIngredients(long recipeId) {
        ContentResolver resolver = getContentResolver();
        ContentValues ingredientValues = new ContentValues();

        EditText ingredient1 = (EditText) findViewById(R.id.ingredient1);
        Ingredient ingredient = new Ingredient(ingredient1.getText().toString().substring(0,
                ingredient1.getText().toString().indexOf(" ")),
                ingredient1.getText().toString().substring(ingredient1.getText().toString().indexOf(" ") + 1,
                        ingredient1.getText().toString().length()));

        ingredientValues.put(KookboekContract.IngredientEntry.COLUMN_RECIPE_ID,
                recipeId);
        ingredientValues.put(KookboekContract.IngredientEntry.COLUMN_AMOUNT,
                ingredient.getAmount());
        ingredientValues.put(KookboekContract.IngredientEntry.COLUMN_DESCRIPTION,
                ingredient.getDescription());

        resolver.insert(KookboekContract.IngredientEntry.CONTENT_URI, ingredientValues);

        for (int i = 0; i < ingredientLayout.getChildCount(); i++) {
            View newIngredientLayout = (View) ingredientLayout.getChildAt(i);
            EditText newIngredient = newIngredientLayout.findViewById(R.id.newTextBox);
            ingredient = new Ingredient(newIngredient.getText().toString().substring(0,
                    newIngredient.getText().toString().indexOf(" ")),
                    newIngredient.getText().toString().substring(newIngredient.getText().toString().indexOf(" ") + 1,
                            newIngredient.getText().toString().length()));

            ingredientValues.put(KookboekContract.IngredientEntry.COLUMN_RECIPE_ID,
                    recipeId);
            ingredientValues.put(KookboekContract.IngredientEntry.COLUMN_AMOUNT,
                    ingredient.getAmount());
            ingredientValues.put(KookboekContract.IngredientEntry.COLUMN_DESCRIPTION,
                    ingredient.getDescription());

            resolver.insert(KookboekContract.IngredientEntry.CONTENT_URI, ingredientValues);
        }
    }

    private void saveSteps(long recipeId) {
        ContentResolver resolver = getContentResolver();
        ContentValues stepValues = new ContentValues();

        EditText newStep = (EditText) findViewById(R.id.step);

        stepValues.put(KookboekContract.StepEntry.COLUMN_RECIPE_ID,
                recipeId);
        stepValues.put(KookboekContract.StepEntry.COLUMN_DESCRIPTION,
                newStep.getText().toString());

        resolver.insert(KookboekContract.StepEntry.CONTENT_URI, stepValues);

        for (int i = 0; i < stepLayout.getChildCount(); i++) {
            View newStepLayout = (View) stepLayout.getChildAt(i);
            newStep = newStepLayout.findViewById(R.id.newTextBox);

            stepValues.put(KookboekContract.StepEntry.COLUMN_RECIPE_ID,
                    recipeId);
            stepValues.put(KookboekContract.StepEntry.COLUMN_DESCRIPTION,
                    newStep.getText().toString());

            resolver.insert(KookboekContract.StepEntry.CONTENT_URI, stepValues);
        }
    }

    private void saveTags(long recipeId) {
        ContentResolver resolver = getContentResolver();
        ContentValues tagValues = new ContentValues();

        for (int i = 0; i < recipeTags.size(); i++) {
            if (recipeTags.get(i) != null) {
                tagValues.put(KookboekContract.TagsOfRecipeEntry.COLUMN_RECIPE_ID,
                        recipeId);
                tagValues.put(KookboekContract.TagsOfRecipeEntry.COLUMN_TAG_ID,
                        recipeTags.get(i));
            }
            resolver.insert(KookboekContract.TagsOfRecipeEntry.CONTENT_URI, tagValues);
        }
    }
}


