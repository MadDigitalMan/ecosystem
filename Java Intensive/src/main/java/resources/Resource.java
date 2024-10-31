package main.java.resources;

import java.util.Map;
import java.util.Objects;


@Deprecated
public record Resource(String type, int amount){
    private static Map<String, Integer> resources;

    public void addResource(String resourceName, int quantity) {
        resources.put(resourceName, quantity);
    }

    public Integer getResource(String resourceName) {
        return resources.get(resourceName);
    }
}
