package main.java.file_data_manager;

import main.java.ecosystem.Ecosystem;
import main.java.species.Animal;
import main.java.species.Plant;
import main.java.species.Species;

import java.util.Scanner;

public class InputManager {
    private final Scanner scanner;
    private final Ecosystem ecosystem;
    private final SimulationManager simulationManager;

    public InputManager(Scanner scanner, Ecosystem ecosystem, SimulationManager simulationManager) {
        this.scanner = scanner;
        this.ecosystem = ecosystem;
        this.simulationManager = simulationManager;
    }

    public void handleUserInput() {
        while (true) {
            System.out.println("Выберите действие: " +
                    "\n1 - Добавить новый вид (plant/animal)" +
                    "\n2 - Просмотреть текущее состояние экосистемы" +
                    "\n3 - Изменить состояние текущей экосистемы" +
                    "\n4 - Удалить состояние текущей экосистемы" +
                    "\n5 - Сохранить экосистему в файл" +
                    "\n6 - Моделирование взаимодействия между видами" +
                    "\n7 - Вернуться обратно");
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    addSpecies();
                    break;
                case "2":
                    viewEcosystem();
                    break;
                case "3":
                    updateSpecies();
                    break;
                case "4":
                    deleteSpecies();
                    break;
                case "5":
                    saveEcosystem();
                    break;
                case "6":
                    runSimulation();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Неизвестная команда.");
                    break;
            }
        }
    }

    private void addSpecies() {
        System.out.println("Вы собираетесь добавить новый вид в экосистему.");
        System.out.println("Обратите внимание:");
        System.out.println("- Животные с энергией 70 и выше будут являться хищниками.");
        System.out.println("- Животные с энергией ниже 70 будут являться жертвами.");
        System.out.println("- Энергия растений должна быть в диапазоне от 0 до 5.");

        while (true) {
            String type = getSpeciesType();
            if (type.equalsIgnoreCase("exit")) {
                break; // Завершаем ввод видов
            }

            String name = getSpeciesName();
            int energy = getSpeciesEnergy();

            addSpeciesToEcosystem(type, name, energy);
        }
    }

    private String getSpeciesType() {
        System.out.print("Введите тип вида (plant/animal) или 'exit' для завершения: ");
        return scanner.nextLine();
    }

    private String getSpeciesName() {
        System.out.print("Введите имя вида: ");
        return scanner.nextLine();
    }

    private int getSpeciesEnergy() {
        int energy;
        while (true) {
            System.out.print("Введите энергию вида: ");
            try {
                energy = Integer.parseInt(scanner.nextLine());
                if (energy < 0) {
                    System.out.print("Энергия не может быть отрицательной. Пожалуйста, введите корректное значение: ");
                } else {
                    return energy; // Корректное значение, возвращаем
                }
            } catch (NumberFormatException e) {
                System.out.print("Пожалуйста, введите корректное значение для энергии: ");
            }
        }
    }

    private void addSpeciesToEcosystem(String type, String name, int energy) {
        if ("plant".equalsIgnoreCase(type)) {
            if (energy >= 0 && energy <= 5) {
                ecosystem.addSpecies(new Plant(name, energy));
                System.out.println("Добавлено растение: " + name + " с энергией: " + energy);
            } else {
                System.out.println("Энергия для растений должна быть от 0 до 5. Растение не добавлено.");
            }
        } else if ("animal".equalsIgnoreCase(type)) {
            Animal animal = new Animal(name, energy);
            ecosystem.addSpecies(animal); // Сначала добавляем животное

            // Определяем, будет ли животное хищником или жертвой
            if (animal.getEnergy() >= 70) {
                System.out.println("Добавлено животное: " + name + " с энергией: " + energy + " (хищник)");
            } else {
                System.out.println("Добавлено животное: " + name + " с энергией: " + energy + " (жертва)");
            }
        } else {
            System.out.println("Некорректный тип вида. Пожалуйста, укажите 'plant' или 'animal'.");
        }
    }

    private void viewEcosystem() {
        System.out.println("Текущие параметры экосистемы:");
        System.out.println(ecosystem.getEnvironment());
        System.out.println("Виды в экосистеме:");
        for (Species s : ecosystem.getSpeciesList()) {
            System.out.println(s);
        }
    }

    private void updateSpecies() {
        System.out.print("Введите имя вида для обновления: ");
        String name = scanner.nextLine();
        Species species = ecosystem.findSpecies(name);

        if (species != null) {
            System.out.print("Введите новую энергию: ");
            int newEnergy = Integer.parseInt(scanner.nextLine());
            species.setEnergy(newEnergy);
            System.out.println("Обновлено.");
        } else {
            System.out.println("Вид не найден.");
        }
    }

    private void deleteSpecies() {
        System.out.print("Введите имя вида для удаления: ");
        String name = scanner.nextLine();
        ecosystem.removeSpecies(name);
        System.out.println("Вид удалён.");
    }

    private void saveEcosystem() {
        System.out.print("Введите имя файла для сохранения: ");
        String filename = scanner.nextLine();
        simulationManager.saveSimulation(filename, ecosystem);
        System.out.println("Экосистема сохранена.");
    }

    private void runSimulation() {
        ecosystem.simulateInteractions(); // Начинаем моделирование
    }
}
