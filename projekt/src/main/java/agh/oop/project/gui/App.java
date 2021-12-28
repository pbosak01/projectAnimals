package agh.oop.project.gui;

import agh.oop.project.*;
import com.sun.javafx.scene.control.DoubleField;
import com.sun.javafx.scene.control.IntegerField;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.FileNotFoundException;

public class App extends Application {
    Stage stage;
    VBox vBox1 = new VBox();
    VBox vBox2 = new VBox();
    HBox generalBox = new HBox();
    GridPane gridPane1 = new GridPane();
    GridPane gridPane2 = new GridPane();
    AbstractMap nonBorderMap;
    AbstractMap borderMap;
    VBox buttonBox1 = new VBox();
    VBox buttonBox2 = new VBox();
    NumberAxis xAxisAnimals1 = new NumberAxis();
    NumberAxis xAxisAnimals2 = new NumberAxis();
    NumberAxis yAxisAnimals1 = new NumberAxis();
    NumberAxis yAxisAnimals2 = new NumberAxis();
    NumberAxis xAxisGrass1 = new NumberAxis();
    NumberAxis xAxisGrass2 = new NumberAxis();
    NumberAxis yAxisGrass1 = new NumberAxis();
    NumberAxis yAxisGrass2 = new NumberAxis();
    LineChart<Number, Number> animalsQtyChart1 = new LineChart<>(xAxisAnimals1, yAxisAnimals1);
    LineChart<Number, Number> animalsQtyChart2 = new LineChart<>(xAxisAnimals2, yAxisAnimals2);
    LineChart<Number, Number> grassQtyChart1 = new LineChart<>(xAxisGrass1, yAxisGrass1);
    LineChart<Number, Number> grassQtyChart2 = new LineChart<>(xAxisGrass2, yAxisGrass2);
    SimulationEngine engine;

    @Override
    public void start(Stage primaryStage)  {
        stage = primaryStage;

        VBox vBox = new VBox();
        Label welcome = new Label("Enter parameters");
        HBox mapWidthBox = new HBox();
        Label mapWidthLabel = new Label("Map width: ");
        IntegerField mapWidthField = new IntegerField();
        mapWidthField.setValue(20);
        mapWidthField.setEditable(true);
        mapWidthBox.getChildren().addAll(mapWidthLabel,mapWidthField);
        mapWidthBox.setAlignment(Pos.BASELINE_LEFT);

        HBox mapHeightBox = new HBox();
        IntegerField mapHeightField = new IntegerField();
        mapHeightField.setEditable(true);
        mapHeightField.setValue(20);
        Label mapHeightLabel = new Label("Map height: ");
        mapHeightBox.getChildren().addAll(mapHeightLabel,mapHeightField);
        mapHeightBox.setAlignment(Pos.BASELINE_LEFT);

        HBox jungleRatioBox = new HBox();
        DoubleField jungleRatioField = new DoubleField();
        jungleRatioField.setEditable(true);
        jungleRatioField.setValue(0.5);
        Label jungleRatioLabel = new Label("Jungle ratio: ");
        jungleRatioBox.getChildren().addAll(jungleRatioLabel,jungleRatioField);
        jungleRatioBox.setAlignment(Pos.BASELINE_LEFT);

        HBox animalsBox = new HBox();
        IntegerField animalsField = new IntegerField();
        animalsField.setEditable(true);
        animalsField.setValue(10);
        Label animalsLabel = new Label("Quantity of animals: ");
        animalsBox.getChildren().addAll(animalsLabel,animalsField);
        animalsBox.setAlignment(Pos.BASELINE_LEFT);

        HBox animalsEnergyBox = new HBox();
        IntegerField animalsEnergyField = new IntegerField();
        animalsEnergyField.setEditable(true);
        animalsEnergyField.setValue(100);
        Label animalsEnergyLabel = new Label("Energy at start: ");
        animalsEnergyBox.getChildren().addAll(animalsEnergyLabel,animalsEnergyField);
        animalsEnergyBox.setAlignment(Pos.BASELINE_LEFT);

        HBox moveEnergyBox = new HBox();
        IntegerField moveEnergyField = new IntegerField();
        moveEnergyField.setEditable(true);
        moveEnergyField.setValue(3);
        Label moveEnergyLabel = new Label("Move energy: ");
        moveEnergyBox.getChildren().addAll(moveEnergyLabel,moveEnergyField);
        moveEnergyBox.setAlignment(Pos.BASELINE_LEFT);

        HBox grassEnergyBox = new HBox();
        IntegerField grassEnergyField = new IntegerField();
        grassEnergyField.setEditable(true);
        grassEnergyField.setValue(50);
        Label grassEnergyLabel = new Label("Grass energy: ");
        grassEnergyBox.getChildren().addAll(grassEnergyLabel,grassEnergyField);
        grassEnergyBox.setAlignment(Pos.BASELINE_LEFT);

        Button startButton = new Button("Start");
        startButton.setAlignment(Pos.BASELINE_CENTER);
        startButton.setMinHeight(50);
        startButton.setMinWidth(100);
        vBox.getChildren().addAll(welcome,mapWidthBox,mapHeightBox,jungleRatioBox,animalsBox,animalsEnergyBox,
                moveEnergyBox,grassEnergyBox,startButton);
        vBox.setSpacing(10);

        Scene scene = new Scene(generalBox, 1200, 700);
        Scene startScene = new Scene(vBox,600,600);
        startButton.setOnAction(e -> startSimulation(scene,mapHeightField,mapWidthField,jungleRatioField,animalsField,
                animalsEnergyField,moveEnergyField,grassEnergyField));
        stage.setResizable(true);
        stage.setScene(startScene);
        stage.show();
    }

