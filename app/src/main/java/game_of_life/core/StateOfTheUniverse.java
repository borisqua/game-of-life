package game_of_life.core;

public class StateOfTheUniverse {
    public int generation;
    public int population;
    public int[][] content;
    
    public StateOfTheUniverse(int generation, int population, int[][] spaceContent) {
        this.generation = generation;
        this.population = population;
        this.content = spaceContent;
    }
    
}
