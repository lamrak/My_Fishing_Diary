package net.validcat.fishing.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Denis on 21.07.2015.
 */
public class Line extends View {
    public Line(Context context) {
        super(context);
    }
    protected void onDraw(Canvas canvas){
        Paint myPaint = new Paint();
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setColor(Color.BLACK);
        canvas.drawRect(20,650,950,680,myPaint);
    }
}
