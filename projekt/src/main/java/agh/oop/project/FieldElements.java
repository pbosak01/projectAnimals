package agh.oop.project;

import java.util.SortedSet;
import java.util.TreeSet;

public class FieldElements {
    private SortedSet<AbstractElement> elements = new TreeSet<>(new ElementComparator());

    public FieldElements(AbstractElement element){
        elements.add(element);
    }

    public void addElement(AbstractElement element) {
        elements.add(element);
    }

    public void removeElement(AbstractElement element){
        elements.remove(element);
    }
    public boolean isEmpty(){
        return elements.isEmpty();
    }

    public SortedSet<AbstractElement> getElements() {
        return elements;
    }
}
