package game_of_life;

import game_of_life.core.StateOfTheUniverse;
import game_of_life.core.Universe;

import javax.swing.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class UniverseSwingWorker extends SwingWorker<Void, int[][]> {
    
    private final Universe universe;
    private final UniverseGridComponent grid;
    private final int size;
    private int fps = 24;
    private volatile boolean paused = true;
    
    UniverseSwingWorker(int size, UniverseGridComponent grid) {
        universe = new Universe(size, size);
        this.grid = grid;
        this.size = size;
        randomFill();
    }
    
    public void randomFill() {
        int generation = 0;
        int population = 0;
        int[][] newContent = new int[size][size];
        pause();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Random rnd = new Random();
                newContent[r][c] = rnd.nextInt(4); // with 4 the start state is most dense
                int neighborCount = newContent[r][c];
                if (neighborCount == 2 || neighborCount == 3) {
                    population++;
                }
            }
        }
        universe.getState().content = newContent;
        universe.getState().generation = generation;
        universe.getState().population = population;
    }
    
    public boolean isPaused() {
        return this.paused;
    }
    
    public void pause() {
        if (!isPaused() && !isDone()) {
            paused = true;
            firePropertyChange("paused", false, true);
        }
    }
    
    public void resume() {
        if (!isDone() && isPaused()) {
            paused = false;
            firePropertyChange("paused", true, false);
        }
    }
    
    public int[][] getUniverseContent() {
        return universe.getState().content;
    }
    
    public void setFps(int fps) {
        this.fps = fps;
    }
    
    @Override
    protected Void doInBackground() {
        try {
            
            while (!isCancelled()) {
                TimeUnit.MILLISECONDS.sleep(1000 / fps);
                if (!isPaused()) {
                    StateOfTheUniverse prevStateOfTheUniverse = new StateOfTheUniverse(
                        universe.getState().generation, universe.getState().population, universe.getState().content.clone());
                    universe.next();
                    publish(universe.getState().content);
                    firePropertyChange("nextGeneration", prevStateOfTheUniverse,
                        new StateOfTheUniverse(
                            universe.getState().generation, universe.getState().population, universe.getState().content));
                }
            }
            
            if (isCancelled()) {
                System.out.println("Universe was canceled.");
            }
            
        } catch (InterruptedException ignored) {
            System.out.println("Universe was interrupted.");
            Thread.currentThread().interrupt();
        }
        return null;
    }
    
    @Override
    protected void process(List<int[][]> generationsOfContent) {
        generationsOfContent.forEach(grid::setSquareMatrix);
    }
    
    
}
