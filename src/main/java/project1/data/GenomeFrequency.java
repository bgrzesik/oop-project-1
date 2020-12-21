package project1.data;

import org.jetbrains.annotations.NotNull;

public class GenomeFrequency implements Comparable<GenomeFrequency> {
    private final String genome;
    private int frequency = 0;

    public GenomeFrequency(String genome) {
        this.genome = genome;
    }

    public String getGenome() {
        return genome;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void incrementFrequency() {
        this.frequency += 1;
    }

    @Override
    public int compareTo(@NotNull GenomeFrequency o) {
        return o.frequency - this.frequency;
    }
}
