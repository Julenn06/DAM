package model;

import java.io.Serializable;
import java.util.List;

public class Gimnasio implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    List<Workout> workoutak;

    public List<Workout> getWorkoutak() {
        return workoutak;
    }

    public void setWorkoutak(List<Workout> workoutak) {
        this.workoutak = workoutak;
    }

    public Gimnasio(List<Workout> workoutak) {
        this.workoutak = workoutak;
    }

    @Override
    public String toString() {
        return "Gimnasio [workoutak=" + workoutak + "]";
    }

}
