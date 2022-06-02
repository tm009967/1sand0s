package studios.dev.tlm.onesandzeroes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class GameActivity extends Activity {

    final static Typeface TYPEFACE = Typeface.MONOSPACE;
    public final static int LIGHT_BLUE_COLOR = 0xff66ffff;

    /** intent extra keys */
    public final static String EXTRA_NUMBER_OF_ROWS = "studios.dev.tlm.onesandzeroes.ROWS";
    public final static String EXTRA_SCORE = "studios.dev.tlm.onesandzeroes.SCORE";

    /** For double-tapping back */
    private Toast backToast;
    private boolean doubleBackToExitPressedOnce;

    final Handler handler = new Handler();

    private Gameboard gameboard;
    private Timer timer;
    private TextView levelView;
    private TextView scoreView;
    private TextView messageBar;
    private Button button1;
    private Button button0;
    private Button buttonGo;

    private int level;
    private int score;
    private Stack<Address> askStack = new Stack<Address>();
    private Gameboard.Gamebox currentAskingBox = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the toast for "double-back" exiting
        Context context = getApplicationContext();
        CharSequence text = "Tap 'BACK' again to quit.";
        int duration = Toast.LENGTH_SHORT;
        backToast = Toast.makeText(context, text, duration);

        // Get game options from the intent (number of rows)
        Intent intent = getIntent();
        int numberOfRows = intent.getIntExtra(EXTRA_NUMBER_OF_ROWS, 0);

        // Get size of screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        // Gameboard dimensions
        int xPad = 20;
        int yPad = 10;
        int boxWidth = (screenWidth - 2*xPad) / Solution.LENGTH_OF_ROW;
        if (boxWidth*4 > .5 * screenHeight) {
            boxWidth = (screenHeight) / 8;
            xPad = (screenWidth - 4*boxWidth)/2;
        }

        // Gameboard
        gameboard = new Gameboard(this, xPad, yPad, boxWidth, numberOfRows);

        LinearLayout TopLL = new LinearLayout(this);
        TopLL.setOrientation(LinearLayout.HORIZONTAL);

        // top info height
        int topHeight = screenHeight / 5;

        // top layout params
        LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(0, topHeight, 1f);

        // timer
        timer = new Timer(this);
        timer.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)topHeight *20/100);
        timer.setTypeface(TYPEFACE);
        timer.setTextColor(LIGHT_BLUE_COLOR);
        LinearLayout timerWrapper = new LinearLayout(this);
        timerWrapper.setLayoutParams(topParams);
        timerWrapper.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        timerWrapper.addView(timer);

        // Level indicator
        levelView = new TextView(this);
        levelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)topHeight *19/100);
        levelView.setTextColor(LIGHT_BLUE_COLOR);
        levelView.setGravity(Gravity.CENTER);
        LinearLayout levelViewWrapper = new LinearLayout(this);
        levelViewWrapper.setLayoutParams(topParams);
        levelViewWrapper.setGravity(Gravity.TOP | Gravity.CENTER);
        levelViewWrapper.addView(levelView);

        // score
        scoreView = new TextView(this);
        scoreView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)topHeight *17/100);
        scoreView.setTextColor(LIGHT_BLUE_COLOR);
        setScore(0);
        LinearLayout scoreViewWrapper = new LinearLayout(this);
        scoreViewWrapper.setLayoutParams(topParams);
        scoreViewWrapper.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        scoreViewWrapper.addView(scoreView);

        // Top display
        TopLL.addView(timerWrapper);
        TopLL.addView(levelViewWrapper);
        TopLL.addView(scoreViewWrapper);

        LinearLayout.LayoutParams TopLLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TopLL.setLayoutParams(TopLLParams);

        // message bar
        int messageBarHeight = screenHeight / 10;
        messageBar = new TextView(this);
        messageBar.setTextSize(TypedValue.COMPLEX_UNIT_PX, messageBarHeight * 2 / 3f);
        messageBar.setGravity(Gravity.CENTER);
        messageBar.setTypeface(TYPEFACE);
        LinearLayout messageBarLLwrapping = new LinearLayout(this);
        messageBarLLwrapping.setGravity(Gravity.CENTER);
        messageBarLLwrapping.addView(messageBar);
        LinearLayout.LayoutParams messageBarLLwrappingParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, messageBarHeight);
        messageBarLLwrapping.setLayoutParams(messageBarLLwrappingParams);

        // bottom buttons
        LinearLayout.LayoutParams buttonLLwrappingParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        button1 = new Button(this);
        button1.setText("1");
        button1.setTypeface(TYPEFACE);
        button1.setOnClickListener(new Button1s0sOnClickListener(1, this));
        LinearLayout button1LLwrapping = new LinearLayout(this);
        button1LLwrapping.setLayoutParams(buttonLLwrappingParams);
        button1LLwrapping.setGravity(Gravity.CENTER);
        button1LLwrapping.addView(button1);
        button0 = new Button(this);
        button0.setText("0");
        button0.setTypeface(TYPEFACE);
        button0.setOnClickListener(new Button1s0sOnClickListener(0, this));
        LinearLayout button0LLwrapping = new LinearLayout(this);
        button0LLwrapping.setLayoutParams(buttonLLwrappingParams);
        button0LLwrapping.setGravity(Gravity.CENTER);
        button0LLwrapping.addView(button0);
        buttonGo = new Button(this);
        buttonGo.setText("Go!");
        buttonGo.setTypeface(TYPEFACE);
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMessage();
                hideGoButton();
                countDownThenTestMem(getTimeForCurrentLevel());
            }
        });
        LinearLayout buttonGoLLwrapping = new LinearLayout(this);
        buttonGoLLwrapping.setLayoutParams(buttonLLwrappingParams);
        buttonGoLLwrapping.setGravity(Gravity.CENTER);
        buttonGoLLwrapping.addView(buttonGo);

        // Bot display
        LinearLayout BotButtonsLL = new LinearLayout(this);
        BotButtonsLL.setOrientation(LinearLayout.HORIZONTAL);
        BotButtonsLL.addView(button1LLwrapping);
        BotButtonsLL.addView(buttonGoLLwrapping);
        BotButtonsLL.addView(button0LLwrapping);

        // Put it all together
        LinearLayout BigLL = new LinearLayout(this);
        BigLL.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams gameboardParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, gameboard.getBoardHeight() + 2*yPad);
        gameboard.setLayoutParams(gameboardParams);

        LinearLayout.LayoutParams messageBarParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        messageBar.setLayoutParams(messageBarParams);

        LinearLayout.LayoutParams BotButtonsLLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        BotButtonsLL.setLayoutParams(BotButtonsLLParams);

        BigLL.addView(TopLL);
        BigLL.addView(gameboard);
        BigLL.addView(messageBarLLwrapping);
        BigLL.addView(BotButtonsLL);

        // prep game for level1
        setLevel(1);
        resetGame();

        setContentView(BigLL);
    }

    public void setLevel(int newLevel) {
        level = newLevel;
        levelView.setText("Level\n" + Integer.toString(newLevel));
    }

    public void setScore(int newScore) {
        score = newScore;
        scoreView.setText(Integer.toString(newScore));
    }

    public void addToScore(int points) {
        score = score + points;
        new CountDownTimer(points * 10, 10) {
            public void onTick(long millisUntilFinished) {
                scoreView.setText(Integer.toString(score - (int)millisUntilFinished / 10));
            }
            public void onFinish() {
                scoreView.setText(Integer.toString(score));
            }
        }.start();
    }

    public long getTimeForCurrentLevel() {
        if (level >= 11) {
            return (long) 1000 / (level - 9);
        }
        else if (9 <= level && level <= 10) {
            return (long) 900 - (level - 9)*200;
        }
        else if (4 <= level && level <= 8) {
            return (long) 5000 - (level - 4)*1000;
        }
        else if (level == 3) {
            return (long) 7000;
        }
        else if (1 <= level && level <= 2) {
            return (long) 15000 - (level - 1)*5000;
        }
        else {
            // HERE BE DRAGONS!
            return (long) -1;
        }
    }

    public void hide1and0buttons() {
        button1.setVisibility(View.INVISIBLE);
        button0.setVisibility(View.INVISIBLE);
    }

    public void show1and0buttons() {
        button1.setVisibility(View.VISIBLE);
        button0.setVisibility(View.VISIBLE);
    }

    public void hideGoButton() {
        buttonGo.setVisibility(View.INVISIBLE);
    }

    public void showGoButton() {
        buttonGo.setVisibility(View.VISIBLE);
    }

    public void resetGame() {
        // prep game and give access to the Go! button for that level
        gameboard.hideValues();
        gameboard.resetSolution();
        timer.setTimer(getTimeForCurrentLevel());
        hide1and0buttons();
        showMessage("Ready?");
        showGoButton();
    }

    public void hideMessage() {
        messageBar.setVisibility(View.INVISIBLE);
    }

    public void showMessage(String message) {
        messageBar.setText(message);
        messageBar.setVisibility(View.VISIBLE);
    }

    public void countDownThenTestMem(long milliseconds) {
        gameboard.showValues();
        new CountDownTimer(milliseconds, 10) {

            public void onTick(long millisUntilFinished) {
                int tens = (int)Math.floor(millisUntilFinished / 10000);
                int ones = (int)Math.floor(millisUntilFinished / 1000f - tens*10);
                int tenths = (int)Math.floor((millisUntilFinished / 1000f - tens*10 - ones)*10);
                int hundredths = (int)Math.floor((millisUntilFinished / 1000f - tens*10 - ones - tenths / 10f)*100);
                timer.setText(Integer.toString(tens) + Integer.toString(ones) + "." + Integer.toString(tenths) + Integer.toString(hundredths));
            }

            public void onFinish() {
                timer.setText("00.00");
                gameboard.hideValues();
                testMem();
            }
        }.start();
    }
    public void ask(Gameboard.Gamebox gamebox) {
        currentAskingBox.showQuestionMark();
        show1and0buttons();
    }

    public void testMem() {
        // Create the asking sequence
        askStack = new Stack<Address>();
        for (int i=0; i<gameboard.numberOfRows; i++) {
            for (int j=0; j<Solution.LENGTH_OF_ROW; j++) {
                askStack.push(new Address(i, j));
            }
        }
        // Randomize
        long seed = System.nanoTime();
        Collections.shuffle(askStack, new Random(seed));

        // Start asking for the first box
        currentAskingBox = gameboard.getBoxAt(askStack.pop());
        ask(currentAskingBox);
    }

    public void checkValue(int value) {
        if (currentAskingBox == null) {return;}// shouldn't happen
        else if (currentAskingBox.value != value) {
            // guessed wrong!
            showMessage("Game Over");
            gameboard.colorBoxValue(currentAskingBox, 0xffee0011);
            gameboard.showValues();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameOver();
                }
            }, 2500);// how long (milliseconds) to wait
        }
        else if (currentAskingBox.value == value) {
            // correct!
            addToScore(level*gameboard.numberOfRows*3);
            currentAskingBox.showValue(); // this should be an animation

            if (askStack.isEmpty()) {
                // beat the level!
                currentAskingBox = null;
                showMessage("Yay!");

                // wait before next level
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setLevel(level+1);
                        resetGame();
                    }
                }, 2500);// how long (milliseconds) to wait
            }
            else {
                // wait before next box
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // move on to next box
                        currentAskingBox = gameboard.getBoxAt(askStack.pop());
                        ask(currentAskingBox);
                    }
                }, 500);// how long (milliseconds) to wait
            }
        }
    }

    public void gameOver() {
        if (true) {
            Intent intent = new Intent(this, NewHighScoreActivity.class);
            intent.putExtra(EXTRA_NUMBER_OF_ROWS, gameboard.numberOfRows);
            intent.putExtra(EXTRA_SCORE, score);
            this.finish();
            startActivity(intent);
        }
        else {
            // reuse old buttons to direct to main menu, restart game, or high scores
            button1.setText("Main Menu");
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GameActivity.this, MainActivity.class);
                    GameActivity.this.finish();
                    startActivity(intent);
                }
            });

            buttonGo.setText("Restart");
            buttonGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GameActivity.this.recreate();
                }
            });

            button0.setText("High Scores");
            button0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GameActivity.this, HighScoresActivity.class);
                    GameActivity.this.finish();
                    startActivity(intent);
                }
            });

            showGoButton();
            show1and0buttons();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        backToast.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
