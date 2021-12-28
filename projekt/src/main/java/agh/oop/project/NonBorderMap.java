package agh.oop.project;

public class NonBorderMap extends AbstractMap{


    public NonBorderMap(int height, int width, double jungleRatio) {
        super(height, width, jungleRatio);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }
}
