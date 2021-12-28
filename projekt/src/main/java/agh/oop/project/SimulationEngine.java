package agh.oop.project;

import agh.oop.project.gui.App;

public class SimulationEngine implements  Runnable {
    private final AbstractMap map1;
    private final AbstractMap map2;
    private final App app;
    private int day =0;



    public SimulationEngine(AbstractMap map1,AbstractMap map2, int animalsAtStart, App app) {
        this.map1 = map1;
        this.map2 = map2;
        this.app = app;
        while (animalsAtStart > 0) {
            Animal animal1 = new Animal(map1);
            Animal animal2 = new Animal(map2);
            map1.addElement(animal1.getPosition(), animal1);
            map2.addElement(animal2.getPosition(), animal2);
            map1.addAnimal(animal1);
            map2.addAnimal(animal2);
            animalsAtStart -= 1;
        }
    }

    public void run() {
        while(!map1.animals.isEmpty() || !map2.animals.isEmpty()) {
            day +=1;
            if (map1.flag) {
                map1.kill();
                map1.move();
                map1.eat();
                map1.reproduction();
                map1.addGrass();
                map1.flag = !map1.animals.isEmpty();
            }
            if(map2.flag){
                map2.kill();
                map2.move();
                map2.eat();
                map2.reproduction();
                map2.addGrass();
                map2.flag = !map2.animals.isEmpty();
            }
            app.drawAll(map1,map2);
            try {
                Thread.sleep(300);
            } catch (InterruptedException exception) {
                System.out.println(exception.getMessage());
            }
        }
        System.out.println("koniec");
    }

    public int getDay(){
        return day;
    }
}


