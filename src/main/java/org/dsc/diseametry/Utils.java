package org.dsc.diseametry;

import java.util.List;

public class Utils {

    public static String collectionToString(Object[] objects) {
        String s = "";

        for(int i = 0; i < objects.length; i++){

            s += objects[i].toString();

            if (i < objects.length - 1) {
                s += ", ";
            }

        }

        return s;
    }

    public static String collectionToString(List<Object> objects) {
        String s = "";

        for(int i = 0; i < objects.size(); i++){

            s += objects.get(i).toString();

            if (i < objects.size() - 1) {
                s += ", ";
            }

        }

        return s;
    }
}
