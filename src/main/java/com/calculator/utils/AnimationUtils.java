package com.calculator.utils;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Utility class for handling animations and visual feedback in the calculator.
 */
public class AnimationUtils {
    private static final Duration DEFAULT_DURATION = Duration.millis(150);
    private static final double BUTTON_PRESS_SCALE = 0.95;
    private static final double BUTTON_HOVER_SCALE = 1.05;
    
    // Button press animation
    public static void playButtonPressAnimation(Button button) {
        ScaleTransition scaleDown = new ScaleTransition(DEFAULT_DURATION, button);
        scaleDown.setToX(BUTTON_PRESS_SCALE);
        scaleDown.setToY(BUTTON_PRESS_SCALE);
        
        ScaleTransition scaleUp = new ScaleTransition(DEFAULT_DURATION, button);
        scaleUp.setToX(1.0);
        scaleUp.setToY(1.0);
        
        SequentialTransition sequence = new SequentialTransition(scaleDown, scaleUp);
        sequence.play();
        
        // Add glow effect
        Glow glow = new Glow(0.5);
        button.setEffect(glow);
        
        // Remove glow after animation
        sequence.setOnFinished(event -> button.setEffect(null));
    }
    
    // Button hover animation
    public static void setupButtonHoverAnimation(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(DEFAULT_DURATION, button);
        scaleUp.setToX(BUTTON_HOVER_SCALE);
        scaleUp.setToY(BUTTON_HOVER_SCALE);
        
        ScaleTransition scaleDown = new ScaleTransition(DEFAULT_DURATION, button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        
        button.setOnMouseEntered(e -> scaleUp.play());
        button.setOnMouseExited(e -> scaleDown.play());
    }
    
    // Error shake animation
    public static void playErrorShakeAnimation(Node node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), node);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        
        // Add red glow effect
        Glow errorGlow = new Glow(0.8);
        node.setEffect(errorGlow);
        
        shake.setOnFinished(event -> {
            node.setEffect(null);
            node.setTranslateX(0);
        });
        
        shake.play();
    }
    
    // Success animation
    public static void playSuccessAnimation(Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(200), node);
        fade.setFromValue(1.0);
        fade.setToValue(0.5);
        fade.setCycleCount(2);
        fade.setAutoReverse(true);
        
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), node);
        scale.setToX(1.1);
        scale.setToY(1.1);
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        
        // Add green glow effect
        DropShadow glow = new DropShadow();
        glow.setColor(Color.GREEN);
        glow.setRadius(20);
        node.setEffect(glow);
        
        ParallelTransition parallel = new ParallelTransition(fade, scale);
        parallel.setOnFinished(event -> {
            node.setEffect(null);
            node.setScaleX(1.0);
            node.setScaleY(1.0);
        });
        
        parallel.play();
    }
    
    // Number input animation
    public static void playNumberInputAnimation(Node display) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(100), display);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.02);
        scale.setToY(1.02);
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        scale.play();
    }
    
    // Clear animation
    public static void playClearAnimation(Node display) {
        FadeTransition fade = new FadeTransition(Duration.millis(200), display);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(event -> {
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();
        });
        fade.play();
    }
    
    // Result animation
    public static void playResultAnimation(Node display) {
        // Scale up
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), display);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);
        
        // Glow effect
        Glow glow = new Glow(0.5);
        display.setEffect(glow);
        
        // Scale back down
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), display);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        
        SequentialTransition sequence = new SequentialTransition(scaleUp, scaleDown);
        sequence.setOnFinished(event -> display.setEffect(null));
        sequence.play();
    }
    
    // Memory operation animation
    public static void playMemoryOperationAnimation(Node memoryIndicator) {
        FadeTransition fade = new FadeTransition(Duration.millis(200), memoryIndicator);
        fade.setFromValue(0.3);
        fade.setToValue(1.0);
        fade.setCycleCount(2);
        fade.setAutoReverse(true);
        
        // Add blue glow effect
        Glow glow = new Glow(0.5);
        memoryIndicator.setEffect(glow);
        
        fade.setOnFinished(event -> memoryIndicator.setEffect(null));
        fade.play();
    }
    
    // Theme toggle animation
    public static void playThemeToggleAnimation(Node root) {
        FadeTransition fade = new FadeTransition(Duration.millis(300), root);
        fade.setFromValue(1.0);
        fade.setToValue(0.7);
        fade.setCycleCount(2);
        fade.setAutoReverse(true);
        fade.play();
    }
    
    // Sound effects
    private static final String BUTTON_CLICK_SOUND = "button-click.wav";
    private static final String ERROR_SOUND = "error.wav";
    private static final String SUCCESS_SOUND = "success.wav";
    
    public static void playButtonClickSound() {
        // Sound effects temporarily disabled until proper audio implementation
        System.out.println("Button click sound");
    }
    
    public static void playErrorSound() {
        // Sound effects temporarily disabled until proper audio implementation
        System.out.println("Error sound");
    }
    
    public static void playSuccessSound() {
        // Sound effects temporarily disabled until proper audio implementation
        System.out.println("Success sound");
    }
    
    // Ripple effect for buttons
    public static void addRippleEffect(Button button) {
        if (!(button.getParent() instanceof StackPane)) {
            StackPane wrapper = new StackPane(button);
            if (button.getParent() != null) {
                Parent parent = button.getParent();
                int index = ((Parent) parent).getChildrenUnmodifiable().indexOf(button);
                // Handle parent replacement here if needed
            }
        }
        
        button.setStyle(button.getStyle() + "; -fx-background-radius: 5;");
        
        button.setOnMousePressed(event -> {
            double x = event.getX();
            double y = event.getY();
            
            Circle ripple = new Circle(x, y, 0, Color.WHITE);
            ripple.setOpacity(0.5);
            
            if (button.getParent() instanceof StackPane) {
                StackPane parent = (StackPane) button.getParent();
                parent.getChildren().add(ripple);
                
                Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(ripple.radiusProperty(), 0)),
                    new KeyFrame(Duration.millis(500), new KeyValue(ripple.radiusProperty(), 100)),
                    new KeyFrame(Duration.ZERO, new KeyValue(ripple.opacityProperty(), 0.5)),
                    new KeyFrame(Duration.millis(500), new KeyValue(ripple.opacityProperty(), 0))
                );
                
                timeline.setOnFinished(e -> parent.getChildren().remove(ripple));
                timeline.play();
            }
        });
    }
    
    // Tooltip animation
    public static void setupTooltipAnimation(Node node, String tooltipText) {
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setShowDelay(Duration.millis(200));
        tooltip.setHideDelay(Duration.millis(200));
        Tooltip.install(node, tooltip);
        
        if (tooltip.getGraphic() != null) {
            node.setOnMouseEntered(event -> {
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), tooltip.getGraphic());
                scale.setToX(1.2);
                scale.setToY(1.2);
                scale.play();
            });
            
            node.setOnMouseExited(event -> {
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), tooltip.getGraphic());
                scale.setToX(1.0);
                scale.setToY(1.0);
                scale.play();
            });
        }
    }
} 