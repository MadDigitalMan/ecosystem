package main.java.ecosystem;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    protected double temperature;
    protected double humidity;
    protected double waterLevel;

    public static final double MIN_TEMP = -10.0; // Минимально допустимая температура
    public static final double MAX_TEMP = 50.0; // Максимально допустимая температура
    public static final double MIN_HUMIDITY = 20.0; // Минимально допустимая влажность
    public static final double MAX_HUMIDITY = 90.0; // Максимально допустимая влажность
    public static final double MIN_WATER_LEVEL = 10.0; // Минимально допустимый уровень воды


    public Environment(double temperature, double humidity, double waterLevel){
        this.temperature = temperature;
        this.humidity = humidity;
        this.waterLevel = waterLevel;
    }


    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    public boolean isSuitableForLife() {
        return isTemperatureSuitable() && isHumiditySuitable() && isWaterLevelSuitable();
    }

    public boolean isTemperatureSuitable() {
        return temperature >= MIN_TEMP && temperature <= MAX_TEMP;
    }

    public boolean isHumiditySuitable() {
        return humidity >= MIN_HUMIDITY && humidity <= MAX_HUMIDITY;
    }

    public boolean isWaterLevelSuitable() {
        return waterLevel >= MIN_WATER_LEVEL;
    }

    @Override
    public String toString() {
        return "Условия окружающей среды:\n" +
                "Температура: " + temperature + "\n" +
                "Влажность (в процентах): " + humidity + "\n" +
                "Уровень воды: " + waterLevel;
    }

    public static Environment parse(String line) {
        String[] parts = line.split(";");
        if (parts.length == 4 && "Environment".equalsIgnoreCase(parts[0])) {
            double temperature = Double.parseDouble(parts[1]);
            double humidity = Double.parseDouble(parts[2]);
            double waterLevel = Double.parseDouble(parts[3]);
            return new Environment(temperature, humidity, waterLevel);
        }
        return null;
    }
}


