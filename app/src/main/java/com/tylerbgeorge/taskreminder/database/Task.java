package com.tylerbgeorge.taskreminder.database;

import java.util.List;

/**
 * Created by Tyler on 12/10/2014.
 */
public class Task {

    //private varibles
    int _id;
    String _name;
    String _description;
    List<String> _links;


    //Empty Constructor
    public Task () {}

    //constructor
    public Task (int id, String name, String description, List<String> links) {
        this._id = id;
        this._name = name;
        this._description = description;
        this._links = links;
    }

    //constructor
    public Task(String name, String description, List<String> links) {
        this._name = name;
        this._description = description;
        this._links = links;
    }

    //get ID
    public int getID() {
        return this._id;
    }

    //set ID
    public void setID(int id) {
        this._id = id;
    }

    //get Name
    public String getName() {
        return this._name;
    }

    //set Name
    public void setName(String name) {
        this._name = name;
    }

    //get Description
    public String getDescription() {
        return this._description;
    }

    //set Description
    public void setDescription(String description) {
        this._description = description;
    }

    //get Links
    public List<String> getLinks() {
        return this._links;
    }

    //set Links
    public void setLinks(List<String> links) {
        this._links = links;
    }
}
