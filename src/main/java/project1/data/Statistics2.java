package project1.data;

/**
 * Regular POJO to export to json.
 */
public class Statistics2 {

    private int aliveAnimalsCount = 0;
    private int presentBushCount = 0;
    private int childrenSum = 0;
    private int energySum = 0;
    private int ageSum = 0;
    private int deadAgeSum = 0;
    private int deadCount = 0;

    private final int[] genes = new int[8];
    private int bushCount = 0;

    private float energyAverage = 0.0f;
    private float ageAverage = 0.0f;
    private float childrenAverage = 0.0f;
    private float deathAgeAverage = 0.0f;


    public int getAliveAnimalsCount() {
        return aliveAnimalsCount;
    }

    public void setAliveAnimalsCount(int aliveAnimalsCount) {
        this.aliveAnimalsCount = aliveAnimalsCount;
    }

    public int getPresentBushCount() {
        return presentBushCount;
    }

    public void setPresentBushCount(int presentBushCount) {
        this.presentBushCount = presentBushCount;
    }

    public int getChildrenSum() {
        return childrenSum;
    }

    public void setChildrenSum(int childrenSum) {
        this.childrenSum = childrenSum;
    }

    public int getEnergySum() {
        return energySum;
    }

    public void setEnergySum(int energySum) {
        this.energySum = energySum;
    }

    public int getAgeSum() {
        return ageSum;
    }

    public void setAgeSum(int ageSum) {
        this.ageSum = ageSum;
    }

    public int getDeadAgeSum() {
        return deadAgeSum;
    }

    public void setDeadAgeSum(int deadAgeSum) {
        this.deadAgeSum = deadAgeSum;
    }

    public int getDeadCount() {
        return deadCount;
    }

    public void setDeadCount(int deadCount) {
        this.deadCount = deadCount;
    }

    public int[] getGenes() {
        return genes;
    }

    public void setGenes(int[] genes) {
        System.arraycopy(genes, 0, this.genes, 0, 8);
    }

    public int getBushCount() {
        return bushCount;
    }

    public void setBushCount(int bushCount) {
        this.bushCount = bushCount;
    }

    public float getEnergyAverage() {
        return energyAverage;
    }

    public void setEnergyAverage(float energyAverage) {
        this.energyAverage = energyAverage;
    }

    public float getAgeAverage() {
        return ageAverage;
    }

    public void setAgeAverage(float ageAverage) {
        this.ageAverage = ageAverage;
    }

    public float getChildrenAverage() {
        return childrenAverage;
    }

    public void setChildrenAverage(float childrenAverage) {
        this.childrenAverage = childrenAverage;
    }

    public float getDeathAgeAverage() {
        return deathAgeAverage;
    }

    public void setDeathAgeAverage(float deathAgeAverage) {
        this.deathAgeAverage = deathAgeAverage;
    }
}
