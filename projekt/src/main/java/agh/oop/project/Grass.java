package agh.oop.project;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Grass extends AbstractElement {
    private static int plantEnergy;
    private final Vector2d position;
    public ImageView imageView;

    public Grass(Vector2d position) {
        this.position = position;
        setImageView();
    }

    public static void setGrassEnergy(int startEnergy){
        Grass.plantEnergy = startEnergy;
    }

    public void setImageView() {
        Image image = null;
        try {
            image = new Image(new FileInputStream("src/main/resources/grass.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView = new ImageView(image);
        imageView.setFitHeight(13);
        imageView.setFitWidth(13);
    }

    @Override
    public int getEnergy() {
        return plantEnergy;
    }


    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "G";
    }
}
