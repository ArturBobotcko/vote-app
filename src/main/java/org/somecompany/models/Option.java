package org.somecompany.models;

public class Option extends Model {
    private String name;
    
    public Option(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}