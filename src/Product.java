import java.io.IOException;
import java.io.RandomAccessFile;

public class Product {
    private String name;
    private String description;
    private String ID;
    private double cost;

    public static final int NAME_SIZE = 35;
    public static final int DESCRIPTION_SIZE = 75;
    public static final int ID_SIZE = 6;
    public static final int COST_SIZE = 8;
    public static final int RECORD_SIZE = NAME_SIZE + DESCRIPTION_SIZE + ID_SIZE + COST_SIZE;

    public Product(String name, String description, String ID, double cost) {
        this.name = name;
        this.description = description;
        this.ID = ID;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getID() {
        return ID;
    }

    public double getCost() {
        return cost;
    }

    public void writeToFile(RandomAccessFile file) throws IOException {
        file.seek(file.length()); // Move to the end of the file
        writeFixedLengthString(file, name, NAME_SIZE);
        writeFixedLengthString(file, description, DESCRIPTION_SIZE);
        writeFixedLengthString(file, ID, ID_SIZE);
        file.writeDouble(cost); // Write the cost
    }

    private void writeFixedLengthString(RandomAccessFile file, String value, int length) throws IOException {
        StringBuilder paddedString = new StringBuilder(value);
        while (paddedString.length() < length) {
            paddedString.append(" ");
        }
        file.writeBytes(paddedString.toString());
    }

    public static Product readFromFile(RandomAccessFile file) throws IOException {
        byte[] nameBytes = new byte[NAME_SIZE];
        byte[] descriptionBytes = new byte[DESCRIPTION_SIZE];
        byte[] idBytes = new byte[ID_SIZE];
        file.read(nameBytes);
        file.read(descriptionBytes);
        file.read(idBytes);
        double cost = file.readDouble();
        String name = new String(nameBytes).trim();
        String description = new String(descriptionBytes).trim();
        String ID = new String(idBytes).trim();
        return new Product(name, description, ID, cost);
    }

    public String toCSV() {
        return String.format("%-35s%-75s%-6s%.2f", name, description, ID, cost);
    }
}
