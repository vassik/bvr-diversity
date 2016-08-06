package no.sintef.bvr.spl;

import java.util.Objects;

/**
 * A feature, identified by its name. 
 */
public class Feature {

    private final String name;

    public Feature(String name) {
        this.name = name;
    }
    
    public String name() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Feature other = (Feature) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }

}
