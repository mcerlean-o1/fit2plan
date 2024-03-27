package com.oisin.fit2plan;

public class Note {

    private int id;
    private String title;
    private String breakfast;
    private String snack1;
    private String lunch;
    private String snack2;
    private String dinner;


    public Note(String title, String breakfast, String snack1, String lunch, String snack2, String dinner) {
        this.title = title;
        this.breakfast = breakfast;
        this.snack1 = snack1;
        this.lunch = lunch;
        this.snack2 = snack2;
        this.dinner = dinner;
    }

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

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getSnack1() {
        return snack1;
    }

    public void setSnack1(String snack1) {
        this.snack1 = snack1;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getSnack2() {
        return snack2;
    }

    public void setSnack2(String snack2) {
        this.snack2 = snack2;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

}
