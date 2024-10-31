package main.java.file_data_manager;

import main.java.ecosystem.Ecosystem;
import main.java.ecosystem.Environment;
import main.java.file_data_manager.InputManager;
import main.java.file_data_manager.SimulationManager;
import main.java.species.Species;

import java.util.Scanner;

public class EcosystemManager {
    private final SimulationManager simulationManager;
    private Ecosystem ecosystem;
    private final Scanner scanner;

    public EcosystemManager(SimulationManager simulationManager){
        this.simulationManager = simulationManager;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("Выберите действие: " +
                    "\n1 - Создать новую экосистему" +
                    "\n2 - Загрузить текущую экосистему" +
                    "\n3 - Список доступных экосистем" +
                    "\n4 - Выход");
            String command = scanner.nextLine();
            switch (command) {
                case "1":
                    ecosystem = createNewEcosystem();
                    if (ecosystem != null) {
                        runEcosystem();
                    }
                    break;
                case "2":
                    ecosystem = loadExistingEcosystem();
                    if (ecosystem != null) {
                        runEcosystem();
                    }
                    break;
                case "3":
                    simulationManager.listSimulations();
                    break;
                case "4":
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неизвестная команда.");
                    break;
            }
        }
    }

    private Ecosystem createNewEcosystem() {
        System.out.println("Введите параметры новой экосистемы:");
        System.out.print("Температура: ");
        double temp = Double.parseDouble(scanner.nextLine());
        System.out.print("Влажность: ");
        int humidity = Integer.parseInt(scanner.nextLine());
        System.out.print("Уровень воды: ");
        int waterLevel = Integer.parseInt(scanner.nextLine());
        Environment environment = new Environment(temp, humidity, waterLevel);
        Ecosystem ecosystem = new Ecosystem(environment);
        displayEcosystemStatus(ecosystem);
        return ecosystem;

    }

    private Ecosystem loadExistingEcosystem() {
        simulationManager.listSimulations();
        System.out.print("Введите имя симуляции для загрузки: ");
        String fileName = scanner.nextLine();
        Ecosystem ecosystem = simulationManager.loadSimulation(fileName);
        if (ecosystem != null) {
            System.out.println("Симуляция загружена.");
            // Проверяем пригодность экосистемы после загрузки
            displayEcosystemStatus(ecosystem);
        } else {
            System.out.println("Не удалось загрузить симуляцию.");
        }
        return ecosystem;
    }

    private void displayEcosystemStatus(Ecosystem ecosystem) {
        Environment env = ecosystem.getEnvironment();
        System.out.println("Текущие параметры экосистемы:");
        System.out.println(env.toString());

        boolean isSuitable = env.isSuitableForLife();
        String status = isSuitable ? "Пригодна для жизни" : "Не пригодна для жизни";
        System.out.println("Статус: " + status);

        // Рекомендуемые действия
        if (!isSuitable) {
            System.out.println("Рекомендуемые действия:");
            if (!env.isTemperatureSuitable()) {
                System.out.println("- Отрегулируйте температуру, она критически низкая или высокая.");
            }
            if (!env.isHumiditySuitable()) {
                System.out.println("- Проверьте уровень влажности, он слишком низкий или высокий.");
            }
            if (!env.isWaterLevelSuitable()) {
                System.out.println("- Уровень воды не в норме. Увеличьте или уменьшите уровень воды.");
            }
        }

        System.out.println("Виды в экосистеме:");
        for (Species species : ecosystem.getSpeciesList()) {
            System.out.println(species.toString());
        }
    }

    private void runEcosystem() {
        InputManager userInputHandler = new InputManager(scanner, ecosystem, simulationManager);
        userInputHandler.handleUserInput(); // Передаем управление вводом пользователю
    }
}
