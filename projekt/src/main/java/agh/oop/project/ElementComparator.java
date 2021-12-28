package agh.oop.project;

import java.util.Comparator;

public class ElementComparator implements Comparator<AbstractElement> {

    @Override
    public int compare(AbstractElement o1, AbstractElement o2) {
        if (o1==o2) return 0;
        if (o1 instanceof Grass && o2 instanceof Grass){
            return 0;
        }
        if (o1 instanceof Grass){
            return 1;
        }
        if (o2 instanceof Grass){
            return -1;
        }
        if (o1.getEnergy() - o2.getEnergy() == 0){
            return 1;
        }
        return o1.getEnergy() - o2.getEnergy();
    }
}
