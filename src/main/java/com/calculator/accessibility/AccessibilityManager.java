package com.calculator.accessibility;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages accessibility features for the calculator application.
 */
public class AccessibilityManager {
    private boolean highContrastMode = false;
    private boolean largeTextMode = false;
    private double baseTextSize = 14.0;
    private final Map<Node, String> screenReaderText = new HashMap<>();
    private final Map<String, KeyCodeCombination> keyboardShortcuts = new HashMap<>();

    public AccessibilityManager() {
        initializeKeyboardShortcuts();
    }

    // Initialize keyboard shortcuts
    private void initializeKeyboardShortcuts() {
        keyboardShortcuts.put("clear", new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN));
        keyboardShortcuts.put("equals", new KeyCodeCombination(KeyCode.ENTER));
        keyboardShortcuts.put("backspace", new KeyCodeCombination(KeyCode.BACK_SPACE));
        keyboardShortcuts.put("toggleTheme", new KeyCodeCombination(KeyCode.T, KeyCombination.ALT_DOWN));
        keyboardShortcuts.put("toggleHighContrast", new KeyCodeCombination(KeyCode.H, KeyCombination.ALT_DOWN));
        keyboardShortcuts.put("toggleLargeText", new KeyCodeCombination(KeyCode.L, KeyCombination.ALT_DOWN));
        keyboardShortcuts.put("help", new KeyCodeCombination(KeyCode.F1));
    }

    // Screen reader support
    public void setScreenReaderText(Node node, String description) {
        screenReaderText.put(node, description);
        node.getProperties().put("screenReaderText", description);
        
        if (node instanceof Button) {
            Button button = (Button) node;
            button.setAccessibleText(description);
            button.setFocusTraversable(true);
        } else if (node instanceof TextField) {
            TextField textField = (TextField) node;
            textField.setAccessibleText(description);
            textField.setPromptText(description);
        } else if (node instanceof Label) {
            Label label = (Label) node;
            label.setAccessibleText(description);
        }
    }

    public String getScreenReaderText(Node node) {
        return screenReaderText.getOrDefault(node, "");
    }

    // High contrast mode
    public void toggleHighContrastMode() {
        highContrastMode = !highContrastMode;
        updateHighContrastStyles();
    }

    private void updateHighContrastStyles() {
        // These colors will be applied through CSS classes
        String backgroundColor = highContrastMode ? "#000000" : "#FFFFFF";
        String textColor = highContrastMode ? "#FFFFFF" : "#000000";
        String buttonColor = highContrastMode ? "#FFFF00" : "#4CAF50";
        
        // Apply styles through CSS
        String highContrastStyle = String.format(
            ".root { -fx-background-color: %s; -fx-text-fill: %s; }" +
            ".button { -fx-background-color: %s; -fx-text-fill: %s; }" +
            ".text-field { -fx-background-color: %s; -fx-text-fill: %s; }",
            backgroundColor, textColor, buttonColor, textColor, backgroundColor, textColor
        );
        
        // The actual style application will be done through the main application
    }

    // Large text mode
    public void toggleLargeTextMode() {
        largeTextMode = !largeTextMode;
        updateTextSize();
    }

    private void updateTextSize() {
        double textSize = largeTextMode ? baseTextSize * 1.5 : baseTextSize;
        Font largeFont = Font.font("System", FontWeight.NORMAL, textSize);
        
        // The actual font application will be done through the main application
    }

    // Keyboard navigation
    public void setupKeyboardNavigation(Node node) {
        node.setFocusTraversable(true);
        
        if (node instanceof Button) {
            Button button = (Button) node;
            button.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ENTER) {
                    button.fire();
                }
            });
        }
    }

    public KeyCodeCombination getShortcut(String action) {
        return keyboardShortcuts.get(action);
    }

    // Focus management
    public void setupFocusIndicators(Node node) {
        node.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                node.setStyle("-fx-effect: dropshadow(three-pass-box, #4CAF50, 10, 0, 0, 0);");
            } else {
                node.setStyle("");
            }
        });
    }

    // Color blindness support
    public Color getAccessibleColor(Color originalColor) {
        // Convert colors to be distinguishable for common types of color blindness
        if (originalColor.equals(Color.RED)) {
            return Color.CRIMSON; // More distinguishable red
        } else if (originalColor.equals(Color.GREEN)) {
            return Color.LIME; // More distinguishable green
        } else if (originalColor.equals(Color.BLUE)) {
            return Color.DEEPSKYBLUE; // More distinguishable blue
        }
        return originalColor;
    }

    // Animation control
    private boolean animationsEnabled = true;

    public void toggleAnimations() {
        animationsEnabled = !animationsEnabled;
    }

    public boolean areAnimationsEnabled() {
        return animationsEnabled;
    }

    // Text-to-speech settings
    private boolean textToSpeechEnabled = false;
    private double speechRate = 1.0;

    public void toggleTextToSpeech() {
        textToSpeechEnabled = !textToSpeechEnabled;
    }

    public void setSpeechRate(double rate) {
        if (rate >= 0.5 && rate <= 2.0) {
            speechRate = rate;
        }
    }

    public boolean isTextToSpeechEnabled() {
        return textToSpeechEnabled;
    }

    public double getSpeechRate() {
        return speechRate;
    }

    // Gesture support
    private boolean gesturesEnabled = true;

    public void toggleGestures() {
        gesturesEnabled = !gesturesEnabled;
    }

    public boolean areGesturesEnabled() {
        return gesturesEnabled;
    }

    // Helper methods for state checking
    public boolean isHighContrastMode() {
        return highContrastMode;
    }

    public boolean isLargeTextMode() {
        return largeTextMode;
    }

    // Settings persistence
    public Map<String, Object> getAccessibilitySettings() {
        Map<String, Object> settings = new HashMap<>();
        settings.put("highContrastMode", highContrastMode);
        settings.put("largeTextMode", largeTextMode);
        settings.put("baseTextSize", baseTextSize);
        settings.put("textToSpeechEnabled", textToSpeechEnabled);
        settings.put("speechRate", speechRate);
        settings.put("animationsEnabled", animationsEnabled);
        settings.put("gesturesEnabled", gesturesEnabled);
        return settings;
    }

    public void applyAccessibilitySettings(Map<String, Object> settings) {
        highContrastMode = (boolean) settings.getOrDefault("highContrastMode", false);
        largeTextMode = (boolean) settings.getOrDefault("largeTextMode", false);
        baseTextSize = (double) settings.getOrDefault("baseTextSize", 14.0);
        textToSpeechEnabled = (boolean) settings.getOrDefault("textToSpeechEnabled", false);
        speechRate = (double) settings.getOrDefault("speechRate", 1.0);
        animationsEnabled = (boolean) settings.getOrDefault("animationsEnabled", true);
        gesturesEnabled = (boolean) settings.getOrDefault("gesturesEnabled", true);
        
        updateHighContrastStyles();
        updateTextSize();
    }
} 