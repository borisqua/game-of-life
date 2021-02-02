package game_of_life;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"DuplicatedCode", "FieldCanBeLocal"})
public class GameOfLife extends JFrame {
    
    private final int sizeOfUniverse = 50;
    private final int fps = 20;
    private final String userDirPath = System.getProperty("user.dir");
    private final String picsPath = userDirPath + File.separator +
        "pics" + File.separator + "icons" +  File.separator;
    
    private final int BUTTON_SIZE = 24;
    private Icon iconPlayButton = null;
    private Icon iconPauseButton = null;
    private Icon iconResetButton = null;
    private Icon iconClearButton = null;
    
    JLabel generationsCounterLabel = new JLabel("Generation #");
    JLabel aliveCellsCounterLabel = new JLabel("Alive: ");
    JToggleButton toggleButtonPausePlay = new JToggleButton();
    JButton buttonReset = new JButton();
    JButton buttonClear = new JButton();
    
    private final LifeGrid grid;
    private final Universe universe;
    
    
    GameOfLife() {
        
        super("Game of life");
        
        System.out.println("JFrame thread name: " + Thread.currentThread().getName()
            + "; is the event dispatch thread? " + (SwingUtilities.isEventDispatchThread() ? "Yes" : "No"));
        
        generationsCounterLabel.setName("GenerationLabel");
        aliveCellsCounterLabel.setName("AliveLabel");
        toggleButtonPausePlay.setName("PlayToggleButton");
        buttonReset.setName("ResetButton");
        buttonClear.setName("ClearButton");
        
        grid = new LifeGrid(new int[sizeOfUniverse][sizeOfUniverse], 10);
        universe = new Universe(sizeOfUniverse, grid);
        grid.setSquareMatrix(universe.getSpace());
        
        createBorderLayoutForm();
        addListeners();
        
        pack();
        setVisible(true);
        
        universe.setFps(fps);
        universe.execute();
        
    }
    
    private void createBorderLayoutForm() {

//    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                universe.cancel(true);
                System.exit(0);
            }
        });
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new Dimension((int) (screenSize.height * 0.9), (int) (screenSize.height * 0.7)));
        setLocationRelativeTo(null);
        
        try {
            iconPlayButton = new ImageIcon(ImageIO.read(new File(picsPath + "play.png"))
                .getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH));
            iconPauseButton = new ImageIcon(ImageIO.read(new File(picsPath + "pause.png"))
                .getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH));
            iconResetButton = new ImageIcon(ImageIO.read(new File(picsPath + "reset.png"))
                .getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH));
            iconClearButton = new ImageIcon(ImageIO.read(new File(picsPath + "openfile.png"))
                .getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH));
        } catch (IOException ignored) {
        }
        
        Color backgroundColor = getBackground();
        Border margins10 = BorderFactory.createLineBorder(backgroundColor, 10);
        //noinspection unused
//    Border margins2 = BorderFactory.createLineBorder(backgroundColor, 2);
        
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
        panelButtons.setBorder(margins10);
        toggleButtonPausePlay.setMargin(new Insets(5, 5, 5, 5));
        toggleButtonPausePlay.setIcon(iconPlayButton);
        buttonReset.setMargin(new Insets(5, 5, 5, 5));
        buttonReset.setIcon(iconResetButton);
        buttonClear.setMargin(new Insets(5, 5, 5, 5));
        buttonClear.setIcon(iconClearButton);
        panelButtons.add(toggleButtonPausePlay);
        panelButtons.add(Box.createRigidArea(new Dimension(10, 10)));
        panelButtons.add(buttonReset);
        panelButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        panelButtons.setAlignmentY(JPanel.TOP_ALIGNMENT);
        
        JPanel panelCounters = new JPanel();
        panelCounters.setLayout(new BoxLayout(panelCounters, BoxLayout.Y_AXIS));
        panelCounters.setBorder(margins10);
        panelCounters.add(generationsCounterLabel);
        panelCounters.add(aliveCellsCounterLabel);
        panelButtons.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        panelButtons.setAlignmentY(JPanel.TOP_ALIGNMENT);
        
        JPanel panelControls = new JPanel();
        panelControls.setLayout(new BoxLayout(panelControls, BoxLayout.Y_AXIS));
        panelControls.add(panelButtons);
        panelControls.add(panelCounters);
        
        panelControls.setBorder(margins10);
        add(panelControls, BorderLayout.WEST);
        add(grid, BorderLayout.CENTER);
        
    }
    
    private void addListeners() {
        
        PropertyChangeListener listener = event -> {
            switch (event.getPropertyName()) {
                case "nextGeneration":
                    Universe.State state = (Universe.State) event.getNewValue();
                    generationsCounterLabel.setText("Generation #" + state.generation);
                    aliveCellsCounterLabel.setText("Alive: " + state.alive);
                    break;
                case "paused":
                    // change pause/resume button face
                    break;
            }
        };
        
        ActionListener actionListenerForPausePlay = event -> {
            if (toggleButtonPausePlay.isSelected()) {
                toggleButtonPausePlay.setIcon(iconPauseButton);
                universe.resume();
            } else {
                toggleButtonPausePlay.setIcon(iconPlayButton);
                universe.pause();
            }
        };
        
        ActionListener actionListenerForResetButton = event -> {
            toggleButtonPausePlay.setIcon(iconPlayButton);
            toggleButtonPausePlay.setSelected(false);
            universe.reset();
            grid.setSquareMatrix(universe.getSpace());
            
        };
        
        toggleButtonPausePlay.addActionListener(actionListenerForPausePlay);
        buttonReset.addActionListener(actionListenerForResetButton);
        universe.addPropertyChangeListener(listener);
    }
}

