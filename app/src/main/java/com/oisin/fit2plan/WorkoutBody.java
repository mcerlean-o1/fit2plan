package com.oisin.fit2plan;


public class WorkoutBody {


    private final String WorkoutName;
    private final String WorkoutDescription;

    public WorkoutBody(Build builder) {
        this.WorkoutName = builder.workoutName;
        this.WorkoutDescription = builder.workoutDesc;
    }

    public static class Build {
        private String workoutName;
        private String workoutDesc;

        public Build setWorkoutName(String workoutName) {
            this.workoutName = workoutName;
            return this;
        }

        public Build setWorkoutDesc(String workoutDesc) {
            this.workoutDesc = workoutDesc;
            return this;
        }

        public WorkoutBody build() {
            return new WorkoutBody(this);
        }
    }


    public String getWorkoutName() {
        return WorkoutName;
    }

    public String getWorkoutDescription() {
        return WorkoutDescription;
    }
}
