package be.student.pxl.kookboek.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.sql.Blob;

public class KookboekContract {

    private KookboekContract() {}

    public static final String CONTENT_AUTHORITY = "be.student.pxl.kookboek";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECIPE = "recipe";
    public static final String PATH_TAG = "tag";
    public static final String PATH_INGREDIENT = "ingredient";
    public static final String PATH_STEP = "step";
    public static final String PATH_TAGS_OF_RECIPE = "tags_of_recipe";

    public static final class RecipeEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;

        public static final String TABLE_NAME = "recipes";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PICTURE = "picture";
        public static final String COLUMN_NUMBER_OF_PERSONS = "number_of_persons";
        public static final String COLUMN_COOKING_TIME = "cooking_time";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_COMMENTARY = "commentary";

        public static Uri buildRecipeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TagEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TAG).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TAG;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TAG;

        public static final String TABLE_NAME = "tags";

        public static final String COLUMN_NAME = "name";

        public static Uri buildTagUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class IngredientEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
              BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;

        public static final String TABLE_NAME = "ingredients";

        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_DESCRIPTION = "description";

        public static Uri buildIngredientUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildIngredientRecipe(long recipeId) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(recipeId)).build();
        }

        public static long getRecipeIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

    public static final class StepEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEP).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;

        public static final String TABLE_NAME = "steps";

        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_DESCRIPTION = "description";

        public static Uri buildStepUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildStepRecipe(long recipeId) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(recipeId)).build();
        }

        public static long getRecipeIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

    public static final class TagsOfRecipeEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TAGS_OF_RECIPE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;

        public static final String TABLE_NAME = "tags_of_recipe";

        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_TAG_ID = "tagId";

        public static Uri buildTagsOfRecipeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTagsRecipe(long recipeId) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(recipeId)).build();
        }

        public static long getRecipeIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static long getTagIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }
}