    private void startSimulation(Scene scene,IntegerField mapHeightField,IntegerField mapWidthField,DoubleField jungleRatioField,
    IntegerField animalsField,IntegerField animalsEnergyField,IntegerField moveEnergyField,IntegerField grassEnergyField){
        int mapHeight = mapHeightField.getValue();
        int mapWidth = mapWidthField.getValue();
        double jungleRatio = jungleRatioField.getValue();
        int animals = animalsField.getValue();
        int animalsEnergy = animalsEnergyField.getValue();
        int moveEnergy = moveEnergyField.getValue();
        int grassEnergy = grassEnergyField.getValue();
        nonBorderMap = new NonBorderMap(mapHeight,mapWidth, jungleRatio);
        borderMap = new BorderMap(mapHeight,mapWidth, jungleRatio);
        xAxisAnimals1.setLabel("day");
        xAxisAnimals2.setLabel("day");
        yAxisAnimals1.setLabel("animals");
        yAxisAnimals2.setLabel("animals");
        xAxisGrass1.setLabel("days");
        xAxisGrass2.setLabel("days");
        yAxisGrass1.setLabel("grasses");
        yAxisGrass2.setLabel("grasses");
        Grass.setGrassEnergy(grassEnergy);
        Animal.setEnergy(animalsEnergy,moveEnergy);
        engine = new SimulationEngine(nonBorderMap,borderMap,animals,this);
        Thread engineThread = new Thread(engine);
        engineThread.start();
        stage.setScene(scene);
    }

    public void drawAll(AbstractMap map1, AbstractMap map2)  {
        Platform.runLater(() -> {
            if(map1.flag){
                drawMap(map1,gridPane1,false);
                drawButton(map1,gridPane1,buttonBox1);
                drawCharts(map1,animalsQtyChart1,grassQtyChart1);
            }
            if(map2.flag){
                drawMap(map2,gridPane2,false);
                drawButton(map2,gridPane2,buttonBox2);
                drawCharts(map2,animalsQtyChart2,grassQtyChart2);
            }
            generalBox.getChildren().clear();
            vBox1.getChildren().clear();
            vBox2.getChildren().clear();

            vBox1.getChildren().addAll(gridPane1,drawAnimalsQuantity(map1),drawGrassQuantity(map1),
                    drawGene(map1),drawAvgEnergy(map1),drawAnimalsAge(map1),drawChildrenQty(map1),buttonBox1, animalsQtyChart1,grassQtyChart1);
            vBox2.getChildren().addAll(gridPane2,drawAnimalsQuantity(map2),drawGrassQuantity(map2),
                    drawGene(map2),drawAvgEnergy(map2),drawAnimalsAge(map2),drawChildrenQty(map2),buttonBox2,animalsQtyChart2,grassQtyChart2);
            generalBox.getChildren().addAll(vBox1,vBox2);
            generalBox.setSpacing(50);
        });
    }
    private void drawCharts(AbstractMap map,LineChart<Number,Number> animalsQtyChart,LineChart<Number,Number> grassQtyChart){

        XYChart.Series seriesAnimals = new XYChart.Series();
        seriesAnimals.getData().add(new XYChart.Data(engine.getDay(),map.getAnimals().size()));
        animalsQtyChart.getData().add(seriesAnimals);


        XYChart.Series seriesGrass = new XYChart.Series();
        seriesGrass.getData().add(new XYChart.Data(engine.getDay(),map.getGrassQuantity()));
        grassQtyChart.getData().add(seriesGrass);
    }

