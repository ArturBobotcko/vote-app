package org.somecompany.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Topic extends Model {
    private String name;
    @JsonProperty("vote_list")
    private List<Vote> voteList;
    private String author;

    public Topic() {
        
    }

    public Topic(String name, String author) {
        this.name = name;
        this.author = author;
    }


    public Topic(String name, List<Vote> voteList, String author) {
        this.name = name;
        this.voteList = voteList;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public List<Vote> getVoteList() {
        return voteList;
    }

    public String getAuthor() {
        return author;
    }
}