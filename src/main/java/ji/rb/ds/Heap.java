package ji.rb.ds;

import java.util.ArrayList;

public class Heap<T extends Comparable<T>> {
    private ArrayList<T> a;
    private int n = 0;
    private boolean min = false;

    // Constructors
    public Heap() {
         a = new ArrayList<>();
         a.add(null);
    }

    public Heap(boolean min) {
         this.min = min;
         a = new ArrayList<>();
         a.add(null);
    }

    // Add an element and restore heap property.
    public void add(T value) {
         a.add(value);
         n++;
         heapifyUp(n);
    }

    private void heapifyUp(int i) {
         while (i > 1 && compare(a.get(i), a.get(i / 2)) > 0) {
              swap(i, i / 2);
              i = i / 2;
         }
    }

    // Remove and return the maximum element.
    public T remove() {
         if (isEmpty()) {
              throw new IllegalStateException("Heap is empty");
         }
         T max = a.get(1);
         a.set(1, a.get(n));
         a.remove(n);
         n--;
         heapifyDown(1);
         return max;
    }

    private void heapifyDown(int i) {
         while (2 * i <= n) {
              int j = 2 * i;
              if (j < n && compare(a.get(j), a.get(j + 1)) < 0) {
                   j++;
              }
              if (compare(a.get(i), a.get(j)) >= 0) {
                   break;
              }
              swap(i, j);
              i = j;
         }
    }

    private void swap(int i, int j) {
         T temp = a.get(i);
         a.set(i, a.get(j));
         a.set(j, temp);
    }

    private int compare(T x, T y) {
         return min ? y.compareTo(x) : x.compareTo(y);
    }

    public int size() {
         return n;
    }

    public boolean isEmpty() {
         return n == 0;
    }

    public T peek() {
         if (isEmpty()) {
              throw new IllegalStateException("Heap is empty");
         }
         return a.get(1);
    }
    
    public void displayLevelOrder() {
         if (isEmpty()) {
              System.out.println("Heap is empty");
              return;
         }
         int levelSize = 1;
         int index = 1;
         while (index <= n) {
              for (int i = 0; i < levelSize && index <= n; i++, index++) {
                   System.out.print(a.get(index) + " ");
              }
              System.out.println();
              levelSize *= 2;
         }
    }
}
