package us.bney.morsekeyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;

public class MKKeyboard extends Keyboard {
    public MKKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public MKKeyboard(Context context, int layoutTemplateResId,
                         CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }


}
