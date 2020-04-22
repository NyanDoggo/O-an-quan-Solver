package main;

public class DoublyLinkedList<E> {
    private Node head;
    private Node tail;
    private int size;

    public DoublyLinkedList(){
        size = 0;
    }

    private class Node{
        E element;
        Node next;
        Node previous;
        int index;

        public Node (E element, Node next, Node previous){
            this.element = element;
            this.next = next;
            this.previous = previous;
        }
    }

    //add to tail
    public void add(E element){
        Node tmp = new Node(element, null, tail);
        //if head == null means list is empty
        if (head == null){
            head = tmp;
        }

        if (tail != null){
            tail.next = tmp;
        }
        size++;
    }


}