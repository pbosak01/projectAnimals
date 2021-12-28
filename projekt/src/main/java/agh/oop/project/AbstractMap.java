package agh.oop.project;

import java.util.*;

public abstract class AbstractMap implements IChangeObserver{
    public final Vector2d lowerLeft;
    public final Vector2d upperRight;
    protected final Vector2d jungleLowerLeft;
    protected final Vector2d jungleUpperRight;
    protected Map<Vector2d,FieldElements> elements = new HashMap<>();
    protected List<Animal> animals = new ArrayList<>();
    protected Map<Gene,Integer> genes = new HashMap<>();
    protected List<Grass> grasses = new ArrayList<>();
    private double deadAnimalsAvgAge =0;
    private int numberOfDeadAnimals =0;
    public boolean flag =true;

    public AbstractMap(int height, int width, double jungleRatio) {
        this.lowerLeft = new Vector2d(0,0);
        this.upperRight = new Vector2d(width,height);
        this.jungleLowerLeft = new Vector2d((int)(width-width*jungleRatio)/2,(int)(height-height*jungleRatio)/2);
        this.jungleUpperRight = new Vector2d((int)(width+width*jungleRatio)/2,(int)(height+height*jungleRatio)/2);
    }

    public abstract boolean canMoveTo(Vector2d position);

    public boolean isOccupied(Vector2d position) {
        return elements.containsKey(position);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, AbstractElement element) {
        removeElement(oldPosition,element);
        addElement(newPosition,element);
    }

    @Override
    public void energyChanged(AbstractElement element) {
        removeElement(element);
        addElement(element.getPosition(), element);
    }

    public void addAnimal(Animal animal){
        animals.add(animal);
        addGene(animal.getGene());
    }
    public void addElement(Vector2d position,AbstractElement element){
        if (elements.containsKey(position)){
            elements.get(position).addElement(element);
        }
        else elements.put(position,new FieldElements(element));
        if (element instanceof Animal animal){
            addGene(animal.getGene());
        }
    }

    public void removeElement(Vector2d position,AbstractElement element){
        if (element instanceof Animal animal){
            removeGene(animal.getGene());
        }
        elements.get(position).removeElement(element);
        if(elements.get(position).isEmpty()){
            elements.remove(position);
        }


    }

    public void removeElement(AbstractElement element){
        if (element instanceof Animal animal){
            removeGene(animal.getGene());
        }
        elements.get(element.getPosition()).removeElement(element);
        if(elements.get(element.getPosition()).isEmpty()){
            elements.remove(element.getPosition());
        }
    }
    private void addGene(Gene gene) {
        int oldValue = genes.getOrDefault(gene,0);
        genes.put(gene,oldValue+1);
    }
    private void removeGene(Gene gene){
        if(genes.get(gene)>1){
            int oldValue = genes.getOrDefault(gene,0);
            genes.put(gene,oldValue-1);
        }
        else { genes.remove(gene);}
    }

    public void move(){
        for (Animal animal : animals){
            animal.move();
            animal.increaseEnergy(-(Animal.moveEnergy));
        }
    }
    private void countAvgAge(int age){
        deadAnimalsAvgAge = (deadAnimalsAvgAge*numberOfDeadAnimals + age)/(numberOfDeadAnimals+1);
        numberOfDeadAnimals +=1;
    }

    public void kill(){
        for(Animal animal : animals){
            if(animal.getEnergy() <= 0) {
                removeElement(animal);
                countAvgAge(animal.getAge());
            }
        }
        animals.removeIf(animal -> animal.getEnergy() <= 0);
    }

    public void addGrass(){
        Random random = new Random();
        ArrayList<Vector2d> jungleFields = new ArrayList<>();
        ArrayList<Vector2d> nonJungleFields = new ArrayList<>();
        int i;
        int j;
        Vector2d vector2d;
        for (i=0;i<= upperRight.x;i++){
            for(j=0;j<= upperRight.y;j++){
                vector2d = new Vector2d(i,j);
                if(isOccupied(vector2d)){
                    continue;
                }
                if(vector2d.follows(jungleLowerLeft) && vector2d.precedes(jungleUpperRight)){
                    jungleFields.add(vector2d);
                }
                else nonJungleFields.add(vector2d);
            }
        }
        if (jungleFields.size()>0){
            Vector2d position = jungleFields.get(random.nextInt(jungleFields.size()));
            Grass grass = new Grass(position);
            addElement(position,grass);
            grasses.add(grass);
        }
        if (nonJungleFields.size()>0){
            Vector2d position = nonJungleFields.get(random.nextInt(nonJungleFields.size()));
            Grass grass = new Grass(position);
            addElement(position,grass);
            grasses.add(grass);
        }
    }

