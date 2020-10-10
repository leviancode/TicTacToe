package com.leviancode.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // 1: yellow, 2:red, 0: empty
    private int mActivePlayer = 1;
    private int n = 3;
    private int[][] mGameState = new int[n][n];
    private TextView mWinnerTextView;
    private Button mPlayAgainButton;
    private boolean mGameActive = true;
    private int mStepsLeft = n * n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWinnerTextView = findViewById(R.id.gameResultTextView);
        mPlayAgainButton = findViewById(R.id.playAgainButton);
    }

    private boolean isWon(){
        int[] row = new int[n];

        // check rows
        for (int y = 0; y < n; y++) {
            System.arraycopy(mGameState[y], 0, row, 0, n);
            if (checkLine(row)){
                return true;
            }
        }

        // check columns
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                row[y] = mGameState[y][x];
            }
            if (checkLine(row)){
                return true;
            }
        }

        // check first diagonal
        for (int i = 0; i < n; i++) {
            row[i] = mGameState[i][i];
        }
        if (checkLine(row)){
            return true;
        }

        // check second diagonal
        for (int i = 0; i < n; i++) {
            row[i] = mGameState[i][n - 1 - i];
        }
        if (checkLine(row)){
            return true;
        }

        return false;
    }

    private boolean checkLine(int[] line){
        int player = line[0];
        if (player == 0) return false;

        int count = 1;
        for (int i = 1; i < line.length; i++) {
            if (line[i] == player) count++;
        }

        return count == 3;
    }

    public void onCellClicked(View view) {
        ImageView counter = (ImageView) view;
        String tappedCounter = counter.getTag().toString();

        String[] position = tappedCounter.split("\\.");
        int y = Integer.parseInt(position[0]);
        int x = Integer.parseInt(position[1]);

        if (mGameState[y][x] == 0 && mGameActive) {
            mGameState[y][x] = mActivePlayer;

            mStepsLeft--;

            if (mStepsLeft == 0) {
                gameOver(false);
            } else if (isWon()){
                gameOver(true);
            }
            dropIn(counter);
        }
    }

    private void dropIn(ImageView counter) {
        counter.setTranslationY(-1500);

        if (mActivePlayer == 1) {
            counter.setImageResource(R.drawable.yellow);
            mActivePlayer = 2;
        } else {
            counter.setImageResource(R.drawable.red);
            mActivePlayer = 1;
        }
        counter.animate().translationYBy(1500).rotation(1800).setDuration(300);
    }

    private void gameOver(boolean hasWinner) {
        String resultText;
        if (hasWinner) {
            resultText = getString(R.string.congratulation, (mActivePlayer == 1 ? "Yellow" : "Red"));
        } else{
            resultText = getString(R.string.nobodyWon);
        }
        mWinnerTextView.setText(resultText);
        mPlayAgainButton.setVisibility(View.VISIBLE);
        mGameActive = false;
    }

    public void onPlayAgainButtonClicked(View view) {
        mGameState = new int[n][n];
        mWinnerTextView.setText("");
        mGameActive = true;
        mStepsLeft = n * n;
        mPlayAgainButton.setVisibility(View.INVISIBLE);

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        removeAllCounters(tableLayout);
    }

    private void removeAllCounters(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup){
                removeAllCounters((ViewGroup) child);
            } else {
                ((ImageView)child).setImageDrawable(null);
            }
        }
    }
}