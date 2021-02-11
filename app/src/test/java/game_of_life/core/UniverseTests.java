package game_of_life.core;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("A tests cases with the instances of the universe class.")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UniverseTests {
    
    @Test
    @Order(1)
    @DisplayName("The instance of universe successfully instantiated.")
    public void testInstantiating() {
        Universe universe = new Universe(10, 20);
        assertNotNull(universe);
    }
    
    
    @Test
    @Order(2)
    @DisplayName("The instance of universe contains correct state information.")
    void getSpaceState() {
        Universe universe = new Universe(10, 20);
        assertEquals(10, universe.getState().content.length);
        assertEquals(20, universe.getState().content[0].length);
    }
    
    
    @Test
    void getState() {
    }
    
    @Test
    void setCellState() {
    }
    
    @Test
    void resetSpaceState() {
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
    
    @Test
    void next() {
    }
}
