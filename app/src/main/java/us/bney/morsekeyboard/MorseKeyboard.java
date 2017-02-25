package us.bney.morsekeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.InputType;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class MorseKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private MKKeyboard mStraightKey;
    private MKKeyboardView mInputView;

    private StringBuilder mComposing = new StringBuilder();

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

        mInputView.setOnKeyboardActionListener(this);
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

        long deltaTime = event.getEventTime() - event.getDownTime();

        if(deltaTime < 300) {
            // Dot
        } else if(deltaTime > 500) {
            // Dash
        } else {
            // New Word
        }

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

    /**
     * Helper to determine if a given character code is alphabetic.
     */
    private boolean isAlphabet(int code) {
        if (Character.isLetter(code)) {
            return true;
        } else {
            return false;
        }
    }

    public void onKey(int primaryCode, int[] keyCodes) {
        // Do shit.
    }

    public void onText(CharSequence text) {
        // Do shit.
    }

    public void swipeRight() {
    }

    public void swipeLeft() {
    }

    public void swipeDown() {
    }

    public void swipeUp() {
    }

    public void onPress(int primaryCode) {
    }

    public void onRelease(int primaryCode) {
    }
}