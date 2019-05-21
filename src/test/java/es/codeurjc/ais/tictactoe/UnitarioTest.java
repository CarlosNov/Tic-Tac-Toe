package es.codeurjc.ais.tictactoe;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(Parameterized.class)
public class UnitarioTest {

    private Board board;
    private String labelX = "X";
    private String labelO = "O";
    private List<Integer> winningList1;
    private List<Integer> winningList2;
    private int[] winningCellsX;
    private int[] winningCellsO;

    @Parameterized.Parameters
    public static Collection<Object[]> parameters () {

        Object [][] values = {
                {Arrays.asList(0,1,2),Arrays.asList(3,7,8,2)},
                {Arrays.asList(3,4,5),Arrays.asList(1,7,8,2,3)},
                {Arrays.asList(6,7,8),Arrays.asList(1,4,5,6)},
                {Arrays.asList(0,3,6),Arrays.asList(1,7,8,2,3)},
                {Arrays.asList(1,4,7),Arrays.asList(0,2,3,7)},
                {Arrays.asList(2,5,8),Arrays.asList(1,3,7,2,8)},
                {Arrays.asList(0,4,8),Arrays.asList(1,2,3,8)},
                {Arrays.asList(6,4,2),Arrays.asList(0,3,7,2,4)}
        };
        return Arrays.asList(values);
    }

    @Parameterized.Parameter(0) public List<Integer> markCells1;
    @Parameterized.Parameter(1) public List<Integer> markCells2;
    
    @Before
    public void init() {
        this.board = new Board();
        winningList1 = null;
        winningList2 = null;
    }

    @Test
    public void player1WinsTest(){
        given(labelX, labelO);
        when();
        assertThat(winningList1).isEqualTo(markCells1); //Player 1 (X) gana
        assertThat(winningList2).isNull(); //Player 2 (O) no gana
    }

    @Test
    public void player2WinsTest(){
        given(labelO, labelX);
        when();
        assertThat(winningList2).isEqualTo(markCells1); //Player 2 (X) gana
        assertThat(winningList1).isNull(); //Player 1 (O) no gana
    }

    @Test
    public void checkDraw(){
        //GIVEN
        //Pongo todas las fichas con O
        for (int i = 0; i < 9; i++){
            TicTacToeGame.Cell cell = this.board.getCell(i);
            cell.value = this.labelO;
        }
        //Coloco las Xs en su lugar para generar un empate
        for (Integer integer : markCells2) {
            TicTacToeGame.Cell cell = this.board.getCell(integer);
            cell.value = this.labelX;
        }
        //WHEN
        when();
        assertThat(winningList1).isNull();  // el player 1 no gana
        assertThat(winningList2).isNull();  // el player 2 no gana
        assertThat(this.board.checkDraw()).isEqualTo(true);  // hay empate
    }

    private void given(String label1, String label2){
        for (int i = 0; i < 3; i++) {
            TicTacToeGame.Cell cell1 = this.board.getCell(markCells1.get(i));
            cell1.value = label1; //combinacion ganadora
            TicTacToeGame.Cell cell2 = this.board.getCell(markCells2.get(i));
            cell2.value = label2; //combinacion perdedora
        }
    }

    private void when(){
        winningCellsX = this.board.getCellsIfWinner(labelX);
        winningCellsO = this.board.getCellsIfWinner(labelO);
        if (winningCellsX != null){
            winningList1 = new ArrayList<>();
            for (Integer integer : winningCellsX) {
                winningList1.add(integer);
            }
        }
        if (winningCellsO != null){
            winningList2 = new ArrayList<>();
            for (Integer integer : winningCellsO) {
                winningList2.add(integer);
            }
        }
    }
}
