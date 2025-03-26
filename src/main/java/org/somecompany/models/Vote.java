package org.somecompany.models;

import java.util.List;

public class Vote extends Model {
    private String name;
    private String description;
    private int optionQuantity;
    private List<Option> optionList;
    
    public Vote(String name, String description, int optionQuantity, List<Option> optionList) {
        this.name = name;
        this.description = description;
        this.optionQuantity = optionQuantity;
        this.optionList = optionList;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getOptionQuantity() {
        return optionQuantity;
    }

    public List<Option> getOptionList() {
        return optionList;
    }
}