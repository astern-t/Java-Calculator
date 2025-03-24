package com.calculator;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 * Controller class for the calculator application.
 * Handles user input, calculations, and UI updates.
 */
public class CalculatorController {
    private CalculatorModel model;
    private TextField display;
    private Label historyLabel;
    private VBox historyContainer;
    private ScrollPane historyScroll;
    private boolean isDarkMode = false;

    public CalculatorController() {
        this.model = new CalculatorModel();
    }

    /**
     * Initializes the UI components
     * @param display The display TextField
     * @param historyLabel The history Label
     * @param historyContainer The history VBox
     * @param historyScroll The history ScrollPane
     */
    public void initialize(TextField display, Label historyLabel, VBox historyContainer, ScrollPane historyScroll) {
        this.display = display;
        this.historyLabel = historyLabel;
        this.historyContainer = historyContainer;
        this.historyScroll = historyScroll;
        
        // Make display uneditable but focusable for keyboard input
        display.setEditable(false);
        display.setFocusTraversable(true);
    }

    /**
     * Handles numeric button clicks
     * @param number The digit pressed
     */
    public void handleNumber(String number) {
        if (model.isError()) {
            handleClear();
        }
        model.appendNumber(number);
        updateDisplay();
    }

    /**
     * Handles operator button clicks
     * @param operator The operator pressed
     */
    public void handleOperator(String operator) {
        if (!model.isError()) {
            model.setOperator(operator);
            updateDisplay();
            updateHistory();
        }
    }

    /**
     * Handles equals button click
     */
    public void handleEquals() {
        if (!model.isError()) {
            model.calculateResult();
            updateDisplay();
            updateHistory();
            addToHistoryContainer(model.getLastCalculation());
        }
    }

    /**
     * Handles scientific function button clicks
     * @param function The function to apply
     */
    public void handleFunction(String function) {
        if (!model.isError()) {
            switch (function) {
                // Existing scientific functions
                case "sin", "cos", "tan", "log", "ln", "√", "x²", "x³", "xʸ", "1/x", "!", "e" -> {
                    model.applyFunction(function);
                    updateDisplay();
                    updateHistory();
                    addToHistoryContainer(model.getLastCalculation());
                }
                
                // Financial functions
                case "PMT" -> handleLoanPayment();
                case "LOAN" -> handleLoanAmount();
                case "TERM" -> handleLoanTerm();
                case "FV" -> handleFutureValue();
                case "PV" -> handlePresentValue();
                case "ROI" -> handleReturnOnInvestment();
                case "MORT" -> handleMortgage();
                case "AMORT" -> handleAmortization();
                case "DOWN" -> handleDownPayment();
                case "BOND" -> handleBondCalculation();
                case "P/E" -> handlePriceToEarnings();
                case "D/E" -> handleDebtToEquity();
            }
        }
    }

    /**
     * Handles memory operation button clicks
     * @param operation The memory operation
     */
    public void handleMemory(String operation) {
        switch (operation) {
            case "MC" -> model.memoryClear();
            case "MR" -> {
                model.memoryRecall();
                updateDisplay();
            }
            case "M+" -> model.memoryAdd();
            case "M-" -> model.memorySubtract();
        }
    }

    /**
     * Handles clear button click
     */
    public void handleClear() {
        model.clear();
        updateDisplay();
        updateHistory();
    }

    /**
     * Handles erase button click
     */
    public void handleErase() {
        if (!model.isError()) {
            model.erase();
            updateDisplay();
        }
    }

