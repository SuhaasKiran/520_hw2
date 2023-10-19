package controller;

import view.ExpenseTrackerView;

import java.util.List;



import model.ExpenseTrackerModel;
import model.Transaction;
public class ExpenseTrackerController {
  
  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;
  private TransactionFilter transactionFilter;

  public ExpenseTrackerController(ExpenseTrackerModel model, ExpenseTrackerView view) {
    this.model = model;
    this.view = view;

    // Set up view event handlers
  }



  public void refresh() {

    // Get transactions from model
    List<Transaction> transactions = model.getTransactions();

    // Pass to view
    view.refreshTable(transactions);

  }

  public void highlightFilters(List<Transaction> transactions) {

    // Pass to view

    view.highlightTable(transactions);

  }

  public void clearFilters() {
    view.setFilterValueField();
    List<Transaction> transactions = model.getTransactions();
    view.deHighlightTable(transactions);
    refresh();
  }

  public boolean addTransaction(double amount, String category) {
    if (!InputValidation.isValidAmount(amount)) {
      return false;
    }
    if (!InputValidation.isValidCategory(category)) {
      return false;
    }
    
    Transaction t = new Transaction(amount, category);
    model.addTransaction(t);
    view.getTableModel().addRow(new Object[]{t.getAmount(), t.getCategory(), t.getTimestamp()});
    refresh();
    return true;
  }

  public void setStrategy(TransactionFilter transactionFilter) {
    this.transactionFilter = transactionFilter;
  }

  public boolean applyFilter() {
    List<Transaction> transactions = model.getTransactions();

    if(!transactionFilter.inputValidation()) {
      return false;
    }

    List<Transaction> filteredTransactions = transactionFilter.filter(transactions);
    highlightFilters(filteredTransactions);
    return true;
  }
  
  // Other controller methods
}