package com.wf.wfdeliverysystem.model;

public class House {
    String id;

    public House(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "House{" +
                "id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
