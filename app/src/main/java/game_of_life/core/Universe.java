package game_of_life.core;

@SuppressWarnings("unused")
public class Universe {
    private final StateOfTheUniverse state;
    private int xSize;
    private int ySize;
    
    public Universe(int rows, int columns) {
        xSize = columns;
        ySize = rows;
        state = new StateOfTheUniverse(1, 0, new int[rows][columns]);
    }
    
    public StateOfTheUniverse getState() {
        return state;
    }
    
    public void setCellState(int x, int y, int cellNeighbors) {
        state.content[x][y] = cellNeighbors;
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
    
    public void next() {
    
        int[][] nextGenerationContent = new int[ySize][xSize];
        int population = 0;
    
        for (int row = 0; row < ySize; row++) {
            for (int col = 0; col < xSize; col++) {
                nextGenerationContent[row][col] = neighbors(row, col);
                int neighborsCount = nextGenerationContent[row][col];
                int prevNeighborsCount = state.content[row][col];
                if (neighborsCount == 2 && prevNeighborsCount != 2 && prevNeighborsCount != 3) {
                    neighborsCount = prevNeighborsCount;
                    nextGenerationContent[row][col] = prevNeighborsCount;
                }
                if (neighborsCount == 2 || neighborsCount == 3) {
                    population++;
                }
            }
        }
        state.content = nextGenerationContent;
        state.population = population;
        state.generation++;
    }
    
    private int neighbors(int row, int col) {
        int neighborsCounter = 0;
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                int neighborsNeighbors = state.content[(ySize + row + r) % ySize][(xSize + col + c) % xSize];
                boolean isAlive = neighborsNeighbors == 3 || neighborsNeighbors == 2;
                if ((r != 0 || c != 0) && isAlive) {
                    neighborsCounter++;
                }
            }
        }
        return neighborsCounter;
    }
    
}
