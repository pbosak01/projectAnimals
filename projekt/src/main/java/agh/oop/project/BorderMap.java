package agh.oop.project;

public class BorderMap extends AbstractMap{


    public BorderMap(int height, int width, double jungleRatio) {
        super(height, width, jungleRatio);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (position.precedes(super.upperRight)
                && position.follows(super.lowerLeft));
    }
}
