package game_of_life.core;

public class Universe {
    private final State state;
    private int xSize;
    private int ySize;
    
    public Universe(int rows, int columns) {
        xSize = columns;
        ySize = rows;
        state = new State(1, new int[rows][columns]);
    }
    
    public State getState() {
        return state;
    }
    
    public void resetSpaceState() {
        state.generation = 1;
        state.population = 0;
        state.content = new int[ySize][xSize];
    }
    
    public void setxSize(int xSize) {
        this.xSize = xSize;
        //todo>> reallocate spaceState array
    }
    
    public void setySize(int ySize) {
        this.ySize = ySize;
        //todo>> reallocate spaceState array
    }
    
    public void setSize(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        //todo>> reallocate spaceState array
    }
    
    private int neighbors(int row, int column) {
        int neighborsCounter = 0;
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                int neighborsOfANeighbor = state.content[(ySize + row + r) % ySize][(xSize + column + c) % xSize];
                boolean isAlive = neighborsOfANeighbor == 3 || neighborsOfANeighbor == 2;
                if ((r != 0 || c != 0) && isAlive) {
                    neighborsCounter++;
                }
            }
        }
        return neighborsCounter;
    }
    
    public void setCellAlive(int row, int column) {
        this.state.content[(ySize + row) % ySize][(xSize + column) % xSize] = 3; // a new born
        this.state.population++;
    }
    
    public void next() {
        
        int[][] nextGenerationContent = new int[ySize][xSize];
        int population = 0;
        
        for (int row = 0; row < ySize; row++) {
            for (int column = 0; column < xSize; column++) {
                int neighborsCount =  neighbors(row, column);
                int prevNeighborsCount = state.content[row][column];
                if (neighborsCount == 2 && prevNeighborsCount != 2 && prevNeighborsCount != 3) {
                    // if it wasn't alive before, it will stay dead <2 or >3
                    neighborsCount = prevNeighborsCount; // neither 2 nor 3
                }
                if (neighborsCount == 2 || neighborsCount == 3) {
                    // if 2, it was alive before, it will stay alive
                    // if 3, maybe it was dead before, but now it has 3 neighbors, so it becomes alive
                    population++;
                }
                // if 3: just born, if 2: keeps alive, the rest values mean dead cell
                nextGenerationContent[row][column] = neighborsCount;
            }
        }
        state.content = nextGenerationContent;
        state.population = population;
        state.generation++;
    }
    
    public void setContent(int[][] newContent) {
        state.setContent(newContent);
    }
    
}
