package project1.system;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import project1.data.GenomeFrequency;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Statistics implements Json.Serializable {
    // package access all for StatisticsSystem
    // to avoid too many setters and make code cleaner
    // and make properties read only for the rest
    int aliveAnimalsCount;
    int presentBushCount;

    int childrenSum;
    int energySum;
    int ageSum;

    int deadEnergySum = 0;
    int deadChildrenSum = 0;
    int deadAgeSum = 0;

    int deadCount = 0;
    int eatenCount = 0;

    final int[] genes = new int[8];
    final int[] deadGenes = new int[8];
    Map<String, GenomeFrequency> genomes = new TreeMap<>();
    Map<String, GenomeFrequency> deadGenomes = new TreeMap<>();

    public int getAliveAnimalsCount() {
        return aliveAnimalsCount;
    }

    public int getPresentBushCount() {
        return presentBushCount;
    }

    public float getEnergyAverage() {
        if (aliveAnimalsCount == 0) {
            return 0;
        }
        return energySum / (float) aliveAnimalsCount;
    }

    public float getAgeAverage() {
        if (aliveAnimalsCount == 0) {
            return 0;
        }
        return ageSum / (float) aliveAnimalsCount;
    }

    public float getChildrenAverage() {
        if (aliveAnimalsCount == 0) {
            return 0;
        }
        return childrenSum / (float) aliveAnimalsCount;
    }

    public float getDeathAgeAverage() {
        if (deadCount == 0) {
            return 0;
        }
        return deadAgeSum / (float) deadCount;
    }

    public int[] getGenes() {
        return genes;
    }

    public int getDeadCount() {
        return deadCount;
    }

    public int getTotalBushCount() {
        return eatenCount + presentBushCount;
    }

    public int getTotalAnimalCount() {
        return deadCount + aliveAnimalsCount;
    }

    public float getTotalEnergyAverage() {
        if (getTotalAnimalCount() == 0) {
            return 0;
        }
        return (energySum + deadEnergySum) / (float) getTotalAnimalCount();
    }

    public float getTotalAgeAverage() {
        if (getTotalAnimalCount() == 0) {
            return 0;
        }
        return (ageSum + deadAgeSum) / (float) getTotalAnimalCount();
    }

    public float getTotalChildrenAverage() {
        if (getTotalAnimalCount() == 0) {
            return 0;
        }
        return (childrenSum + deadChildrenSum) / (float) getTotalAnimalCount();
    }

    public Map<String, GenomeFrequency> getGenomeFrequency() {
        return genomes;
    }

    @Override
    protected Object clone() {
        Statistics stat = new Statistics();
        stat.aliveAnimalsCount = this.aliveAnimalsCount;
        stat.presentBushCount = this.presentBushCount;
        stat.childrenSum = this.childrenSum;
        stat.energySum = this.energySum;
        stat.ageSum = this.ageSum;
        stat.deadEnergySum = this.deadEnergySum;
        stat.deadChildrenSum = this.deadChildrenSum;
        stat.deadAgeSum = this.deadAgeSum;
        stat.deadCount = this.deadCount;
        stat.eatenCount = this.eatenCount;


        this.genomes.forEach((k, v) -> {
            stat.genomes.put(k, new GenomeFrequency(k, v.getFrequency()));
        });

        this.deadGenomes.forEach((k, v) -> {
            stat.deadGenomes.put(k, new GenomeFrequency(k, v.getFrequency()));
        });

        System.arraycopy(this.genes, 0, stat.genes, 0, 8);
        System.arraycopy(this.deadGenes, 0, stat.deadGenes, 0, 8);

        return stat;
    }

    @Override
    public void write(Json json) {
        // adding additional fields

        json.writeValue("aliveAnimalsCount", aliveAnimalsCount);
        json.writeValue("presentBushCount", presentBushCount);
        json.writeValue("childrenSum", childrenSum);
        json.writeValue("energySum", energySum);
        json.writeValue("ageSum", ageSum);
        json.writeValue("deadEnergySum", deadEnergySum);
        json.writeValue("deadChildrenSum", deadChildrenSum);
        json.writeValue("deadAgeSum", deadAgeSum);
        json.writeValue("deadCount", deadCount);
        json.writeValue("eatenCount", eatenCount);
        json.writeValue("genes", this.genes);
        json.writeValue("deadGenes", this.genes);
        json.writeValue("totalBushCount", this.getTotalBushCount());
        json.writeValue("totalAnimalCount", this.getTotalAnimalCount());
        json.writeValue("totalEnergyAverage", this.getTotalEnergyAverage());
        json.writeValue("totalAgeAverage", this.getTotalAgeAverage());
        json.writeValue("totalChildrenAverage", this.getTotalChildrenAverage());
        json.writeValue("energyAverage", this.getEnergyAverage());
        json.writeValue("ageAverage", this.getAgeAverage());
        json.writeValue("childrenAverage", this.getChildrenAverage());
        json.writeValue("deathAgeAverage", this.getDeathAgeAverage());

        json.writeObjectStart("genomes");
        genomes.forEach((k, v) -> {
            json.writeValue(k, v.getFrequency());
        });
        json.writeObjectEnd();

        json.writeObjectStart("deadGenomes");
        deadGenomes.forEach((k, v) -> {
            json.writeValue(k, v.getFrequency());
        });
        json.writeObjectEnd();

        Set<String> allGenomes = new HashSet<>();
        allGenomes.addAll(genomes.keySet());
        allGenomes.addAll(deadGenomes.keySet());

        json.writeObjectStart("totalGenomes");
        for (String genome : allGenomes) {
            int freq = 0;

            if (genomes.containsKey(genome)) {
                freq += genomes.get(genome).getFrequency();
            }

            if (deadGenomes.containsKey(genome)) {
                freq += deadGenomes.get(genome).getFrequency();
            }

            json.writeValue(genome, freq);
        }
        json.writeObjectEnd();

    }

    @Override
    public void read(Json json, JsonValue jsonData) {
    }
}
