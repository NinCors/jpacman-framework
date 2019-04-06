package nl.tudelft.jpacman.assignment1;

import static org.assertj.core.api.Assertions.assertThat;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Unit;

import nl.tudelft.jpacman.level.Player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


/*
As a player,
 I want to move my Pacman around on the board;
So that I can earn all points and win the game.

Scenario S2.1: The player consumes
Given the game has started,
 and  my Pacman is next to a square containing a pellet;
When  I press an arrow key towards that square;
Then  my Pacman can move to that square,
 and  I earn the points for the pellet,
 and  the pellet disappears from that square.

Scenario S2.2: The player moves on empty square
Given the game has started,
 and  my Pacman is next to an empty square;
When  I press an arrow key towards that square;
Then  my Pacman can move to that square
 and  my points remain the same.

Scenario S2.3: The move fails
Given the game has started,
  and my Pacman is next to a cell containing a wall;
When  I press an arrow key towards that cell;
Then  the move is not conducted.

Scenario S2.4: The player dies
Given the game has started,
 and  my Pacman is next to a cell containing a ghost;
When  I press an arrow key towards that square;
Then  my Pacman dies,
 and  the game is over.

Scenario S2.5: Player wins, extends S2.1
When  I have eaten the last pellet;
Then  I win the game.

*/

public class MoveTest02 {
    public static final String TESTMAP = "/map/test02/moveTest02_board.txt";
    private Launcher launcher;
    private Game game;
    private Player player;

    /**
     * Start a launcher, which can display the user interface.
     */
    @BeforeEach
    public void before() {
        launcher = new Launcher().withMapFile(TESTMAP);
        launcher.launch();

        game = getGame();
        assertThat(getGame().isInProgress()).isFalse();
        game.start();
        assertThat(getGame().isInProgress()).isTrue();
        player = getSinglePlayer();
        assertThat(player.getScore()).isZero();
    }

    /**
     * Close the user interface.
     */
    @AfterEach
    public void after() {
        launcher.dispose();
    }


    /* Scenario S2.1: The player consumes
        Given the game has started,
         and  my Pacman is next to a square containing a pellet;
        When  I press an arrow key towards that square;
        Then  my Pacman can move to that square,
         and  I earn the points for the pellet,
         and  the pellet disappears from that square.
     */

    @Test
    public void playerConsume(){
        // Save original score
        int score = player.getScore();
        Square left = getSquareAt(Direction.WEST);
        List<Unit> occupants = left.getOccupants();
        Unit pellet = occupants.get(0);
        // Push to the left
        game.move(player,Direction.WEST);
        assertThat(player.getScore()).isEqualTo(score+10);
        assertThat(left.getOccupants().contains(pellet)).isFalse();
        assertThat(left.getOccupants().contains(player)).isTrue();
    }

    /*
        Scenario S2.2: The player moves on empty square
        Given the game has started,
         and  my Pacman is next to an empty square;
        When  I press an arrow key towards that square;
        Then  my Pacman can move to that square
         and  my points remain the same.
     */
    @Test
    public void movesOnEmpty(){
        // Move to east
        int score = player.getScore();

        // Make sure East is empty
        Square right = getSquareAt(Direction.EAST);
        assertThat(right.getOccupants().isEmpty()).isTrue();

        game.move(player,Direction.EAST);
        assertThat(player.getScore()).isEqualTo(score);
        assertThat(right.getOccupants().contains(player)).isTrue();
    }

    /*
        Scenario S2.3: The move fails
        Given the game has started,
          and my Pacman is next to a cell containing a wall;
        When  I press an arrow key towards that cell;
        Then  the move is not conducted.
     */
    @Test
    public void moveFail(){
        Square current = player.getSquare();
        // Move to North
        game.move(player,Direction.NORTH);
        // Check if current still contains player
        assertThat(current.getOccupants().contains(player)).isTrue();
    }

    /*
        Given the game has started,
         and  my Pacman is next to a cell containing a ghost;
        When  I press an arrow key towards that square;
        Then  my Pacman dies,
         and  the game is over.
     */
    @Test
    public void playerDie(){
        // Move to south to face monster
        game.move(player,Direction.SOUTH);
        assertThat(player.isAlive()).isFalse();
    }


    /*
        Scenario S2.5: Player wins, extends S2.1
        When  I have eaten the last pellet;
        Then  I win the game.
     */
    @Test
    public void eatLast(){
        game.move(player, Direction.WEST);
        assertThat(player.isAlive()).isTrue();
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
