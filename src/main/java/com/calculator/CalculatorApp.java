package com.calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.util.Duration;

/**
 * Main application class for the calculator
 */
public class CalculatorApp extends Application {
    private CalculatorController controller;
    private VBox root;
    private TextField display;
    private Label historyLabel;
    private VBox historyContainer;
    private ScrollPane historyScroll;

    @Override
    public void start(Stage primaryStage) {
        controller = new CalculatorController();
        
        // Root container with animation
        root = new VBox(10);
        root.getStyleClass().add("root");
        root.setPadding(new Insets(10));
        FadeTransition rootFade = new FadeTransition(Duration.seconds(0.5), root);
        rootFade.setFromValue(0);
        rootFade.setToValue(1);
        rootFade.play();
        
        // Main container with responsive layout and animation
        HBox mainContainer = new HBox();
        mainContainer.getStyleClass().add("main-container");
        VBox.setVgrow(mainContainer, Priority.ALWAYS);
        HBox.setHgrow(mainContainer, Priority.ALWAYS);
        
        ScaleTransition containerScale = new ScaleTransition(Duration.seconds(0.5), mainContainer);
        containerScale.setFromX(0.95);
        containerScale.setFromY(0.95);
        containerScale.setToX(1);
        containerScale.setToY(1);
        containerScale.play();
        
        // Left side with display and number pad
        VBox leftSide = new VBox(10);
        leftSide.getStyleClass().add("left-side");
        
        // Display area with history
        VBox displayArea = createDisplayArea();
        FadeTransition displayFade = new FadeTransition(Duration.seconds(0.5), displayArea);
        displayFade.setFromValue(0);
        displayFade.setToValue(1);
        displayFade.setDelay(Duration.seconds(0.2));
        displayFade.play();
        
        // Memory buttons with animation
        HBox memoryButtons = createMemoryButtons();
        memoryButtons.setAlignment(Pos.CENTER);
        FadeTransition memoryFade = new FadeTransition(Duration.seconds(0.5), memoryButtons);
        memoryFade.setFromValue(0);
        memoryFade.setToValue(1);
        memoryFade.setDelay(Duration.seconds(0.3));
        memoryFade.play();
        
        // Button grid with animation
        GridPane buttonGrid = createButtonGrid();
        FadeTransition gridFade = new FadeTransition(Duration.seconds(0.5), buttonGrid);
        gridFade.setFromValue(0);
        gridFade.setToValue(1);
        gridFade.setDelay(Duration.seconds(0.4));
        gridFade.play();
        
        // Theme toggle button with animation
        Button themeButton = new Button("Dark Mode");
        themeButton.getStyleClass().add("theme-button");
        themeButton.setOnAction(e -> toggleTheme());
        themeButton.setMaxWidth(Double.MAX_VALUE);
        controller.addTooltip(themeButton, "Toggle between light and dark mode (Alt+T)");
        FadeTransition themeFade = new FadeTransition(Duration.seconds(0.5), themeButton);
        themeFade.setFromValue(0);
        themeFade.setToValue(1);
        themeFade.setDelay(Duration.seconds(0.5));
        themeFade.play();
        
        leftSide.getChildren().addAll(displayArea, memoryButtons, buttonGrid, themeButton);
        
        // Right side with advanced functions
        ScrollPane rightScroll = new ScrollPane();
        rightScroll.setFitToWidth(true);
        rightScroll.getStyleClass().add("calculator-scroll");
        HBox.setHgrow(rightScroll, Priority.ALWAYS);
        
        VBox rightSide = new VBox(15);
        rightSide.getStyleClass().add("right-side");
        rightSide.setAlignment(Pos.TOP_CENTER);
        
        // Add calculator sections with sequential animations
        VBox[] sections = {
            createScientificSection(),
            createFinancialSection(),
            createProgrammerSection(),
            createConverterSection()
        };
        
        for (int i = 0; i < sections.length; i++) {
            VBox section = sections[i];
            rightSide.getChildren().add(section);
            
            FadeTransition sectionFade = new FadeTransition(Duration.seconds(0.5), section);
            sectionFade.setFromValue(0);
            sectionFade.setToValue(1);
            sectionFade.setDelay(Duration.seconds(0.1 * (i + 1)));
            sectionFade.play();
            
            TranslateTransition sectionSlide = new TranslateTransition(Duration.seconds(0.5), section);
            sectionSlide.setFromY(20);
            sectionSlide.setToY(0);
            sectionSlide.setDelay(Duration.seconds(0.1 * (i + 1)));
            sectionSlide.play();
        }
        
        rightScroll.setContent(rightSide);
        
        mainContainer.getChildren().addAll(leftSide, rightScroll);
        root.getChildren().add(mainContainer);
        
        // Create scene with modern styling
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        // Add keyboard support
        scene.setOnKeyPressed(e -> controller.handleKeyPressed(e));
        
        // Configure stage
        primaryStage.setTitle("Advanced Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Initialize controller with UI components
        controller.initialize(display, historyLabel, historyContainer, historyScroll);
    }

    private VBox createDisplayArea() {
        VBox displayArea = new VBox();
        displayArea.getStyleClass().add("display-area");

        // History scroll pane
        historyContainer = new VBox();
        historyContainer.getStyleClass().add("history-container");
        
        historyScroll = new ScrollPane(historyContainer);
        historyScroll.getStyleClass().add("history-scroll");
        historyScroll.setFitToWidth(true);
        historyScroll.setPrefHeight(100);
        
        // Current calculation history
        historyLabel = new Label("");
        historyLabel.getStyleClass().add("history-label");

        // Main display
        display = new TextField("0");
        display.setEditable(false);
        display.getStyleClass().add("display");

        displayArea.getChildren().addAll(historyScroll, historyLabel, display);
        return displayArea;
    }

    private HBox createMemoryButtons() {
        HBox memoryBox = new HBox(10);
        memoryBox.getStyleClass().add("memory-container");
        memoryBox.setAlignment(Pos.CENTER);

        String[] memoryOps = {"MC", "MR", "M+", "M-"};
        for (String op : memoryOps) {
            Button btn = new Button(op);
            btn.getStyleClass().addAll("calculator-button", "memory-button");
            btn.setOnAction(e -> controller.handleMemory(op));
            controller.addTooltip(btn, getMemoryTooltip(op));
            memoryBox.getChildren().add(btn);
        }

        return memoryBox;
    }

    private GridPane createButtonGrid() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("button-grid");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(8);
        grid.setVgap(8);

        // Make columns responsive
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            column.setFillWidth(true);
            column.setPercentWidth(25);
            grid.getColumnConstraints().add(column);
        }

