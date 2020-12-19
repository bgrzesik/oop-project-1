package project1.data;

public class SimulationConfig {
    private int minimalBreedEnergy = 20;
    private int spawnBushEnergy = 10;
    private int spawnInJungle = 1;
    private int spawnOutsideJungle = 1;
    private int moveEnergy = 1;
    private int worldWidth = 100;
    private int worldHeight = 30;
    private int jungleWidth = 10;
    private int jungleHeight = 10;
    private float parentEnergyPart = 0.25f;

    private boolean spawnSystemOn = true;
    private boolean moveSystemOn = true;
    private boolean deathSystemOn = true;
    private boolean statisticsSystemOn = true;
    private boolean breedingSystemOn = true;
    private boolean feedingSystemOn = true;

    private SimulationConfig() {
    }

    public int getMinimalBreedEnergy() {
        return minimalBreedEnergy;
    }

    public int getSpawnBushEnergy() {
        return spawnBushEnergy;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public int getSpawnInJungle() {
        return spawnInJungle;
    }

    public int getSpawnOutsideJungle() {
        return spawnOutsideJungle;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public int getJungleWidth() {
        return jungleWidth;
    }

    public int getJungleHeight() {
        return jungleHeight;
    }

    public float getParentEnergyPart() {
        return parentEnergyPart;
    }

    public boolean isBreedingSystemOn() {
        return breedingSystemOn;
    }

    public boolean isDeathSystemOn() {
        return deathSystemOn;
    }

    public boolean isFeedingSystemOn() {
        return feedingSystemOn;
    }

    public boolean isMoveSystemOn() {
        return moveSystemOn;
    }

    public boolean isSpawnSystemOn() {
        return spawnSystemOn;
    }

    public boolean isStatisticsSystemOn() {
        return statisticsSystemOn;
    }

    public static class Builder {
        private final SimulationConfig config = new SimulationConfig();

        public Builder setMinimalBreedEnergy(int minimalBreedEnergy) {
            this.config.minimalBreedEnergy = minimalBreedEnergy;
            return this;
        }

        public Builder setSpawnBushEnergy(int spawnBushEnergy) {
            this.config.spawnBushEnergy = spawnBushEnergy;
            return this;
        }

        public Builder setMoveEnergy(int moveEnergy) {
            this.config.moveEnergy = moveEnergy;
            return this;
        }

        public Builder setSpawnInJungle(int spawnInJungle) {
            this.config.spawnInJungle = spawnInJungle;
            return this;
        }

        public Builder setSpawnOutsideJungle(int spawnOutsideJungle) {
            this.config.spawnOutsideJungle = spawnOutsideJungle;
            return this;
        }

        public Builder setParentEnergyPart(float parentEnergyPart) {
            this.config.parentEnergyPart = parentEnergyPart;
            return this;
        }

        public Builder setBreedingSystemOn(boolean breedingSystemO) {
            this.config.breedingSystemOn = breedingSystemO;
            return this;
        }

        public Builder setDeathSystemOn(boolean deathSystemOn) {
            this.config.deathSystemOn = deathSystemOn;
            return this;
        }

        public Builder setFeedingSystemOn(boolean feedingSystemOn) {
            this.config.feedingSystemOn = feedingSystemOn;
            return this;
        }

        public Builder setMoveSystemOn(boolean moveSystemOn) {
            this.config.moveSystemOn = moveSystemOn;
            return this;
        }

        public Builder setSpawnSystemOn(boolean spawnSystemOn) {
            this.config.spawnSystemOn = spawnSystemOn;
            return this;
        }

        public Builder setStatisticsSystemOn(boolean statisticsSystemOn) {
            this.config.statisticsSystemOn = statisticsSystemOn;
            return this;
        }

        public Builder setWorldWidth(int worldWidth) {
            this.config.worldWidth = worldWidth;
            return this;
        }

        public Builder setWorldHeight(int worldHeight) {
            this.config.worldHeight = worldHeight;
            return this;
        }

        public Builder setJungleWidth(int jungleWidth) {
            this.config.jungleWidth = jungleWidth;
            return this;
        }

        public Builder setJungleHeight(int jungleHeight) {
            this.config.jungleHeight = jungleHeight;
            return this;
        }

        public Builder noSystems() {
            return this.setSpawnSystemOn(false)
                    .setBreedingSystemOn(false)
                    .setDeathSystemOn(false)
                    .setFeedingSystemOn(false)
                    .setMoveSystemOn(false)
                    .setStatisticsSystemOn(false);
        }

        public SimulationConfig build() {
            return this.config;
        }
    }

}
