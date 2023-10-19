package controller;

import model.ExpenseTrackerModel;
import model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CategoryFilter implements TransactionFilter {
    private String categoryToFilter;

    public CategoryFilter(String categoryToFilter) {
        this.categoryToFilter = categoryToFilter;
    }

    public boolean inputValidation() {
        if (!InputValidation.isValidCategory(categoryToFilter)) {
            return false;
        }
        return true;
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {

        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getCategory().equals(categoryToFilter)) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }
}
