import java.util.Arrays;
import java.util.Objects;

public class ArrayList<T extends Comparable<T>> implements List<T> {
    private T[] array;
    private int size;
    private boolean isSorted;

    //initialize the list to a length of two, size of 0 and isSorted to true
    public ArrayList() {
        array = (T[]) new Comparable[2];
        size = 0;
        isSorted = true;
    }
    //add and element to the end of the list
    public boolean add(T element) {
        if (element == null) {
            return false;
        }
        ensureCapacity();
        if (size > 0 && element.compareTo(array[size - 1]) < 0) {
            isSorted = false;
        }
        array[size] = element;
        size++;
        return true;
    }
    //add an element at a given index
    public boolean add(int index, T element) {
        if (element == null || index < 0 || index > size) {
            return false;
        }
        ensureCapacity();
        for (int i = size; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = element;
        size++;
        //if the element is added to the front or end of the array
        //then isSorted will be updated properly
        if (index > 0) {
            isSorted = isSorted && (element.compareTo(array[index - 1]) >= 0);
        }
        if (index < size - 1) {
            isSorted = isSorted && (element.compareTo(array[index + 1]) <= 0);
        }
        return true;
    }
    //clears the array and resets it back to the original parameters
    public void clear() {
        array = Arrays.copyOf(array, 2);
        size = 0;
        isSorted = true;
    }
    //returns the element at a given index
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return array[index];
    }
    //returns the index of a given element
    public int indexOf(T element) {
        if (element == null) {
            return -1;
        }
        if (isSorted) { //use the binary search if the array is sorted
            return binarySearch(element);
        } else { //linear search for when the array is not sorted
            for (int index = 0; index < size; index++) {
                if (Objects.equals(element, array[index])) {
                    return index;
                }
            }
        }
        return -1;
    }
    //returns true or false depending on if the array is empty or not
    public boolean isEmpty() {
        return size == 0;
    }
    //returns the size of the array
    public int size() {
        return size;
    }
    //sorts the array using insertion or binary search
    //depending on the state of the array
    public void sort() {
        //Don't sort the array if it is already sorted
        if (isSorted) {
            return;
        }
        //sort the array using insertion sort
        for (int i = 1; i < size; i++) {
            T currElement = array[i];
            int j = i - 1;
            while (j >= 0 && currElement.compareTo(array[j]) < 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = currElement;
        }
        isSorted = true;
    }
    //removes the element at the given index
    public T remove(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        T removedElement = array[index];
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        size--;
        isSorted = true;
        //loop through the array to check if the removed element
        //unsorted the array
        for (int i = 1; i < size; i++) {
            if (array[i - 1].compareTo(array[i]) > 0) {
                isSorted = false;
                break;
            }
        }
        return removedElement;
    }
    //removes the elements that are not equal to the given
    //element from the current array
    public void equalTo(T element) {
        if (element == null) {
            return;
        }
        //if the array is already sorted, use a binary search
        if (isSorted) {
            int newIndex = binarySearch(element);
            if (newIndex < 0) {
                size = 0;
            } else {
                while (newIndex < size && array[newIndex].equals(element)) {
                    newIndex++;
                }
                size = newIndex;
            }
        }else { //conduct a linear search if the array is not sorted
            int newIndex = 0;
            for (int i = 0; i < size; i++) {
                if (element.equals(array[i])) {
                    array[newIndex] = array[i];
                    newIndex++;
                }
            }
            size = newIndex;
            isSorted = true;
        }
    }
    //reverse the current order of the array
    public void reverse() {
        for (int i = 0; i < size / 2; i++) {
            T temp = array[i];
            array[i] = array[size - i - 1];
            array[size - i - 1] = temp;
        }
        boolean sorted = true;
        //loop through the array and see if the reverse sorted the array
        for (int i = 1; i < size; i++) {
            if (array[i].compareTo(array[i - 1]) < 0) {
                sorted = false;
                break;
            }
        }
        isSorted = sorted;
    }
    //takes in another array, compares it to the current array
    //and adds the intersecting elements to a new array
    public void intersect(List<T> otherList) {
        if (otherList == null) {
            return;
        }
        otherList.sort();
        sort();
        ArrayList<T> other = (ArrayList<T>) otherList;
        ArrayList<T> intersect = new ArrayList<>();
        int currIndex = 0;
        int otherIndex = 0;
        //loop through the size of both arrays while keeping track of the indexes
        //making sure to compare each element and seeing if they are the same
        //if they are the same, it will be added to the intersect array
        while (currIndex < size && otherIndex < other.size()) {
            int comparison = array[currIndex].compareTo(other.get(otherIndex));
            if (comparison == 0) {
                intersect.add(array[currIndex]);
                currIndex++;
                otherIndex++;
            } else if (comparison < 0) {
                currIndex++;
            } else {
                otherIndex++;
            }
        }
        //Here we just clear the current array then add the elements
        //of the intersect array to the current array
        clear();
        for (int i = 0; i < intersect.array.length; i++) {
            T element = intersect.array[i];
            if (element != null) {
                add(element);
            }
        }
        isSorted = true;
    }
    //get the minimum element, by first sorting the array
    //then just returning the min element
    public T getMin() {
        if (isEmpty()) {
            return null;
        }
        sort();
        return array[0];
    }
    //get the max element, by first sorting the list
    //then just returning it
    public T getMax() {
        if (isEmpty()) {
            return null;
        }
        sort();
        return array[size - 1];
    }
    //create a String of the list
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(array[i]).append("\n");
        }
        return sb.toString();
    }
    //return the isSorted boolean
    public boolean isSorted() {
        return isSorted;
    }
    //used to check the capacity of the array
    //then if the capacity is full, it will increase the current size
    //by a factor of two
    private void ensureCapacity() {
        if (size == array.length) {
            int newCapacity = array.length * 2;
            array = Arrays.copyOf(array, newCapacity);
        }
    }
    //used when isSorted is true and performs a binary search
    //which is quicker than the linear search
    private int binarySearch(T element) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int middle = low + (high - low) / 2;
            int comparison = element.compareTo(array[middle]);
            if (comparison == 0) {
                while (middle > 0 && element.compareTo(array[middle - 1]) == 0) {
                    middle--;
                }
                return middle;
            } else if (comparison < 0) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return -1;
    }
}
