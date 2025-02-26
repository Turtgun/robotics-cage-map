package ji.rb.service;

import ji.rb.model.Item;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVParserService {
    
    /**
     * Parse items from a CSV file.
     * 
     * Expected CSV format:
     * Item Name,Location,Image,Special Note,Feature1,Feature2
     * 
     * @param filePath Path to the CSV file
     * @return List of Item objects
     * @throws IOException If there's an error reading the file
     */
    public List<Item> parseItemsFromCSV(String filePath) throws IOException {
        List<Item> items = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header line
            String line = br.readLine();
            
            // Process data lines
            while ((line = br.readLine()) != null) {
                // Handle CSV parsing with potential quoted fields containing commas
                String[] parts = parseCSVLine(line);
                
                if (parts.length >= 6) {
                    String name = parts[0];
                    String location = parts[1];
                    String imagePath = parts[2];
                    String specialNote = parts[3];
                    
                    // Parse feature values
                    double feature1 = 0;
                    double feature2 = 0;
                    
                    try {
                        feature1 = Double.parseDouble(parts[4]);
                        feature2 = Double.parseDouble(parts[5]);
                    } catch (NumberFormatException e) {
                        // Use default values if parsing fails
                        System.err.println("Error parsing feature values for item: " + name);
                    }
                    
                    // Create Item object
                    Item item;
                    if (specialNote != null && !specialNote.isEmpty()) {
                        item = new Item(name, location, imagePath, specialNote, new double[]{feature1, feature2});
                    } else {
                        item = new Item(name, location, imagePath, new double[]{feature1, feature2});
                    }
                    
                    items.add(item);
                }
            }
        }
        
        return items;
    }
    
    /**
     * Parse a CSV line handling quoted values that may contain commas.
     * 
     * @param line The CSV line to parse
     * @return Array of field values
     */
    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                // Toggle the inQuotes flag
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                // End of field
                result.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                // Add character to the current field
                currentField.append(c);
            }
        }
        
        // Add the last field
        result.add(currentField.toString());
        
        return result.toArray(new String[0]);
    }
    
    /**
     * Integration with ItemService to load items from CSV
     */
    public void loadItemsIntoItemService(ItemService itemService, String csvFilePath) throws IOException {
        List<Item> items = parseItemsFromCSV(csvFilePath);
        itemService.setItems(items); // You would need to add this setter method to ItemService
    }
}