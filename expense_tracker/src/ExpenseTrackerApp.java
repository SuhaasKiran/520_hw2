import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import controller.AmountFilter;
import controller.CategoryFilter;
import controller.ExpenseTrackerController;
import model.ExpenseTrackerModel;
import view.ExpenseTrackerView;
import model.Transaction;
import controller.InputValidation;

public class ExpenseTrackerApp {

  private final static String AMOUNT = "Amount";
  private final static String CATEGORY = "Category";

  public static void main(String[] args) {
    
    // Create MVC components
    ExpenseTrackerModel model = new ExpenseTrackerModel();
    ExpenseTrackerView view = new ExpenseTrackerView();
    ExpenseTrackerController controller = new ExpenseTrackerController(model, view);

    // Initialize view
    view.setVisible(true);

    // Handle add transaction button clicks
    view.getAddTransactionBtn().addActionListener(e -> {
      // Get transaction data from view
      double amount = view.getAmountField();
      String category = view.getCategoryField();
      
      // Call controller to add transaction
      boolean added = controller.addTransaction(amount, category);
      
      if (!added) {
        JOptionPane.showMessageDialog(view, "Invalid amount or category entered");
        view.toFront();
      }

    });

    view.getFilterButton().addActionListener(e -> {

      String filterType = view.getComboBoxValue();

      if(filterType == AMOUNT) {
        double amount = view.getFilterAmountValue();

        controller.setStrategy(new AmountFilter(amount));
      }

      else if(filterType == CATEGORY) {
        String category = view.getCategoryFilterValue();

        controller.setStrategy(new CategoryFilter(category));
      }
      // Call controller to filter transactions
      boolean filtered = controller.applyFilter();

      if (!filtered) {
        JOptionPane.showMessageDialog(view, "Invalid amount or category entered");
        view.toFront();
        
      }

    });

    view.getClearFilterButton().addActionListener(e -> {
      // Get transaction data from view
      controller.clearFilters();
    });

  }

}