    public void eat(){
        FieldElements[] setList = elements.values().toArray(new FieldElements[0]);

        for ( FieldElements fieldSet  : setList){
            SortedSet<AbstractElement> set = fieldSet.getElements();
            if(set.isEmpty()) {continue;}
            if(set.first() instanceof Grass){continue;}
            if(set.last() instanceof Animal){continue;}
            int counter = 0;
            int maxEnergy = set.first().getEnergy();
            int i =0;
            AbstractElement[] animalList = set.toArray(new AbstractElement[0]);
            while (animalList[i].getEnergy() == maxEnergy && animalList[i] instanceof Animal){
                counter += 1;
                i+=1;
            }
            i=0;
            int energyToAdd=set.last().getEnergy()/counter;
            while(counter!=0){
                ((Animal) animalList[i]).increaseEnergy(energyToAdd);
                counter-=1;
                i+=1;
            }
            grasses.remove((Grass) set.last());
            removeElement(set.last().getPosition(),set.last());
        }
    }
    private void born(Animal mom, Animal dad){
        Gene dadGene = dad.getGene();
        Gene momGene = mom.getGene();
        Random random = new Random();
        boolean i = random.nextBoolean();
        Gene gene;
        if(i){
            gene = momGene.mergeGene(dadGene, mom.getEnergy()*32 / (mom.getEnergy()+ dad.getEnergy()));
        }
        else {
            gene = dadGene.mergeGene(momGene, dad.getEnergy()*32 / (mom.getEnergy() + dad.getEnergy()));
        }
        int startEnergy = (dad.getEnergy()+ mom.getEnergy())/4;
        Animal child = new Animal(MapDirection.drawMapDirection(),dad.getPosition(),this,startEnergy,gene);
        addElement(dad.getPosition(),child);
        addAnimal(child);
        mom.increaseEnergy(-(mom.getEnergy())/4);
        dad.increaseEnergy(-(dad.getEnergy())/4);
        mom.addChild();
        dad.addChild();
    }

    public void reproduction(){
        FieldElements[] setList = elements.values().toArray(new FieldElements[0]);
        for( FieldElements fieldSet  : setList){
            SortedSet<AbstractElement> set = fieldSet.getElements();
            if (set.size()<2){continue;}
            Iterator<AbstractElement> iterator = set.iterator();
            int reproductionEnergyLevel = Animal.startEnergy/2;
            AbstractElement mom = iterator.next();
            AbstractElement dad = iterator.next();
            if (mom.getEnergy() >= reproductionEnergyLevel && dad.getEnergy() >= reproductionEnergyLevel &&
                    mom instanceof Animal momAnimal && dad instanceof Animal dadAnimal){
                born(momAnimal,dadAnimal);
            }
        }
    }
    public Vector2d drawPosition(){
        Random random = new Random();
        int x = random.nextInt(upperRight.x);
        int y = random.nextInt(upperRight.y);
        return new Vector2d(x,y);
    }
    public AbstractElement[] generateElementsList(){
        List<AbstractElement> elementsList = new ArrayList<>();
        elementsList.addAll(animals);
        elementsList.addAll(grasses);
        return elementsList.toArray(elementsList.toArray(new AbstractElement[0]));
    }

    public Vector2d adjustPosition(Vector2d position){
        if(position.precedes(upperRight) && position.follows(lowerLeft))
            return position;
        return new Vector2d((position.x + upperRight.x) % upperRight.x, (position.y+ upperRight.y) % upperRight.y);

    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public boolean isInJungle(Vector2d position){
        return (position.follows(jungleLowerLeft) && position.precedes(jungleUpperRight));
    }

    public Gene getDominantGene(){
        Gene result = null;
        for(Gene gene : genes.keySet()){
            if (result==null){
                result= gene;
            }
            else{
                if(genes.get(result)<genes.get(gene)){
                    result = gene;
                }
            }
        }
        return result;
    }

    public double getAverageEnergy(){
        double sum=0;
        for (Animal animal : animals){
            sum+=animal.getEnergy();
        }
        return sum/ animals.size();
    }
    public int getAnimalsQuantity(){
        return animals.size();
    }

    public int getGrassQuantity(){
        return grasses.size();
    }

    public double getAvgAge(){
        return deadAnimalsAvgAge;
    }

    public double getAvgChildrenQty(){
        double sum=0;
        for (Animal animal : animals){
            sum+=animal.getChildren();
        }
        return sum/ animals.size();
    }

    public AbstractElement[] generateDominantElementList(){
        List<AbstractElement> result = new ArrayList<>();
        for(Animal animal : animals ){
            if(animal.getGene().equals(this.getDominantGene())){
                result.add(animal);
            }
        }
        return result.toArray(new AbstractElement[0]);
    }

}

