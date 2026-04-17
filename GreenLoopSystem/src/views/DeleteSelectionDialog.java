package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

public class DeleteSelectionDialog<T> extends JDialog {
    private final List<T> dialogItems;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final Function<T, Boolean> deleteFunction;
    private final Consumer<T> afterDelete;
    private final int idColumnIndex;
    private final int searchColumnIndex;
    private final JTextField idFilterField;
    private final JTextField searchFilterField;

    public DeleteSelectionDialog(
            Component parent,
            String title,
            List<T> items,
            String[] columnNames,
            Function<T, Object[]> rowMapper,
            String searchLabel,
            int idColumnIndex,
            int searchColumnIndex,
            Function<T, Boolean> deleteFunction,
            Consumer<T> afterDelete
    ) {
        super(SwingUtilities.getWindowAncestor(parent), title, ModalityType.APPLICATION_MODAL);
        this.dialogItems = new ArrayList<>(items);
        this.deleteFunction = deleteFunction;
        this.afterDelete = afterDelete;
        this.idColumnIndex = idColumnIndex;
        this.searchColumnIndex = searchColumnIndex;

        this.setLayout(null);
        this.setSize(820, 520);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBounds(20, 15, 760, 28);
        this.add(titleLabel);

        JLabel idLabel = new JLabel("Filter by ID");
        idLabel.setBounds(40, 60, 120, 25);
        this.add(idLabel);

        idFilterField = new JTextField();
        idFilterField.setBounds(40, 88, 180, 28);
        this.add(idFilterField);

        JLabel nameLabel = new JLabel(searchLabel);
        nameLabel.setBounds(250, 60, 180, 25);
        this.add(nameLabel);

        searchFilterField = new JTextField();
        searchFilterField.setBounds(250, 88, 220, 28);
        this.add(searchFilterField);

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (T item : dialogItems) {
            tableModel.addRow(rowMapper.apply(item));
        }

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBounds(40, 135, 720, 280);
        this.add(tableScrollPane);

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setBounds(470, 430, 140, 32);
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        this.add(deleteButton);

        JButton closeButton = new JButton("Close");
        closeButton.setBounds(620, 430, 140, 32);
        closeButton.setFocusPainted(false);
        this.add(closeButton);

        idFilterField.getDocument().addDocumentListener(new SimpleDocumentListener(this::applyFilters));
        searchFilterField.getDocument().addDocumentListener(new SimpleDocumentListener(this::applyFilters));

        deleteButton.addActionListener(e -> deleteSelectedRow());
        closeButton.addActionListener(e -> dispose());

        this.setLocationRelativeTo(parent);
    }

    private void applyFilters() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        String idText = idFilterField.getText().trim();
        if (!idText.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(idText), idColumnIndex));
        }

        String searchText = searchFilterField.getText().trim();
        if (!searchText.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(searchText), searchColumnIndex));
        }

        sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private void deleteSelectedRow() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        T selectedItem = dialogItems.get(modelRow);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the selected record?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean deleted = deleteFunction.apply(selectedItem);
        if (!deleted) {
            JOptionPane.showMessageDialog(this, "Failed to delete the selected record.", "Delete Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        afterDelete.accept(selectedItem);
        dialogItems.remove(modelRow);
        tableModel.removeRow(modelRow);
        JOptionPane.showMessageDialog(this, "Record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