        // Make rows responsive
        for (int i = 0; i < 5; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            row.setFillHeight(true);
            grid.getRowConstraints().add(row);
        }

        String[][] buttonLabels = {
            {"C", "⌫", "%", "÷"},
            {"7", "8", "9", "×"},
            {"4", "5", "6", "-"},
            {"1", "2", "3", "+"},
            {"0", ".", "="}
        };

        for (int i = 0; i < buttonLabels.length; i++) {
            for (int j = 0; j < buttonLabels[i].length; j++) {
                Button button = createButton(buttonLabels[i][j]);
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMaxHeight(Double.MAX_VALUE);
                
                if (i == 4) { // Last row
                    if (j == 0) { // "0" button
                        button.getStyleClass().add("zero-button");
                        grid.add(button, j, i, 2, 1); // Span 2 columns
                    } else if (j == 1) { // "." button
                        button.getStyleClass().add("decimal-button");
                        grid.add(button, 2, i); // Place in third column
                    } else if (j == 2) { // "=" button
                        button.getStyleClass().add("equals-button");
                        grid.add(button, 3, i); // Place in fourth column
                    }
                } else {
                    grid.add(button, j, i);
                }
            }
        }

        return grid;
    }

    private VBox createScientificSection() {
        VBox scientificArea = new VBox(10);

        Label scientificTitle = new Label("Scientific Calculator");
        scientificTitle.getStyleClass().addAll("section-title", "scientific-title");
        
        Region separator = new Region();
        separator.getStyleClass().add("separator");

        // Basic trigonometry
        VBox trigGroup = createFunctionGroup("Trigonometry", 
            new String[]{"sin", "cos", "tan", "sec", "csc", "cot"});
        
        // Inverse trigonometry
        VBox invTrigGroup = createFunctionGroup("Inverse Trigonometry", 
            new String[]{"asin", "acos", "atan", "asec", "acsc", "acot"});
        
        // Hyperbolic functions
        VBox hypGroup = createFunctionGroup("Hyperbolic", 
            new String[]{"sinh", "cosh", "tanh", "sech", "csch", "coth"});
        
        // Logarithms and exponentials
        VBox logGroup = createFunctionGroup("Logarithms", 
            new String[]{"log", "ln", "log₂", "eˣ", "10ˣ", "2ˣ"});
        
        // Powers and roots
        VBox powerGroup = createFunctionGroup("Powers & Roots", 
            new String[]{"x²", "x³", "xʸ", "√", "∛", "ʸ√x"});
        
        // Special functions
        VBox specialGroup = createFunctionGroup("Special Functions", 
            new String[]{"abs", "ceil", "floor", "rand", "fact", "mod"});

        scientificArea.getChildren().addAll(
            scientificTitle,
            separator,
            trigGroup,
            createGroupSeparator(),
            invTrigGroup,
            createGroupSeparator(),
            hypGroup,
            createGroupSeparator(),
            logGroup,
            createGroupSeparator(),
            powerGroup,
            createGroupSeparator(),
            specialGroup
        );

        return scientificArea;
    }

    private VBox createFinancialSection() {
        VBox financialArea = new VBox(10);

        Label financialTitle = new Label("Financial Calculator");
        financialTitle.getStyleClass().addAll("section-title", "financial-title");
        
        Region separator = new Region();
        separator.getStyleClass().add("separator");

        // Loan calculations
        VBox loanGroup = createFunctionGroup("Loan Calculator", 
            new String[]{"PMT", "LOAN", "TERM", "RATE", "NPER", "IPMT"});
        
        // Investment calculations
        VBox investmentGroup = createFunctionGroup("Investment", 
            new String[]{"FV", "PV", "ROI", "IRR", "NPV", "MIRR"});
        
        // Mortgage calculations
        VBox mortgageGroup = createFunctionGroup("Mortgage", 
            new String[]{"MORT", "AMORT", "DOWN", "PRIN", "EQUITY", "LTV"});
        
        // Bond calculations
        VBox bondGroup = createFunctionGroup("Bonds", 
            new String[]{"PRICE", "YIELD", "ACCINT", "DISC", "DURATION", "MDURATION"});
        
        // Financial ratios
        VBox ratioGroup = createFunctionGroup("Financial Ratios", 
            new String[]{"P/E", "P/B", "D/E", "ROE", "ROA", "CR"});

        financialArea.getChildren().addAll(
            financialTitle,
            separator,
            loanGroup,
            createGroupSeparator(),
            investmentGroup,
            createGroupSeparator(),
            mortgageGroup,
            createGroupSeparator(),
            bondGroup,
            createGroupSeparator(),
            ratioGroup
        );

        return financialArea;
    }

    private VBox createProgrammerSection() {
        VBox programmerArea = new VBox(10);

        Label programmerTitle = new Label("Programmer Calculator");
        programmerTitle.getStyleClass().addAll("section-title", "programmer-title");
        
        Region separator = new Region();
        separator.getStyleClass().add("separator");

        // Number systems
        VBox baseGroup = createFunctionGroup("Number Systems", 
            new String[]{"HEX", "DEC", "OCT", "BIN", "CHAR", "ASCII"});
        
        // Bitwise operations
        VBox bitwiseGroup = createFunctionGroup("Bitwise Operations", 
            new String[]{"AND", "OR", "XOR", "NOT", "NAND", "NOR"});
        
        // Bit shifts
        VBox shiftGroup = createFunctionGroup("Bit Shifts", 
            new String[]{"LSH", "RSH", "ROSH", "ROL", "ROR", "SAR"});
        
        // Data types
        VBox typeGroup = createFunctionGroup("Data Types", 
            new String[]{"BYTE", "WORD", "DWORD", "QWORD", "FLOAT", "DOUBLE"});
        
        // Logical operations
        VBox logicGroup = createFunctionGroup("Logic Operations", 
            new String[]{"BOOL", "XNOR", "IMP", "EQV", "TRUE", "FALSE"});

        programmerArea.getChildren().addAll(
            programmerTitle,
            separator,
            baseGroup,
            createGroupSeparator(),
            bitwiseGroup,
            createGroupSeparator(),
            shiftGroup,
            createGroupSeparator(),
            typeGroup,
            createGroupSeparator(),
            logicGroup
        );

        return programmerArea;
    }

    private VBox createConverterSection() {
        VBox converterArea = new VBox(10);

        Label converterTitle = new Label("Unit Converter");
        converterTitle.getStyleClass().addAll("section-title", "converter-title");
        
        Region separator = new Region();
        separator.getStyleClass().add("separator");

        // Length conversions
        VBox lengthGroup = createFunctionGroup("Length", 
            new String[]{"km", "m", "cm", "mile", "yard", "foot"});
        
        // Weight/Mass conversions
        VBox weightGroup = createFunctionGroup("Weight/Mass", 
            new String[]{"kg", "g", "mg", "lb", "oz", "ton"});
        
        // Temperature conversions
        VBox tempGroup = createFunctionGroup("Temperature", 
            new String[]{"°C", "°F", "K", "°R", "°De", "°N"});
        
        // Area conversions
        VBox areaGroup = createFunctionGroup("Area", 
            new String[]{"km²", "m²", "cm²", "mi²", "yd²", "ft²"});
        
        // Volume conversions
        VBox volumeGroup = createFunctionGroup("Volume", 
            new String[]{"m³", "L", "mL", "gal", "qt", "pt"});
        
        // Time conversions
        VBox timeGroup = createFunctionGroup("Time", 
            new String[]{"yr", "mo", "wk", "day", "hr", "min"});
        
        // Speed conversions
        VBox speedGroup = createFunctionGroup("Speed", 
            new String[]{"m/s", "km/h", "mph", "knot", "mach", "c"});
        
        // Energy conversions
        VBox energyGroup = createFunctionGroup("Energy", 
            new String[]{"J", "cal", "kWh", "eV", "BTU", "erg"});

        converterArea.getChildren().addAll(
            converterTitle,
            separator,
            lengthGroup,
            createGroupSeparator(),
            weightGroup,
            createGroupSeparator(),
            tempGroup,
            createGroupSeparator(),
            areaGroup,
            createGroupSeparator(),
            volumeGroup,
            createGroupSeparator(),
            timeGroup,
            createGroupSeparator(),
            speedGroup,
            createGroupSeparator(),
            energyGroup
        );

        return converterArea;
    }

    private VBox createFunctionGroup(String groupName, String[] functions) {
        VBox group = new VBox(5);
        group.getStyleClass().add("function-group");
        group.setAlignment(Pos.CENTER);

        Label groupTitle = new Label(groupName);
        groupTitle.getStyleClass().add("group-title");

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(5);
        buttonGrid.setVgap(5);
        buttonGrid.getStyleClass().add("function-grid");
        buttonGrid.setAlignment(Pos.CENTER);

        // Make columns responsive
        for (int i = 0; i < functions.length; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            column.setFillWidth(true);
            column.setPercentWidth(100.0 / functions.length);
            buttonGrid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < functions.length; i++) {
            Button button = createButton(functions[i]);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setMaxHeight(Double.MAX_VALUE);
            buttonGrid.add(button, i, 0);
        }

        group.getChildren().addAll(groupTitle, buttonGrid);
        return group;
    }

    private Region createGroupSeparator() {
        Region separator = new Region();
        separator.getStyleClass().add("group-separator");
        return separator;
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("calculator-button");
        
        // Add hover effect
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        
        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });
        
        if (text.matches("[0-9.]")) {
            button.getStyleClass().add("number-button");
            button.setOnAction(e -> {
                playButtonClickAnimation(button);
                controller.handleNumber(text);
            });
            controller.addTooltip(button, "Enter " + text);
        } else if (text.matches("[+\\-×÷%]")) {
            button.getStyleClass().add("operator-button");
            button.setOnAction(e -> {
                playButtonClickAnimation(button);
                controller.handleOperator(text);
            });
            controller.addTooltip(button, getOperatorTooltip(text));
        } else if (text.equals("=")) {
            button.getStyleClass().add("equals-button");
            button.setOnAction(e -> {
                playButtonClickAnimation(button);
                controller.handleEquals();
            });
            controller.addTooltip(button, "Calculate result (Enter)");
        } else if (text.equals("C")) {
            button.getStyleClass().add("clear-button");
            button.setOnAction(e -> {
                playButtonClickAnimation(button);
                controller.handleClear();
            });
            controller.addTooltip(button, "Clear all (Esc)");
        } else if (text.equals("⌫")) {
            button.getStyleClass().add("erase-button");
            button.setOnAction(e -> {
                playButtonClickAnimation(button);
                controller.handleErase();
            });
            controller.addTooltip(button, "Erase last digit (Backspace)");
        } else {
            button.getStyleClass().add("function-button");
            button.setOnAction(e -> {
                playButtonClickAnimation(button);
                controller.handleFunction(text);
            });
            controller.addTooltip(button, getFunctionTooltip(text));
        }
        
        return button;
    }

    private void playButtonClickAnimation(Button button) {
        ParallelTransition pt = new ParallelTransition();
        
        ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
        st.setToX(0.95);
        st.setToY(0.95);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        
        FadeTransition ft = new FadeTransition(Duration.millis(100), button);
        ft.setFromValue(1.0);
        ft.setToValue(0.8);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        
        pt.getChildren().addAll(st, ft);
        pt.play();
    }

    private void toggleTheme() {
        controller.toggleTheme();
        if (controller.isDarkMode()) {
            root.getStyleClass().add("dark");
        } else {
            root.getStyleClass().remove("dark");
        }
    }

    private String getOperatorTooltip(String operator) {
        return switch (operator) {
            case "+" -> "Add (+ key)";
            case "-" -> "Subtract (- key)";
            case "×" -> "Multiply (* key)";
            case "÷" -> "Divide (/ key)";
            case "%" -> "Modulo";
            default -> operator;
        };
    }

    private String getFunctionTooltip(String function) {
        return switch (function) {
            case "sin" -> "Sine (degrees)";
            case "cos" -> "Cosine (degrees)";
            case "tan" -> "Tangent (degrees)";
            case "log" -> "Logarithm base 10";
            case "ln" -> "Natural logarithm";
            case "√" -> "Square root";
            case "x²" -> "Square";
            case "x³" -> "Cube";
            case "xʸ" -> "Power";
            case "1/x" -> "Reciprocal";
            case "!" -> "Factorial";
            case "e" -> "Euler's number";
            default -> function;
        };
    }

    private String getMemoryTooltip(String op) {
        return switch (op) {
            case "MC" -> "Memory Clear";
            case "MR" -> "Memory Recall";
            case "M+" -> "Memory Add";
            case "M-" -> "Memory Subtract";
            default -> op;
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}