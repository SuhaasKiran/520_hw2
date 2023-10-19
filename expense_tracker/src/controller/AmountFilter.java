package controller;

import controller.TransactionFilter;
import model.ExpenseTrackerModel;
import model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class AmountFilter implements TransactionFilter {
    private double amount;

    public AmountFilter(double amount) {
        this.amount = amount;
    }

    public boolean inputValidation() {
        if (!InputValidation.isValidAmount(amount)) {
            return false;
        }
        return true;
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {

        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            double amount = transaction.getAmount();
            if (amount == this.amount) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }
}
