package us.bney.morsekeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import java.util.List;

public class MorseKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    // Extends the keyboard class, constructed with an xml layout for the keyboard.
    private MKKeyboard mStraightKey;
    // The view of the keyboard, what's drawn over the screen.
    private MKKeyboardView mInputView;

    private StringBuilder mComposing = new StringBuilder();
    private String mInput = "";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onInitializeInterface() {
        mStraightKey = new MKKeyboard(this, R.xml.straightkey);
    }

    @Override
    public View onCreateInputView() {
        mInputView =
                (MKKeyboardView) getLayoutInflater().inflate(R.layout.input, null);

        // Disables long press and hold on keyboard keys.
        mInputView.setPreviewEnabled(false);
        // Sets the keyboard's action listener to an instance of this class, which implements KeyboardActionListener
        mInputView.setOnKeyboardActionListener(this);
        // Sets the layout of the keyboard.
        mInputView.setKeyboard(mStraightKey);

        return mInputView;
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        mComposing.setLength(0);
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();

        mComposing.setLength(0);
        if(mInputView != null) {
            mInputView.closing();
        }
    }

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        mInputView.setKeyboard(mStraightKey);
        mInputView.closing();
    }

    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd,
                                  int newSelStart, int newSelEnd,
                                  int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);

        if (mComposing.length() > 0 && (newSelStart != candidatesEnd
                || newSelEnd != candidatesEnd)) {
            mComposing.setLength(0);

            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }

    // Helper function to determine the length of a 'press'
    private boolean fuzz(long givenTime, long expectedTime, long fuzzAmount) {
        if(givenTime > (expectedTime - fuzzAmount) && givenTime < (expectedTime + fuzzAmount)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // The InputMethodService already takes care of the back
                // key for us, to dismiss the input method if it is shown.
                // However, our keyboard could be showing a pop-up window
                // that back should dismiss, so we first allow it to do that.
                if (event.getRepeatCount() == 0 && mInputView != null) {
                    if (mInputView.handleBack()) {
                        return true;
                    }
                }
                break;

            case KeyEvent.KEYCODE_DEL:
                // Special handling of the delete key: if we currently are
                // composing text for the user, we want to modify that instead
                // of let the application to the delete itself.
                if (mComposing.length() > 0) {
                    onKey(Keyboard.KEYCODE_DELETE, null);
                    return true;
                }
                break;

            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        // Dot Length: 125 ms
        // Dash Length: 375 ms
        // Pause between elements: 125 ms
        // Pause between characters: 375 ms
        // Pause between words: 875 ms
//
//        long deltaTime = event.getEventTime() - event.getDownTime();
//
//        if(deltaTime < 300) {
//            // Dot
//        } else if(deltaTime > 500) {
//            // Dash
//        } else {
//            // New Word
//        }

        return super.onKeyUp(keyCode, event);
    }

    /**
     * Helper function to commit any text being composed in to the editor.
     */
    private void commitTyped(InputConnection inputConnection) {
        if (mComposing.length() > 0) {
            inputConnection.commitText(mComposing, mComposing.length());
            mComposing.setLength(0);
        }
    }

    // If the dot or dash keys are pressed, adds a 0 or 1 to the mInput string
    // as appropriate. If the send input key is pressed it will translate the
    // inputted 0s and 1s to the appropriate ASCII character.
    public void onKey(int primaryCode, int[] keyCodes) {
        // This is the dot key.
        if(primaryCode == 0)
        {
//            Log.d("mk", "dot pressed");
            mInput += "0";
        }
        // This is the dash key.
        else if (primaryCode == 1)
        {
//            Log.d("mk", "dash pressed");
            mInput += "1";
        }
        // This is the space key.
        // Want to translate inputted morse to text.
        else if (primaryCode == 2)
        {
//            Log.d("mk", "space pressed");
            mComposing.append(translate(mInput));
            mInput = "";
            commitTyped(this.getCurrentInputConnection());
        }
        else if (primaryCode == -1)
        {

        }
        else {
            Log.d("mk", "something unexpected pressed");
        }

        List<Keyboard.Key> temp = mStraightKey.getKeys();
        for (Keyboard.Key a : temp) {
            if (a.codes[0] == -1) {
//                Log.d("mk", "SPICY MEATBALL!");
                a.text = mInput;
                a.label = mInput;

                mInputView.invalidateAllKeys();
            }
        }
    }

    public void onText(CharSequence text) {

    }

    public void swipeRight() {
        Log.d("mk", "swipe right");
    }

    public void swipeLeft() {
        Log.d("mk", "swipe left");
    }

    public void swipeDown() {
        Log.d("mk", "swipe down");
    }

    public void swipeUp() {
        Log.d("mk", "swipe up");
    }


    // Translates a string of 0s and 1s to an appropriate UTF8 character based off of the
    // morse code standard.
    private String translate(String mInput)
    {
        switch (mInput)
        {
            case "0": return "E";
            case "1": return "T";
            case "00": return "I";
            case "01": return "A";
            case "10": return "N";
            case "11": return "M";
            case "000": return "S";
            case "001": return "U";
            case "010": return "R";
            case "011": return "W";
            case "100": return "D";
            case "101": return "K";
            case "110": return "G";
            case "111": return "O";
            case "0000": return "H";
            case "0001": return "V";
            case "0010": return "F";
            case "0100": return "L";
            case "0110": return "P";
            case "0111": return "J";
            case "1000": return "B";
            case "1001": return "X";
            case "1011": return "Y";
            case "1010": return "C";
            case "1100": return "Z";
            case "1101": return "Q";
            case "1110":
                this.getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, 62));
                return "";
            case "1111":
                this.getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, 67));
                return "";
            case "00000": return "5";
            case "00001": return "4";
            case "00011": return "3";
            case "00111": return "2";
            case "01111": return "1";
            case "10000": return "6";
            case "11000": return "7";
            case "11100": return "8";
            case "11110": return "9";
            case "11111": return "0";
            case "001110": return new String(Character.toChars(128514));
            case "001111": return new String(Character.toChars(10084));
        }
        return "";
    }

    public void onPress(int primaryCode) {

    }

    public void onRelease(int primaryCode) {
    }
}
