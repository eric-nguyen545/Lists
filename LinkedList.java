public class LinkedList<T extends Comparable<T>> implements List<T>{
    private Node<T> head;
    private int length;
    private boolean isSorted;
    //initialize the list
    public LinkedList() {
        head = new Node<>(null);
        length = 0;
        isSorted = true;
    }
    //add an element to the list
    public boolean add(T element) {
        if (element == null) {
            return false;
        }
        Node<T> newNode = new Node<>(element);
        Node<T> currNode = head;
        while (currNode.getNext() != null) {
            currNode = currNode.getNext();
        }
        currNode.setNext(newNode);
        length++;
        isSorted = isSorted && (length <= 1 || element.compareTo(currNode.getData()) >= 0);
        return true;
    }
    //add and element at a certain index of the list
    public boolean add(int index, T element) {
        if (element == null || index < 0 || index > length) {
            return false;
        }
        Node<T> newNode = new Node<>(element);
        Node<T> currNode = head;
        int currNodeIndex = 0;
        while (currNodeIndex < index) {
            currNode = currNode.getNext();
            currNodeIndex++;
        }
        newNode.setNext(currNode.getNext());
        currNode.setNext(newNode);
        length++;
        //check if the element is added to the beginning or end of the list
        //update isSorted accordingly
        if (index != 0 && element.compareTo(currNode.getData()) < 0) {
            isSorted = isSorted && element.compareTo(currNode.getData()) >= 0;
        }
        isSorted = isSorted && (newNode.getNext() == null || element.compareTo(newNode.getNext().getData()) <= 0);
        return true;
    }
    //clears the list and sets it back to the default
    public void clear() {
        head.setNext(null);
        length = 0;
        isSorted = true;
    }
    //returns the element at the given index
    public T get(int index) {
        if (index < 0 || index >= length) {
            return null;
        }
        Node<T> currNode = head.getNext();
        int currNodeIndex = 0;
        while (currNodeIndex < index) {
            currNode = currNode.getNext();
            currNodeIndex++;
        }
        return currNode.getData();
    }
    //returns the index of the given element
    public int indexOf(T element) {
        if (isSorted) { //conducts a binary search if sorted
            return binarySearch(element);
        } else { //conducts a linear search if not sorted
            int index = 0;
            Node<T> currNode = head.getNext();
            while (currNode != null) {
                if (element == null ? currNode.getData() == null : element.equals(currNode.getData())) {
                    return index;
                }
                currNode = currNode.getNext();
                index++;
            }
        }
        return -1;
    }
    //returns true or false depending on if the list is empty
    public boolean isEmpty() {
        return length == 0;
    }
    //returns the size of the list
    public int size() {
        return length;
    }
    //sorts the list using insertion or binary search
    //depending on the state of the list
    public void sort() {
        //Don't sort the list if it is already sorted
        if (isSorted) {
            return;
        }
        Node<T> sortedHead = new Node<>(null);
        Node<T> currNode = head.getNext();
        //sort the list using insertion sort
        while (currNode != null) {
            Node<T> nextNode = currNode.getNext();
            Node<T> prevNode = sortedHead;
            while (prevNode.getNext() != null && currNode.getData().compareTo(prevNode.getNext().getData()) >= 0) {
                prevNode = prevNode.getNext();
            }
            currNode.setNext(prevNode.getNext());
            prevNode.setNext(currNode);
            currNode = nextNode;
        }
        head.setNext(sortedHead.getNext());
        isSorted = true;
    }
    //removes the element at the given index
    public T remove(int index) {
        if (index < 0 || index >= length) {
            return null;
        }
        Node<T> currNode = head;
        int currNodeIndex = 0;
        while (currNodeIndex < index) {
            currNode = currNode.getNext();
            currNodeIndex++;
        }
        Node<T> removedNode = currNode.getNext();
        currNode.setNext(removedNode.getNext());
        length--;
        isSorted = true;
        currNode = head.getNext();
        //loop through the list to check if the removed element
        //unsorted the list
        while (currNode != null && currNode.getNext() != null) {
            if (currNode.getData().compareTo(currNode.getNext().getData()) > 0) {
                isSorted = false;
                break;
            }
            currNode = currNode.getNext();
        }
        return removedNode.getData();
    }
    //removes the elements that are not equal to the given
    //element from the current list
    public void equalTo(T element) {
        if (element == null) {
            return;
        }
        if (isSorted) { //if the array is already sorted use a binary search
            int index = binarySearch(element);
            if (index != -1) {
                remove(index);
            }
        } else { //conduct a linear search if list is not sorted
            Node<T> currNode = head.getNext();
            Node<T> prevNode = head;
            while (currNode != null) {
                if (!element.equals(currNode.getData())) {
                    prevNode.setNext(currNode.getNext());
                    length--;
                } else {
                    prevNode = currNode;
                }
                currNode = currNode.getNext();
            }
            isSorted = true;
        }
    }
    //reverse the current order of the list
    public void reverse() {
        Node<T> prevNode = null;
        Node<T> currNode = head.getNext();
        while (currNode != null) {
            Node<T> nextNode = currNode.getNext();
            currNode.setNext(prevNode);
            prevNode = currNode;
            currNode = nextNode;
        }
        head.setNext(prevNode);
        isSorted = true;
        currNode = head.getNext();
        //loop through the list and see if the reverse sorted the list
        while (currNode != null && currNode.getNext() != null) {
            if (currNode.getData().compareTo(currNode.getNext().getData()) > 0) {
                isSorted = false;
                break;
            }
            currNode = currNode.getNext();
        }
    }
    //takes in another list, compares it to the current list
    //and adds the intersecting elements to a new list
    public void intersect(List<T> otherList) {
        if (otherList == null) {
            return;
        }
        otherList.sort();
        sort();
        isSorted = true;
        LinkedList<T> other = (LinkedList<T>) otherList;
        LinkedList<T> result = new LinkedList<>();
        Node<T> currNode = head.getNext();
        Node<T> otherCurrNode = other.head.getNext();
        //loop through the length of both lists while keeping track of the indexes
        //making sure to compare each element and seeing if they are the same
        //if they are the same, it will be added to the intersect list
        while (currNode != null && otherCurrNode != null) {
            int comparison = currNode.getData().compareTo(otherCurrNode.getData());
            if (comparison < 0) {
                currNode = currNode.getNext();
            } else if (comparison > 0) {
                otherCurrNode = otherCurrNode.getNext();
            } else {
                result.add(currNode.getData());
                currNode = currNode.getNext();
                otherCurrNode = otherCurrNode.getNext();
            }
        }
        //Here we just clear the current list then add the elements
        //of the intersect array to the current list
        clear();
        Node<T> resultCurrNode = result.head.getNext();
        while (resultCurrNode != null) {
            add(resultCurrNode.getData());
            resultCurrNode = resultCurrNode.getNext();
        }
    }
    //sort the element then get the min element of the list
    public T getMin() {
        if (isEmpty()) {
            return null;
        }
        sort();
        return head.getNext().getData();
    }
    //sort the list then get the max element of the list
    public T getMax() {
        if (isEmpty()) {
            return null;
        }
        sort();
        Node<T> currNode = head.getNext();

        while (currNode.getNext() != null) {
            currNode = currNode.getNext();
        }
        return currNode.getData();
    }
    //create a String of the list
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> currNode = head.getNext();
        while (currNode != null) {
            sb.append(currNode.getData()).append("\n");
            currNode = currNode.getNext();
        }
        return sb.toString();
    }
    //return isSorted
    public boolean isSorted() {
        return isSorted;
    }
    //a binary search that is used when isSorted is true
    //this is used so that a more efficient search can be used
    private int binarySearch(T element) {
        int low = 0;
        int high = length - 1;
        while (low <= high) {
            int middle = low + (high - low) / 2;
            T middleElement = get(middle);
            int comparison = element.compareTo(middleElement);
            if (comparison == 0) {
                while (middle > 0 && element.compareTo(get(middle - 1)) == 0) {
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
