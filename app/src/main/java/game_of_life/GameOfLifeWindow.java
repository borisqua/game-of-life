package game_of_life;

import game_of_life.core.State;

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

public class GameOfLifeWindow extends JFrame {
    
    private final int sizeOfTheUniverse = 50;
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
//    JButton buttonSavePattern = new JButton();
//    JButton buttonLoadPattern = new JButton();
//    JSlider fpsSlider = new JSlider(JSlider.VERTICAL, 0 , 100, 10);
//    JSlider scaleSlider = new JSlider(JSlider.VERTICAL, 1 , 100, 50);
    
    private final UniverseGridComponent grid;
    private final UniverseSwingWorker universeSwingWorker;
    
    
    GameOfLifeWindow() {
        
        super("Game of life");
        
        System.out.println("JFrame thread name: " + Thread.currentThread().getName()
            + "; is the event dispatch thread? " + (SwingUtilities.isEventDispatchThread() ? "Yes" : "No"));
        
        generationsCounterLabel.setName("GenerationLabel");
        aliveCellsCounterLabel.setName("AliveLabel");
        toggleButtonPausePlay.setName("PlayToggleButton");
        buttonReset.setName("ResetButton");
        buttonClear.setName("ClearButton");
        buttonClear.setName("SavePatternButton");
        buttonClear.setName("LoadPatternButton");
        // todo >> add fps slider,add scale slider
        
        grid = new UniverseGridComponent(new int[sizeOfTheUniverse][sizeOfTheUniverse], 10);
        universeSwingWorker = new UniverseSwingWorker(sizeOfTheUniverse, grid);
        grid.setSquareMatrix(universeSwingWorker.getUniverseContent());
        
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
                    State state = (State) event.getNewValue();
                    generationsCounterLabel.setText("Generation #" + state.getGeneration());
                    aliveCellsCounterLabel.setText("Alive: " + state.getPopulation());
                    break;
                case "paused":
                    // change pause/resume button face
                    break;
            }
        };
        
        ActionListener actionListenerForPausePlay = event -> {
            if (toggleButtonPausePlay.isSelected()) {
                toggleButtonPausePlay.setIcon(iconPauseButton);
                universeSwingWorker.resume();
            } else {
                toggleButtonPausePlay.setIcon(iconPlayButton);
                universeSwingWorker.pause();
            }
        };
        
        ActionListener actionListenerForResetButton = event -> {
            toggleButtonPausePlay.setIcon(iconPlayButton);
            toggleButtonPausePlay.setSelected(false);
            universeSwingWorker.randomFill();
            grid.setSquareMatrix(universeSwingWorker.getUniverseContent());
            
        };
        
        toggleButtonPausePlay.addActionListener(actionListenerForPausePlay);
        buttonReset.addActionListener(actionListenerForResetButton);
        universeSwingWorker.addPropertyChangeListener(listener);
    }
}

