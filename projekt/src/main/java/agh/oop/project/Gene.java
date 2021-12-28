package agh.oop.project;

import java.util.Arrays;
import java.util.Random;

public class Gene {
    public int[] genes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gene gene = (Gene) o;
        return Arrays.equals(genes, gene.genes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genes);
    }

    public Gene(){
        genes = new int[32];
        Random random = new Random();
        int i;
        for (i=0;i<32;i++){
            genes[i]= random.nextInt(8);
        }
        Arrays.sort(genes);

    }

    public Gene(int[] genes){
        this.genes = genes;
    }

    public int drawGene(){
        Random random = new Random();
        return genes[random.nextInt(32)];
    }

    public Gene mergeGene(Gene dad, int split){
        int[] result = new int[32];
        int i = 0;
        while(i<split){
            result[i] = genes[i];
            i +=1;
        }
        while(i<32){
            result[i] = dad.genes[i];
            i+=1;
        }
        return new Gene(result);
    }

    @Override
    public String toString() {
        return Arrays.toString(genes);
    }
}
