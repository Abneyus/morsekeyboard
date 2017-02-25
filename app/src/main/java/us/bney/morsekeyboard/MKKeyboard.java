package us.bney.morsekeyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;

public class MKKeyboard extends Keyboard {
    public MKKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }
}
