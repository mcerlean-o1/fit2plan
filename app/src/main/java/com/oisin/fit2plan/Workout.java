package com.oisin.fit2plan;

public class Workout {

    private int id;
    private String Dates;
    private String A1;
    private String A2;
    private String B1;
    private String B2;
    private String C1;
    private String C2;
    private String multiNotes;

    public Workout(String dates, String a1, String a2, String b1, String b2, String c1, String c2, String multiNotes) {

        this.Dates = dates;
        this.A1 = a1;
        this.A2 = a2;
        this.B1 = b1;
        this.B2 = b2;
        this.C1 = c1;
        this.C2 = c2;
        this.multiNotes = multiNotes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDates() {
        return Dates;
    }

    public void setDates(String dates) {
        Dates = dates;
    }

    public String getA1() {
        return A1;
    }

    public void setA1(String a1) {
        A1 = a1;
    }


    public String getA2() {
        return A2;
    }

    public void setA2(String a2) {
        A2 = a2;
    }
    public String getB1() {
        return B1;
    }

    public void setB1(String b1) {
        B1 = b1;
    }


    public String getB2() {
        return B2;
    }

    public void setB2(String b2) {
        B2 = b2;
    }

    public String getC1() {
        return C1;
    }

    public void setC1(String c1) {
        C1 = c1;
    }

    public String getC2() {
        return C2;
    }

    public void setC2(String c2) {
        C2 = c2;
    }

    public String getMultiNotes() {
        return multiNotes;
    }

    public void setMultiNotes(String multiNotes) {
        this.multiNotes = multiNotes;
    }
}

