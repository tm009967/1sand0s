package studios.dev.tlm.onesandzeroes;

import android.content.Context;
import android.view.View;

/**
 * Created by tm009967 on 7/30/2014.
 */
public class Button1s0sOnClickListener implements View.OnClickListener {
    private int value;
    private GameActivity gameActivity;
    public Button1s0sOnClickListener(int value, GameActivity gameActivity) {
        this.value = value;
        this.gameActivity = gameActivity;
    }

    @Override
    public void onClick(View v) {
        gameActivity.hide1and0buttons();
        gameActivity.checkValue(value);
    }
}
