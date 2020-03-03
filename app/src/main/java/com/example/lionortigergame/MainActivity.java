package com.example.lionortigergame;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.IdentityHashMap;

public class MainActivity extends AppCompatActivity {
    // we will create an enum for a new type of variable called player and give it two possible values
    // ONE and TWO
    enum Player{
        ONE,TWO
    }
    private Button btnReStart;
    private GridLayout mGridLayout;

    // Player.ONE = tiger, Player.TWO = lion
    Player currentPlayer = Player.ONE;  // sets the currentPlayer as player one at game start
    int activePlayer = 0; // this integer is for player one
    boolean gameIsActive = true;
    // value of 2 in gameState index signifies un-played and available for play as the starting state;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}; // 25 grid panels
    // an array within an array int [][]
    // the array within are called winningPosition, singular.  It has tags as elements.
    // Element 0 of winningPositions, plural and master array (the complete set of possible winning sequences)
    // contains a winningPosition array with 4 elements, (0,1,2,3) each of which
    // represents a tag on a TextView in the grid.
    int[][] winningPositions = {
            {0, 1, 2, 3}, {1, 2, 3, 4}, {5, 6, 7, 8}, {6, 7, 8, 9}, {10, 11, 12, 13}, {11, 12, 13, 14}, {15, 16, 17, 18},
            {16, 17, 18, 19}, {20, 21, 22, 23}, {21, 22, 23, 24}, //horizontal
            {0, 5, 10, 15}, {5, 10, 15, 20}, {1, 6, 11, 16}, {6, 11, 16, 21}, {2, 7, 12, 17}, {7, 12, 17, 22}, {3, 8, 13, 18},
            {8, 13, 18, 23}, // vertical
            {3, 7, 11, 15}, {4, 8, 12, 16}, {8, 12, 16, 20}, {9, 13, 17, 21}, {5, 11, 17, 23}, {0, 6, 12, 18}, {6, 12, 18, 24},
            {1, 7, 13, 19}//diagonal
    };
    // onCreate is only used to set the layout in this app and to house the method imageViewIsTapped
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReStart = findViewById(R.id.btnRestart);
        mGridLayout = findViewById(R.id.gridLayout);
        btnReStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reStartGame();
            }
        });
    }
    public void imageViewIsTapped(View tappedImageView){
        ImageView tappedView = (ImageView) tappedImageView;
        // the View obtained from the onClick method imageViewIsTapped
        // is downcast to (ImageView) the subclass. That ImageView is labelled tappedImageView
        // and the return is stored in the variable tappedView of class ImageView
        // this is downcasting (see notes, lecture 73)
        int tappedCounter = Integer.parseInt(tappedView.getTag().toString());
        Log.i("myTag", "tappedCounter = "+tappedCounter);
        // code above stores the tag, converte to a string converted to an integer, for each image view in
        // the grid in a variable of type int called tappedCounter.  It is later used as the index
        // number for the gameState array (line 26).  If the square is unplayed its value will be 2
        // and, if the square is played

        //tappedView.setTranslationX(-500);
        // we will now create an if stmt to have a tiger drawn if it is player one
        // and to have a lion, if it is player 2.

        if (gameState[tappedCounter] == 2  && gameIsActive) {
            // if gameState[tappedCounter]==2 then the square has not yet been played(see line 26)
            // and all of the following code can be considered down to the else stmt, line 109
            // if the square has already been tapped, tapped counter = 0 (tiger) or 1 (lion) & code
            // is consequently skipped
            gameState[tappedCounter] = activePlayer;//if tapped by player 1 it is now 0, plyr 2 = 1
            Log.i("myTag","gameState[tappedCounter] value = "+gameState[tappedCounter]);
            tappedImageView.setTranslationX(-1000);
            if (currentPlayer==Player.ONE) {
                tappedView.setImageResource(R.drawable.tiger);
                tappedView.animate().translationXBy(1000).rotation(1080).setDuration(1000);
                currentPlayer = Player.TWO;
                activePlayer = 1;

            }
            else if(currentPlayer==Player.TWO){

                tappedView.setImageResource(R.drawable.lion);
                tappedView.animate().translationXBy(1000).rotation(-1080).setDuration(1000);
                currentPlayer=Player.ONE;
                activePlayer = 0;
            }
            //checking for the winner
            // the colon between winningPosition and winningPositions means "set"
            // iteratively, winningPosition is given each of the sets in winningPositions, one by one
            // the first set would be {0, 1, 2, 3}. This is then processed in the following code.
            // this is the array within the array winningPositions in line 26 above
            for (int[] winningPosition : winningPositions) {
                // if all four numbers in any one potential winning array match
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[2]] == gameState[winningPosition[3]] &&
                        gameState[winningPosition[0]] !=2 ) {
                    //Someone has won
                    gameIsActive = false; // this shuts down further play, line 59 above
                    String winner = "Lion"; // setting this as the default state
                    // but if the actual results show otherwise because player 0 is in the game state zeroth
                    // element of any of the combinations
                    btnReStart.setVisibility(View.VISIBLE);
                    if (gameState[winningPosition[0]] == 0){ // it is only zero when plyr 1 (tiger)
                                                            // is the plyr that has filled the whole array
                                    // no need to check if all other elements in that array are plyr one tigers
                                    // that was checked in the code in line 84
                        winner = "Tiger";
                    }
                    TextView winnerMsg = (TextView)  findViewById(R.id.winnerMsg);
                    winnerMsg.setText(winner + " has won!");
                    // LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
                    // layout.setVisibility(View.VISIBLE);

                }
                else {
                    // if no one has one yet then,
                    boolean gameIsOver = true;
                    for (int counterState : gameState ) { //this sets counterState to each of the values
                                                        // in gameState, one after the other (iteration)
                        // some of those values may be 0 (tiger), some 1 (lion) and the rest 2 (unplayed)
                        // see this link on the web; https://stackoverflow.com/questions/2399590/
                        // what-does-the-colon-operator-do
                        // it is checking to see if game play is still possible (one or more 2's remaining)

                        if (counterState == 2) {
                            gameIsOver = false; // and game play should continue
                        }
                    }
                    // if gameIsOver is true, on the other hand.. and no squares are left to click.
                    if (gameIsOver) {
                        TextView winnerMsg = (TextView) findViewById(R.id.winnerMsg);
                        winnerMsg.setText("It is a DRAW!");
                        btnReStart.setVisibility(View.VISIBLE);

                        //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
                        //layout.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
    // reStart game function
    private void reStartGame(){
        currentPlayer = Player.ONE;  // sets the currentPlayer as player one at game start
        int activePlayer = 0; // this integer is for player one
        boolean gameIsActive = true;
        // value of 2 in gameState index signifies un-played and available for play as the starting state;
        int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2};
        Log.i("myTag", "play again was pressed");
        // we create an integer called index with a value of zero
        // we terminate when that integer reaches 25 (number of views in grid)
        // we increment the index by 1 each time the for loop loops
        for (int index = 0; index < mGridLayout.getChildCount(); index++){
            // now we access our imageViews, one by one
            // we create a new variable of type ImageView called gridView
            // we assign it the value of cast(ImageView) containing the view of the child in mGridLayout
            // with the index number incremented
            ImageView gridView = (ImageView) mGridLayout.getChildAt(index);
        }

    }
}


