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

    public static double calculateDeficit(User user) {
        double tdee = calculateTDEE(user);
        return tdee - 500;
    }

    public static double calculateSurplus(User user) {
        double tdee = calculateTDEE(user);
        return tdee + 500;
    }

    public static double calculateProtein(double weight, String activity) {
        double proteinPerKg = 0;
        if (activity.equalsIgnoreCase("1 - Sedentary")) {
            proteinPerKg = 0.8;
        } else if (activity.equalsIgnoreCase("2 - Lightly active")) {
            proteinPerKg = 1.3;
        } else if (activity.equalsIgnoreCase("3 - Moderately active")) {
            proteinPerKg = 1.6;
        } else if (activity.equalsIgnoreCase("4 - Highly active")) {
            proteinPerKg = 1.8;
        } else if (activity.equalsIgnoreCase("5 - Extremely active")) {
            proteinPerKg = 2.2;
        }
        return weight * proteinPerKg;
    }

    public static double calculateBMRInput(Nutrition nutrition) {
        int age = nutrition.getAge();
        double weight = nutrition.getWeight();
        double height = nutrition.getHeight();
        String gender = nutrition.getGender();
        if (gender.equalsIgnoreCase("male")) {
            return  88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            return 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }
    }

    public static double calculateTDEEInput(Nutrition nutrition) {
        String activityLevel = nutrition.getActivityLevel();
        double bmr = calculateBMRInput(nutrition);
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

