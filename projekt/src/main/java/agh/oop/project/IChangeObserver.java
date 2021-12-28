package agh.oop.project;

public interface IChangeObserver {

    void positionChanged(Vector2d oldPosition, Vector2d newPosition, AbstractElement element);

    void energyChanged(AbstractElement element);
}
