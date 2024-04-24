package com.oisin.fit2plan;

public class CalorieCalculator {

    public static double calculateBMR(User user) {
        int age = user.getAge();
        double weight = user.getWeight();
        double height = user.getHeight();
        String gender = user.getGender();
        if (gender.equalsIgnoreCase("male")) {
            return  88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            return 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }
    }
    public static double calculateTDEE(User user) {
        String activityLevel = user.getActivityLevel();
        double bmr = calculateBMR(user);
        if (activityLevel.equalsIgnoreCase("1 - Sedentary")) {
            return bmr * 1.2;
        } else if (activityLevel.equalsIgnoreCase("2 - Lightly active")) {
            return bmr * 1.375;
        } else if (activityLevel.equalsIgnoreCase("3 - Moderately active")) {
            return bmr * 1.55;
        } else if (activityLevel.equalsIgnoreCase("4 - Highly active")) {
            return bmr * 1.725;
        } else if (activityLevel.equalsIgnoreCase("5 - Extremely active")) {
            return bmr * 1.9;
        }
        return bmr;
    }
}
