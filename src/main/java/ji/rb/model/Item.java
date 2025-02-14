package ji.rb.model;

public class Item {
     private String name;
     private String location;
     private String imagePath;
     private double[] features; // Feature vector for linear algebra operations


     public Item(String name, String location, String imagePath, double[] features) {
          this.name = name;
          this.location = location;
          this.imagePath = imagePath;
          this.features = features;
     }

     public Item(String name, String location, String imagePath, String note, double[] features) {
          this.name = name;
          this.location = location;
          this.imagePath = imagePath;
          this.features = features;
     }

     public String getName() {
          return name;
     }

     public String getLocation() {
          return location;
     }

     public String getImagePath() {
          return imagePath;
     }

     public double[] getFeatures() {
          return features;
     }

     @Override
     public String toString() {
          return "Item{" +
               "name='" + name + '\'' +
               ", location='" + location + '\'' +
               ", imagePath=" + imagePath +
               '}';
     }
}
