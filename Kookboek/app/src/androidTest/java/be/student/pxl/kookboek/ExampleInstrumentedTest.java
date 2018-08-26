package be.student.pxl.kookboek;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import be.student.pxl.kookboek.Data.KookboekContract;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("be.student.pxl.kookboek", appContext.getPackageName());
    }


    @Test
    public void testGetType() {
        String type = InstrumentationRegistry.getTargetContext().getContentResolver().getType(
                KookboekContract.IngredientEntry.buildIngredientRecipe(1));
        assertEquals("Error: IngredientEnty CONTENT_URI with recipeId should return IngredientEntry.CONTENT_TYPE",
                KookboekContract.IngredientEntry.CONTENT_TYPE, type);
    }

    @Test
    public void testRecipeIngredientsQuery() {

    }
}
