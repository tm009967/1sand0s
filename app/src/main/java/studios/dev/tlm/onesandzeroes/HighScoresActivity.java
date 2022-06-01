package studios.dev.tlm.onesandzeroes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import studios.dev.tlm.onesandzeroes.R;

public class HighScoresActivity extends Activity {
    private HighScores highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        highScores = new HighScores(this);

        TableLayout table = new TableLayout(this);
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        table.setLayoutParams(tableParams);
        table.setColumnStretchable(0, true);
        table.setColumnShrinkable(1, true);
        table.setColumnStretchable(2, true);

        TableRow headingsRow = new TableRow(this);

        TextView nameHeading = new TextView(this);
        nameHeading.setText(getString(R.string.name_heading));
        nameHeading.setTypeface(GameActivity.TYPEFACE);

        TextView rowsHeading = new TextView(this);
        rowsHeading.setText(getString(R.string.rows_heading));
        rowsHeading.setTypeface(GameActivity.TYPEFACE);
        rowsHeading.setGravity(Gravity.CENTER);

        TextView scoreHeading = new TextView(this);
        scoreHeading.setText(getString(R.string.score_heading));
        scoreHeading.setTypeface(GameActivity.TYPEFACE);
        scoreHeading.setGravity(Gravity.RIGHT);

        headingsRow.addView(nameHeading);
        headingsRow.addView(rowsHeading);
        headingsRow.addView(scoreHeading);

        table.addView(headingsRow);

        // Get size of screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        float textSize = screenHeight / 20f;

        Typeface typeface = GameActivity.TYPEFACE;
        int color = GameActivity.LIGHT_BLUE_COLOR;

        for (HighScores.HighScore highScore : highScores.getHighScoresArray()) {
            TableRow row = new TableRow(this);

            TextView name = new TextView(this);
            name.setText(highScore.name);
            name.setTypeface(typeface);
            name.setTextColor(color);
            name.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            TextView rows = new TextView(this);
            rows.setText(Integer.toString(highScore.numberOfRows));
            rows.setTypeface(typeface);
            rows.setTextColor(color);
            rows.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            rows.setGravity(Gravity.CENTER);

            TextView score = new TextView(this);
            score.setText(Integer.toString(highScore.score));
            score.setTypeface(typeface);
            score.setTextColor(color);
            score.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            score.setGravity(Gravity.RIGHT);

            row.addView(name);
            row.addView(rows);
            row.addView(score);

            table.addView(row);
        }

        setContentView(table);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.high_scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        boolean isConfirmed = false;
        if (id == R.id.reset_high_scores) {new AlertDialog.Builder(this)
                .setTitle("Confirm Reset")
                .setMessage("Do you really want to reset the high scores?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        highScores.resetHighScores();
                        HighScoresActivity.this.recreate();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
