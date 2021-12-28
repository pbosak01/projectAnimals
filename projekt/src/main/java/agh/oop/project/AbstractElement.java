package agh.oop.project;

import javafx.scene.image.ImageView;

import java.util.ArrayList;


public abstract class AbstractElement {
    private ArrayList<IChangeObserver> observers = new ArrayList<>();


    public abstract Vector2d getPosition();

    public void addObserver(IChangeObserver observer){
        for (IChangeObserver observ : observers){
            if (observ.equals(observer)) return;
        }
        this.observers.add(observer);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, AbstractElement element){
        for (IChangeObserver observer : observers){
            observer.positionChanged(oldPosition,newPosition,element);
        }
    }

    public ImageView getImage(){
        if(this instanceof Animal animal){
            return animal.getImage();
        }
        else{
            Grass grass = (Grass) this;
            return grass.imageView;
        }
    }

    public void energyChanged(){
        for (IChangeObserver observer : observers){
            observer.energyChanged(this);
        }
    }
    public abstract int getEnergy();



}
