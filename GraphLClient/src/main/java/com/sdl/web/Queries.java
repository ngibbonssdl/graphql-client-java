package com.sdl.web;

public class Queries {
    public static String Load(String queryName){
        QueryResources.LoadFromResource("GraphLClient", queryName);
        return null;
    }
}
