package i.am.eipeks.corporatestore.welcome.classes;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DataProvider {

    public static HashMap<String, List<String>> getData(){

        HashMap<String, List<String>> data = new HashMap<>();
        List<String> first_children = new ArrayList<>();
        first_children.add("Bags");
        first_children.add("Slippers");
        first_children.add("Shoes");
        first_children.add("Hats");
        first_children.add("Jewelries");
        first_children.add("Clothes");
        Collections.sort(first_children);

        List<String> second_children = new ArrayList<>();
        second_children.add("Bags and Shoes");
        second_children.add("Bags and Slippers");
        second_children.add("Bags and Hats");
        second_children.add("Bags and Clothes");
        Collections.sort(second_children);

        data.put("Single", first_children);
        data.put("Double", second_children);

        return data;
    }
}
