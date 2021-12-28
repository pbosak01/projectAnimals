package agh.oop.project;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

import static java.lang.Math.min;
import static java.lang.Math.max;


public class Animal extends AbstractElement{
    public static int startEnergy;
    public static int moveEnergy;
    private MapDirection orientation;
    private Vector2d position;
    private final AbstractMap map;
    private final Gene gene;
    private int energy;
    private ImageView imageView;
    private int age = 0;
    private int children = 0;

    public Animal(MapDirection orientation, Vector2d position, AbstractMap map,int energy,Gene gene) {
        this.orientation = orientation;
        this.position = position;
        this.map = map;
        this.energy = energy;
        this.gene = gene;
        super.addObserver(map);
        setImageView();
    }
    private void setImageView() {
        Image image = null;
        try {
            image = new Image(new FileInputStream("src/main/resources/animal.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView = new ImageView(image);
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);
    }

    public ImageView getImage() {
        imageView.setFitWidth(max(min(15,15*(energy*1.0/startEnergy)),1));
        imageView.setFitHeight(max(min(15,15*(energy*1.0/startEnergy)),1));
        return imageView;
    }

    public Animal(AbstractMap map){
        this.orientation = MapDirection.drawMapDirection();
        this.position = map.drawPosition();
        this.map=map;
        this.energy = startEnergy;
        this.gene = new Gene();
        super.addObserver(map);
        setImageView();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return energy == animal.energy && orientation == animal.orientation && Objects.equals(position, animal.position) && Objects.equals(map, animal.map) && Objects.equals(gene, animal.gene);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orientation, position, map, gene);
    }

    public static void setEnergy(int startEnergy, int diffEnergy){
        Animal.moveEnergy = diffEnergy;
        Animal.startEnergy = startEnergy;
    }

    public void increaseEnergy(int energy){

        this.energy += energy;
        super.energyChanged();
    }

    public Gene getGene() {
        return gene;
    }

    @Override
    public Vector2d getPosition(){
        return this.position;
    }

    public void addChild(){
        children +=1;
    }

    public void move(){
        age +=1 ;
        Vector2d unitVector = this.orientation.toUnitVector();
        Vector2d oldPosition = this.position;
        switch (gene.drawGene()){
            case 0:
                if(this.map.canMoveTo(this.position.add(unitVector))){
                    this.position = map.adjustPosition(this.position.add(unitVector));
                    positionChanged(oldPosition,this.position,this);
                }
                break;
            case 1:
                this.orientation = this.orientation.next();
                break;
            case 2:
                this.orientation = this.orientation.next().next();
                break;
            case 3:
                this.orientation = this.orientation.next().next().next();
                break;
            case 4:
                if(this.map.canMoveTo(this.position.subtract(unitVector))){
                    this.position = map.adjustPosition(this.position.subtract(unitVector));
                    positionChanged(oldPosition,this.position,this);
                }
                break;
            case 5:
                this.orientation = this.orientation.previous().previous().previous();
                break;
            case 6:
                this.orientation = this.orientation.previous().previous();
                break;
            case 7:
                this.orientation = this.orientation.previous();

        }
    }

    public int getAge() {
        return age;
    }

    public int getChildren() {
        return children;
    }

    @Override
    public int getEnergy() {
        return this.energy;
    }
}
