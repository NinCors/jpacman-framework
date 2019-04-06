package nl.tudelft.jpacman.assignment1;

/*
As a player,
 I want to be able to suspend the game;
So  that I can pause and do something else.

Scenario S4.1: Suspend the game.
Given the game has started;
When  the player clicks the "Stop" button;
Then  all moves from ghosts and the player are suspended.

Scenario S4.2: Restart the game.
Given the game is suspended;
When  the player hits the "Start" button;
Then  the game is resumed.
 */

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameSuspendTest04 {

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

        game.start();
    }

    /**
     * Close the user interface.
     */
    @AfterEach
    public void after() {
        launcher.dispose();
    }


    /*
        Scenario S4.1: Suspend the game.
        Given the game has started;
        When  the player clicks the "Stop" button;
        Then  all moves from ghosts and the player are suspended.
     */
    @Test
    public void SuspendGame() throws InterruptedException {
        assertThat(game.isInProgress()).isTrue();
        game.stop();
        assertThat(game.isInProgress()).isFalse();

        Square player_square = player.getSquare();
        //Wait and let ghost and player move
        Thread.sleep(300L);
        game.move(player,Direction.WEST);

        //Check if they still stay in the same location
        assertThat(player_square.getOccupants().contains(player)).isTrue();
        assertThat(ghost_square.getOccupants().contains(ghost)).isTrue();
    }

    /*
        Scenario S4.2: Restart the game.
        Given the game is suspended;
        When  the player hits the "Start" button;
        Then  the game is resumed.
     */
    @Test
    public void restartGame(){
        assertThat(game.isInProgress()).isTrue();
        game.stop();
        assertThat(game.isInProgress()).isFalse();

        game.start();
        assertThat(game.isInProgress()).isTrue();
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
