package game_of_life;

import javax.swing.*;
import java.awt.*;

class UniverseGridComponent extends JComponent {
    // todo >> add a mouse click behaviour: inverse state of the cell by the click.
    
    private int[][] squareMatrix;
    private final int cellSize;
//  private final Rectangle gridCanvas;
    
    UniverseGridComponent(int[][] squareMatrix, int cellSize) {
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
        return new Dimension(cellSize * squareMatrix.length, cellSize * squareMatrix.length);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (int x = 0; x < getPreferredSize().width; x += cellSize) {
            for (int y = 0; y < getPreferredSize().height; y += cellSize) {
                int neighborsCont = squareMatrix[y / cellSize][x / cellSize];
                g2.setColor(Color.LIGHT_GRAY);//.brighter());//.darker().darker());
                //g2.setStroke(new BasicStroke(1)); // default stroke is ok
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
    
    public void setMatrix(int[][] squareMatrix) {
        this.squareMatrix = squareMatrix;
        repaint();
//    paintImmediately(gridCanvas);
    }
    
}

