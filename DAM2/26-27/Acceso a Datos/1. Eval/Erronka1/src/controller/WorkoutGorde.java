package controller;

public class WorkoutGorde {

	// Workout-aren IDa modu estatikoan gordetzeko aldagai globala
	public static String workoutID;

	// IDa lortzeko getter metodoa
	public static String getWorkoutID() {
		return workoutID;
	}

	// IDa esleitzeko setter metodoa
	public static void setWorkoutID(String workoutID) {
		WorkoutGorde.workoutID = workoutID;
	}

	// Eraikitzaile hutsa
	public WorkoutGorde() {

	}
}
