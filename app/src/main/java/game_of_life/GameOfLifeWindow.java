package game_of_life;

import game_of_life.core.State;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GameOfLifeWindow extends JFrame {
    
    private int[][] prevContent;
    private final int sizeOfTheUniverse = 50;
    private final int fps = 20;
    private final String userDirPath = System.getProperty("user.dir");
    private final String picsPath = userDirPath + File.separator +
        "pics" + File.separator + "icons" + File.separator + "png" + File.separator + "set1" + File.separator;;
    
    private final int BUTTON_SIZE = 24;
    private Icon iconPlayButton = null;
    private Icon iconPauseButton = null;
    private Icon iconSetRandomButton = null;
    private Icon iconResetButton = null;
    private Icon iconClearButton = null;
    private Icon iconLoadPatternButton = null;
    private Icon iconSavePatternButton = null;
    
    JLabel generationsCounterLabel = new JLabel("Generation #");
    JLabel populationCounterLabel = new JLabel("Alive: ");
    JToggleButton toggleButtonPausePlay = new JToggleButton();
    JButton buttonSetRandom = new JButton();
    JButton buttonReset = new JButton();
    JButton buttonClear = new JButton();
    JButton buttonSavePattern = new JButton();
    JButton buttonLoadPattern = new JButton();
//    JSlider fpsSlider = new JSlider(JSlider.VERTICAL, 0 , 100, 10);
//    JSlider scaleSlider = new JSlider(JSlider.VERTICAL, 1 , 100, 50);

//    JPanel controls = new JPanel(new FlowLayout(FlowLayout.TRAILING), true); // todo?? why isDoubleBuffered
    
    private final UniverseGridComponent grid;
    private final UniverseSwingWorker universeSwingWorker;
    
    
    GameOfLifeWindow() {
        
        super("Game of life");
        
        System.out.println("JFrame thread name: " + Thread.currentThread().getName()
            + "; is the event dispatch thread? " + (SwingUtilities.isEventDispatchThread() ? "Yes" : "No"));
        
        generationsCounterLabel.setName("GenerationLabel");
        populationCounterLabel.setName("AliveLabel");
        toggleButtonPausePlay.setName("PlayToggleButton");
        buttonSetRandom.setName("SetRandomButton");
        buttonReset.setName("ResetButton");
        buttonClear.setName("ClearButton");
        buttonSavePattern.setName("SavePatternButton");
        buttonLoadPattern.setName("LoadPatternButton");
        // todo>> add fps slider,
        // todo>> add scale slider
        
        grid = new UniverseGridComponent(new int[sizeOfTheUniverse][sizeOfTheUniverse], 10);
        universeSwingWorker = new UniverseSwingWorker(sizeOfTheUniverse, grid, generationsCounterLabel, populationCounterLabel);
        grid.setMatrix(universeSwingWorker.getUniverseState().getContent());
        
        createBorderLayoutForm();
        addListeners();
        
        pack();
        setVisible(true);
        
        universeSwingWorker.setFps(fps);
        universeSwingWorker.execute();
        
    }
    
    private void createBorderLayoutForm() {

//    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                universeSwingWorker.cancel(true);
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
            iconSetRandomButton = new ImageIcon(ImageIO.read(new File(picsPath + "random.png"))
                .getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH));
            iconResetButton = new ImageIcon(ImageIO.read(new File(picsPath + "refresh.png"))
                .getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH));
            iconClearButton = new ImageIcon(ImageIO.read(new File(picsPath + "reset.png"))
                .getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH));
            iconLoadPatternButton = new ImageIcon(ImageIO.read(new File(picsPath + "open_color.png"))
                .getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH));
            iconSavePatternButton = new ImageIcon(ImageIO.read(new File(picsPath + "save_color.png"))
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
        buttonSetRandom.setMargin(new Insets(5, 5, 5, 5));
        buttonSetRandom.setIcon(iconSetRandomButton);
        buttonReset.setMargin(new Insets(5, 5, 5, 5));
        buttonReset.setIcon(iconResetButton);
        buttonClear.setMargin(new Insets(5, 5, 5, 5));
        buttonClear.setIcon(iconClearButton);
        buttonSavePattern.setMargin(new Insets(5, 5, 5, 5));
        buttonSavePattern.setIcon(iconSavePatternButton);
        buttonLoadPattern.setMargin(new Insets(5, 5, 5, 5));
        buttonLoadPattern.setIcon(iconLoadPatternButton);
        panelButtons.add(toggleButtonPausePlay);
        panelButtons.add(Box.createRigidArea(new Dimension(10, 10)));
        panelButtons.add(buttonSetRandom);
        panelButtons.add(Box.createRigidArea(new Dimension(10, 10)));
        panelButtons.add(buttonReset);
        panelButtons.add(Box.createRigidArea(new Dimension(10, 10)));
        panelButtons.add(buttonClear);
        panelButtons.add(Box.createRigidArea(new Dimension(10, 10)));
        panelButtons.add(buttonLoadPattern);
        panelButtons.add(Box.createRigidArea(new Dimension(10, 10)));
        panelButtons.add(buttonSavePattern);
        panelButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        panelButtons.setAlignmentY(JPanel.TOP_ALIGNMENT);
        
        JPanel panelCounters = new JPanel();
        
        panelCounters.setLayout(new BoxLayout(panelCounters, BoxLayout.X_AXIS));
        panelCounters.setBorder(margins10);
        panelCounters.add(generationsCounterLabel);
        panelCounters.add(Box.createRigidArea(new Dimension(10, 10)));
        panelCounters.add(populationCounterLabel);
        panelButtons.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        panelButtons.setAlignmentY(JPanel.TOP_ALIGNMENT);
        
        add(panelButtons, BorderLayout.NORTH);
        add(panelCounters, BorderLayout.SOUTH);
        add(grid, BorderLayout.CENTER);
        
    }
    
    private void addListeners() {
        
/*
        PropertyChangeListener listener = event -> {
            switch (event.getPropertyName()) {
*/
/*
                case "nextGeneration":
                    State state = (State) event.getNewValue();
                    generationsCounterLabel.setText("Generation #" + state.getGeneration());
                    aliveCellsCounterLabel.setText("Alive: " + state.getPopulation());
                    grid.setMatrix(state.getContent());
                    break;
*//*

                case "paused":
                    // change pause/resume button face
                    break;
            }
        };
*/
        
        ActionListener actionListenerForPausePlay = event -> {
            if (toggleButtonPausePlay.isSelected()) {
                toggleButtonPausePlay.setIcon(iconPauseButton);
                prevContent = universeSwingWorker.getUniverseState().getContent();
                universeSwingWorker.resume();
            } else {
                toggleButtonPausePlay.setIcon(iconPlayButton);
                universeSwingWorker.pause();
            }
        };
    
        ActionListener actionListenerForRandomFillButton = event -> {
            universeSwingWorker.pause();
            prevContent = universeSwingWorker.getUniverseState().getContent();
            toggleButtonPausePlay.setIcon(iconPlayButton);
            toggleButtonPausePlay.setSelected(false);
            universeSwingWorker.setContent(fillWithRandoms(prevContent));
            grid.setMatrix(universeSwingWorker.getUniverseState().getContent());
            setStateStatistics(universeSwingWorker.getUniverseState());
        };
    
        ActionListener actionListenerForResetButton = event -> {
            universeSwingWorker.pause();
            toggleButtonPausePlay.setIcon(iconPlayButton);
            toggleButtonPausePlay.setSelected(false);
            universeSwingWorker.setContent(prevContent);
            grid.setMatrix(universeSwingWorker.getUniverseState().getContent());
            setStateStatistics(universeSwingWorker.getUniverseState());
        };
    
        ActionListener actionListenerForClearButton = event -> {
            universeSwingWorker.pause();
            toggleButtonPausePlay.setIcon(iconPlayButton);
            toggleButtonPausePlay.setSelected(false);
            prevContent = universeSwingWorker.getUniverseState().getContent();
            universeSwingWorker.setContent(new int[prevContent.length][prevContent[0].length]);
            grid.setMatrix(universeSwingWorker.getUniverseState().getContent());
            setStateStatistics(universeSwingWorker.getUniverseState());
        };
    
        toggleButtonPausePlay.addActionListener(actionListenerForPausePlay);
        buttonSetRandom.addActionListener(actionListenerForRandomFillButton);
        buttonReset.addActionListener(actionListenerForResetButton);
        buttonClear.addActionListener(actionListenerForClearButton);
//        universeSwingWorker.addPropertyChangeListener(listener);
    }
    
    private int[][] fillWithRandoms(int[][] contentMatrix) {
        int rows = contentMatrix.length;
        int columns = contentMatrix[0].length;
        int[][] newContent = new int[rows][columns];
        Random rnd = new Random(System.currentTimeMillis() % 1_000_000_009L);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                newContent[r][c] = rnd.nextInt(4); // with 4 the start state is most dense
            }
        }
        return newContent;
    }
    
    private void setStateStatistics(State state) {
        generationsCounterLabel.setText("Generation #" + state.getGeneration());
        populationCounterLabel.setText("Alive: " + state.getPopulation());
    }
}