    /**
     * Handles keyboard input
     * @param event The key event
     */
    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            handleClear();
        } else if (event.getCode() == KeyCode.ENTER) {
            handleEquals();
        } else if (event.getCode() == KeyCode.BACK_SPACE) {
            handleErase();
        } else if (event.getCode() == KeyCode.T && event.isAltDown()) {
            toggleTheme();
        } else {
            String key = event.getText();
            if (key.matches("[0-9.]")) {
                handleNumber(key);
            } else {
                switch (key) {
                    case "+" -> handleOperator("+");
                    case "-" -> handleOperator("-");
                    case "*" -> handleOperator("×");
                    case "/" -> handleOperator("÷");
                    case "%" -> handleOperator("%");
                }
            }
        }
    }

    /**
     * Updates the display with the current value
     */
    private void updateDisplay() {
        if (model.isError()) {
            display.setText(model.getErrorMessage());
            display.setStyle("-fx-text-fill: #ff4444;");
        } else {
            display.setText(model.getCurrentDisplay());
            display.setStyle("");
        }
    }

    /**
     * Updates the history display
     */
    private void updateHistory() {
        historyLabel.setText(model.getHistoryText());
    }

    /**
     * Adds a calculation to the history container
     * @param calculation The calculation to add
     */
    private void addToHistoryContainer(String calculation) {
        Label historyItem = new Label(calculation);
        historyItem.getStyleClass().add("history-item");
        historyContainer.getChildren().add(0, historyItem);
        
        // Limit history items to prevent memory issues
        if (historyContainer.getChildren().size() > 10) {
            historyContainer.getChildren().remove(10);
        }
        
        // Scroll to top
        historyScroll.setVvalue(0);
    }

    /**
     * Adds tooltips to buttons
     * @param button The button to add tooltip to
     * @param text The tooltip text
     */
    public void addTooltip(Button button, String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(javafx.util.Duration.millis(500));
        button.setTooltip(tooltip);
    }

    /**
     * Toggles between light and dark mode
     */
    public void toggleTheme() {
        isDarkMode = !isDarkMode;
    }

    /**
     * Returns the current theme state
     * @return true if dark mode is active
     */
    public boolean isDarkMode() {
        return isDarkMode;
    }

    // Financial calculator handlers
    private void handleLoanPayment() {
        try {
            double principal = Double.parseDouble(model.getCurrentDisplay());
            // Show dialog to get rate and term
            // For now, using sample values
            double annualRate = 5.0; // 5% annual rate
            int years = 5;
            
            double payment = model.calculateLoanPayment(principal, annualRate, years);
            model.setResult(payment);
            updateDisplay();
            updateHistory();
            addToHistoryContainer(String.format("PMT(%.2f, %.1f%%, %d years) = %.2f", 
                principal, annualRate, years, payment));
        } catch (NumberFormatException e) {
            model.setError("Invalid input for loan payment calculation");
            updateDisplay();
        }
    }

    private void handleLoanAmount() {
        try {
            double payment = Double.parseDouble(model.getCurrentDisplay());
            // Show dialog to get rate and term
            double annualRate = 5.0;
            int years = 5;
            
            double loanAmount = model.calculateLoanAmount(payment, annualRate, years);
            model.setResult(loanAmount);
            updateDisplay();
            updateHistory();
            addToHistoryContainer(String.format("LOAN(%.2f, %.1f%%, %d years) = %.2f", 
                payment, annualRate, years, loanAmount));
        } catch (NumberFormatException e) {
            model.setError("Invalid input for loan amount calculation");
            updateDisplay();
        }
    }

    private void handleLoanTerm() {
        try {
            double payment = Double.parseDouble(model.getCurrentDisplay());
            // Show dialog to get principal and rate
            double principal = 100000.0;
            double annualRate = 5.0;
            
            int term = model.calculateLoanTerm(principal, payment, annualRate);
            model.setResult(term);
            updateDisplay();
            updateHistory();
            addToHistoryContainer(String.format("TERM(%.2f, %.2f, %.1f%%) = %d years", 
                principal, payment, annualRate, term));
        } catch (NumberFormatException e) {
            model.setError("Invalid input for loan term calculation");
            updateDisplay();
        }
    }

    private void handleFutureValue() {
        try {
            double presentValue = Double.parseDouble(model.getCurrentDisplay());
            // Show dialog to get rate and term
            double annualRate = 5.0;
            int years = 5;
            
            double futureValue = model.calculateFutureValue(presentValue, annualRate, years);
            model.setResult(futureValue);
            updateDisplay();
            updateHistory();
            addToHistoryContainer(String.format("FV(%.2f, %.1f%%, %d years) = %.2f", 
                presentValue, annualRate, years, futureValue));
        } catch (NumberFormatException e) {
            model.setError("Invalid input for future value calculation");
            updateDisplay();
        }
    }

    private void handlePresentValue() {
        try {
            double futureValue = Double.parseDouble(model.getCurrentDisplay());
            // Show dialog to get rate and term
            double annualRate = 5.0;
            int years = 5;
            
            double presentValue = model.calculatePresentValue(futureValue, annualRate, years);
            model.setResult(presentValue);
            updateDisplay();
            updateHistory();
            addToHistoryContainer(String.format("PV(%.2f, %.1f%%, %d years) = %.2f", 
                futureValue, annualRate, years, presentValue));
        } catch (NumberFormatException e) {
            model.setError("Invalid input for present value calculation");
            updateDisplay();
        }
    }

    private void handleReturnOnInvestment() {
        try {
            double initialInvestment = Double.parseDouble(model.getCurrentDisplay());
            // Show dialog to get final value
            double finalValue = initialInvestment * 1.5; // Sample 50% return
            
            double roi = model.calculateROI(initialInvestment, finalValue);
            model.setResult(roi);
            updateDisplay();
            updateHistory();
            addToHistoryContainer(String.format("ROI(%.2f, %.2f) = %.1f%%", 
                initialInvestment, finalValue, roi));
        } catch (NumberFormatException e) {
            model.setError("Invalid input for ROI calculation");
            updateDisplay();
        }
    }

    private void handleMortgage() {
        try {
            double principal = Double.parseDouble(model.getCurrentDisplay());
            // Show dialog to get rate, term, and down payment
            double annualRate = 4.5;
            int years = 30;
            double downPayment = principal * 0.2; // 20% down
            
            model.calculateMortgage(principal, annualRate, years, downPayment);
            updateDisplay();
            updateHistory();
            addToHistoryContainer(String.format("Mortgage calculation for $%.2f", principal));
        } catch (NumberFormatException e) {
            model.setError("Invalid input for mortgage calculation");
            updateDisplay();
        }
    }

    private void handleAmortization() {
        try {
            double principal = Double.parseDouble(model.getCurrentDisplay());
            // Show amortization schedule in a new window
            model.showAmortizationSchedule(principal);
        } catch (NumberFormatException e) {
            model.setError("Invalid input for amortization schedule");
            updateDisplay();
        }
    }

    private void handleDownPayment() {
        try {
            double homePrice = Double.parseDouble(model.getCurrentDisplay());
            // Show dialog to get down payment percentage
            double percentage = 20.0; // 20% down payment
            
            double downPayment = homePrice * (percentage / 100);
            model.setResult(downPayment);
            updateDisplay();
            updateHistory();
            addToHistoryContainer(String.format("Down payment (%.1f%%) on $%.2f = $%.2f", 
                percentage, homePrice, downPayment));
        } catch (NumberFormatException e) {
            model.setError("Invalid input for down payment calculation");
            updateDisplay();
        }
    }

    private void handleBondCalculation() {
        try {
            double faceValue = Double.parseDouble(model.getCurrentDisplay());
            // Show dialog to get coupon rate, market rate, and term
            double couponRate = 5.0;
            double marketRate = 6.0;
            int years = 10;
            
            model.calculateBond(faceValue, couponRate, marketRate, years);
            updateDisplay();
            updateHistory();
            addToHistoryContainer(String.format("Bond calculation for $%.2f", faceValue));
        } catch (NumberFormatException e) {
            model.setError("Invalid input for bond calculation");
            updateDisplay();
        }
    }

    private void handlePriceToEarnings() {
        try {
            double stockPrice = Double.parseDouble(model.getCurrentDisplay());
            // Show dialog to get earnings per share
            double eps = stockPrice / 15; // Sample P/E ratio of 15
            
            double peRatio = model.calculatePERatio(stockPrice, eps);
            model.setResult(peRatio);
            updateDisplay();
            updateHistory();
            addToHistoryContainer(String.format("P/E(%.2f, %.2f) = %.2f", 
                stockPrice, eps, peRatio));
        } catch (NumberFormatException e) {
            model.setError("Invalid input for P/E ratio calculation");
            updateDisplay();
        }
    }

    private void handleDebtToEquity() {
        try {
            double totalDebt = Double.parseDouble(model.getCurrentDisplay());
            // Show dialog to get total equity
            double totalEquity = totalDebt * 2; // Sample D/E ratio of 0.5
            
            double deRatio = model.calculateDERatio(totalDebt, totalEquity);
            model.setResult(deRatio);
            updateDisplay();
            updateHistory();
            addToHistoryContainer(String.format("D/E(%.2f, %.2f) = %.2f", 
                totalDebt, totalEquity, deRatio));
        } catch (NumberFormatException e) {
            model.setError("Invalid input for D/E ratio calculation");
            updateDisplay();
        }
    }
} 