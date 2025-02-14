package ji.rb.model;

public class SearchResult implements Comparable<SearchResult> {
    private Item item;
    private double score;

    public SearchResult(Item item, double score) {
         this.item = item;
         this.score = score;
    }

    public Item getItem() {
         return item;
    }

    public double getScore() {
         return score;
    }

    @Override
    public int compareTo(SearchResult other) {
         // Higher score is "greater" in our max-heap.
         return Double.compare(this.score, other.score);
    }
}
