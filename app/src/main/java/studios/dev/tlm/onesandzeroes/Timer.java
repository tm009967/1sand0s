package studios.dev.tlm.onesandzeroes;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by tm009967 on 7/29/2014.
 */
public class Timer extends TextView {
    public Timer(Context context) {
        super(context);
        this.setText("00.00");
    }

    public void setTimer(long millisUntilFinished) {
        int tens = (int)Math.floor(millisUntilFinished / 10000);
        int ones = (int)Math.floor(millisUntilFinished / 1000f - tens*10);
        int tenths = (int)Math.floor((millisUntilFinished / 1000f - tens*10 - ones)*10);
        int hundredths = (int)Math.floor((millisUntilFinished / 1000f - tens*10 - ones - tenths / 10f)*100);
        setText(Integer.toString(tens) + Integer.toString(ones) + "." + Integer.toString(tenths) + Integer.toString(hundredths));
    }

    public void countDown(long milliseconds) {
        new CountDownTimer(milliseconds, 10) {

            public void onTick(long millisUntilFinished) {
                int tens = (int)Math.floor(millisUntilFinished / 10000);
                int ones = (int)Math.floor(millisUntilFinished / 1000f - tens*10);
                int tenths = (int)Math.floor((millisUntilFinished / 1000f - tens*10 - ones)*10);
                int hundredths = (int)Math.floor((millisUntilFinished / 1000f - tens*10 - ones - tenths / 10f)*100);
                setText(Integer.toString(tens) + Integer.toString(ones) + "." + Integer.toString(tenths) + Integer.toString(hundredths));
            }

            public void onFinish() {
                setText("00.00");
            }
        }.start();

    }
}
