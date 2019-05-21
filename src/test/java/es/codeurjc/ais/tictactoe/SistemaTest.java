package es.codeurjc.ais.tictactoe;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.junit.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

public class SistemaTest
{
    private WebDriver driver1;
    private WebDriver driver2;
    private static ArrayList<WebElement> webElements1;
    private static ArrayList<WebElement> webElements2;

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
        WebApp.start();
    }

    @Before
    public void setupTest()
    {
        driver1 = new ChromeDriver();
        driver2 = new ChromeDriver();

        webElements1 = new ArrayList<>();
        webElements2 = new ArrayList<>();
    }

    @After
    public void teardown()
    {
        if (driver1 != null)
        {
            driver1.quit();
        }

        if (driver2 != null)
        {
            driver2.quit();
        }
    }

    @AfterClass
    public static void closeClass()
    {
        WebApp.stop();
    }

    public void initWebApp()
    {
        driver1.get("http://localhost:8080/");
        driver2.get("http://localhost:8080/");

        WebElement nameInput1 = driver1.findElement(By.id("nickname"));
        WebElement nameInput2 = driver2.findElement(By.id("nickname"));

        nameInput1.click();
        nameInput1.sendKeys("player1");

        nameInput2.click();
        nameInput2.sendKeys("player2");

        WebElement startBtn1 = driver1.findElement(By.id("startBtn"));
        WebElement startBtn2 = driver2.findElement(By.id("startBtn"));

        startBtn1.click();
        startBtn2.click();

        WebDriverWait wait1 = new WebDriverWait(driver1, 10);
        WebElement element1 = wait1.until(ExpectedConditions.elementToBeClickable(By.id("cell-8")));

        WebDriverWait wait2 = new WebDriverWait(driver1, 10);
        WebElement element2 = wait2.until(ExpectedConditions.elementToBeClickable(By.id("cell-8")));

    }

    public void checkResult(int [] player1Moves, int [] player2Moves, String winner)
    {
        for(int i = 0; i < player1Moves.length; i++)
        {
            webElements1.add(driver1.findElement(By.id("cell-"+player1Moves[i])));
        }

        for(int i = 0; i < player2Moves.length; i++)
        {
            webElements2.add(driver2.findElement(By.id("cell-"+player2Moves[i])));
        }

        for (int i = 0; i < player1Moves.length + player2Moves.length; i++)
        {
            // Si es el turno del player1
            if ( i % 2 == 0)
            {
                webElements1.get(0).click();
                webElements1.remove(0);
            }
            else // Si es el turno del player2
            {
                webElements2.get(0).click();
                webElements2.remove(0);
            }
        }

        driver1.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        driver2.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        assertThat(driver1.switchTo().alert().getText()).startsWith(winner);
        assertThat(driver2.switchTo().alert().getText()).startsWith(winner);
    }

    @Test
    public void Player1WinsTest()
    {
        /*
            X O O   1º 2º 8º
            O X X   4º 3º 7º   -> Jugador 1 ganador en 9 turnos
            X O X   5º 6º 9º
         */

        int [] player1Moves  = {0,4,6,5,8};
        int [] player2Moves  = {1,3,7,2};

        initWebApp();

        checkResult(player1Moves, player2Moves, "player1");
    }

    @Test
    public void DrawTest()
    {
        /*
            X O X   1º 2º 7º
            X O O   3º 4º 8º   -> Ganador X en 5 turnos
            O X X   6º 5º 9º
         */

        int [] player1Moves = {0,3,7,2,8};
        int [] player2Moves = {1,4,6,5};

        initWebApp();

        checkResult(player1Moves, player2Moves, "Draw");
    }

    @Test
    public void Player2WinsTest()
    {
        /*
            O X     2º 1º
            O X X   4º 3º 5º   -> Jugador 2 ganador en 6 turnos
            O       6º
         */

        int [] player1Moves = {1,4,5};
        int [] player2Moves = {0,3,6};

        initWebApp();

        checkResult(player1Moves, player2Moves, "player2");
    }
}
