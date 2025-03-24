package com.calculator.conversion;

import java.util.HashMap;
import java.util.Map;

/**
 * Comprehensive unit converter supporting various measurement types.
 */
public class UnitConverter {
    // Length conversions (base: meters)
    private static final Map<String, Double> LENGTH_FACTORS = new HashMap<>();
    static {
        LENGTH_FACTORS.put("mm", 0.001);
        LENGTH_FACTORS.put("cm", 0.01);
        LENGTH_FACTORS.put("m", 1.0);
        LENGTH_FACTORS.put("km", 1000.0);
        LENGTH_FACTORS.put("in", 0.0254);
        LENGTH_FACTORS.put("ft", 0.3048);
        LENGTH_FACTORS.put("yd", 0.9144);
        LENGTH_FACTORS.put("mi", 1609.344);
    }

    // Weight/Mass conversions (base: kilograms)
    private static final Map<String, Double> WEIGHT_FACTORS = new HashMap<>();
    static {
        WEIGHT_FACTORS.put("mg", 0.000001);
        WEIGHT_FACTORS.put("g", 0.001);
        WEIGHT_FACTORS.put("kg", 1.0);
        WEIGHT_FACTORS.put("oz", 0.0283495);
        WEIGHT_FACTORS.put("lb", 0.453592);
        WEIGHT_FACTORS.put("st", 6.35029);
        WEIGHT_FACTORS.put("t", 1000.0);
    }

    // Volume conversions (base: liters)
    private static final Map<String, Double> VOLUME_FACTORS = new HashMap<>();
    static {
        VOLUME_FACTORS.put("ml", 0.001);
        VOLUME_FACTORS.put("l", 1.0);
        VOLUME_FACTORS.put("m³", 1000.0);
        VOLUME_FACTORS.put("fl_oz", 0.0295735);
        VOLUME_FACTORS.put("cup", 0.236588);
        VOLUME_FACTORS.put("pt", 0.473176);
        VOLUME_FACTORS.put("qt", 0.946353);
        VOLUME_FACTORS.put("gal", 3.78541);
    }

    // Area conversions (base: square meters)
    private static final Map<String, Double> AREA_FACTORS = new HashMap<>();
    static {
        AREA_FACTORS.put("mm²", 0.000001);
        AREA_FACTORS.put("cm²", 0.0001);
        AREA_FACTORS.put("m²", 1.0);
        AREA_FACTORS.put("km²", 1000000.0);
        AREA_FACTORS.put("in²", 0.00064516);
        AREA_FACTORS.put("ft²", 0.092903);
        AREA_FACTORS.put("yd²", 0.836127);
        AREA_FACTORS.put("ac", 4046.86);
        AREA_FACTORS.put("ha", 10000.0);
    }

    // Temperature conversion methods
    public static double celsiusToFahrenheit(double celsius) {
        return celsius * 9/5 + 32;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5/9;
    }

