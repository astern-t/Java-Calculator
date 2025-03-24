package com.calculator.utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Manages calculation history and logging for the calculator application.
 */
public class HistoryManager {
    private static final int MAX_HISTORY_SIZE = 100;
    private static final String HISTORY_FILE = "calculator_history.txt";
    private final ObservableList<CalculationEntry> history;
    private final DateTimeFormatter formatter;
    
    public HistoryManager() {
        history = FXCollections.observableArrayList();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        loadHistory();
    }
    
    /**
     * Represents a single calculation entry in the history.
     */
    public static class CalculationEntry {
        private final String expression;
        private final String result;
        private final LocalDateTime timestamp;
        private final String category;
        
        public CalculationEntry(String expression, String result, String category) {
            this.expression = expression;
            this.result = result;
            this.timestamp = LocalDateTime.now();
            this.category = category;
        }
        
        public String getExpression() {
            return expression;
        }
        
        public String getResult() {
            return result;
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
        
        public String getCategory() {
            return category;
        }
        
        @Override
        public String toString() {
            return String.format("%s | %s = %s | %s", 
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                expression, result, category);
        }
    }
    
    /**
     * Adds a new calculation to the history.
     */
    public void addCalculation(String expression, String result, String category) {
        CalculationEntry entry = new CalculationEntry(expression, result, category);
        history.add(0, entry); // Add to the beginning of the list
        
        // Maintain maximum history size
        if (history.size() > MAX_HISTORY_SIZE) {
            history.remove(MAX_HISTORY_SIZE);
        }
        
        // Save to file
        saveHistory();
    }
    
    /**
     * Returns the complete history list.
     */
    public ObservableList<CalculationEntry> getHistory() {
        return history;
    }
    
    /**
     * Clears the entire history.
     */
    public void clearHistory() {
        history.clear();
        saveHistory();
    }
    
    /**
     * Searches the history for entries matching the given criteria.
     */
    public List<CalculationEntry> searchHistory(String searchTerm) {
        List<CalculationEntry> results = new ArrayList<>();
        for (CalculationEntry entry : history) {
            if (entry.getExpression().contains(searchTerm) ||
                entry.getResult().contains(searchTerm) ||
                entry.getCategory().contains(searchTerm)) {
                results.add(entry);
            }
        }
        return results;
    }
    
    /**
     * Filters history by category.
     */
    public List<CalculationEntry> filterByCategory(String category) {
        List<CalculationEntry> filtered = new ArrayList<>();
        for (CalculationEntry entry : history) {
            if (entry.getCategory().equals(category)) {
                filtered.add(entry);
            }
        }
        return filtered;
    }
    
    /**
     * Returns the most recent calculation result.
     */
    public Optional<String> getLastResult() {
        if (!history.isEmpty()) {
            return Optional.of(history.get(0).getResult());
        }
        return Optional.empty();
    }
    
    /**
     * Exports the history to a CSV file.
     */
    public void exportToCSV(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Timestamp,Expression,Result,Category");
            for (CalculationEntry entry : history) {
                writer.printf("%s,\"%s\",\"%s\",%s%n",
                    entry.getTimestamp().format(formatter),
                    entry.getExpression().replace("\"", "\"\""),
                    entry.getResult().replace("\"", "\"\""),
                    entry.getCategory());
            }
        }
    }
    
    /**
     * Loads history from file.
     */
    private void loadHistory() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    LocalDateTime timestamp = LocalDateTime.parse(parts[0].trim(), formatter);
                    String[] calculation = parts[1].trim().split("=");
                    String expression = calculation[0].trim();
                    String result = calculation[1].trim();
                    String category = parts[2].trim();
                    
                    CalculationEntry entry = new CalculationEntry(expression, result, category);
                    history.add(entry);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading history: " + e.getMessage());
        }
    }
    
    /**
     * Saves history to file.
     */
    private void saveHistory() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HISTORY_FILE))) {
            for (CalculationEntry entry : history) {
                writer.println(entry.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving history: " + e.getMessage());
        }
    }
    
    /**
     * Returns statistics about the calculation history.
     */
    public HistoryStatistics getStatistics() {
        int totalCalculations = history.size();
        int basicOperations = 0;
        int scientificOperations = 0;
        int financialCalculations = 0;
        int unitConversions = 0;
        
        for (CalculationEntry entry : history) {
            switch (entry.getCategory()) {
                case "Basic":
                    basicOperations++;
                    break;
                case "Scientific":
                    scientificOperations++;
                    break;
                case "Financial":
                    financialCalculations++;
                    break;
                case "Conversion":
                    unitConversions++;
                    break;
            }
        }
        
        return new HistoryStatistics(totalCalculations, basicOperations,
            scientificOperations, financialCalculations, unitConversions);
    }
    
    /**
     * Statistics about the calculation history.
     */
    public static class HistoryStatistics {
        private final int totalCalculations;
        private final int basicOperations;
        private final int scientificOperations;
        private final int financialCalculations;
        private final int unitConversions;
        
        public HistoryStatistics(int totalCalculations, int basicOperations,
                               int scientificOperations, int financialCalculations,
                               int unitConversions) {
            this.totalCalculations = totalCalculations;
            this.basicOperations = basicOperations;
            this.scientificOperations = scientificOperations;
            this.financialCalculations = financialCalculations;
            this.unitConversions = unitConversions;
        }
        
        public int getTotalCalculations() {
            return totalCalculations;
        }
        
        public int getBasicOperations() {
            return basicOperations;
        }
        
        public int getScientificOperations() {
            return scientificOperations;
        }
        
        public int getFinancialCalculations() {
            return financialCalculations;
        }
        
        public int getUnitConversions() {
            return unitConversions;
        }
        
        public double getBasicOperationsPercentage() {
            return totalCalculations == 0 ? 0 : 
                (basicOperations * 100.0) / totalCalculations;
        }
        
        public double getScientificOperationsPercentage() {
            return totalCalculations == 0 ? 0 : 
                (scientificOperations * 100.0) / totalCalculations;
        }
        
        public double getFinancialCalculationsPercentage() {
            return totalCalculations == 0 ? 0 : 
                (financialCalculations * 100.0) / totalCalculations;
        }
        
        public double getUnitConversionsPercentage() {
            return totalCalculations == 0 ? 0 : 
                (unitConversions * 100.0) / totalCalculations;
        }
    }
} 