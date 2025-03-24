package com.calculator.financial;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Financial calculator with support for various financial calculations and analysis.
 */
public class FinancialCalculator {
    // Loan Calculations
    public static double calculateLoanPayment(double principal, double annualRate, int years) {
        double monthlyRate = annualRate / 12 / 100;
        int months = years * 12;
        return principal * monthlyRate * Math.pow(1 + monthlyRate, months) 
            / (Math.pow(1 + monthlyRate, months) - 1);
    }

    public static double calculateLoanTotal(double payment, double annualRate, int years) {
        return payment * years * 12;
    }

    public static double calculateLoanInterest(double principal, double payment, int years) {
        return calculateLoanTotal(payment, 0, years) - principal;
    }

    // Mortgage Calculations
    public static class MortgageDetails {
        public final double monthlyPayment;
        public final double totalPayment;
        public final double totalInterest;
        public final double[] amortizationSchedule;

        public MortgageDetails(double monthlyPayment, double totalPayment, 
                             double totalInterest, double[] amortizationSchedule) {
            this.monthlyPayment = monthlyPayment;
            this.totalPayment = totalPayment;
            this.totalInterest = totalInterest;
            this.amortizationSchedule = amortizationSchedule;
        }
    }

    public static MortgageDetails calculateMortgage(double principal, double annualRate, 
                                                  int years, double downPayment) {
        double loanAmount = principal - downPayment;
        double monthlyRate = annualRate / 12 / 100;
        int months = years * 12;
        
        double monthlyPayment = calculateLoanPayment(loanAmount, annualRate, years);
        double totalPayment = monthlyPayment * months;
        double totalInterest = totalPayment - loanAmount;
        
        // Calculate amortization schedule
        double[] schedule = new double[months];
        double balance = loanAmount;
        for (int i = 0; i < months; i++) {
            double interest = balance * monthlyRate;
            double principalPayment = monthlyPayment - interest;
            balance -= principalPayment;
            schedule[i] = balance;
        }
        
        return new MortgageDetails(monthlyPayment, totalPayment, totalInterest, schedule);
    }

    // Investment Calculations
    public static double calculateCompoundInterest(double principal, double annualRate, 
                                                 int years, int compoundingPerYear) {
        double rate = annualRate / 100;
        return principal * Math.pow(1 + rate / compoundingPerYear, 
                                  compoundingPerYear * years);
    }

    public static double calculateContinuousCompoundInterest(double principal, 
                                                           double annualRate, int years) {
        double rate = annualRate / 100;
        return principal * Math.exp(rate * years);
    }

    public static double calculatePresentValue(double futureValue, double annualRate, int years) {
        double rate = annualRate / 100;
        return futureValue / Math.pow(1 + rate, years);
    }

    public static double calculateFutureValue(double presentValue, double annualRate, int years) {
        double rate = annualRate / 100;
        return presentValue * Math.pow(1 + rate, years);
    }

    // Investment Analysis
    public static class InvestmentAnalysis {
        public final double returnOnInvestment;
        public final double annualizedReturn;
        public final double paybackPeriod;

        public InvestmentAnalysis(double returnOnInvestment, double annualizedReturn, 
                                double paybackPeriod) {
            this.returnOnInvestment = returnOnInvestment;
            this.annualizedReturn = annualizedReturn;
            this.paybackPeriod = paybackPeriod;
        }
    }

    public static InvestmentAnalysis analyzeInvestment(double initialInvestment, 
                                                      double finalValue, 
                                                      LocalDate startDate, 
                                                      LocalDate endDate) {
        double roi = (finalValue - initialInvestment) / initialInvestment * 100;
        
        double years = ChronoUnit.DAYS.between(startDate, endDate) / 365.25;
        double annualizedReturn = (Math.pow(finalValue / initialInvestment, 1/years) - 1) * 100;
        
        double paybackPeriod = initialInvestment / (finalValue / years);
        
        return new InvestmentAnalysis(roi, annualizedReturn, paybackPeriod);
    }

    // Bond Calculations
    public static class BondAnalysis {
        public final double price;
        public final double yield;
        public final double duration;
        public final double convexity;

        public BondAnalysis(double price, double yield, double duration, double convexity) {
            this.price = price;
            this.yield = yield;
            this.duration = duration;
            this.convexity = convexity;
        }
    }

    public static BondAnalysis analyzeBond(double faceValue, double couponRate, 
                                         double marketRate, int yearsToMaturity, 
                                         int paymentsPerYear) {
        double couponPayment = faceValue * couponRate / paymentsPerYear;
        int totalPayments = yearsToMaturity * paymentsPerYear;
        double yieldPerPeriod = marketRate / paymentsPerYear;
        
        // Calculate price
        double price = 0;
        for (int i = 1; i <= totalPayments; i++) {
            price += couponPayment / Math.pow(1 + yieldPerPeriod, i);
        }
        price += faceValue / Math.pow(1 + yieldPerPeriod, totalPayments);
        
        // Calculate duration
        double weightedTime = 0;
        for (int i = 1; i <= totalPayments; i++) {
            weightedTime += i * couponPayment / Math.pow(1 + yieldPerPeriod, i);
        }
        weightedTime += totalPayments * faceValue / Math.pow(1 + yieldPerPeriod, totalPayments);
        double duration = weightedTime / price;
        
        // Calculate convexity
        double weightedTimeSquared = 0;
        for (int i = 1; i <= totalPayments; i++) {
            weightedTimeSquared += i * (i + 1) * couponPayment 
                / Math.pow(1 + yieldPerPeriod, i);
        }
        weightedTimeSquared += totalPayments * (totalPayments + 1) * faceValue 
            / Math.pow(1 + yieldPerPeriod, totalPayments);
        double convexity = weightedTimeSquared / (price * Math.pow(1 + yieldPerPeriod, 2));
        
        return new BondAnalysis(price, marketRate, duration, convexity);
    }

    // Financial Ratios
    public static double calculatePriceToEarningsRatio(double stockPrice, double earningsPerShare) {
        return stockPrice / earningsPerShare;
    }

    public static double calculateDebtToEquityRatio(double totalDebt, double totalEquity) {
        return totalDebt / totalEquity;
    }

    public static double calculateCurrentRatio(double currentAssets, double currentLiabilities) {
        return currentAssets / currentLiabilities;
    }

    public static double calculateQuickRatio(double currentAssets, double inventory, 
                                           double currentLiabilities) {
        return (currentAssets - inventory) / currentLiabilities;
    }

    public static double calculateReturnOnEquity(double netIncome, double shareholderEquity) {
        return netIncome / shareholderEquity * 100;
    }

    public static double calculateReturnOnAssets(double netIncome, double totalAssets) {
        return netIncome / totalAssets * 100;
    }

    // Risk Analysis
    public static double calculateBeta(double[] stockReturns, double[] marketReturns) {
        if (stockReturns.length != marketReturns.length) {
            throw new IllegalArgumentException("Return series must be of equal length");
        }
        
        double stockMean = calculateMean(stockReturns);
        double marketMean = calculateMean(marketReturns);
        
        double covariance = 0;
        double marketVariance = 0;
        
        for (int i = 0; i < stockReturns.length; i++) {
            double stockDiff = stockReturns[i] - stockMean;
            double marketDiff = marketReturns[i] - marketMean;
            
            covariance += stockDiff * marketDiff;
            marketVariance += marketDiff * marketDiff;
        }
        
        covariance /= stockReturns.length;
        marketVariance /= marketReturns.length;
        
        return covariance / marketVariance;
    }

    private static double calculateMean(double[] values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }
} 