package no.sintef.bvr.sampler.diversity.evolution;

/**
 * Couple of individual, ready to mate
 */
public class Couple {

    private final Individual father;
    private final Individual mother;

    public Couple(Individual father, Individual mother) {
        this.father = father;
        this.mother = mother;
    }

    public Individual father() {
        return father;
    }

    public Individual mother() {
        return mother;
    }

    @Override
    public String toString() {
        return "(" + father.toString() + ", " + mother.toString() + ")";
    }

}
