package view;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.InputValidation;

import java.awt.*;
import java.text.NumberFormat;

import model.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpenseTrackerView extends JFrame {

  private JTable transactionsTable;
  private JButton addTransactionBtn;
  private JButton filterBtn;
  private JButton clearFilterBtn;

  private JTextField filterValuField;
  private JFormattedTextField amountField;
  private JTextField categoryField;

  private DefaultTableModel model;
  
  private JComboBox filterList;

  public ExpenseTrackerView() {
    setTitle("Expense Tracker"); // Set title
    setSize(1000, 800); // Make GUI larger

    String[] columnNames = {"serial", "Amount", "Category", "Date"};
    this.model = new DefaultTableModel(columnNames, 0);

    addTransactionBtn = new JButton("Add Transaction");
    filterBtn = new JButton("Filter");
    clearFilterBtn = new JButton("Clear Filter");

    // Create UI components
    JLabel amountLabel = new JLabel("Amount:");
    NumberFormat format = NumberFormat.getNumberInstance();

    amountField = new JFormattedTextField(format);
    amountField.setColumns(10);

    
    JLabel categoryLabel = new JLabel("Category:");
    categoryField = new JTextField(10);

    // Create table
    transactionsTable = new JTable(model);
  
    // Layout components
    JPanel inputPanel = new JPanel();
    inputPanel.add(amountLabel);
    inputPanel.add(amountField);
    inputPanel.add(categoryLabel); 
    inputPanel.add(categoryField);
    inputPanel.add(addTransactionBtn);

    // JLabel amountFilterLabel = new JLabel("Amount Filter:");
    JLabel filterTypeLabel = new JLabel("Select Filter");

    String[] filterTypeStrings = { "Amount", "Category"};

    //Create the drop down using combo box
    filterList = new JComboBox(filterTypeStrings);
    filterList.setSelectedIndex(1);

    JLabel filterValueLabel = new JLabel("Filter Value");

    filterValuField = new JTextField(10);

    // Create table
    transactionsTable = new JTable(model);

    // Layout components
    JPanel filterPanel = new JPanel();
    filterPanel.add(filterTypeLabel);
    filterPanel.add(filterList);
    filterPanel.add(filterValueLabel);
    filterPanel.add(filterValuField);
    filterPanel.add(filterBtn);
    filterPanel.add(clearFilterBtn);
  
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(addTransactionBtn);
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    mainPanel.add(inputPanel);
    mainPanel.add(filterPanel);

    // Add the main panel and the table to the frame
    add(mainPanel, BorderLayout.NORTH);
    add(new JScrollPane(transactionsTable), BorderLayout.CENTER); 
    add(buttonPanel, BorderLayout.SOUTH);
  
    // Set frame properties
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  
  }

  public void refreshTable(List<Transaction> transactions) {
      // Clear existing rows
      model.setRowCount(0);
      // Get row count
      int rowNum = model.getRowCount();
      double totalCost=0;
      // Calculate total cost
      for(Transaction t : transactions) {
        totalCost+=t.getAmount();
      }
      // Add rows from transactions list
      for(Transaction t : transactions) {
        model.addRow(new Object[]{rowNum+=1,t.getAmount(), t.getCategory(), t.getTimestamp()}); 
      }
        // Add total row
        Object[] totalRow = {"Total", null, null, totalCost};
        model.addRow(totalRow);
  
      // Fire table update
      transactionsTable.updateUI();
  
  }

  
  public JButton getAddTransactionBtn() {
    return addTransactionBtn;
  }

  public JButton getFilterButton() {
    return filterBtn;
  }

  public JButton getClearFilterButton() {
    return clearFilterBtn;
  }

  public JComboBox getComboBox() {
    return filterList;
  }

  public DefaultTableModel getTableModel() {
    return model;
  }
  // Other view methods
    public JTable getTransactionsTable() {
    return transactionsTable;
  }

  public double getAmountField() {
    if(amountField.getText().isEmpty()) {
      return 0;
    }else {
    double amount = Double.parseDouble(amountField.getText());
    return amount;
    }
  }

  public void setAmountField(JFormattedTextField amountField) {
    this.amountField = amountField;
  }


  public String getCategoryField() {
    return categoryField.getText();
  }

  public void setCategoryField(JTextField categoryField) {
    this.categoryField = categoryField;
  }

  public String getFilterValueField() {
    return filterValuField.getText();
  }

  public void setFilterValueField() {
    this.filterValuField.setText("");
  }

  public Double getFilterAmountValue() {
    String value = getFilterValueField();
    if(value == null || value.length() == 0) {
      return Double.valueOf("0");
    }
    else return Double.parseDouble(value);
  }

  public String getCategoryFilterValue() {
    return getFilterValueField();
  }

  public String getComboBoxValue() {
    String filterType = (String)filterList.getSelectedItem();
    return filterType;
  }

  public void highlightTable(List<Transaction> transactions) {
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    renderer.setBackground(Color.GREEN);
    Set<Integer> rowIndexes = findRowsByTransactions(transactions);

    transactionsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        // Check if the current row should be highlighted
        if (rowIndexes.contains(row)) {
          c.setBackground(Color.GREEN);
        } else {
          c.setBackground(table.getBackground());
        }

        return c;
      }
    });

    transactionsTable.repaint();

  }

  public void deHighlightTable(List<Transaction> transactions) {
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    renderer.setBackground(Color.WHITE);

    transactionsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        c.setBackground(Color.WHITE);
        return c;
      }
    });
    transactionsTable.repaint();
  }

  private Set<Integer> findRowsByTransactions(List<Transaction> transactions) {
    Set<Integer> rowIndices = new HashSet<>();

    for (Transaction transaction : transactions) {
      for (int row = 0; row < model.getRowCount()-1; row++) {
        if (model.getValueAt(row, 1).equals(transaction.getAmount())
                && model.getValueAt(row, 2).equals(transaction.getCategory())
                && model.getValueAt(row, 3).equals(transaction.getTimestamp())) {
          rowIndices.add(row);
        }
      }
    }

    return rowIndices;
  }

}
