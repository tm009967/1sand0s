package studios.dev.tlm.onesandzeroes;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import studios.dev.tlm.onesandzeroes.R;

public class HighScoresActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        Context context = this;

        // get high scores
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("high scores").orderBy("score", Query.Direction.DESCENDING).limit(12).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                HighScore highScore = document.toObject(HighScore.class);
                                TableRow row = new TableRow(context);

                                TextView name = new TextView(context);
                                name.setText(highScore.getName());
                                name.setTypeface(typeface);
                                name.setTextColor(color);
                                name.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

                                TextView rows = new TextView(context);
                                rows.setText(Integer.toString(highScore.getNumberOfRows()));
                                rows.setTypeface(typeface);
                                rows.setTextColor(color);
                                rows.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                rows.setGravity(Gravity.CENTER);

                                TextView score = new TextView(context);
                                score.setText(Integer.toString(highScore.getScore()));
                                score.setTypeface(typeface);
                                score.setTextColor(color);
                                score.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                score.setGravity(Gravity.RIGHT);

                                row.addView(name);
                                row.addView(rows);
                                row.addView(score);

                                table.addView(row);
                            }
                        } /*else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }*/
                    }
                });

        setContentView(table);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.high_scores, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        boolean isConfirmed = false;
//        if (id == R.id.reset_high_scores) {new AlertDialog.Builder(this)
//                .setTitle("Confirm Reset")
//                .setMessage("Do you really want to reset the high scores?")
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int whichButton) {
////                        TODO
////                        highScores.resetHighScores();
////                        HighScoresActivity.this.recreate();
//                    }})
//                .setNegativeButton(android.R.string.no, null).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
