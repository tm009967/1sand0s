package studios.dev.tlm.onesandzeroes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class MainActivity extends Activity {

    public final static String ROWS_PREFS_KEY = "studios.dev.tlm.onesandzeroes.ROWS_PREFS_KEY";

    private int numberOfRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getSharedPreferences(this.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        numberOfRows = sharedPref.getInt(ROWS_PREFS_KEY, 1);

        RadioButton radio_1 = (RadioButton) findViewById(R.id.radio_1);
        RadioButton radio_2 = (RadioButton) findViewById(R.id.radio_2);
        RadioButton radio_3 = (RadioButton) findViewById(R.id.radio_3);
        RadioButton radio_4 = (RadioButton) findViewById(R.id.radio_4);

        if (numberOfRows == 2) {
            radio_2.setChecked(true);
        }
        else if (numberOfRows == 3) {
            radio_3.setChecked(true);
        }
        else if (numberOfRows == 4) {
            radio_4.setChecked(true);
        }
        else {
            radio_1.setChecked(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    /** Called when the user clicks the New Game button */
    public void startNewGame(View view) {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.EXTRA_NUMBER_OF_ROWS, Integer.parseInt( (String) selectedRadioButton.getText()));
        startActivity(intent);
    }

    /** Called when the user clicks the High Scores! button */
    public void goHighScores(View view) {
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }
}
