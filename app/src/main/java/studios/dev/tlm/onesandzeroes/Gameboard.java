package studios.dev.tlm.onesandzeroes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

/**
 * Created by tm009967 on 7/28/2014.
 */

public class Gameboard extends View {

    public class Gamebox {
        private ShapeDrawable innerRect;
        private ShapeDrawable outerRect;
        private boolean isShowingText;
        public final int value;
        public final Address address;
        private float textX;
        private float textY;
        private int x;
        private int y;
        private int width;
        private float textSize;
        private Paint textPaint;
        private String text;
        private Gameboard gameboard;

        public Gamebox(Address address, int x, int y, int width, int value, Gameboard gameboard) {
            /** address (i, j) [topleft is at (0, 0)]
             * upperleft x,y
             */
            this.gameboard = gameboard;
            isShowingText = false;//set to false by default
            this.address = address;
            this.x = x;
            this.y = y;
            this.value = value;
            this.width = width;
            text = Integer.toString(this.value);
            textSize = (float)this.width*3/4;

            innerRect = new ShapeDrawable(new RectShape());
            innerRect.setBounds(x, y, x + this.width, y + this.width);
            innerRect.getPaint().setColor(0xff003388);
            innerRect.getPaint().setStyle(Paint.Style.FILL);

            outerRect = new ShapeDrawable(new RectShape());
            outerRect.setBounds(x, y, x + this.width, y + this.width);
            outerRect.getPaint().setColor(GameActivity.LIGHT_BLUE_COLOR);
            outerRect.getPaint().setStyle(Paint.Style.STROKE);
            outerRect.getPaint().setStrokeWidth(10);

            textPaint = new Paint();
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(textSize);
            textPaint.setColor(GameActivity.LIGHT_BLUE_COLOR);
            textPaint.setAntiAlias(true);
            textPaint.setTypeface(GameActivity.TYPEFACE);
            setText(Integer.toString(value));
        }

        private void setText(String text) {
            float centerX = x + (float)width/2;
            float centerY = y + (float)width/2;
            textX = centerX;
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            float textHeight = bounds.bottom - bounds.top;
            textY = centerY + textHeight/2;

            this.text = text;
        }

        public void showValue() {
            setText(Integer.toString(value));
            isShowingText = true;
            gameboard.invalidate();
        }

        public void showQuestionMark() {
            setText("?");
            isShowingText = true;
            gameboard.invalidate();
        }

        public void setValueColor(int color) {
            textPaint.setColor(color);
        }

        public void hideText() {
            isShowingText = false;
            gameboard.invalidate();
        }

        public void draw(Canvas canvas) {
            // Draw inside
            innerRect.draw(canvas);

            // Draw outline
            outerRect.draw(canvas);

            // Draw value
            if (isShowingText) {
                canvas.drawText(text, textX, textY, textPaint);
            }
        }

    }

    public int numberOfRows;
    private int boxWidth;
    private Solution solution;
    private Gamebox[][] board;
    private int x;
    private int y;
    public Gameboard(Context context, int x, int y, int boxWidth, int numberOfRows) {
        // upperleft x, y
        super(context);

        this.numberOfRows = numberOfRows;
        this.boxWidth = boxWidth;
        this.x = x;
        this.y = y;
        solution = new Solution(numberOfRows);
        resetSolution(); // starts with a solution
    }

    public void resetSolution() {
        solution.generateSolution(numberOfRows);
        buildBoard(solution);
    }

    private void buildBoard(Solution solution) {
        board = new Gamebox[this.numberOfRows][Solution.LENGTH_OF_ROW];
        for (int i=0; i<this.numberOfRows; i++) {
            for (int j=0; j<Solution.LENGTH_OF_ROW; j++) {
                board[i][j] = new Gamebox(new Address(i, j), x + j*boxWidth, y + i*boxWidth, boxWidth, solution.array[i][j], this);
            }
        }
    }

    public Gamebox getBoxAt(Address address) {
        return board[address.i][address.j];
    }

    public void showValues() {
        for (Gamebox[] rowOfGameboxes : board) {
            for (Gamebox gamebox : rowOfGameboxes) {
                gamebox.showValue();
            }
        }
        this.invalidate();
    }

    public void hideValues() {
        for (Gamebox[] rowOfGameboxes : board) {
            for (Gamebox gamebox : rowOfGameboxes) {
                gamebox.hideText();
            }
        }
        this.invalidate();
    }

    public int getBoardHeight() {
        //returns the height (of a 4-row board) in pixels
        //decided to not shrink the gameboard view when using rows < 4
        return 4 * boxWidth;
    }

    public void colorBoxValue(Gamebox box, int color) {
        box.setValueColor(color);
        this.invalidate();
    }

    protected void onDraw(Canvas canvas) {
        for (Gamebox[] rowOfGameboxes : board) {
            for (Gamebox gamebox : rowOfGameboxes) {
                gamebox.draw(canvas);
            }
        }
    }
}