class LifeGrid extends JComponent {
    // todo >> add mouse behaviour inverse state of the cell by the click.
    
    private int[][] squareMatrix;
    private final int cellSize;
//  private final Rectangle gridCanvas;
    
    LifeGrid(int[][] squareMatrix, int cellSize) {
        this.squareMatrix = squareMatrix;
        this.cellSize = cellSize;
//    RepaintManager.setCurrentManager(new MyRepainter());
//    setDoubleBuffered(true);
        repaint();
//    Dimension gridCanvasDimension = getPreferredSize();
//    gridCanvas = new Rectangle(0, 0, gridCanvasDimension.width, gridCanvasDimension.height);
//    paintImmediately(gridCanvas);
    }
    
    @Override
    public Dimension getPreferredSize() {
        // todo >> change behaviour something like tracking the game filed size and dynamically reallocate matrix of the universe
        // todo >> change behaviour run universe in a modular world (modulo window size, optional)
        return new Dimension(cellSize * squareMatrix.length, cellSize * squareMatrix.length);
    }
    
    @SuppressWarnings("DuplicatedCode")
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (int x = 0; x < getPreferredSize().width; x += cellSize) {
            for (int y = 0; y < getPreferredSize().height; y += cellSize) {
                int neighborsCont = squareMatrix[y / cellSize][x / cellSize];
                g2.setColor(Color.LIGHT_GRAY);//.brighter());//.darker().darker());
                g2.drawRect(x,y ,cellSize, cellSize);
                //todo >> track age of each cell
                //todo >> change color in more shades depending on the age of the cell
                if (neighborsCont == 2) { // survivors
                    g2.setColor(Color.GREEN.darker().darker());
                    g2.fillRect(x, y, cellSize, cellSize);
                }
                if (neighborsCont == 3) { // newborns
                    g2.setColor(Color.GREEN.darker());
                    g2.fillRect(x, y, cellSize, cellSize);
                }
            }
        }
    }
    
    public void setSquareMatrix(int[][] squareMatrix) {
        this.squareMatrix = squareMatrix;
        repaint();
//    paintImmediately(gridCanvas);
    }
    
}

@SuppressWarnings("DuplicatedCode")
class Universe extends SwingWorker<Void, int[][]> {
    
    private final LifeGrid grid;
    private final int size;
    private int[][] space;
    private int generation = 0;
    private int alive = 0;
    private int fps = 24;
    private volatile boolean paused = true;
    
    Universe(int size, LifeGrid grid) {
        generation++;
        this.grid = grid;
        this.size = size;
        space = new int[size][size];
        reset();
    }
    
    public void reset() {
        pause();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Random rnd = new Random();
                space[r][c] = rnd.nextInt(4);
                int neighborCount = space[r][c];
                if (neighborCount == 2 || neighborCount == 3) {
                    alive++;
                }
            }
        }
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
    
    public int[][] getSpace() {
        return space;
    }
    
    public void setFps(int fps) {
        this.fps = fps;
    }
    
    private void next() {
        
        int[][] nextGeneration = new int[size][size];
        alive = 0;
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                nextGeneration[row][col] = neighbors(row, col);
                int neighborCount = nextGeneration[row][col];
                int prevNeighborCount = space[row][col];
                if (neighborCount == 2 && prevNeighborCount != 2 && prevNeighborCount != 3) {
                    neighborCount = prevNeighborCount;
                    nextGeneration[row][col] = prevNeighborCount;
                }
                if (neighborCount == 2 || neighborCount == 3) {
                    alive++;
                }
            }
        }
        space = nextGeneration;
        generation++;
        
    }
    
    private int neighbors(int row, int col) {
        int neighborsCounter = 0;
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                int neighborsNeighbors = space[(size + row + r) % size][(size + col + c) % size];
                boolean isAlive = neighborsNeighbors == 3 || neighborsNeighbors == 2;
                if ((r != 0 || c != 0) && isAlive) {
                    neighborsCounter++;
                }
            }
        }
        return neighborsCounter;
    }
    
    @Override
    protected Void doInBackground() {
        try {
            
            while (!isCancelled()) {
                TimeUnit.MILLISECONDS.sleep(1000 / fps);
                if (!isPaused()) {
                    State prevState = new State(generation, alive);
                    next();
                    publish(space);
                    firePropertyChange("nextGeneration", prevState, new State(generation, alive));
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
    protected void process(List<int[][]> generations) {
        generations.forEach(grid::setSquareMatrix);
    }
    
    static class State {
        int generation;
        int alive;
        
        State(int generation, int alive) {
            this.generation = generation;
            this.alive = alive;
        }
        
    }
    
}
