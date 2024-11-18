import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.*;

public class RandProductSearch {
    private static final String FILE_PATH = "products.dat";

    public static void main(String[] args) {
        new RandProductSearch().searchProduct();
    }

    public void searchProduct() {
        while (true) {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.out.println("Error creating file: " + e.getMessage());
                    return;
                }
            }

            try (RandomAccessFile productFile = new RandomAccessFile(FILE_PATH, "r")) {
                String searchQuery = JOptionPane.showInputDialog("Enter product name:");

                if (searchQuery == null || searchQuery.trim().isEmpty()) {
                    break;
                }

                long fileLength = productFile.length();
                int numRecords = (int) (fileLength / Product.RECORD_SIZE);

                boolean found = false;

                for (int i = 0; i < numRecords; i++) {
                    Product product = Product.readFromFile(productFile);
                    if (product.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                        JOptionPane.showMessageDialog(null, product.toCSV());
                        found = true;
                    }
                }

                if (!found) {
                    JOptionPane.showMessageDialog(null, "No products found matching \"" + searchQuery + "\".");
                }

            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
            }
        }

        JOptionPane.showMessageDialog(null, "Exiting the search.");
    }
}
