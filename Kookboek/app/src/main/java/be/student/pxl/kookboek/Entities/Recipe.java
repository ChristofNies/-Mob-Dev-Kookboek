package be.student.pxl.kookboek.Entities;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private long id;
    private String title;
    private String picture;
    private String cookingTime;
    private int numberOfPersons;
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

    public Recipe(int id, String title, String picture, String cookingTime, int numberOfPersons, String description, String commentary) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
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

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
}
