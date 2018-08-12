package be.student.pxl.kookboek.Entities;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private int id;
    private String title;
    private byte[] picture;
    private String cookingTime;
    private int numberOfPersons;
    //private byte[] photoRecipe;
    private String description;
    private String commentary;

    private List<Integer> tagIds;
    private List<Ingredient> ingredients;
    private List<String> steps;

    public Recipe() {
        tagIds = new ArrayList<Integer>();
        ingredients = new ArrayList<Ingredient>();
        steps = new ArrayList<String>();
    }

    public Recipe(int id, String title, byte[] picture, String cookingTime, int numberOfPersons, String description, String commentary) {
        this.id = id;
        this.title = title;
        this.picture = picture;
        this.cookingTime = cookingTime;
        this.numberOfPersons = numberOfPersons;
        this.description = description;
        this.commentary = commentary;
        tagIds = new ArrayList<Integer>();
        ingredients = new ArrayList<Ingredient>();
        steps = new ArrayList<String>();
    }

//    public Recipe(String title, byte[] picture, String cookingTime, int numberOfPersons, byte[] photoRecipe, String description, String commentary) {
//        this.title = title;
//        this.picture = picture;
//        this.cookingTime = cookingTime;
//        this.numberOfPersons = numberOfPersons;
//        this.photoRecipe = photoRecipe;
//        this.description = description;
//        this.commentary = commentary;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public List<Integer> getTagId() {
        return tagIds;
    }

    public void addTagId(int tagId) {
        this.tagIds.add(tagId);
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public int getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(int numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public List<String> getSteps() {
        return steps;
    }

    public void addStep(String step) {
        this.steps.add(step);
    }

//    public byte[] getPhotoRecipe() {
//        return photoRecipe;
//    }
//
//    public void setPhotoRecipe(byte[] photoRecipe) {
//        this.photoRecipe = photoRecipe;
//    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
}
