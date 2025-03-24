package com.calculator.programmer;

/**
 * Programmer calculator with support for different number systems and bitwise operations.
 */
public class ProgrammerCalculator {
    public enum NumberSystem {
        BINARY(2),
        OCTAL(8),
        DECIMAL(10),
        HEXADECIMAL(16);

        private final int base;

        NumberSystem(int base) {
            this.base = base;
        }

        public int getBase() {
            return base;
        }
    }

    // Number System Conversions
    public static String convertNumber(String number, NumberSystem from, NumberSystem to) {
        try {
            long decimal = parseNumber(number, from);
            return formatNumber(decimal, to);
        } catch (NumberFormatException e) {
            throw new ArithmeticException("Invalid number format for " + from);
        }
    }

    private static long parseNumber(String number, NumberSystem system) {
        return switch (system) {
            case BINARY -> Long.parseLong(number, 2);
            case OCTAL -> Long.parseLong(number, 8);
            case DECIMAL -> Long.parseLong(number);
            case HEXADECIMAL -> Long.parseLong(number, 16);
        };
    }

    private static String formatNumber(long number, NumberSystem system) {
        return switch (system) {
            case BINARY -> Long.toBinaryString(number);
            case OCTAL -> Long.toOctalString(number);
            case DECIMAL -> Long.toString(number);
            case HEXADECIMAL -> Long.toHexString(number).toUpperCase();
        };
    }

    // Bitwise Operations
    public static long and(long a, long b) {
        return a & b;
    }

    public static long or(long a, long b) {
        return a | b;
    }

    public static long xor(long a, long b) {
        return a ^ b;
    }

    public static long not(long a) {
        return ~a;
    }

    public static long leftShift(long a, int bits) {
        return a << bits;
    }

    public static long rightShift(long a, int bits) {
        return a >> bits;
    }

    public static long unsignedRightShift(long a, int bits) {
        return a >>> bits;
    }

    // Bit Manipulation
    public static int countSetBits(long number) {
        return Long.bitCount(number);
    }

    public static int leadingZeros(long number) {
        return Long.numberOfLeadingZeros(number);
    }

    public static int trailingZeros(long number) {
        return Long.numberOfTrailingZeros(number);
    }

    public static boolean getBit(long number, int position) {
        return ((number >> position) & 1) == 1;
    }

    public static long setBit(long number, int position) {
        return number | (1L << position);
    }

    public static long clearBit(long number, int position) {
        return number & ~(1L << position);
    }

    public static long toggleBit(long number, int position) {
        return number ^ (1L << position);
    }

    // Bit Field Operations
    public static long getBitField(long number, int position, int length) {
        long mask = ((1L << length) - 1) << position;
        return (number & mask) >>> position;
    }

    public static long setBitField(long number, int position, int length, long value) {
        long mask = ((1L << length) - 1) << position;
        return (number & ~mask) | ((value & ((1L << length) - 1)) << position);
    }

    // Utility Functions
    public static String toBinaryString(long number, int groupSize) {
        String binary = Long.toBinaryString(number);
        StringBuilder padded = new StringBuilder("0".repeat(Math.max(0, groupSize - binary.length() % groupSize)));
        padded.append(binary);
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < padded.length(); i++) {
            if (i > 0 && i % groupSize == 0) {
                result.append(" ");
            }
            result.append(padded.charAt(i));
        }
        return result.toString().trim();
    }

    public static String toHexString(long number, boolean upperCase) {
        String hex = Long.toHexString(number);
        return upperCase ? hex.toUpperCase() : hex;
    }

    // Data Type Conversions
    public static byte toByte(long number) {
        return (byte) number;
    }

    public static short toShort(long number) {
        return (short) number;
    }

    public static int toInt(long number) {
        return (int) number;
    }

    public static float toFloat(long number) {
        return (float) number;
    }

    public static double toDouble(long number) {
        return (double) number;
    }

    // IEEE 754 Floating Point Analysis
    public static class FloatAnalysis {
        private final int sign;
        private final int exponent;
        private final int mantissa;

        public FloatAnalysis(float value) {
            int bits = Float.floatToRawIntBits(value);
            sign = (bits >>> 31) & 0x1;
            exponent = (bits >>> 23) & 0xFF;
            mantissa = bits & 0x7FFFFF;
        }

        public String getBinaryRepresentation() {
            return String.format("%d | %8s | %23s",
                sign,
                toBinaryString(exponent, 8).replace(" ", ""),
                toBinaryString(mantissa, 23).replace(" ", ""));
        }

        public String getComponents() {
            return String.format("Sign: %d%nExponent: %d (bias: 127)%nMantissa: %d",
                sign, exponent - 127, mantissa);
        }
    }

    public static class DoubleAnalysis {
        private final int sign;
        private final int exponent;
        private final long mantissa;

        public DoubleAnalysis(double value) {
            long bits = Double.doubleToRawLongBits(value);
            sign = (int) ((bits >>> 63) & 0x1);
            exponent = (int) ((bits >>> 52) & 0x7FF);
            mantissa = bits & 0xFFFFFFFFFFFFFL;
        }

        public String getBinaryRepresentation() {
            return String.format("%d | %11s | %52s",
                sign,
                toBinaryString(exponent, 11).replace(" ", ""),
                toBinaryString(mantissa, 52).replace(" ", ""));
        }

        public String getComponents() {
            return String.format("Sign: %d%nExponent: %d (bias: 1023)%nMantissa: %d",
                sign, exponent - 1023, mantissa);
        }
    }
} 