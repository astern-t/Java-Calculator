package com.calculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for the calculator application.
 * Handles calculations, memory operations, and state management.
 */
public class CalculatorModel {
    private String currentNumber = "";
    private String operator = "";
    private double result = 0;
    private double memory = 0;
    private boolean startNewNumber = true;
    private String errorMessage = "";
    private List<String> history = new ArrayList<>();

    public void appendNumber(String number) {
        if (number.equals(".") && currentNumber.contains(".")) {
            return; // Prevent multiple decimal points
        }
        
        if (startNewNumber) {
            currentNumber = number;
            startNewNumber = false;
        } else {
            currentNumber += number;
        }
        errorMessage = "";
    }

    public void setOperator(String newOperator) {
        if (!currentNumber.isEmpty()) {
            if (!operator.isEmpty()) {
                calculateResult();
            } else {
                result = Double.parseDouble(currentNumber);
            }
            operator = newOperator;
            startNewNumber = true;
        }
        errorMessage = "";
    }

    public void calculateResult() {
        if (!currentNumber.isEmpty() && !operator.isEmpty()) {
            double secondNumber = Double.parseDouble(currentNumber);
            String calculation = result + " " + operator + " " + secondNumber;
            
            try {
                switch (operator) {
                    case "+" -> result += secondNumber;
                    case "-" -> result -= secondNumber;
                    case "×" -> result *= secondNumber;
                    case "÷" -> {
                        if (secondNumber == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        result /= secondNumber;
                    }
                    case "%" -> {
                        if (secondNumber == 0) {
                            throw new ArithmeticException("Modulo by zero");
                        }
                        result %= secondNumber;
                    }
                }
                calculation += " = " + formatNumber(result);
                history.add(calculation);
                currentNumber = String.valueOf(result);
                operator = "";
                startNewNumber = true;
            } catch (ArithmeticException e) {
                errorMessage = e.getMessage();
            }
        }
    }

    public void applyFunction(String function) {
        if (!currentNumber.isEmpty()) {
            double number = Double.parseDouble(currentNumber);
            String calculation = function + "(" + number + ")";
            
            try {
                switch (function) {
                    case "sin" -> result = Math.sin(Math.toRadians(number));
                    case "cos" -> result = Math.cos(Math.toRadians(number));
                    case "tan" -> result = Math.tan(Math.toRadians(number));
                    case "log" -> {
                        if (number <= 0) {
                            throw new ArithmeticException("Invalid input for logarithm");
                        }
                        result = Math.log10(number);
                    }
                    case "ln" -> {
                        if (number <= 0) {
                            throw new ArithmeticException("Invalid input for natural logarithm");
                        }
                        result = Math.log(number);
                    }
                    case "√" -> {
                        if (number < 0) {
                            throw new ArithmeticException("Invalid input for square root");
                        }
                        result = Math.sqrt(number);
                    }
                    case "x²" -> result = Math.pow(number, 2);
                    case "x³" -> result = Math.pow(number, 3);
                    case "xʸ" -> {
                        if (number == 0 && result == 0) {
                            throw new ArithmeticException("0^0 is undefined");
                        }
                        double base = result;
                        result = Math.pow(base, number);
                    }
                    case "1/x" -> {
                        if (number == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        result = 1 / number;
                    }
                    case "!" -> {
                        if (number < 0 || number != Math.floor(number)) {
                            throw new ArithmeticException("Invalid input for factorial");
                        }
                        result = factorial((int) number);
                    }
                    case "e" -> result = Math.E;
                }
                calculation += " = " + formatNumber(result);
                history.add(calculation);
                currentNumber = String.valueOf(result);
                operator = "";
                startNewNumber = true;
            } catch (ArithmeticException e) {
                errorMessage = e.getMessage();
            }
        }
    }

    private double factorial(int n) {
        if (n > 170) {
            throw new ArithmeticException("Number too large for factorial");
        }
        double result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public void clear() {
        currentNumber = "";
        operator = "";
        result = 0;
        startNewNumber = true;
        errorMessage = "";
    }

    public void erase() {
        if (!currentNumber.isEmpty() && !startNewNumber) {
            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
            if (currentNumber.isEmpty()) {
                currentNumber = "0";
                startNewNumber = true;
            }
        }
        errorMessage = "";
    }

    public void memoryClear() {
        memory = 0;
    }

    public void memoryRecall() {
        currentNumber = String.valueOf(memory);
        startNewNumber = true;
    }

    public void memoryAdd() {
        if (!currentNumber.isEmpty()) {
            memory += Double.parseDouble(currentNumber);
        }
    }

    public void memorySubtract() {
        if (!currentNumber.isEmpty()) {
            memory -= Double.parseDouble(currentNumber);
        }
    }

    public String getCurrentDisplay() {
        return currentNumber.isEmpty() ? "0" : currentNumber;
    }

    public String getHistoryText() {
        if (operator.isEmpty()) {
            return "";
        }
        return String.format("%.2f %s", result, operator);
    }

    public String getLastCalculation() {
        return history.isEmpty() ? "" : history.get(history.size() - 1);
    }

    public boolean isError() {
        return !errorMessage.isEmpty();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private String formatNumber(double number) {
        if (Double.isInfinite(number)) {
            return "Error: Overflow";
        }
        if (Double.isNaN(number)) {
            return "Error: Invalid";
        }
        // Remove trailing zeros and decimal point if not needed
        String formatted = String.format("%.10f", number).replaceAll("0*$", "").replaceAll("\\.$", "");
        return formatted;
    }

    // Financial calculator methods
    public void setResult(double value) {
        result = value;
        currentNumber = String.valueOf(value);
        startNewNumber = true;
    }

    public void setError(String message) {
        errorMessage = message;
    }

    public double calculateLoanPayment(double principal, double annualRate, int years) {
        double monthlyRate = annualRate / 12 / 100;
        int months = years * 12;
        return principal * monthlyRate * Math.pow(1 + monthlyRate, months) 
            / (Math.pow(1 + monthlyRate, months) - 1);
    }

    public double calculateLoanAmount(double payment, double annualRate, int years) {
        double monthlyRate = annualRate / 12 / 100;
        int months = years * 12;
        return payment * (Math.pow(1 + monthlyRate, months) - 1) 
            / (monthlyRate * Math.pow(1 + monthlyRate, months));
    }

    public int calculateLoanTerm(double principal, double payment, double annualRate) {
        double monthlyRate = annualRate / 12 / 100;
        double n = Math.log(payment / (payment - principal * monthlyRate)) 
            / Math.log(1 + monthlyRate);
        return (int) Math.ceil(n / 12);
    }

    public double calculateFutureValue(double presentValue, double annualRate, int years) {
        double rate = annualRate / 100;
        return presentValue * Math.pow(1 + rate, years);
    }

    public double calculatePresentValue(double futureValue, double annualRate, int years) {
        double rate = annualRate / 100;
        return futureValue / Math.pow(1 + rate, years);
    }

    public double calculateROI(double initialInvestment, double finalValue) {
        return ((finalValue - initialInvestment) / initialInvestment) * 100;
    }

    public void calculateMortgage(double principal, double annualRate, int years, double downPayment) {
        double loanAmount = principal - downPayment;
        double monthlyPayment = calculateLoanPayment(loanAmount, annualRate, years);
        double totalPayment = monthlyPayment * years * 12;
        double totalInterest = totalPayment - loanAmount;
        
        setResult(monthlyPayment);
        history.add(String.format("Mortgage: $%.2f/month (Total: $%.2f, Interest: $%.2f)", 
            monthlyPayment, totalPayment, totalInterest));
    }

    public void showAmortizationSchedule(double principal) {
        // This would typically open a new window showing the amortization schedule
        // For now, we'll just add a message to history
        history.add("Amortization schedule requested for $" + formatNumber(principal));
    }

    public void calculateBond(double faceValue, double couponRate, double marketRate, int years) {
        double couponPayment = faceValue * (couponRate / 100);
        double presentValue = 0;
        
        // Calculate present value of all coupon payments
        for (int i = 1; i <= years; i++) {
            presentValue += couponPayment / Math.pow(1 + marketRate / 100, i);
        }
        
        // Add present value of face value
        presentValue += faceValue / Math.pow(1 + marketRate / 100, years);
        
        setResult(presentValue);
        history.add(String.format("Bond value: $%.2f (Face value: $%.2f, Coupon: %.1f%%)", 
            presentValue, faceValue, couponRate));
    }

    public double calculatePERatio(double stockPrice, double earningsPerShare) {
        if (earningsPerShare == 0) {
            throw new ArithmeticException("Earnings per share cannot be zero");
        }
        return stockPrice / earningsPerShare;
    }

    public double calculateDERatio(double totalDebt, double totalEquity) {
        if (totalEquity == 0) {
            throw new ArithmeticException("Total equity cannot be zero");
        }
        return totalDebt / totalEquity;
    }
} 