    public static double celsiusToKelvin(double celsius) {
        return celsius + 273.15;
    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    public static double fahrenheitToKelvin(double fahrenheit) {
        return celsiusToKelvin(fahrenheitToCelsius(fahrenheit));
    }

    public static double kelvinToFahrenheit(double kelvin) {
        return celsiusToFahrenheit(kelvinToCelsius(kelvin));
    }

    // Generic conversion method
    private static double convert(double value, String fromUnit, String toUnit, 
                                Map<String, Double> factors) {
        if (!factors.containsKey(fromUnit) || !factors.containsKey(toUnit)) {
            throw new IllegalArgumentException("Invalid unit specified");
        }
        return value * factors.get(fromUnit) / factors.get(toUnit);
    }

    // Public conversion methods
    public static double convertLength(double value, String fromUnit, String toUnit) {
        return convert(value, fromUnit, toUnit, LENGTH_FACTORS);
    }

    public static double convertWeight(double value, String fromUnit, String toUnit) {
        return convert(value, fromUnit, toUnit, WEIGHT_FACTORS);
    }

    public static double convertVolume(double value, String fromUnit, String toUnit) {
        return convert(value, fromUnit, toUnit, VOLUME_FACTORS);
    }

    public static double convertArea(double value, String fromUnit, String toUnit) {
        return convert(value, fromUnit, toUnit, AREA_FACTORS);
    }

    // Time conversion (base: seconds)
    public static double convertTime(double value, String fromUnit, String toUnit) {
        Map<String, Double> timeFactors = new HashMap<>();
        timeFactors.put("ms", 0.001);
        timeFactors.put("s", 1.0);
        timeFactors.put("min", 60.0);
        timeFactors.put("h", 3600.0);
        timeFactors.put("d", 86400.0);
        timeFactors.put("wk", 604800.0);
        timeFactors.put("mo", 2592000.0); // Approximate - 30 days
        timeFactors.put("yr", 31536000.0); // Non-leap year
        
        return convert(value, fromUnit, toUnit, timeFactors);
    }

    // Speed conversion (using length and time)
    public static double convertSpeed(double value, String fromLengthUnit, String fromTimeUnit,
                                    String toLengthUnit, String toTimeUnit) {
        double lengthInMeters = convertLength(value, fromLengthUnit, "m");
        double timeInSeconds = convertTime(1, fromTimeUnit, "s");
        double speedInMPS = lengthInMeters / timeInSeconds;
        
        double targetTimeInSeconds = convertTime(1, toTimeUnit, "s");
        double targetLength = convertLength(1, "m", toLengthUnit);
        
        return speedInMPS * targetLength * targetTimeInSeconds;
    }

    // Pressure conversion (base: Pascal)
    public static double convertPressure(double value, String fromUnit, String toUnit) {
        Map<String, Double> pressureFactors = new HashMap<>();
        pressureFactors.put("Pa", 1.0);
        pressureFactors.put("kPa", 1000.0);
        pressureFactors.put("MPa", 1000000.0);
        pressureFactors.put("bar", 100000.0);
        pressureFactors.put("psi", 6894.76);
        pressureFactors.put("atm", 101325.0);
        pressureFactors.put("mmHg", 133.322);
        pressureFactors.put("inHg", 3386.39);
        
        return convert(value, fromUnit, toUnit, pressureFactors);
    }

    // Energy conversion (base: Joules)
    public static double convertEnergy(double value, String fromUnit, String toUnit) {
        Map<String, Double> energyFactors = new HashMap<>();
        energyFactors.put("J", 1.0);
        energyFactors.put("kJ", 1000.0);
        energyFactors.put("cal", 4.184);
        energyFactors.put("kcal", 4184.0);
        energyFactors.put("Wh", 3600.0);
        energyFactors.put("kWh", 3600000.0);
        energyFactors.put("BTU", 1055.06);
        energyFactors.put("eV", 1.602177e-19);
        
        return convert(value, fromUnit, toUnit, energyFactors);
    }

    // Digital storage conversion (base: bytes)
    public static double convertDigitalStorage(double value, String fromUnit, String toUnit) {
        Map<String, Double> storageFactors = new HashMap<>();
        storageFactors.put("B", 1.0);
        storageFactors.put("KB", 1024.0);
        storageFactors.put("MB", 1048576.0);
        storageFactors.put("GB", 1073741824.0);
        storageFactors.put("TB", 1099511627776.0);
        storageFactors.put("PB", 1125899906842624.0);
        
        return convert(value, fromUnit, toUnit, storageFactors);
    }

    // Angle conversion (base: radians)
    public static double convertAngle(double value, String fromUnit, String toUnit) {
        Map<String, Double> angleFactors = new HashMap<>();
        angleFactors.put("rad", 1.0);
        angleFactors.put("deg", Math.PI / 180);
        angleFactors.put("grad", Math.PI / 200);
        angleFactors.put("turn", 2 * Math.PI);
        
        return convert(value, fromUnit, toUnit, angleFactors);
    }

    // Frequency conversion (base: Hertz)
    public static double convertFrequency(double value, String fromUnit, String toUnit) {
        Map<String, Double> frequencyFactors = new HashMap<>();
        frequencyFactors.put("Hz", 1.0);
        frequencyFactors.put("kHz", 1000.0);
        frequencyFactors.put("MHz", 1000000.0);
        frequencyFactors.put("GHz", 1000000000.0);
        frequencyFactors.put("rpm", 1.0/60);
        
        return convert(value, fromUnit, toUnit, frequencyFactors);
    }
} 