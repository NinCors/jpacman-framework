package nl.tudelft.jpacman.assignment1;


import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.sprite.Sprite;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.assertj.core.api.Assertions.assertThat;

public class GhostTest03 {
    public static final String TESTMAP = "/map/test03/ghostTest03_01_board.txt";
    private Launcher launcher;
    private Game game;
    private Player player;

    private Square dot;
    private Square empty;
    private Square ghost_square;
    private Unit ghost;
    /**
     * Start a launcher, which can display the user interface.
     */
    @BeforeEach
    public void before() {
        launcher = new Launcher().withMapFile(TESTMAP);
        launcher.launch();

        game = getGame();
        assertThat(getGame().isInProgress()).isFalse();

        player = getSinglePlayer();
        assertThat(player.getScore()).isZero();

        // Design the map with G  . P form
        dot = getSquareAt(Direction.WEST);
        empty = dot.getSquareAt(Direction.WEST);
        ghost_square = empty.getSquareAt(Direction.WEST);
        ghost = ghost_square.getOccupants().get(0);
    }

    /**
     * Close the user interface.
     */
    @AfterEach
    public void after() {
        launcher.dispose();
    }


    /*
        Scenario S3.1: A ghost moves.
        Given the game has started,
         and  a ghost is next to an empty cell;
        When  a tick event occurs;
        Then  the ghost can move to that cell.
     */
    @Test
    public void ghostMoveEmpty() throws InterruptedException {

        game.start();
        assertThat(getGame().isInProgress()).isTrue();

        Thread.sleep(200L);
        //Let ghost move one time
        assertThat(ghost_square.getOccupants().contains(ghost)).isFalse();
        assertThat(empty.getOccupants().contains(ghost)).isTrue();

    }

    /*
        Scenario S3.2: The ghost moves over a square with a pellet.
        Given the game has started,
         and  a ghost is next to a cell containing a pellet;
        When  a tick event occurs;
        Then  the ghost can move to the cell with the pellet,
         and  the pellet on that cell is not visible anymore.
     */

    @Test
    public void ghostMovePellet() throws InterruptedException {

        Sprite ghost_sprite = ghost.getSprite();
        Sprite pellet = dot.getOccupants().get(0).getSprite();

        //Restart the game
        game.start();
        assertThat(getGame().isInProgress()).isTrue();

        //Let ghost move one time
        Thread.sleep(200L);
        assertThat(ghost_square.getOccupants().contains(ghost)).isFalse();
        assertThat(empty.getOccupants().contains(ghost)).isTrue();

        //Let ghost move second time
        Thread.sleep(300L);
        assertThat(empty.getOccupants().contains(ghost)).isFalse();
        assertThat(dot.getOccupants().contains(ghost)).isTrue();

        game.stop();
        // Find what's in the top of sprite in the dot square
        Sprite dot_top = dot.getOccupants().get(dot.getOccupants().size()-1).getSprite();
        assertThat(dot_top.equals(pellet)).isFalse();
        assertThat(dot_top.equals(ghost_sprite)).isTrue();

    }

    /*
        Scenario S3.3: The ghost leaves a cell with a pellet.
        Given a ghost is on a cell with a pellet (see S3.2);
        When  a tick even occurs;
        Then  the ghost can move away from the cell with the pellet,
         and  the pellet on that cell is is visible again.
     */
    @Test
    public void ghostMovePelletAfter() throws InterruptedException{
        Sprite ghost_sprite = ghost.getSprite();
        Sprite pellet = dot.getOccupants().get(0).getSprite();

        //Restart the game
        game.start();
        assertThat(getGame().isInProgress()).isTrue();

        Thread.sleep(500L);
        // Now the ghost in the pellet square
        assertThat(empty.getOccupants().contains(ghost)).isFalse();
        assertThat(dot.getOccupants().contains(ghost)).isTrue();

        Thread.sleep(300L);
        // Now the ghost left the pellet square
        assertThat(dot.getOccupants().contains(ghost)).isFalse();
        game.stop();
        // Check the top of the pellet square
        Sprite dot_top = dot.getOccupants().get(dot.getOccupants().size()-1).getSprite();
        assertThat(dot_top.equals(pellet)).isTrue();
        assertThat(dot_top.equals(ghost_sprite)).isFalse();
    }

    /*
         Scenario S3.4: The player dies.
        Given the game has started,
         and  a ghost is next to a cell containing the player;
        When  a tick event occurs;
        Then  the ghost can move to the player,
         and  the game is over.
     */
    @Test
    public void playerDie() throws InterruptedException{
        Sprite ghost_sprite = ghost.getSprite();
        Sprite pellet = dot.getOccupants().get(0).getSprite();

        //Restart the game
        game.start();
        assertThat(getGame().isInProgress()).isTrue();

        Thread.sleep(500L);
        // Now the ghost in the pellet square
        assertThat(empty.getOccupants().contains(ghost)).isFalse();
        assertThat(dot.getOccupants().contains(ghost)).isTrue();

        Thread.sleep(300L);
        // Now the ghost left the pellet square to the player square
        assertThat(dot.getOccupants().contains(ghost)).isFalse();
        assertThat(player.getSquare().getOccupants().contains(ghost)).isTrue();

        // check the player status
        assertThat(player.isAlive()).isFalse();
        assertThat(game.isInProgress()).isFalse();
    }


    private Game getGame(){
        return launcher.getGame();
    }

    private Player getSinglePlayer(){
        return getGame().getPlayers().get(0);
    }

    private Square getSquareAt(Direction direction){
        return getSinglePlayer().getSquare().getSquareAt(direction);
    }

}
