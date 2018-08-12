package be.student.pxl.kookboek.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import be.student.pxl.kookboek.Entities.Ingredient;
import be.student.pxl.kookboek.Entities.Recipe;

public class KookboekDBHelper extends SQLiteOpenHelper {
    // Database name
    private static final String DATABASE_NAME = "kookboek.db";

    // Tables
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_TAGS = "tags";
    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String TABLE_STEPS = "steps";
    private static final String TABLE_TAGS_OF_RECIPES = "tags_of_recipes";

    public KookboekDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT UNIQUE, picture BLOB, cooking_time," +
                " number_of_persons, photorecipe BLOB, description, commentary)");
        db.execSQL("CREATE TABLE tags (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
        db.execSQL("CREATE TABLE ingredients (id INTEGER PRIMARY KEY AUTOINCREMENT, recipeId, amount, description," +
                "FOREIGN KEY(recipeId) REFERENCES recipes(id))");
        db.execSQL("CREATE TABLE steps (id INTEGER PRIMARY KEY AUTOINCREMENT, recipeId, description," +
                "FOREIGN KEY(recipeId) REFERENCES recipes(id))");
        db.execSQL("CREATE TABLE tags_of_recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, recipeId, tagId," +
                "FOREIGN KEY(recipeId) REFERENCES recipes(id), FOREIGN KEY(tagId) REFERENCES tags(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS_OF_RECIPES);
        onCreate(db);
    }

    public boolean addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", recipe.getTitle());
        contentValues.put("picture", recipe.getPicture());
        contentValues.put("cooking_time", recipe.getCookingTime());
        contentValues.put("number_of_persons", recipe.getNumberOfPersons());
        //contentValues.put("photorecipe", recipe.getPhotoRecipe());
        contentValues.put("description", recipe.getDescription());
        contentValues.put("commentary", recipe.getCommentary());

        long result = db.insert(TABLE_RECIPES, null, contentValues);
        Cursor recipeIdCursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES + " WHERE title = '" + recipe.getTitle() + "'", null);
        int recipeId = 0;

        while (recipeIdCursor.moveToNext()) {
            recipeId = recipeIdCursor.getInt(0);
        }

        boolean ingredientsControl = addIngredients(recipe.getIngredients(), recipeId);
        boolean stepsControl = addSteps(recipe.getSteps(), recipeId);
        boolean tagsControl = addTags(recipe.getTagId(), recipeId);

        if (result == -1 && ingredientsControl == false && stepsControl == false && tagsControl == false)
            return false;

        return true;
    }

    public boolean addIngredients(List<Ingredient> ingredients, int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result;
        for (int i = 0; i < ingredients.size(); i++) {
            contentValues.put("recipeId", recipeId);
            contentValues.put("amount", ingredients.get(i).getAmount());
            contentValues.put("description", ingredients.get(i).getDescription());
            result = db.insert(TABLE_INGREDIENTS, null, contentValues);
            if (result == -1)
                return false;
        }
        return true;
    }

    public boolean addSteps(List<String> steps, int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result;
        for (int i = 0; i < steps.size(); i++) {
            contentValues.put("recipeId", recipeId);
            contentValues.put("description", steps.get(i));
            result = db.insert(TABLE_STEPS, null, contentValues);
            if (result == -1)
                return false;
        }
        return true;
    }

    public boolean addTags(List<Integer> tagIds, int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result;
        for (int i = 0; i < tagIds.size(); i++) {
            contentValues.put("tagId", tagIds.get(i));
            contentValues.put("recipeId", recipeId);
            result = db.insert(TABLE_TAGS_OF_RECIPES, null, contentValues);
            if (result == -1)
                return false;
        }
        return true;
    }

    public Cursor getAllRecipes() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);
        return res;
    }

    public Cursor getRecipeById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_RECIPES + " WHERE id = " + id, null);
        return res;
    }

    public Cursor getRecipeIngredients(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_INGREDIENTS + " WHERE recipeId = " + recipeId, null);
        return res;
    }

    public Cursor getRecipeTags(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT tagId FROM " + TABLE_TAGS_OF_RECIPES + " WHERE recipeId = " + recipeId, null);
        return res;
    }

    public Cursor getTagById(int tagId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_TAGS + " WHERE id = " + tagId, null);
        return res;
    }

    public Cursor getRecipeSteps(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_STEPS + " WHERE recipeId = " + recipeId, null);
        return res;
    }

    public Cursor getAllTags() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_TAGS, null);
        return res;
    }

    public void populateTags() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor allTags = getAllTags();

        if (allTags.getCount() == 0) {
            String[] tags = {"Hoofdgerecht", "Voorgerecht", "Dessert", "Vis", "Vlees", "Veggie", "Soep", "Gezond", "Snack"};
            ContentValues contentValues = new ContentValues();
            for (int i = 0; i <= tags.length; i++) {
                contentValues.put("name", tags[i]);
                db.insert(TABLE_TAGS, null, contentValues);
            }
        }
    }
}

