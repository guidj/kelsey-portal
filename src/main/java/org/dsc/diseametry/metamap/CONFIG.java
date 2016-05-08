package org.dsc.diseametry.metamap;

public enum CONFIG {
    HOST("Host");

    private String name;

    private CONFIG(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
