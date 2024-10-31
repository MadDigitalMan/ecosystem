package main.java.mainApp;

import main.java.file_data_manager.EcosystemManager;
import main.java.file_data_manager.SimulationManager;

public class Main {
    public static void main(String[] args) {
        SimulationManager simulationManager = new SimulationManager();
        EcosystemManager ecosystemManager = new EcosystemManager(simulationManager);
        ecosystemManager.start();
    }
}