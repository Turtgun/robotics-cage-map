package ji.rb.service;

import ji.rb.model.Item;
import ji.rb.model.SearchResult;
import ji.rb.ds.Heap;
import org.apache.commons.math3.linear.*;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    private List<Item> items = new ArrayList<>();

    @PostConstruct
    public void init() {

     items.add(new Item("VEX Wrench (11/4)", "Red Tool Drawer, 1st", "images/wrench.jpeg", new double[]{0.8, 0.9}));
         items.add(new Item("Saw: Metal cutting", "Red Tool Drawer, 2nd", "images/saw.jpeg", new double[]{18, 0}));
         items.add(new Item("File", "Red Tool Drawer, 3rd", "images/files.jpeg", new double[]{4, 0}));
         items.add(new Item("Mallet", "Red Tool Drawer, 3rd", "images/mallets.jpeg", new double[]{6, 0}));
         items.add(new Item("Hammer", "Red Tool Drawer, 3rd", "images/hammers.jpeg", new double[]{6, 0}));
         items.add(new Item("Zip-tie gun", "Red Tool Drawer, 3rd", "images/zip tie gun.jpeg", new double[]{11, 0}));
         items.add(new Item("Plier", "Red Tool Drawer, 4th", "images/pliers.jpeg", new double[]{5, 0}));
         items.add(new Item("Cutting Plier", "Red Tool Drawer, 4th", "images/cutting pliers.jpeg", new double[]{13, 0}));
         items.add(new Item("Twist Plier", "Red Tool Drawer, 4th", "images/twist plier.jpeg", new double[]{11, 0}));
         items.add(new Item("Saw: Wood cutting", "Red Tool Drawer, 5th", "images/wood saw.jpeg", new double[]{17, 0}));
         items.add(new Item("Clamp", "Red Tool Drawer, 6th", "images/clamps.jpeg", new double[]{5, 0}));
         items.add(new Item("Vise Clamp", "Red Tool Drawer, 6th", "images/vise clamp.jpeg", new double[]{10, 0}));
         items.add(new Item("Crimping Tool", "Red Tool Drawer, 7th", "images/crimping tool.jpeg", new double[]{13, 0}));
         items.add(new Item("VEX Plate", "Red Tool Drawer, 7th", "images/VEX plates.jpeg", new double[]{13, 0}));
         items.add(new Item("White Lithium Grease", "Red Tool Drawer, Right Cabinet", "images/grease.jpeg", new double[]{13, 0}));
         items.add(new Item("WD-40", "Red Tool Drawer, Right Cabinet", "images/wd40.jpeg", new double[]{13, 0}));
         items.add(new Item("Tape Measure", "Red Tool Drawer, Right Cabinet", "images/tape measures.jpeg", new double[]{13, 0}));
         items.add(new Item("Drill bits: Extra", "Red Tool Drawer, Right Cabinet", "images/extra drill bits.jpeg", new double[]{13, 0}));

         // Add more items as needed...
    }

    public List<Item> searchItems(String query) {
         Heap<SearchResult> heap = new Heap<>();
         for (Item item : items) {
          //System.out.println(item.getName() + "- " + item.getName().length() + ", " + item.getName().toLowerCase().chars().sum() % 191);
              double score = computeScore(item, query);
              heap.add(new SearchResult(item, score));
         }
         List<Item> results = new ArrayList<>();
         while (!heap.isEmpty()) {
              SearchResult sr = heap.remove();
              results.add(sr.getItem());
         }
         return results;
    }

    /**
     * Compute a relevance score between an item and the query.
     *
     * Linear Algebra Recap:
     * 1. Each item’s attributes form a feature vector.
     * 2. We construct a diagonal matrix from these features.
     * 3. Its eigen decomposition yields eigenvalues and eigenvectors.
     * 4. The principal eigenvector (for the largest eigenvalue) captures the dominant attribute.
     * 5. We convert the query into a vector (e.g. using query length and a dummy character code sum),
     *    and the dot product of this vector with the eigenvector gives a similarity score.
     */
    private double computeScore(Item item, String query) {
          // Normalize strings for comparison
          String normalizedQuery = query.toLowerCase().trim();
          String normalizedItemName = item.getName().toLowerCase().trim();
          
          // Text similarity component (0.7 weight)
          double textScore = computeTextSimilarity(normalizedItemName, normalizedQuery);
          
          // Feature vector component (0.3 weight)
          double featureScore = computeFeatureScore(item, query);
          
          // Weighted combination
          return (0.7 * textScore) + (0.3 * featureScore);
     }
     
     private double computeTextSimilarity(String itemName, String query) {
          // Exact match gets highest score
          if (itemName.equals(query)) {
          return 1.0;
          }
          
          // Contains full query as substring
          if (itemName.contains(query)) {
          return 0.8;
          }
          
          // Query contains item name
          if (query.contains(itemName)) {
          return 0.7;
          }
          
          // Word-level partial matches
          String[] queryWords = query.split("\\s+");
          String[] itemWords = itemName.split("\\s+");
          
          int matchedWords = 0;
          for (String queryWord : queryWords) {
          for (String itemWord : itemWords) {
               if (itemWord.contains(queryWord) || queryWord.contains(itemWord)) {
                    matchedWords++;
                    break;
               }
          }
          }
          
          if (matchedWords > 0) {
          return 0.5 * ((double) matchedWords / queryWords.length);
          }
          
          // Character-level similarity for non-matching cases
          return computeLevenshteinSimilarity(itemName, query);
     }
     
     private double computeLevenshteinSimilarity(String str1, String str2) {
          int[][] dp = new int[str1.length() + 1][str2.length() + 1];
          
          for (int i = 0; i <= str1.length(); i++) {
          for (int j = 0; j <= str2.length(); j++) {
               if (i == 0) {
                    dp[i][j] = j;
               } else if (j == 0) {
                    dp[i][j] = i;
               } else {
                    dp[i][j] = min(
                         dp[i - 1][j - 1] + (str1.charAt(i - 1) == str2.charAt(j - 1) ? 0 : 1),
                         dp[i - 1][j] + 1,
                         dp[i][j - 1] + 1
                    );
               }
          }
          }
          
          int maxLength = Math.max(str1.length(), str2.length());
          return 1.0 - ((double) dp[str1.length()][str2.length()] / maxLength);
     }
     
     private double computeFeatureScore(Item item, String query) {
          // Original feature-based scoring
          double q1 = query.length();
          double q2 = query.toLowerCase().chars().sum() % 191;
          double[] queryVector = new double[]{q1, q2};
          
          double[] features = item.getFeatures();
          RealMatrix featureMatrix = MatrixUtils.createRealDiagonalMatrix(features);
          EigenDecomposition ed = new EigenDecomposition(featureMatrix);
          double[] eigenVector = ed.getEigenvector(0).toArray();
          
          double score = 0;
          for (int i = 0; i < Math.min(queryVector.length, eigenVector.length); i++) {
          score += queryVector[i] * eigenVector[i];
          }
          
          // Normalize the feature score to [0,1] range
          // Assuming typical feature scores fall between -1000 and 1000
          return (score + 1000) / 2000;
     }
     
     private int min(int a, int b, int c) {
          return Math.min(Math.min(a, b), c);
     }
}
