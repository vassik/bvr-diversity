/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr;

import java.util.Objects;


public class Feature {
    
    private final int index;
    private final String name;

    public Feature(int index, String name) {
        this.index = index;
        this.name = String.format(name, index);
    }

    public int index() {
        return index;
    }

    public String name() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.index;
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
        if (this.index != other.index) {
            return false;
        }
        return Objects.equals(this.name, other.name);
    }
    
    
}
