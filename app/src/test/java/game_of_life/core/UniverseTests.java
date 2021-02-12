package game_of_life.core;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("A universe class:")
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UniverseTests {
    
    Random rnd = new Random(System.currentTimeMillis() % 1_000_000_009L/*large prime*/);
    private Universe universe;
    private final int rows = 5;
    private final int columns = 7;
    
    @BeforeEach
    public void init() {
        universe = new Universe(rows, columns);
    }
    
    @Test
    @Order(1)
    @DisplayName("Check that instance of the universe successfully instantiated and contains correct state information.")
    public void testInstantiating() {
        assertNotNull(universe);
        assertEquals(rows, universe.getState().content.length);
        assertEquals(columns, universe.getState().content[0].length);
        assertEquals(0, universe.getState().content[rows - 1][columns - 1]);
        assertEquals(0, universe.getState().content[2][3]);
        assertEquals(0, universe.getState().content[0][0]);
    }
    
    @Test
    @Order(2)
    @DisplayName("Check that getState method returns correct state information")
    void getState() {
        int[] arr = new int[8];
        Arrays.fill(arr, 111);
        Arrays.fill(universe.getState().content, arr);
        assertEquals(111, universe.getState().content[rows - 1][columns - 1]);
        assertEquals(111, universe.getState().content[2][5]);
        assertEquals(111, universe.getState().content[0][0]);
        universe.next();
        assertEquals(0, universe.getState().content[0][0]);
        assertEquals(0, universe.getState().getContent()[0][0]);
        assertEquals(2, universe.getState().generation);
        assertEquals(2, universe.getState().getGeneration());
        assertEquals(0, universe.getState().population);
        assertEquals(0, universe.getState().getPopulation());
        universe.next();
        universe.next();
        assertEquals(4, universe.getState().generation);
        assertEquals(4, universe.getState().getGeneration());
        assertEquals(0, universe.getState().population);
        assertEquals(0, universe.getState().getPopulation());
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                universe.getState().content[r][c] = rnd.nextInt(4);
            }
        }
        universe.next();
        assertEquals(5, universe.getState().generation);
        assertEquals(5, universe.getState().getGeneration());
        assertTrue(universe.getState().population > 0);
        assertTrue(universe.getState().getPopulation() > 0);
    }
    
    @Test
    @Order(2)
    @DisplayName("Check that setCellAlive(row, column) works as expected")
    void setCellAlive() {
        
        assertEquals(0, universe.getState().population);
        assertEquals(1, universe.getState().generation);
        
        // row of three alive cells: makes formation of a line of length 3,
        // that constantly changes its position from horizontal to vertical
        // 0 0 0 0 0 0 0     0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0     0 1 0 3 0 1 0
        // 0 0 3 3 3 0 0 <=> 0 1 1 2 1 1 0
        // 0 0 0 0 0 0 0     0 1 0 3 0 1 0
        // 0 0 0 0 0 0 0     0 0 0 0 0 0 0
    
        // 0 0 0 0 0 0 0     0 0 1 1 1 0 0
        // 0 1 0 3 0 1 0     0 0 0 1 0 0 0
        // 0 1 1 2 1 1 0 <=> 0 0 3 2 3 0 0
        // 0 1 0 3 0 1 0     0 0 0 1 0 0 0
        // 0 0 0 0 0 0 0     0 0 1 1 1 0 0
        
        universe.setCellAlive(2, 2);
        universe.setCellAlive(2, 3);
        universe.setCellAlive(2, 4);
        
        universe.next();
        
        assertEquals(3, universe.getState().population);
        assertEquals(2, universe.getState().generation);
        
        for (int c = 0; c < columns; c++) {
            assertEquals(0, universe.getState().content[0][c]);
            assertEquals(0, universe.getState().content[4][c]);
        }
        assertEquals(0, universe.getState().content[1][0]);
        assertEquals(1, universe.getState().content[1][1]);
        assertEquals(0, universe.getState().content[1][2]);
        assertEquals(3, universe.getState().content[1][3]);
        assertEquals(0, universe.getState().content[1][4]);
        assertEquals(1, universe.getState().content[1][5]);
        assertEquals(0, universe.getState().content[1][6]);
        
        assertEquals(0, universe.getState().content[2][0]);
        assertEquals(1, universe.getState().content[2][1]);
        assertEquals(1, universe.getState().content[2][2]);
        assertEquals(2, universe.getState().content[2][3]);
        assertEquals(1, universe.getState().content[2][4]);
        assertEquals(1, universe.getState().content[2][5]);
        assertEquals(0, universe.getState().content[2][6]);
        
        assertEquals(0, universe.getState().content[3][0]);
        assertEquals(1, universe.getState().content[3][1]);
        assertEquals(0, universe.getState().content[3][2]);
        assertEquals(3, universe.getState().content[3][3]);
        assertEquals(0, universe.getState().content[3][4]);
        assertEquals(1, universe.getState().content[3][5]);
        assertEquals(0, universe.getState().content[3][6]);
        
        universe.next();
        
        assertEquals(3, universe.getState().population);
        assertEquals(3, universe.getState().generation);
        
        for (int c = 2; c < columns - 2; c++) {
            assertEquals(1, universe.getState().content[0][c]);
            assertEquals(1, universe.getState().content[4][c]);
        }
        
        assertEquals(0, universe.getState().content[1][1]);
        assertEquals(0, universe.getState().content[1][2]);
        assertEquals(1, universe.getState().content[1][3]);
        assertEquals(0, universe.getState().content[1][4]);
        assertEquals(0, universe.getState().content[1][1]);
        
        assertEquals(0, universe.getState().content[2][1]);
        assertEquals(3, universe.getState().content[2][2]);
        assertEquals(2, universe.getState().content[2][3]);
        assertEquals(3, universe.getState().content[2][4]);
        assertEquals(0, universe.getState().content[2][5]);
        
        assertEquals(0, universe.getState().content[3][1]);
        assertEquals(0, universe.getState().content[3][2]);
        assertEquals(1, universe.getState().content[3][3]);
        assertEquals(0, universe.getState().content[3][4]);
        assertEquals(0, universe.getState().content[3][4]);
        
        universe.next();
        
        assertEquals(3, universe.getState().population);
        assertEquals(4, universe.getState().generation);
        
        for (int c = 0; c < columns; c++) {
            assertEquals(0, universe.getState().content[0][c]);
            assertEquals(0, universe.getState().content[4][c]);
        }
        assertEquals(0, universe.getState().content[1][0]);
        assertEquals(1, universe.getState().content[1][1]);
        assertEquals(0, universe.getState().content[1][2]);
        assertEquals(3, universe.getState().content[1][3]);
        assertEquals(0, universe.getState().content[1][4]);
        assertEquals(1, universe.getState().content[1][5]);
        assertEquals(0, universe.getState().content[1][6]);
        
        assertEquals(0, universe.getState().content[2][0]);
        assertEquals(1, universe.getState().content[2][1]);
        assertEquals(1, universe.getState().content[2][2]);
        assertEquals(2, universe.getState().content[2][3]);
        assertEquals(1, universe.getState().content[2][4]);
        assertEquals(1, universe.getState().content[2][5]);
        assertEquals(0, universe.getState().content[2][6]);
        
        assertEquals(0, universe.getState().content[3][0]);
        assertEquals(1, universe.getState().content[3][1]);
        assertEquals(0, universe.getState().content[3][2]);
        assertEquals(3, universe.getState().content[3][3]);
        assertEquals(0, universe.getState().content[3][4]);
        assertEquals(1, universe.getState().content[3][5]);
        assertEquals(0, universe.getState().content[3][6]);
        
    }
    
    @Test
    @DisplayName("Check that resetSpaceState() works as expected")
    void resetSpaceState() {
        
        assertEquals(0, universe.getState().population);
        assertEquals(1, universe.getState().generation);
        
        // row of three alive cells: makes formation of a line of length 3,
        // that constantly changes its position from horizontal to vertical
        // 0 0 0 0 0 0 0     0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0     0 1 0 3 0 1 0
        // 0 0 X X X 0 0 <=> 0 1 1 2 1 1 0
        // 0 0 0 0 0 0 0     0 1 0 3 0 1 0
        // 0 0 0 0 0 0 0     0 0 0 0 0 0 0
        
        // 0 0 0 0 0 0 0     0 0 1 1 1 0 0
        // 0 0 0 0 0 0 0     0 0 0 1 0 0 0
        // 0 0 X X X 0 0 <=> 0 0 3 2 3 0 0
        // 0 0 0 0 0 0 0     0 0 0 1 0 0 0
        // 0 0 0 0 0 0 0     0 0 1 1 1 0 0
        
        universe.setCellAlive(2, 2);
        universe.setCellAlive(2, 3);
        universe.setCellAlive(2, 4);
        
        universe.next();
        
        assertEquals(3, universe.getState().population);
        assertEquals(2, universe.getState().generation);
        
        universe.resetSpaceState();
        
        // reset was successful
        assertEquals(0, universe.getState().population);
        assertEquals(1, universe.getState().generation);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                assertEquals(0, universe.getState().getContent()[row][column]);
            }
        }
    }
    
    @Test
    @DisplayName("Check that next() works as expected")
    void next() {
        assertEquals(0, universe.getState().population);
        assertEquals(1, universe.getState().generation);
    
        // row of three alive cells: makes formation of a line of length 3,
        // that constantly changes its position from horizontal to vertical
        // 0 0 0 0 0 0 0     0 1 0 3 0 1 0
        // 0 0 3 3 3 0 0     0 1 1 2 1 1 0
        // 0 0 0 0 0 0 0 <=> 0 0 4 6 4 0 0
        // 0 0 3 3 3 0 0     0 1 1 2 1 1 0
        // 0 0 0 0 0 0 0     0 1 0 3 0 1 0
    
        // 0 1 0 3 0 1 0     0 0 3 2 3 0 0
        // 0 1 1 2 1 1 0     0 0 1 1 1 0 0
        // 0 0 4 6 4 0 0 <=> 0 0 4 6 4 0 0
        // 0 1 1 2 1 1 0     0 0 1 1 1 0 0
        // 0 1 0 3 0 1 0     0 0 3 2 3 0 0
    
        // 0 0 3 2 3 0 0     0 0 3 5 3 0 0
        // 0 0 1 1 1 0 0     0 1 1 3 1 1 0
        // 0 0 4 6 4 0 0 <=> 0 0 0 0 0 0 0
        // 0 0 1 1 1 0 0     0 1 1 3 1 1 0
        // 0 0 3 2 3 0 0     0 0 3 5 3 0 0
    
        // 0 0 3 5 3 0 0     0 0 2 5 2 0 0
        // 0 1 1 3 1 1 0     0 1 1 2 1 1 0
        // 0 0 0 0 0 0 0 <=> 0 0 0 0 0 0 0
        // 0 1 1 3 1 1 0     0 1 1 2 1 1 0
        // 0 0 3 5 3 0 0     0 0 2 5 2 0 0
    
        universe.setCellAlive(1, 2);
        universe.setCellAlive(1, 3);
        universe.setCellAlive(1, 4);
        universe.setCellAlive(3, 2);
        universe.setCellAlive(3, 3);
        universe.setCellAlive(3, 4);
    
        assertEquals(6, universe.getState().population);
        assertEquals(1, universe.getState().generation);
    
        universe.next();
        
        assertEquals(4, universe.getState().population);
        assertEquals(2, universe.getState().generation);
    
        universe.next();
    
        assertEquals(6, universe.getState().population);
        assertEquals(3, universe.getState().generation);
    
        universe.next();
    
        assertEquals(6, universe.getState().population);
        assertEquals(4, universe.getState().generation);
        
        universe.next();
    
        assertEquals(6, universe.getState().population);
        assertEquals(5, universe.getState().generation);
    
        universe.next();
    
        assertEquals(6, universe.getState().population);
        assertEquals(6, universe.getState().generation);
    
        universe.next();
    
        assertEquals(6, universe.getState().population);
        assertEquals(7, universe.getState().generation);
    }
    
    @Test
    void setxSize() {
    }
    
    @Test
    void setySize() {
    }
    
    @Test
    void setSize() {
    }
    
}
