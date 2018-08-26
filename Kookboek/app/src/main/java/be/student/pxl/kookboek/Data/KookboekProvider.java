package be.student.pxl.kookboek.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static android.icu.text.UnicodeSet.CASE;

public class KookboekProvider extends ContentProvider {
    private static final int RECIPE = 100;
    private static final int RECIPE_ID = 101;

    private static final int TAG = 200;
    private static final int TAG_ID = 201;

    private static final int INGREDIENT = 300;
    private static final int INGREDIENT_RECIPE_ID = 301;

    private static final int STEP = 400;
    private static final int STEP_RECIPE_ID = 401;

    private static final int TAG_OF_RECIPE = 500;
    private static final int TAG_RECIPE_ID = 501;

    private KookboekDBHelper kookboekDBHelper;
    private static final SQLiteQueryBuilder queryBuilder;

    static {
        queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(
                KookboekContract.RecipeEntry.TABLE_NAME + " INNER JOIN " +
                        KookboekContract.IngredientEntry.TABLE_NAME +
                        " ON " + KookboekContract.RecipeEntry.TABLE_NAME +
                        "." + KookboekContract.RecipeEntry._ID +
                        " = " + KookboekContract.IngredientEntry.TABLE_NAME +
                        "." + KookboekContract.IngredientEntry.COLUMN_RECIPE_ID +
                        " INNER JOIN " + KookboekContract.StepEntry.TABLE_NAME +
                        " ON " + KookboekContract.RecipeEntry.TABLE_NAME +
                        "." + KookboekContract.RecipeEntry._ID +
                        " = " + KookboekContract.StepEntry.TABLE_NAME +
                        "." + KookboekContract.StepEntry.COLUMN_RECIPE_ID +
                        " INNER JOIN " + KookboekContract.TagsOfRecipeEntry.TABLE_NAME +
                        " ON " + KookboekContract.RecipeEntry.TABLE_NAME +
                        "." + KookboekContract.RecipeEntry._ID +
                        " = " + KookboekContract.TagsOfRecipeEntry.TABLE_NAME +
                        "." + KookboekContract.TagsOfRecipeEntry.COLUMN_RECIPE_ID +
                        " INNER JOIN " + KookboekContract.TagEntry.TABLE_NAME +
                        " ON " + KookboekContract.TagsOfRecipeEntry.TABLE_NAME +
                        "." + KookboekContract.TagsOfRecipeEntry.COLUMN_TAG_ID +
                        " = " + KookboekContract.TagEntry.TABLE_NAME +
                        "." + KookboekContract.TagEntry._ID);
    }

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = KookboekContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, KookboekContract.PATH_RECIPE, RECIPE);
        matcher.addURI(authority, KookboekContract.PATH_RECIPE + "/#", RECIPE_ID);

        matcher.addURI(authority, KookboekContract.PATH_TAG, TAG);
        matcher.addURI(authority, KookboekContract.PATH_TAG + "/#", TAG_ID);

        matcher.addURI(authority, KookboekContract.PATH_INGREDIENT, INGREDIENT);
        matcher.addURI(authority, KookboekContract.PATH_INGREDIENT + "/#", INGREDIENT_RECIPE_ID);

        matcher.addURI(authority, KookboekContract.PATH_STEP, STEP);
        matcher.addURI(authority, KookboekContract.PATH_STEP + "/#", STEP_RECIPE_ID);

        matcher.addURI(authority, KookboekContract.PATH_TAGS_OF_RECIPE, TAG_OF_RECIPE);
        matcher.addURI(authority, KookboekContract.PATH_TAGS_OF_RECIPE + "/#", TAG_RECIPE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        kookboekDBHelper = new KookboekDBHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match) {
            case RECIPE:
                return KookboekContract.RecipeEntry.CONTENT_TYPE;
            case RECIPE_ID:
                return KookboekContract.RecipeEntry.CONTENT_ITEM_TYPE;
            case TAG:
                return KookboekContract.TagEntry.CONTENT_TYPE;
            case TAG_ID:
                return KookboekContract.TagEntry.CONTENT_ITEM_TYPE;
            case INGREDIENT:
                return KookboekContract.IngredientEntry.CONTENT_ITEM_TYPE;
            case INGREDIENT_RECIPE_ID:
                return KookboekContract.IngredientEntry.CONTENT_TYPE;
            case STEP:
                return KookboekContract.StepEntry.CONTENT_ITEM_TYPE;
            case STEP_RECIPE_ID:
                return KookboekContract.StepEntry.CONTENT_TYPE;
            case TAG_OF_RECIPE:
                return KookboekContract.TagsOfRecipeEntry.CONTENT_ITEM_TYPE;
            case TAG_RECIPE_ID:
                return KookboekContract.TagEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Cursor retCursor;
        long _id;

        switch (uriMatcher.match(uri)) {
            case RECIPE:
                retCursor = kookboekDBHelper.getReadableDatabase().query(
                        KookboekContract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case RECIPE_ID:
                _id = ContentUris.parseId(uri);
                retCursor = kookboekDBHelper.getReadableDatabase().query(
                        KookboekContract.RecipeEntry.TABLE_NAME,
                        projection,
                        KookboekContract.RecipeEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case TAG:
                retCursor = kookboekDBHelper.getReadableDatabase().query(
                        KookboekContract.TagEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TAG_ID:
                _id = ContentUris.parseId(uri);
                retCursor = kookboekDBHelper.getReadableDatabase().query(
                        KookboekContract.TagEntry.TABLE_NAME,
                        projection,
                        KookboekContract.TagEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case INGREDIENT_RECIPE_ID:
                //retCursor = getIngredientsByRecipeId(uri, projection, sortOrder);
                retCursor = kookboekDBHelper.getReadableDatabase().query(
                        KookboekContract.IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case STEP_RECIPE_ID:
                retCursor = getStepsByRecipeId(uri, projection, sortOrder);
                break;
            case TAG_RECIPE_ID:
                retCursor = getTagsByRecipeId(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long _id;
        Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case RECIPE:
                _id = kookboekDBHelper.getWritableDatabase().insert(KookboekContract.RecipeEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = KookboekContract.RecipeEntry.buildRecipeUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert row in: " + uri);
                }
                break;
            case INGREDIENT:
                _id = kookboekDBHelper.getWritableDatabase().insert(KookboekContract.IngredientEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = KookboekContract.IngredientEntry.buildIngredientUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert row in: " + uri);
                }
                break;
            case STEP:
                _id = kookboekDBHelper.getWritableDatabase().insert(KookboekContract.StepEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = KookboekContract.StepEntry.buildStepUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert row in: " + uri);
                }
                break;
            case TAG:
                _id = kookboekDBHelper.getWritableDatabase().insert(KookboekContract.TagEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = KookboekContract.TagEntry.buildTagUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert row in: " + uri);
                }
                break;
            case TAG_OF_RECIPE:
                _id = kookboekDBHelper.getWritableDatabase().insert(KookboekContract.TagsOfRecipeEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = KookboekContract.TagsOfRecipeEntry.buildTagsOfRecipeUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert row in: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;
        if ( null == selection ) selection = "1";

        switch (uriMatcher.match(uri)) {
            case RECIPE:
                rowsDeleted = kookboekDBHelper.getWritableDatabase().delete(KookboekContract.RecipeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INGREDIENT:
                rowsDeleted = kookboekDBHelper.getWritableDatabase().delete(KookboekContract.IngredientEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case STEP:
                rowsDeleted = kookboekDBHelper.getWritableDatabase().delete(KookboekContract.StepEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TAG_OF_RECIPE:
                rowsDeleted = kookboekDBHelper.getWritableDatabase().delete(KookboekContract.TagsOfRecipeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsUpdated;

        switch (uriMatcher.match(uri)) {
            case RECIPE:
                rowsUpdated = kookboekDBHelper.getWritableDatabase().update(
                        KookboekContract.RecipeEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case INGREDIENT:
                rowsUpdated = kookboekDBHelper.getWritableDatabase().update(
                        KookboekContract.IngredientEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case STEP:
                rowsUpdated = kookboekDBHelper.getWritableDatabase().update(
                        KookboekContract.StepEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case TAG_OF_RECIPE:
                rowsUpdated = kookboekDBHelper.getWritableDatabase().update(
                        KookboekContract.TagsOfRecipeEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    private Cursor getIngredientsByRecipeId(Uri uri, String[] projection, String sortOrder) {
        long recipeId = KookboekContract.IngredientEntry.getRecipeIdFromUri(uri);

        String[] selectionArgs = new String[]{Long.toString(recipeId)};
        String selection = KookboekContract.IngredientEntry.COLUMN_RECIPE_ID + " = ?";

        return kookboekDBHelper.getReadableDatabase().query(KookboekContract.IngredientEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getStepsByRecipeId(Uri uri, String[] projection, String sortOrder) {
        long recipeId = KookboekContract.StepEntry.getRecipeIdFromUri(uri);

        String[] selectionArgs = new String[]{Long.toString(recipeId)};
        String selection = KookboekContract.StepEntry.TABLE_NAME + "." +
                KookboekContract.StepEntry.COLUMN_RECIPE_ID + " = ?";

        return queryBuilder.query(kookboekDBHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getTagsByRecipeId(Uri uri, String[] projection, String sortOrder) {
        long recipeId = KookboekContract.TagsOfRecipeEntry.getRecipeIdFromUri(uri);

        String[] selectionArgs = new String[]{Long.toString(recipeId)};
        String selection = KookboekContract.TagsOfRecipeEntry.TABLE_NAME + "." +
                KookboekContract.TagsOfRecipeEntry.COLUMN_RECIPE_ID + " = ?";

        Cursor tagIdsCursor = queryBuilder.query(kookboekDBHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        List<String> tagIds = new ArrayList<>();

        while (tagIdsCursor.moveToNext()) {
            tagIds.add(Long.toString(tagIdsCursor.getLong(2)));
        }

        selectionArgs = new String[tagIds.size()];
        tagIds.toArray(selectionArgs);

        selection = KookboekContract.TagEntry.TABLE_NAME + "." +
                KookboekContract.TagEntry._ID + " IN (";

        for (int i = 0; i < tagIds.size(); i++) {
            if (i == tagIds.size() - 1) {
                selection += "?";
            } else {
                selection += "?, ";
            }

        }
        selection += ")";

        return queryBuilder.query(kookboekDBHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }
}
