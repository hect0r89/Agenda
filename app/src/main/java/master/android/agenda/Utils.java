package master.android.agenda;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;

/**
 * Created by hector on 26/10/16.
 */

public class Utils {
    public static int getMatColor(String typeColor, Context context)
    {
        int returnColor = Color.BLACK;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getApplicationContext().getPackageName());

        if (arrayId != 0)
        {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }
}
