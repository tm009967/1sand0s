package studios.dev.tlm.onesandzeroes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import studios.dev.tlm.onesandzeroes.R;

public class NewHighScoreActivity extends Activity {

    /** For double-tapping back */
    private Toast backToast;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_high_score);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_high_score, menu);
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

    /** Called when the user clicks the Submit button */
    public void submitName(View view) {
        EditText nameText = (EditText) findViewById(R.id.their_name);
        String theirName = nameText.getText().toString();
        HighScores highScores = new HighScores(this);

        // grab score and #rows
        Intent incomingIntent = getIntent();
        int numberOfRows = incomingIntent.getIntExtra(GameActivity.EXTRA_NUMBER_OF_ROWS, 0);
        int score = incomingIntent.getIntExtra(GameActivity.EXTRA_SCORE, 0);

        highScores.addHighScore(theirName, numberOfRows, score);

        Intent intent = new Intent(this, HighScoresActivity.class);
        this.finish();
        startActivity(intent);
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
