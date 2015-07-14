package com.example.brich200.mobileideasportal;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Created by brich200 on 7/10/15.
 */
public class IdeaImages {
    Node first;

    public IdeaImages(){
        first = null;
    }

    public Node getFirst() {
        return first;
    }

    public Node setFirstToNext() {
        first = first.getNext();
        return first;
    }

    public void addImage(byte[] image) {
        if(first == null) {
            first = new Node(image);
        } else {
            first = new Node(image, first);
        }
    }

    public void addImage(byte[] image, int imageId) {
        if(first == null) {
            first = new Node(image);
        } else {
            first = new Node(image, imageId, first);
        }
    }

    public byte[] getImageById(int imageId) {
        Node current = first;
        while(current.getImageId() != imageId && current != null){
            current = current.getNext();
        }
        if (current == null){
            throw  new NoSuchElementException();
        } else {
            return current.getImage();
        }
    }

    public byte[] getFirstImage(){
        return first.getImage();
    }

    public void setImageId(byte[] image, int imageId) {
        Node current = first;
        while(!current.getImage().equals(image) && current != null){
            current = current.getNext();
        }
        if (current == null){
            throw  new NoSuchElementException();
        } else {
            current.setImageId(imageId);
        }
    }

    public int removeImage(byte [] image) {
        Node current = first;
        while(!current.getImage().equals(image) && current != null){
            current = current.getNext();
        }
        if (current == null){
            throw  new NoSuchElementException();
        } else {
            int id = current.getImageId();
            current = current.getNext();
            return id;
        }

    }

    private class Node{
        Node next;
        byte [] image;
        int imageId;

        public void setImage(byte[] image) {
            this.image = image;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public void setImageId(int imageId) {
            this.imageId = imageId;
        }

        public byte[] getImage() {
            return image;
        }

        public int getImageId() {
            return imageId;
        }

        public Node getNext() {
            return next;
        }

        Node() {
            setNext(null);
            setImage(null);
            setImageId(-1);
        }
        Node(byte [] image) {
            setNext(null);
            setImage(image);
            setImageId(-1);
        }
        Node(byte [] image, int imageId, Node next) {
            setNext(next);
            setImage(image);
            setImageId(imageId);
        }
        Node(byte [] image, Node next) {
            setNext(next);
            setImage(image);
            setImageId(-1);
        }
    }


}
