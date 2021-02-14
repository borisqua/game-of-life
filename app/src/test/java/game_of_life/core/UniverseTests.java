package game_of_life.core;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testing the universe class: game_of_life.core.Universe")
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UniverseTests {
    
    Random rnd = new Random(System.currentTimeMillis() % 1_000_000_009L/*large prime*/);
    private Universe universe;
    private final int rows = 5;
    private final int columns = 7;
    
    private int[][] getRandomArray() {
        int randomInt = 1 + rnd.nextInt(15);
        int[][] arr = new int[randomInt][randomInt];
        int[] arrRow = new int[randomInt];
        Arrays.fill(arrRow, randomInt);
        Arrays.fill(arr, arrRow);
        return arr;
    }
    
    @BeforeEach
    void init() {
        universe = new Universe(rows, columns);
    }
    
    @Test
//    @Order(1)
    @DisplayName("Check if instance of the universe successfully instantiated and contains correct state information.")
    void testInstantiating() {
        assertNotNull(universe);
        assertEquals(rows, universe.getState().content.length);
        assertEquals(columns, universe.getState().content[0].length);
        assertEquals(0, universe.getState().content[rows - 1][columns - 1]);
        assertEquals(0, universe.getState().content[rows / 2][columns / 2]);
        assertEquals(0, universe.getState().content[0][0]);
    }
    
    @Test
//    @Order(2)
    @DisplayName("Check if getState method returns correct state information")
    void getState() {
        int[] arr = new int[8];
        Arrays.fill(arr, 111);
        Arrays.fill(universe.getState().content, arr);
        assertEquals(111, universe.getState().content[rows - 1][columns - 1]);
        assertEquals(111, universe.getState().content[rows / 2][columns / 2]);
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
//    @Order(3)
    @DisplayName("Check if setCellAlive(row, column) works as expected")
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
//    @Order(4)
    @DisplayName("Check if resetSpaceState() works as expected")
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
//    @Order(4)
    @DisplayName("Check if next() works as expected")
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
    @Order(4)
    @DisplayName("Check if setContent() works as expected")
    void setContent() {
        int[][] arr = getRandomArray();
        int randomInt = arr.length;
        assertEquals(randomInt, arr.length);
        assertEquals(randomInt, arr[0].length);
        assertEquals(randomInt, arr[randomInt / 2][randomInt / 2]);
        universe.setContent(arr);
        assertEquals(randomInt, universe.getState().getContent().length);
        assertEquals(randomInt, universe.getState().getContent()[0].length);
        assertEquals(randomInt, universe.getState().getContent()[randomInt / 2][randomInt / 2]);
    }
    
    @Test
    @Order(5)
    @DisplayName("Check if the universe can change its x-size (columns)")
    void setxSize() {
        
        assertEquals(rows, universe.getState().content.length);
        assertEquals(columns, universe.getState().content[0].length);
        
        assertEquals(0, universe.getState().content[0][0]);
        assertEquals(0, universe.getState().content[rows / 2][columns / 2]);
        assertEquals(0, universe.getState().content[rows - 1][columns - 1]);
        
        int[] arr = new int[columns];
        Arrays.fill(arr, 1);
        Arrays.fill(universe.getState().content, arr);
        
        assertEquals(1, universe.getState().content[0][0]);
        assertEquals(1, universe.getState().content[rows / 2][columns / 2]);
        assertEquals(1, universe.getState().content[rows - 1][columns - 1]);
        Exception e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows - 1][columns];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
        
        universe.setxSize(columns + 1);
        
        assertEquals(0, universe.getState().content[rows - 1][columns]);
        
        e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows - 1][columns + 1];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
        
        universe.setxSize(columns - 1);
        
        assertEquals(1, universe.getState().content[rows - 1][columns - 2]);
        
        e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows - 1][columns - 1];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
        
    }
    
    @Test
    @Order(5)
    @DisplayName("Check if the universe can change its y-size (rows)")
    void setySize() {
    
        assertEquals(rows, universe.getState().content.length);
        assertEquals(columns, universe.getState().content[0].length);
    
        assertEquals(0, universe.getState().content[0][0]);
        assertEquals(0, universe.getState().content[rows / 2][columns / 2]);
        assertEquals(0, universe.getState().content[rows - 1][columns - 1]);
    
        int[] arr = new int[columns];
        Arrays.fill(arr, 1);
        Arrays.fill(universe.getState().content, arr);
    
        assertEquals(1, universe.getState().content[0][0]);
        assertEquals(1, universe.getState().content[rows / 2][columns / 2]);
        assertEquals(1, universe.getState().content[rows - 1][columns - 1]);
        Exception e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows][columns - 1];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
    
        universe.setySize(rows + 1);
    
        assertEquals(0, universe.getState().content[rows][columns - 1]);
    
        e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows + 1][columns - 1];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
    
        universe.setySize(rows - 1);
    
        assertEquals(1, universe.getState().content[rows - 2][columns - 1]);
    
        e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows - 1][columns - 1];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
    
    }
    
    @Test
    @Order(6)
    @DisplayName("Check if the universe can change its size (matrix size)")
    void setSize() {
        
        assertEquals(rows, universe.getState().content.length);
        assertEquals(columns, universe.getState().content[0].length);
    
        assertEquals(0, universe.getState().content[0][0]);
        assertEquals(0, universe.getState().content[rows / 2][columns / 2]);
        assertEquals(0, universe.getState().content[rows - 1][columns - 1]);
    
        int[] arr = new int[columns];
        Arrays.fill(arr, 1);
        Arrays.fill(universe.getState().content, arr);
    
        assertEquals(1, universe.getState().content[0][0]);
        assertEquals(1, universe.getState().content[rows / 2][columns / 2]);
        assertEquals(1, universe.getState().content[rows - 1][columns - 1]);
        Exception e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows][columns - 1];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
        e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows - 1][columns];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
        e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows][columns];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
    
        universe.setSize(rows + 1, columns + 1);
    
        assertEquals(0, universe.getState().content[rows][columns]);
    
        e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows + 1][columns];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
        e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows][columns + 1];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
        e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows + 1][columns + 1];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
    
        universe.setSize(rows - 1, columns - 1);
    
        assertEquals(1, universe.getState().content[rows - 2][columns - 2]);
    
        e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows - 1][columns - 2];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
        e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows - 2][columns - 1];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
        e = assertThrows(IndexOutOfBoundsException.class, () -> {
            int i = universe.getState().content[rows - 1][columns - 1];
        });
        assertTrue(e.getMessage().toLowerCase(Locale.ROOT).contains("out of bounds"));
    }
    
}
