package es.codeurjc.ais.tictactoe;

import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerValue;

import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.hamcrest.MockitoHamcrest.*;
import static org.assertj.core.api.Assertions.*;

public class DobleTest
{
    private static TicTacToeGame ticTacToeGame;
    private static Connection connection1;
    private static Connection connection2;
    private static Player player1;
    private static Player player2;
    private static ArrayList<Player> players = new ArrayList<>();

    @Before
    public void setUpClass()
    {
        ticTacToeGame = new TicTacToeGame();

        connection1 =  mock(Connection.class);
        connection2 =  mock(Connection.class);

        player1 = new Player(1, "X","player1");
        player2 = new Player(2, "O","player2");

        players = new ArrayList<>();
    }

    /*
     *   Inicializa el objeto TicTacToeGame añadiendo las conexiones y jugadores.
     *   Además, comprueba los mensajes de la función sendEvent para comprobar que son coherentes.
     */
    public void initTest()
    {
        ticTacToeGame.addConnection(connection1);
        ticTacToeGame.addConnection(connection2);

        players.add(player1);
        players.add(player2);

        ticTacToeGame.addPlayer(player1);

        verify(connection1).sendEvent(eq(EventType.JOIN_GAME), argThat(hasItems(player1)));
        verify(connection2).sendEvent(eq(EventType.JOIN_GAME), argThat(hasItems(player1)));

        reset(connection1);
        reset(connection2);

        ticTacToeGame.addPlayer(player2);

        verify(connection1).sendEvent(eq(EventType.JOIN_GAME), argThat(hasItems(player1, player2)));
        verify(connection2).sendEvent(eq(EventType.JOIN_GAME), argThat(hasItems(player1, player2)));
        verify(connection1).sendEvent(EventType.SET_TURN, player1);

        reset(connection1);
        reset(connection2);
    }

    /*
     *   Comprueba si el objeto TicTacToeGame cambia de turno cada vez que se llama a la función mark()
     *   Además, comprueba que el resultado de la partida es igual al esperado.
     */
    public void checkTurnAndResult(int[] gameDisplay, Player winner)
    {
        assertThat(gameDisplay.length).isLessThan(10); //No debe haber más de 9 movimientos

        for(int i = 0; i < gameDisplay.length; i++)
        {
            ticTacToeGame.mark(gameDisplay[i]);

            // Comprobamos que se cambia el turno, excepto para la última jugada.
            if (i != gameDisplay.length -1)
            {
                /* Debemos cambiar turno mientras no haya ganador, si se activa este assertThat, entonces el array
                   de entrada no está bien construido. */
                assertThat(ticTacToeGame.checkWinner().win).isFalse();

                verify(connection1).sendEvent(eq(EventType.SET_TURN), eq(players.get((i+1)%2)));
                verify(connection2).sendEvent(eq(EventType.SET_TURN), eq(players.get((i+1)%2)));

                reset(connection1);
                reset(connection2);
            }
        }

        ArgumentCaptor<WinnerValue> argument1 = ArgumentCaptor.forClass(WinnerValue.class);
        verify(connection1).sendEvent(eq(EventType.GAME_OVER), argument1.capture());
        Object event1 = argument1.getValue();

        ArgumentCaptor<WinnerValue> argument2 = ArgumentCaptor.forClass(WinnerValue.class);
        verify(connection1).sendEvent(eq(EventType.GAME_OVER), argument2.capture());
        Object event2 = argument2.getValue();

        // Si hay debe ser empate
        if(winner == null)
        {
            try {
                assertThat(event1).isNull();
                assertThat(event2).isNull();
            }
            catch (ComparisonFailure e)
            {
                e.printStackTrace();
                fail("El resultado de la partida ha sido victoria para el jugador " +
                        ((WinnerValue) event1).player.getName() + ". Sin embargo, se esperaba un empate.");
            }

        }
        else // Si hay ganador, debe corresponderse con el ganador esperado.
        {
            try {
                assertThat(((WinnerValue) event1).player).isEqualTo(winner);
                assertThat(((WinnerValue) event2).player).isEqualTo(winner);
            }
            catch (NullPointerException e )
            {
                e.printStackTrace();
                fail("El resultado de la partida ha sido empate. Sin embargo, se esperaba " +
                        "a " + winner.getName() + " como ganador.");
            }

        }
    }

    @Test
    public void Player1WinsTest()
    {
        /*
            X O O   1º 2º 8º
            O X X   4º 3º 7º   -> Jugador 1 ganador en 9 turnos
            X O X   5º 6º 9º
         */

        int gameDisplay [] = {0,1,4,3,6,7,5,2,8};

        initTest();

        checkTurnAndResult(gameDisplay, player1);
    }

    @Test
    public void DrawTest() {
        /*
            X O X   1º 2º 7º
            X O O   3º 4º 8º   -> Ganador X en 5 turnos
            O X X   6º 5º 9º
         */

        int gameDisplay [] = {0,1,3,4,7,6,2,5,8};

        initTest();

        checkTurnAndResult(gameDisplay, null);
    }

    @Test
    public void Player2WinsTest()
    {
        /*
            O X     2º 1º
            O X X   4º 3º 5º   -> Jugador 2 ganador en 6 turnos
            O       6º
         */

        int gameDisplay [] = {1,0,4,3,5,6};

        initTest();

        checkTurnAndResult(gameDisplay, player2);
    }
}
