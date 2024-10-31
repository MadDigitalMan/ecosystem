package main.java.species;

import java.util.ArrayList;
import java.util.List;

public abstract class Species {
    protected String name;
    protected String type;
    protected int energy;

    protected List<Species> prey;

    public Species(String name, String type, int energy){
        this.name = name;
        this.type = type;
        this.energy = energy;
        this.prey = new ArrayList<>();
    }


    public void addPrey(Species species){
        if(species != null) {
            prey.add(species);
            System.out.println(this.name + " теперь может поедать " + species.getName());

        }
    }

    public boolean canEat(Species prey) {
        return this.prey.contains(prey) && this.energy > prey.getEnergy();

    }

    public void eat(Species species) {
        if (canEat(species)) {
            this.energy += species.getEnergy(); // Увеличиваем энергию хищника на энергию жертвы
        }
    }



    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getEnergy() {
        return energy;
    }

    public List<Species> getPrey() {
        return prey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    // Статический метод для создания вида из строки
    public static Species parse(String line) {
        String[] parts = line.split(";");
        if (parts.length < 3) return null;

        String type = parts[0];
        String name = parts[1];
        int energy = Integer.parseInt(parts[2]);
        String plant = "Растение", animal = "Животное";

        if (plant.equalsIgnoreCase(type)) {
            return new Plant(name, energy);
        }
        else if (animal.equalsIgnoreCase(type) && parts.length >= 3) {
            return new Animal(name, energy);
        }
        return null;
    }

    @Override
    public String toString() {
        return type + ";" + name + ";" + energy;
    }
}