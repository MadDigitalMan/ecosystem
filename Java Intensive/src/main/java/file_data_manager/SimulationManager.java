package main.java.file_data_manager;

import main.java.ecosystem.Ecosystem;
import main.java.ecosystem.Environment;
import main.java.species.Animal;
import main.java.species.Plant;
import main.java.species.Species;

import java.io.*;

public class SimulationManager {
    private static final String SAVE_DIRECTORY = "D:\\Senla Task Java Intensive\\Java Intensive\\src\\resources\\data\\";

    public SimulationManager(){
        File directory = new File(SAVE_DIRECTORY);
        if(!directory.exists()){
            directory.mkdir();
        }
    }

    public void saveSimulation(String filename, Ecosystem ecosystem) {

        String filePath = SAVE_DIRECTORY + filename +".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            Environment env = ecosystem.getEnvironment();
            writer.println("Условия окружающей среды:");
            writer.println("Температура: " + env.getTemperature());
            writer.println("Влажность (в процентах): " + env.getHumidity());
            writer.println("Уровень воды: " + env.getWaterLevel());

            // Сохраняем каждый вид
            for (Species species : ecosystem.getSpeciesList()) {
                writer.println(species.toString());
            }

            System.out.println("Симуляция сохранена в файл: " + filename + ".txt");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения симуляции: " + e.getMessage());
        }
    }

    public Ecosystem loadSimulation(String filename) {
        String filePath = SAVE_DIRECTORY + filename + ".txt";
        Ecosystem ecosystem = null;
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line = reader.readLine();
            if (line != null && line.equals("Условия окружающей среды:")){
                ecosystem = loadEnvironment(reader);
                loadSpecies(reader,ecosystem);
            }
        }catch (IOException | NumberFormatException e){
            System.out.println("Ошибка при загрузке симуляции: " + e.getMessage());
        }
        return ecosystem;
    }



    private Ecosystem loadEnvironment(BufferedReader reader) throws IOException{
        double temperature = Double.parseDouble(reader.readLine().split(":")[1].trim());
        double humidity = Double.parseDouble(reader.readLine().split(":")[1].trim());
        double water = Double.parseDouble(reader.readLine().split(":")[1].trim());
        return new Ecosystem(new Environment(temperature,humidity,water));
    }

    private void loadSpecies(BufferedReader reader, Ecosystem ecosystem) throws IOException{
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            String type = parts[0];
            String name = parts[1];
            int energy = Integer.parseInt(parts[2]);

            if ("Animal".equals(type)) {
                Animal animal = createAnimal(parts, name, energy, ecosystem);
                ecosystem.addSpecies(animal);
            } else if ("Plant".equals(type)) {
                ecosystem.addSpecies(new Plant(name, energy));
            }
        }
    }

    private Animal createAnimal(String[] parts, String name, int energy, Ecosystem ecosystem) {
        Animal animal = new Animal(name, energy);
        if (parts.length > 3) { // проверка на наличие жертв
            String[] preyNames = parts[3].split(",");
            for (String preyName : preyNames) {
                Species prey = ecosystem.findSpecies(preyName.trim());
                if (prey != null) {
                    animal.addPrey(prey);
                }
            }
        }
        return animal;
    }

    public void listSimulations() {
        File directory = new File(SAVE_DIRECTORY);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files != null && files.length > 0) {
            System.out.println("Доступные симуляции:");
            for (File file : files) {
                System.out.println(file.getName().replace(".txt", ""));
            }
        } else {
            System.out.println("Нет доступных симуляций.");
        }
    }
}