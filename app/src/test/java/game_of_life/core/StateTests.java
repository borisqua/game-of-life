package game_of_life.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing the state of the universe class: game_of_life.core.State")
class StateTests {
    
    Random rnd = new Random(System.currentTimeMillis() % 1_000_000_009L/*large prime*/);
    private State state;
    private final int rows = 5;
    private final int columns = 7;
    
    @BeforeEach
    void setUp() {
        int[] arr = new int[columns];
        int[][] content = new int[rows][columns];
        Arrays.fill(arr, 1);
        Arrays.fill(content, arr);
        state = new State(content);
    }
    
    @Test
    @Order(0)
    @DisplayName("Check if instance of the universe state successfully instantiated and contains correct information.")
    void testInstantiating() {
        assertNotNull(state);
        assertEquals(1, state.generation);
        assertEquals(0, state.population);
        assertEquals(rows, state.content.length);
        assertEquals(columns, state.content[0].length);
    }
    
    @Test
    @Order(1)
    @DisplayName("Check if state instance returns correct content matrix.")
    void getContent() {
        int[][] content = state.getContent();
        assertEquals(columns, content[0].length);
        assertEquals(rows, content.length);
        assertEquals(1, content[0][0]);
        assertEquals(1, content[rows / 2][columns / 2]);
        assertEquals(1, content[rows - 1][columns - 1]);
        
    }
    
    @Test
    @Order(2)
    @DisplayName("Check if state instance correctly sets new content matrix.")
    void setContent() {
        int[] arr = new int[columns * 2];
        int[][] content = new int[rows * 2][columns * 2];
        Arrays.fill(arr, 2);
        Arrays.fill(content, arr);
        state.setContent(content);
        assertEquals(columns *2, content[0].length);
        assertEquals(rows*2, content.length);
        assertEquals(2, content[0][0]);
        assertEquals(2, content[rows][columns]);
        assertEquals(2, content[rows * 2 - 1][columns * 2 - 1]);
    }
    
    @Test
    @Order(3)
    @DisplayName("Check if state instance returns correct population value.")
    void getPopulation() {
        
        assertEquals(0, state.getPopulation());
        
        int[] arr = new int[columns * 2];
        int[][] content = new int[rows * 2][columns * 2];
        Arrays.fill(arr, 2);
        Arrays.fill(content, arr);
        state.setContent(content);
    
        assertEquals(rows * columns * 4, state.getPopulation());
    }
    
    @Test
    @Order(3)
    @DisplayName("Check if state instance returns correct generation value.")
    void getGeneration() {
        assertEquals(1, state.getGeneration());
        state.generation = 10;
        assertEquals(10, state.getGeneration());
    }
}