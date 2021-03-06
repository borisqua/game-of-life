package game_of_life;

import game_of_life.core.State;
import game_of_life.core.Universe;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

class UniverseSwingWorker extends SwingWorker<Void, State> {
    
    private final Universe universe;
    private final UniverseGridComponent grid;
    private final JLabel generationMonitor;
    private final JLabel populationMonitor;
    private final int size; // todo>> not only square matrix
    private int fps = 24;
    private volatile boolean paused = true;
    
    UniverseSwingWorker(int size, UniverseGridComponent grid, JLabel generationMonitor, JLabel populationMonitor ) {
        universe = new Universe(size, size);
        this.grid = grid;
        this.generationMonitor = generationMonitor;
        this.populationMonitor = populationMonitor;
        this.size = size;
    }
    
    public void setContent(int[][] newContent) {
        universe.setContent(newContent);
        publish(universe.getState());
    }
    
    public boolean isPaused() {
        return this.paused;
    }
    
    public void pause() {
        if (!isPaused() && !isDone()) {
            paused = true;
//            firePropertyChange("paused", false, true);
        }
    }
    
    public void resume() {
        if (!isDone() && isPaused()) {
            paused = false;
//            firePropertyChange("paused", true, false);
        }
    }
    
    public State getUniverseState() {
        return universe.getState();
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
//                    State prevState = new State(universe.getState().getContent());
                    universe.next();
                    publish(universe.getState());
//                    firePropertyChange("nextGeneration", prevState,
//                        new State(universe.getState().getContent()));
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
    protected void process(List<State> generationsOfUniverseStates) {
        generationsOfUniverseStates.forEach(state -> {
            grid.setMatrix(state.getContent());
            generationMonitor.setText("Generation #" + state.getGeneration());
            populationMonitor.setText("Alive: " + state.getPopulation());
        });
    }
    
}
