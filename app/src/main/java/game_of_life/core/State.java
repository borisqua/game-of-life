package game_of_life.core;

public class State {
    int generation;
    int population;
    int[][] content;
    
    public State(int[][] content) {
        setContent(content);
    }
    
    public void setContent(int[][] newContent) {
        this.generation = 1;
        this.content = newContent;
        for (int row = 0; row < newContent.length; row++) {
            for (int column = 0; column < newContent[0].length; column++) {
                if (this.content[row][column] == 2 || this.content[row][column] == 3) {
                    population++;
                }
            }
        }
    }
    
    public int[][] getContent() {
        return content;
    }
    
    public int getPopulation() {
        return population;
    }
    
    public int getGeneration() {
        return generation;
    }
    
}
