package be.student.pxl.kookboek;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import be.student.pxl.kookboek.Data.KookboekDBHelper;
import be.student.pxl.kookboek.Entities.Recipe;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    KookboekDBHelper kookboekDBHelper;

    ImageView imageUpload;
    EditText title, time, quantity, description, comment;
    Recipe recipe;

    ImageButton addIngredient, addStep;
    LinearLayout ingredientLayout, stepLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        kookboekDBHelper = new KookboekDBHelper(this);

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
        imageUpload = (ImageView) findViewById(R.id.imageView);
        imageUpload.setOnClickListener(this);

        recipe = new Recipe();

        ingredientLayout = (LinearLayout) findViewById(R.id.ingredientLayout);
        stepLayout = (LinearLayout) findViewById(R.id.stepLayout);
        addIngredient = (ImageButton) findViewById(R.id.addIngredient);
        addIngredient.setOnClickListener(this);
        addStep = (ImageButton) findViewById(R.id.addStep);
        addStep.setOnClickListener(this);
    }

    public void addIngredient() {

        //newText.setId("@+id/ingredient" + numberOfIngredients);

    }

    public void Save() {
        // make a byte array of the image
        Bitmap image = ((BitmapDrawable) imageUpload.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArrayImage = byteArrayOutputStream.toByteArray();

        recipe.setTitle(title.getText().toString());
        recipe.setNumberOfPersons(Integer.parseInt(quantity.getText().toString()));
        recipe.setCookingTime(time.getText().toString());
        recipe.setDescription(description.getText().toString());
        recipe.setCommentary(description.getText().toString());
        recipe.setPicture(byteArrayImage);

        boolean isInserted = kookboekDBHelper.addRecipe(recipe);
        if (isInserted) {
            Toast.makeText(AddRecipeActivity.this, "Recept toegevoegd", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddRecipeActivity.this, "Er is iets foutgelopen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
}
