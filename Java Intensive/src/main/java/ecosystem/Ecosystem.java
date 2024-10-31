package main.java.ecosystem;

import main.java.species.Animal;
import main.java.species.Plant;
import main.java.species.Species;

import java.util.*;

public class Ecosystem {
    private List<Species> speciesList;
    private Environment environment;

    public Ecosystem(Environment environment){
        this.speciesList = new ArrayList<>();
        this.environment = environment;
    }

    public void addSpecies(Species species){
        speciesList.add(species);
        System.out.println("Добавлен вид: " + species.getName());
    }

    public void simulateInteractions() {
        System.out.println("Начало моделирования взаимодействий...");

        List<Animal> predators = new ArrayList<>();
        List<Animal> prey = new ArrayList<>();
        List<Plant> plants = new ArrayList<>();

        // Разделяем виды
        categorizeSpecies(predators, prey, plants);

        // Моделирование взаимодействий
        if (predators.isEmpty() || (prey.isEmpty() && plants.isEmpty())) {
            System.out.println("Моделирование остановлено: нет доступной пищи для животных.");
        } else {
            System.out.println("В экосистеме хищники и жертвы.");
            simulatePreyEatingPlants(prey, plants);
            simulatePredatorsEatingPrey(predators, prey);
            simulatePredatorsEatingEachOther(predators);
        }

        // Обновление списка выживших видов
        updateSpeciesList(predators, prey, plants);

        // Вывод результатов симуляции
        printSimulationResults();

        System.out.println("Моделирование взаимодействий завершено.");
    }

    private void categorizeSpecies(List<Animal> predators, List<Animal> prey, List<Plant> plants) {
        for (Species species : speciesList) {
            if (species instanceof Animal) {
                Animal animal = (Animal) species;
                if (animal.getEnergy() >= 70) {
                    predators.add(animal);
                } else {
                    prey.add(animal);
                }
            } else if (species instanceof Plant) {
                plants.add((Plant) species);
            }
        }
    }

    private void simulatePreyEatingPlants(List<Animal> prey, List<Plant> plants) {
        for (Animal animal : prey) {
            if (!plants.isEmpty()) {
                plants.sort(Comparator.comparingInt(Plant::getEnergy).reversed());
                Plant targetPlant = plants.get(0);

                if (animal.getEnergy() >= targetPlant.getEnergy()) {
                    animal.eat(targetPlant);
                    System.out.println(animal.getName() + " съел " + targetPlant.getName());
                    animal.setEnergy(animal.getEnergy() + targetPlant.getEnergy());
                    plants.remove(targetPlant);
                }
            }
        }
    }

    private void simulatePredatorsEatingPrey(List<Animal> predators, List<Animal> prey) {
        for (Animal predator : predators) {
            if (!prey.isEmpty()) {
                prey.sort(Comparator.comparingInt(Animal::getEnergy).reversed());
                Animal targetPrey = prey.get(0);

                if (predator.getEnergy() > targetPrey.getEnergy()) {
                    predator.eat(targetPrey);
                    System.out.println(predator.getName() + " съел " + targetPrey.getName());
                    predator.setEnergy(predator.getEnergy() + targetPrey.getEnergy());
                    prey.remove(targetPrey);
                }
            }
        }
    }

    private void simulatePredatorsEatingEachOther(List<Animal> predators) {
        while (predators.size() > 1) {
            predators.sort(Comparator.comparingInt(Animal::getEnergy).reversed());
            Animal weakestPredator = predators.remove(predators.size() - 1);
            System.out.println(weakestPredator.getName() + " был съеден хищником.");
        }
    }

    private void updateSpeciesList(List<Animal> predators, List<Animal> prey, List<Plant> plants) {
        speciesList.clear();
        speciesList.addAll(predators);
        speciesList.addAll(prey);
        speciesList.addAll(plants);
    }

    private void printSimulationResults() {
        System.out.println("Результаты симуляции:");
        for (Species species : speciesList) {
            System.out.println(species.getName() + " имеет энергию: " + species.getEnergy());
        }
    }


    public void removeSpecies(String name){
        Species speciesToRemove = getSpeciesByName(name);
        if (speciesToRemove != null) {
            speciesList.remove(speciesToRemove);
            System.out.println("Удален вид: " + name);
        } else {
            System.out.println("Нет такого вида в экосистеме.");
        }
    }

    public Species getSpeciesByName(String name) {
        for (Species species : speciesList) {
            if (species.getName().equalsIgnoreCase(name)) {
                return species;
            }
        }
        return null; // возвращает null, если вид не найден
    }

    public Species findSpecies(String name){
        return speciesList.stream().filter(species -> species.getName().equals(name)).findFirst().orElse(null);
    }

    public List<Species> getSpeciesList() {
        return speciesList;
    }

    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(environment.toString()).append("\n");
        for (Species s : speciesList) {
            builder.append(s.toString()).append("\n");
        }
        return builder.toString();
    }

    // Загрузка экосистемы из строки
    public static Ecosystem parse(String data) {
        String[] lines = data.split("\n");
        Environment env = null;
        List<Species> species = new ArrayList<>();

        for (String line : lines) {
            if (line.startsWith("Environment")) {
                env = Environment.parse(line);
            } else {
                Species s = Species.parse(line);
                if (s != null) species.add(s);
            }
        }
        if (env != null) {
            Ecosystem ecosystem = new Ecosystem(env);
            species.forEach(ecosystem::addSpecies);
            return ecosystem;
        }
        return null;
    }
}