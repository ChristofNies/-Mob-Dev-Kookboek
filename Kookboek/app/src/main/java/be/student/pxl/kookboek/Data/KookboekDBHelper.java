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
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "kookboek.db";

    public KookboekDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + KookboekContract.RecipeEntry.TABLE_NAME + " (" +
                KookboekContract.RecipeEntry._ID + " INTEGER PRIMARY KEY, " +
                KookboekContract.RecipeEntry.COLUMN_TITLE + " TEXT UNIQUE, " +
                KookboekContract.RecipeEntry.COLUMN_PICTURE + ", " +
                KookboekContract.RecipeEntry.COLUMN_NUMBER_OF_PERSONS + ", " +
                KookboekContract.RecipeEntry.COLUMN_COOKING_TIME + ", " +
                KookboekContract.RecipeEntry.COLUMN_DESCRIPTION + ", " +
                KookboekContract.RecipeEntry.COLUMN_COMMENTARY + ")");

        db.execSQL("CREATE TABLE " + KookboekContract.TagEntry.TABLE_NAME + " (" +
                KookboekContract.TagEntry._ID + " INTEGER PRIMARY KEY, " +
                KookboekContract.TagEntry.COLUMN_NAME + " TEXT)");

        db.execSQL("CREATE TABLE " + KookboekContract.IngredientEntry.TABLE_NAME + " (" +
                KookboekContract.IngredientEntry._ID + " INTEGER PRIMARY KEY," +
                KookboekContract.IngredientEntry.COLUMN_RECIPE_ID + ", " +
                KookboekContract.IngredientEntry.COLUMN_AMOUNT + ", " +
                KookboekContract.IngredientEntry.COLUMN_DESCRIPTION + ", " +
                "FOREIGN KEY (" + KookboekContract.IngredientEntry.COLUMN_RECIPE_ID + ") REFERENCES " +
                KookboekContract.RecipeEntry.TABLE_NAME + " (" + KookboekContract.RecipeEntry._ID + "))");

        db.execSQL("CREATE TABLE " + KookboekContract.StepEntry.TABLE_NAME + " (" +
                KookboekContract.StepEntry._ID + " INTEGER PRIMARY KEY, "+
                KookboekContract.StepEntry.COLUMN_RECIPE_ID + ", " +
                KookboekContract.StepEntry.COLUMN_DESCRIPTION + ", " +
                "FOREIGN KEY (" + KookboekContract.StepEntry.COLUMN_RECIPE_ID + ") REFERENCES " +
                KookboekContract.RecipeEntry.TABLE_NAME + " (" + KookboekContract.RecipeEntry._ID + "))");

        db.execSQL("CREATE TABLE " + KookboekContract.TagsOfRecipeEntry.TABLE_NAME + " (" +
                KookboekContract.TagsOfRecipeEntry._ID + " INTEGER PRIMARY KEY, " +
                KookboekContract.TagsOfRecipeEntry.COLUMN_RECIPE_ID + ", " +
                KookboekContract.TagsOfRecipeEntry.COLUMN_TAG_ID + ", " +
                "FOREIGN KEY (" + KookboekContract.TagsOfRecipeEntry.COLUMN_RECIPE_ID + ") REFERENCES " +
                KookboekContract.RecipeEntry.TABLE_NAME + " (" + KookboekContract.RecipeEntry._ID + "), " +
                "FOREIGN KEY (" + KookboekContract.TagsOfRecipeEntry.COLUMN_TAG_ID + ") REFERENCES " +
                KookboekContract.TagEntry.TABLE_NAME + " (" + KookboekContract.TagEntry._ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + KookboekContract.RecipeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + KookboekContract.TagEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + KookboekContract.IngredientEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + KookboekContract.StepEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + KookboekContract.TagsOfRecipeEntry.TABLE_NAME);
        onCreate(db);
    }
}

