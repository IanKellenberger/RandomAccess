import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandProductMaker extends JFrame {
    private static final String FILE_PATH = "products.dat";
    private RandomAccessFile productFile;
    private JTextField nameField, descriptionField, idField, costField, recordCountField;

    public RandProductMaker() {
        setTitle("Product Maker");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2));

        formPanel.add(new JLabel("Product Name (35 chars max):"));
        nameField = new JTextField(35);
        formPanel.add(nameField);

        formPanel.add(new JLabel("Description (75 chars max):"));
        descriptionField = new JTextField(75);
        formPanel.add(descriptionField);

        formPanel.add(new JLabel("Product ID (6 chars):"));
        idField = new JTextField(6);
        formPanel.add(idField);

        formPanel.add(new JLabel("Product Cost:"));
        costField = new JTextField(10);
        formPanel.add(costField);

        formPanel.add(new JLabel("Record Count:"));
        recordCountField = new JTextField(10);
        recordCountField.setEditable(false);  // Don't allow editing
        formPanel.add(recordCountField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(e -> addProduct());
        buttonPanel.add(addButton);
        add(buttonPanel, BorderLayout.SOUTH);

        try {
            productFile = new RandomAccessFile(FILE_PATH, "rw");
            updateRecordCount();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error opening file: " + e.getMessage());
        }
    }

    private void addProduct() {
        try {
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();
            String id = idField.getText().trim();
            double cost = Double.parseDouble(costField.getText().trim());

            if (name.length() > 35 || description.length() > 75 || id.length() > 6) {
                JOptionPane.showMessageDialog(this, "Input exceeds maximum length.");
                return;
            }

            Product product = new Product(name, description, id, cost);

            product.writeToFile(productFile);

            nameField.setText("");
            descriptionField.setText("");
            idField.setText("");
            costField.setText("");

            // Update the record count
            updateRecordCount();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid cost value. Please enter a valid number.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage());
        }
    }

    private void updateRecordCount() {
        try {
            long recordCount = productFile.length() / Product.RECORD_SIZE;
            recordCountField.setText(String.valueOf(recordCount));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating record count: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RandProductMaker frame = new RandProductMaker();
            frame.setVisible(true);
        });
    }
}