    private void drawButton(AbstractMap map,GridPane gridPane,VBox buttonBox) {
        buttonBox.getChildren().clear();
        String start = "Start";
        String stop = "Stop";
        Button stopButton = new Button(stop);
        if(map.flag) {
            stopButton.setText(stop);
        }
        else{
            stopButton.setText(start);
        }
        buttonBox.getChildren().add(stopButton);
        stopButton.setOnAction(event -> {
            map.flag = !map.flag;
            if(map.flag){
                stopButton.setText(stop);
            }
            else{
                stopButton.setText(start);
                Button dominantAnimals = new Button("Show dominant animals");
                dominantAnimals.setOnAction(event1 -> drawMap(map,gridPane,true));
                buttonBox.getChildren().add(dominantAnimals);
            }

        });
    }

    private void drawMap(AbstractMap map,GridPane gridPane,Boolean drawDomiant){
        gridPane.getChildren().clear();
        AbstractElement[] elements;
        if(drawDomiant){
            elements = map.generateDominantElementList();
        }
        else {
            elements = map.generateElementsList();
        }
        Vector2d upperRight = map.upperRight;
        Vector2d lowerLeft = map.lowerLeft;
        drawBackground(upperRight,lowerLeft,map,gridPane);
        try {
            drawObjects(elements, upperRight, lowerLeft,gridPane);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Label drawAnimalsAge(AbstractMap map){
        return new Label("Average age: " + map.getAvgAge());
    }
    private Label drawChildrenQty(AbstractMap map){
        return new Label("Average quantity of children: " + map.getAvgChildrenQty());
    }

    private Label drawAnimalsQuantity(AbstractMap map){
        return new Label("Animals quantity: " + map.getAnimalsQuantity());
    }
    private Label drawGrassQuantity(AbstractMap map){
        return new Label("Grasses quantity: " + map.getGrassQuantity());
    }
    private Label drawGene(AbstractMap map){
        return new Label("Dominant: " + map.getDominantGene().toString());
    }
    private Label drawAvgEnergy(AbstractMap map1){
        return new Label("Average energy: " + map1.getAverageEnergy());
    }

    private void drawBackground(Vector2d upperRight,Vector2d lowerLeft,AbstractMap map,GridPane gridPane){
        for (int i = lowerLeft.x;i<=upperRight.x;i++){
            for (int j = upperRight.y;j>=lowerLeft.y;j--){
                Rectangle rectangle;
                if(map.isInJungle(new Vector2d(i,j))) {
                    rectangle = new Rectangle(15,15,Color.DARKGREEN);
                    gridPane.add(rectangle,i,j);
                }
                else if ((map instanceof BorderMap)&&(i == lowerLeft.x  || i== upperRight.x || j == lowerLeft.y  || j== upperRight.y )){
                    rectangle = new Rectangle(15,15,Color.BROWN);
                    gridPane.add(rectangle,i,j);
                }
                else{
                    rectangle = new Rectangle(15,15,Color.GREEN);
                    gridPane.add(rectangle,i,j);
                }
            }
        }
    }

    private void drawObjects(AbstractElement[] elements, Vector2d upperRight,Vector2d lowerLeft,GridPane gridPane) throws FileNotFoundException {
        for(AbstractElement element : elements) {
            ImageView imageView = element.getImage();
            imageView.setOnMouseClicked(e -> System.out.println(element.getEnergy()));
            try {
                gridPane.add(imageView, element.getPosition().x - lowerLeft.x , upperRight.y - element.getPosition().y );
                GridPane.setHalignment(imageView, HPos.CENTER);
            }catch(IllegalArgumentException ignored){}
        }
    }
}
