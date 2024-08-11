package com.example.gamesusunhurufdts;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private TextView timerTextView;
    private Button resetButton;
    private CountDownTimer countDownTimer;

    private List<Character> letters = new ArrayList<>();
    private long startTime = 0L;
    private long elapsedTime = 0L;
    private boolean isGameRunning = false;
    private int currentLevel = 1;

    private final int[] levelTimeLimits = new int[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);
        timerTextView = findViewById(R.id.timerTextView);
        resetButton = findViewById(R.id.resetButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });

        for (int i = 0; i < 100; i++) {
            levelTimeLimits[i] = 60 - i / 2;
            if (levelTimeLimits[i] < 10) {
                levelTimeLimits[i] = 10;
            }
        }

        startGame();
    }

    private void startGame() {
        letters.clear();
        gridLayout.removeAllViews();

        int numLetters = currentLevel + 2;
        for (int i = 0; i < numLetters; i++) {
            letters.add((char) ('A' + i));
        }
        Collections.shuffle(letters);
        Collections.sort(letters, Collections.reverseOrder());

        for (char letter : letters) {
            Button button = new Button(this);
            button.setText(String.valueOf(letter));
            button.setTextColor(Color.BLACK);
            button.setBackgroundColor(Color.YELLOW);
            button.setGravity(Gravity.CENTER);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkLetter(button);
                }
            });

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            gridLayout.addView(button, params);
        }

        startTime = System.currentTimeMillis();
        isGameRunning = true;
        startTimer();
    }

    private void checkLetter(Button button) {
        if (!isGameRunning) return;

        char clickedLetter = button.getText().charAt(0);
        if (clickedLetter == letters.get(0)) {
            letters.remove(0);
            button.setVisibility(View.GONE);

            if (letters.isEmpty()) {
                endGame(true);
            }
        }
    }

    private void endGame(boolean isWin) {
        isGameRunning = false;
        countDownTimer.cancel();

        if (isWin) {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            if (currentLevel < 100) {
                currentLevel++;
                startGame();
            } else {
                Toast.makeText(this, "Game Complete", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Time's up", Toast.LENGTH_SHORT).show();
            resetGame();
        }
    }

    private void resetGame() {
        if (isGameRunning) {
            endGame(false);
        }
        startGame();
    }

    private void startTimer() {
        long timeLimit = levelTimeLimits[currentLevel -1] * 1000L;
        countDownTimer = new CountDownTimer(timeLimit, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                long minutes = elapsedTime / 60;
                long seconds = elapsedTime % 60;
                timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                endGame(false);
            }
        };
        countDownTimer.start();
    }
}