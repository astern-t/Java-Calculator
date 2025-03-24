package com.calculator.utils;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Manages keyboard shortcuts and key bindings for the calculator.
 */
public class KeyboardManager {
    private final Map<KeyCombination, Runnable> shortcuts = new HashMap<>();
    private final Map<KeyCode, Button> numpadMap = new HashMap<>();
    private final Map<KeyCode, Button> operatorMap = new HashMap<>();
    private final Map<KeyCode, Button> functionMap = new HashMap<>();
    private Consumer<String> inputHandler;
    private Scene scene;
    
    public KeyboardManager(Scene scene) {
        this.scene = scene;
        initializeKeyboardHandling();
    }
    
    /**
     * Initializes keyboard event handling.
     */
    private void initializeKeyboardHandling() {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        setupDefaultShortcuts();
    }
    
    /**
     * Sets up default keyboard shortcuts.
     */
    private void setupDefaultShortcuts() {
        // File operations
        registerShortcut(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
            () -> System.out.println("Save history"));
        registerShortcut(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN),
            () -> System.out.println("Open history"));
        registerShortcut(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN),
            () -> System.out.println("Print"));
            
        // Edit operations
        registerShortcut(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN),
            () -> System.out.println("Copy"));
        registerShortcut(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN),
            () -> System.out.println("Paste"));
        registerShortcut(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN),
            () -> System.out.println("Undo"));
        registerShortcut(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN),
            () -> System.out.println("Redo"));
            
        // View operations
        registerShortcut(new KeyCodeCombination(KeyCode.T, KeyCombination.ALT_DOWN),
            () -> System.out.println("Toggle theme"));
        registerShortcut(new KeyCodeCombination(KeyCode.H, KeyCombination.ALT_DOWN),
            () -> System.out.println("Toggle history panel"));
        registerShortcut(new KeyCodeCombination(KeyCode.M, KeyCombination.ALT_DOWN),
            () -> System.out.println("Toggle memory panel"));
            
        // Help
        registerShortcut(new KeyCodeCombination(KeyCode.F1),
            () -> System.out.println("Show help"));
    }
    
    /**
     * Registers a keyboard shortcut.
     */
    public void registerShortcut(KeyCombination combination, Runnable action) {
        shortcuts.put(combination, action);
    }
    
    /**
     * Maps a numpad key to a button.
     */
    public void mapNumpadKey(KeyCode key, Button button) {
        numpadMap.put(key, button);
    }
    
    /**
     * Maps an operator key to a button.
     */
    public void mapOperatorKey(KeyCode key, Button button) {
        operatorMap.put(key, button);
    }
    
    /**
     * Maps a function key to a button.
     */
    public void mapFunctionKey(KeyCode key, Button button) {
        functionMap.put(key, button);
    }
    
    /**
     * Sets the input handler for keyboard input.
     */
    public void setInputHandler(Consumer<String> handler) {
        this.inputHandler = handler;
    }
    
    /**
     * Handles key press events.
     */
    private void handleKeyPress(KeyEvent event) {
        // Check for shortcuts first
        for (Map.Entry<KeyCombination, Runnable> entry : shortcuts.entrySet()) {
            if (entry.getKey().match(event)) {
                entry.getValue().run();
                event.consume();
                return;
            }
        }
        
        // Handle numpad input
        if (numpadMap.containsKey(event.getCode())) {
            Button button = numpadMap.get(event.getCode());
            button.fire();
            event.consume();
            return;
        }
        
        // Handle operator input
        if (operatorMap.containsKey(event.getCode())) {
            Button button = operatorMap.get(event.getCode());
            button.fire();
            event.consume();
            return;
        }
        
        // Handle function input
        if (functionMap.containsKey(event.getCode())) {
            Button button = functionMap.get(event.getCode());
            button.fire();
            event.consume();
            return;
        }
        
        // Handle direct number input
        if (event.getCode().isDigitKey() && inputHandler != null) {
            inputHandler.accept(event.getText());
            event.consume();
            return;
        }
        
        // Handle special keys
        handleSpecialKeys(event);
    }
    
    /**
     * Handles special keyboard keys.
     */
    private void handleSpecialKeys(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER:
                if (event.isShiftDown()) {
                    // Handle Shift+Enter if needed
                    break;
                }
                // Handle regular Enter
                if (operatorMap.containsKey(KeyCode.ENTER)) {
                    operatorMap.get(KeyCode.ENTER).fire();
                }
                break;
                
            case BACK_SPACE:
                // Handle backspace
                if (functionMap.containsKey(KeyCode.BACK_SPACE)) {
                    functionMap.get(KeyCode.BACK_SPACE).fire();
                }
                break;
                
            case ESCAPE:
                // Handle escape (clear)
                if (functionMap.containsKey(KeyCode.ESCAPE)) {
                    functionMap.get(KeyCode.ESCAPE).fire();
                }
                break;
                
            case DECIMAL:
            case PERIOD:
                // Handle decimal point
                if (inputHandler != null) {
                    inputHandler.accept(".");
                }
                break;
                
            default:
                // Handle other special keys if needed
                break;
        }
        event.consume();
    }
    
    /**
     * Sets up default key mappings for the calculator.
     */
    public void setupDefaultMappings() {
        // Numpad mappings
        for (int i = 0; i <= 9; i++) {
            final String number = String.valueOf(i);
            registerShortcut(new KeyCodeCombination(KeyCode.valueOf("NUMPAD" + i)),
                () -> { if (inputHandler != null) inputHandler.accept(number); });
        }
        
        // Operator mappings
        registerShortcut(new KeyCodeCombination(KeyCode.PLUS),
            () -> { if (inputHandler != null) inputHandler.accept("+"); });
        registerShortcut(new KeyCodeCombination(KeyCode.MINUS),
            () -> { if (inputHandler != null) inputHandler.accept("-"); });
        registerShortcut(new KeyCodeCombination(KeyCode.MULTIPLY),
            () -> { if (inputHandler != null) inputHandler.accept("*"); });
        registerShortcut(new KeyCodeCombination(KeyCode.DIVIDE),
            () -> { if (inputHandler != null) inputHandler.accept("/"); });
        
        // Function mappings
        registerShortcut(new KeyCodeCombination(KeyCode.P, KeyCombination.ALT_DOWN),
            () -> { if (inputHandler != null) inputHandler.accept("π"); });
        registerShortcut(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN),
            () -> { if (inputHandler != null) inputHandler.accept("e"); });
        registerShortcut(new KeyCodeCombination(KeyCode.R, KeyCombination.ALT_DOWN),
            () -> { if (inputHandler != null) inputHandler.accept("√"); });
    }
    
    /**
     * Updates the scene reference.
     */
    public void updateScene(Scene newScene) {
        if (scene != null) {
            scene.removeEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        }
        scene = newScene;
        scene.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }
    
    /**
     * Cleans up resources.
     */
    public void cleanup() {
        if (scene != null) {
            scene.removeEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        }
        shortcuts.clear();
        numpadMap.clear();
        operatorMap.clear();
        functionMap.clear();
        inputHandler = null;
        scene = null;
    }
} 