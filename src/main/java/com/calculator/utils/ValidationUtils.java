package com.calculator.utils;

import java.util.regex.Pattern;

/**
 * Utility class for input validation and error handling.
 */
public class ValidationUtils {
    // Regular expressions for validation
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^-?\\d*\\.?\\d*$");
    private static final Pattern OPERATOR_PATTERN = Pattern.compile("[+\\-*/^%]");
    private static final Pattern SCIENTIFIC_PATTERN = Pattern.compile("^-?\\d*\\.?\\d*[Ee][+-]?\\d+$");
    private static final Pattern HEXADECIMAL_PATTERN = Pattern.compile("^[0-9A-Fa-f]+$");
    private static final Pattern BINARY_PATTERN = Pattern.compile("^[01]+$");
    private static final Pattern OCTAL_PATTERN = Pattern.compile("^[0-7]+$");
    
    /**
     * Validates numeric input.
     */
    public static boolean isValidNumericInput(String input) {
        return input != null && (NUMERIC_PATTERN.matcher(input).matches() || 
                               SCIENTIFIC_PATTERN.matcher(input).matches());
    }
    
    /**
     * Validates operator input.
     */
    public static boolean isValidOperator(String operator) {
        return operator != null && OPERATOR_PATTERN.matcher(operator).matches();
    }
    
    /**
     * Validates hexadecimal input.
     */
    public static boolean isValidHexadecimal(String input) {
        return input != null && HEXADECIMAL_PATTERN.matcher(input).matches();
    }
    
    /**
     * Validates binary input.
     */
    public static boolean isValidBinary(String input) {
        return input != null && BINARY_PATTERN.matcher(input).matches();
    }
    
    /**
     * Validates octal input.
     */
    public static boolean isValidOctal(String input) {
        return input != null && OCTAL_PATTERN.matcher(input).matches();
    }
    
    /**
     * Checks for potential division by zero.
     */
    public static boolean isDivisionByZero(double divisor) {
        return Math.abs(divisor) < Double.MIN_NORMAL;
    }
    
    /**
     * Validates input for trigonometric functions.
     */
    public static boolean isValidTrigonometricInput(double angle, boolean isDegrees) {
        if (isDegrees) {
            // Convert to radians for internal validation
            angle = Math.toRadians(angle);
        }
        return !Double.isInfinite(angle) && !Double.isNaN(angle);
    }
    
    /**
     * Validates input for logarithmic functions.
     */
    public static boolean isValidLogarithmicInput(double value) {
        return value > 0 && !Double.isInfinite(value) && !Double.isNaN(value);
    }
    
    /**
     * Validates input for square root.
     */
    public static boolean isValidSquareRootInput(double value) {
        return value >= 0 && !Double.isInfinite(value) && !Double.isNaN(value);
    }
    
    /**
     * Validates input for power function.
     */
    public static boolean isValidPowerInput(double base, double exponent) {
        // Check for special cases that would result in invalid operations
        if (base == 0 && exponent <= 0) {
            return false;
        }
        if (base < 0 && !isWholeNumber(exponent)) {
            return false;
        }
        return !Double.isInfinite(base) && !Double.isNaN(base) &&
               !Double.isInfinite(exponent) && !Double.isNaN(exponent);
    }
    
    /**
     * Checks if a number is a whole number.
     */
    public static boolean isWholeNumber(double value) {
        return Math.floor(value) == value;
    }
    
    /**
     * Validates factorial input.
     */
    public static boolean isValidFactorialInput(double value) {
        return value >= 0 && value <= 170 && isWholeNumber(value);
    }
    
    /**
     * Validates percentage input.
     */
    public static boolean isValidPercentageInput(double value) {
        return !Double.isInfinite(value) && !Double.isNaN(value);
    }
    
    /**
     * Validates memory operation input.
     */
    public static boolean isValidMemoryInput(double value) {
        return !Double.isInfinite(value) && !Double.isNaN(value);
    }
    
    /**
     * Generates user-friendly error messages.
     */
    public static String getErrorMessage(ValidationError error) {
        switch (error) {
            case INVALID_NUMBER:
                return "Please enter a valid number";
            case INVALID_OPERATOR:
                return "Invalid operator";
            case DIVISION_BY_ZERO:
                return "Cannot divide by zero";
            case NEGATIVE_SQUARE_ROOT:
                return "Cannot calculate square root of a negative number";
            case INVALID_LOGARITHM:
                return "Cannot calculate logarithm of zero or negative number";
            case INVALID_POWER:
                return "Invalid base or exponent for power operation";
            case INVALID_FACTORIAL:
                return "Factorial is only defined for positive integers up to 170";
            case OVERFLOW:
                return "Result is too large to display";
            case UNDERFLOW:
                return "Result is too small to display";
            case INVALID_TRIGONOMETRIC:
                return "Invalid input for trigonometric function";
            case INVALID_MEMORY:
                return "Invalid value for memory operation";
            default:
                return "An error occurred";
        }
    }
    
    /**
     * Enumeration of validation error types.
     */
    public enum ValidationError {
        INVALID_NUMBER,
        INVALID_OPERATOR,
        DIVISION_BY_ZERO,
        NEGATIVE_SQUARE_ROOT,
        INVALID_LOGARITHM,
        INVALID_POWER,
        INVALID_FACTORIAL,
        OVERFLOW,
        UNDERFLOW,
        INVALID_TRIGONOMETRIC,
        INVALID_MEMORY
    }
    
    /**
     * Checks for numeric overflow.
     */
    public static boolean isOverflow(double value) {
        return Double.isInfinite(value) || value > Double.MAX_VALUE;
    }
    
    /**
     * Checks for numeric underflow.
     */
    public static boolean isUnderflow(double value) {
        return Math.abs(value) < Double.MIN_NORMAL && value != 0;
    }
    
    /**
     * Validates and formats display output.
     */
    public static String formatOutput(double value) {
        if (Double.isNaN(value)) {
            return "Error";
        }
        if (Double.isInfinite(value)) {
            return value > 0 ? "∞" : "-∞";
        }
        if (isWholeNumber(value)) {
            return String.format("%.0f", value);
        }
        // Use scientific notation for very large or small numbers
        if (Math.abs(value) >= 1e12 || (Math.abs(value) < 1e-6 && value != 0)) {
            return String.format("%.6E", value);
        }
        // Regular decimal format with up to 10 decimal places
        return String.format("%.10g", value).replaceAll("\\.?0+$", "");
    }
    
    /**
     * Validates expression syntax.
     */
    public static boolean isValidExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }
        
        // Check for balanced parentheses
        int parenthesesCount = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                parenthesesCount++;
            } else if (c == ')') {
                parenthesesCount--;
                if (parenthesesCount < 0) {
                    return false;
                }
            }
        }
        if (parenthesesCount != 0) {
            return false;
        }
        
        // Check for invalid operator combinations
        if (expression.matches(".*[+\\-*/^%][+*/^%].*")) {
            return false;
        }
        
        // Check for valid start and end
        return !expression.matches("^[*/^%].*") && !expression.matches(".*[+\\-*/^%]$");
    }
    
    /**
     * Validates and sanitizes user input.
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Remove any characters that aren't digits, operators, decimal points, or parentheses
        return input.replaceAll("[^0-9+\\-*/^%().Ee]", "");
    }
} 