/*
 * Copyright (C) 2008-2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vinsofts.keyborad.key;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.Image;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.text.InputType;
import android.text.method.MetaKeyKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.vinsofts.keyborad.R;
import com.vinsofts.keyborad.utils.DeviceUtils;
import com.vinsofts.keyborad.utils.GlobalFunction;
import com.vinsofts.keyborad.utils.MLog;
import com.vinsofts.keyborad.utils.SoundsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example of writing an input method for a soft keyboard.  This code is
 * focused on simplicity over completeness, so it should in no way be considered
 * to be a complete soft keyboard implementation.  Its purpose is to provide
 * a basic example for how you would get started writing an input method, to
 * be fleshed out as appropriate.
 */
public class SoftKeyboard extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener, SpellCheckerSession.SpellCheckerSessionListener {
    static final boolean DEBUG = false;

    /**
     * This boolean indicates the optional example code for performing
     * processing of hard keys in addition to regular text generation
     * from on-screen interaction.  It would be used for input methods that
     * perform language translations (such as converting text entered on
     * a QWERTY keyboard to Chinese), but may not be used for input methods
     * that are primarily intended to be used for on-screen text entry.
     */
    static final boolean PROCESS_HARD_KEYS = true;

    private InputMethodManager mInputMethodManager;

    private LatinKeyboardView mInputView;
    private CandidateView mCandidateView;
    private CompletionInfo[] mCompletions;

    private StringBuilder mComposing = new StringBuilder();
    private boolean mPredictionOn;
    private boolean mCompletionOn;
    private int mLastDisplayWidth;
    private boolean mCapsLock;
    private long mLastShiftTime;
    private long mMetaState;

    private LatinKeyboard mSymbolsKeyboard;
    private LatinKeyboard mSymbolsShiftedKeyboard;
    private LatinKeyboard mQwertyKeyboard;
    private LatinKeyboard mQwertyMoreKeyboard;

    private LatinKeyboard mCurKeyboard;

    private String mWordSeparators;

    private SpellCheckerSession mScs;
    private List<String> mSuggestions;
    private LatinKeyboard mNumberKeyboard;
    private TextView tvTitle;
    private Context context;
    private int width;
    private TextView tvTitle2;
    private PopupWindow popup2;
    private TextView tvTitle3;
    private PopupWindow popup3;
    private ImageView imvBack;
    private LatinKeyboard mSymbolsMoreKeyboard;
    private LatinKeyboard mSymbolsShiftedMoreKeyboard;
    private int height;
    private CountDownTimer timer;
    private boolean isLongClick = false;
    private String keyLongClick = "ộp";
    private CountDownTimer longClickTimer;


    /**
     * Main initialization of the input method component.  Be sure to call
     * to super class.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mWordSeparators = getResources().getString(R.string.word_separators);
        final TextServicesManager tsm = (TextServicesManager) getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        mScs = tsm.newSpellCheckerSession(null, null, this, true);
        init();
    }

    private void init() {
        width = GlobalFunction.getWidth(context);
        height = GlobalFunction.getHeight(context);

    }

    /**
     * This is the point where you can do all of your UI initialization.  It
     * is called after creation and any configuration change.
     */
    @Override
    public void onInitializeInterface() {
        if (mQwertyKeyboard != null) {
            // Configuration changes can happen after the keyboard gets recreated,
            // so we need to be able to re-build the keyboards if the available
            // space has changed.
            int displayWidth = getMaxWidth();
            if (displayWidth == mLastDisplayWidth) return;
            mLastDisplayWidth = displayWidth;
        }
        mQwertyKeyboard = new LatinKeyboard(this, R.xml.qwerty);
        mQwertyMoreKeyboard = new LatinKeyboard(this, R.xml.qwerty_more);

        mSymbolsKeyboard = new LatinKeyboard(this, R.xml.symbols);
        mSymbolsMoreKeyboard = new LatinKeyboard(this, R.xml.symbols_more);

        mSymbolsShiftedKeyboard = new LatinKeyboard(this, R.xml.symbols_shift);
        mSymbolsShiftedMoreKeyboard = new LatinKeyboard(this, R.xml.symbols_shift_more);

        mNumberKeyboard = new LatinKeyboard(this, R.xml.number);
        mNumberKeyboard.isNumber = true;
    }

    /**
     * Called by the framework when your view for creating input needs to
     * be generated.  This will be called the first time your input method
     * is displayed, and every time it needs to be re-created such as due to
     * a configuration change.
     */
    boolean isPopup;

    @Override
    public View onCreateInputView() {
        if (GlobalFunction.getPopup(context)) {
            isPopup = true;
            mInputView = (LatinKeyboardView) getLayoutInflater().inflate(R.layout.input_second, null, false);
        } else {
            isPopup = false;
            mInputView = (LatinKeyboardView) getLayoutInflater().inflate(R.layout.input, null, false);
        }
        mInputView.setOnKeyboardActionListener(this);
        mInputView.setPreviewEnabled(false);
        if (GlobalFunction.getExtraRow(context)) {
            setLatinKeyboard(mQwertyMoreKeyboard);
        } else {
            setLatinKeyboard(mQwertyKeyboard);
        }
        return mInputView;
    }

    private void setLatinKeyboard(LatinKeyboard nextKeyboard) {
        boolean shouldSupportLanguageSwitchKey = mInputMethodManager.switchToNextInputMethod(getToken(), true);
        nextKeyboard.setLanguageSwitchKeyVisibility(shouldSupportLanguageSwitchKey);
        mInputView.isNumber = nextKeyboard.isNumber;
        mInputView.setKeyboard(nextKeyboard);
    }

    /**
     * Called by the framework when your view for showing candidates needs to
     * be generated, like {@link #onCreateInputView}.
     */
    @Override
    public View onCreateCandidatesView() {
        mCandidateView = new CandidateView(this);
        mCandidateView.setService(this);
        return mCandidateView;
    }

    /**
     * This is the main point where we do our initialization of the input method
     * to begin operating on an application.  At this point we have been
     * bound to the client, and are now receiving all of the detailed information
     * about the target of our edits.
     */
    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        if (GlobalFunction.getPopup(context) != isPopup) {
//            stopSelf();
            setInputView(onCreateInputView());

            MLog.e("IAM");
        }

        // Reset our state.  We want to do this even if restarting, because
        // the underlying state of the text editor could have changed in any way.
        mComposing.setLength(0);
        updateCandidates();

        if (!restarting) {
            // Clear shift states.
            mMetaState = 0;
        }

        mPredictionOn = false;
        mCompletionOn = false;
        mCompletions = null;

        // We are now going to initialize our state based on the type of
        // text being edited.
        switch (attribute.inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_NUMBER:
                mCurKeyboard = mNumberKeyboard;
                break;
            case InputType.TYPE_CLASS_DATETIME:
                // Numbers and dates default to the symbols keyboard, with
                // no extra features.
                if (GlobalFunction.getExtraRow(context)) {
                    mCurKeyboard = (mSymbolsMoreKeyboard);
                } else {
                    mCurKeyboard = (mSymbolsKeyboard);
                }
                break;

            case InputType.TYPE_CLASS_PHONE:
                // Phones will also default to the symbols keyboard, though
                // often you will want to have a dedicated phone keyboard.
                mCurKeyboard = mNumberKeyboard;
                break;

            case InputType.TYPE_CLASS_TEXT:
                // This is general text editing.  We will default to the
                // normal alphabetic keyboard, and assume that we should
                // be doing predictive text (showing candidates as the
                // user types).
                if (GlobalFunction.getExtraRow(context)) {
                    mCurKeyboard = (mQwertyMoreKeyboard);
                } else {
                    mCurKeyboard = (mQwertyKeyboard);
                }
                mPredictionOn = true;

                // We now look for a few special variations of text that will
                // modify our behavior.
                int variation = attribute.inputType & InputType.TYPE_MASK_VARIATION;
                if (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                        variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Do not display predictions / what the user is typing
                    // when they are entering a password.
                    mPredictionOn = false;
                }

                if (variation == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        || variation == InputType.TYPE_TEXT_VARIATION_URI
                        || variation == InputType.TYPE_TEXT_VARIATION_FILTER) {
                    // Our predictions are not useful for e-mail addresses
                    // or URIs.
                    mPredictionOn = false;
                }

                if ((attribute.inputType & InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
                    // If this is an auto-complete text view, then our predictions
                    // will not be shown and instead we will allow the editor
                    // to supply their own.  We only show the editor's
                    // candidates when in fullscreen mode, otherwise relying
                    // own it displaying its own UI.
                    mPredictionOn = false;
                    mCompletionOn = isFullscreenMode();
                }

                // We also want to look at the current state of the editor
                // to decide whether our alphabetic keyboard should start out
                // shifted.
                updateShiftKeyState(attribute);
                break;

            default:
                // For all unknown input types, default to the alphabetic
                // keyboard with no special features.
                if (GlobalFunction.getExtraRow(context)) {
                    mCurKeyboard = (mQwertyMoreKeyboard);
                } else {
                    mCurKeyboard = (mQwertyKeyboard);
                }
                updateShiftKeyState(attribute);
        }

        // Update the label on the enter key, depending on what the application
        // says it will do.
        mCurKeyboard.setImeOptions(getResources(), attribute.imeOptions);
    }

    /**
     * This is called when the user is done editing a field.  We can use
     * this to reset our state.
     */
    @Override
    public void onFinishInput() {
        super.onFinishInput();

        // Clear current composing text and candidates.
        mComposing.setLength(0);
        updateCandidates();

        // We only hide the candidates window when finishing input on
        // a particular editor, to avoid popping the underlying application
        // up and down if the user is entering text into the bottom of
        // its window.
        setCandidatesViewShown(false);

        if (GlobalFunction.getExtraRow(context)) {
            mCurKeyboard = (mQwertyMoreKeyboard);
        } else {
            mCurKeyboard = (mQwertyKeyboard);
        }
        if (mInputView != null) {
            mInputView.closing();
        }
    }

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        // Apply the selected keyboard to the input view.
        setLatinKeyboard(mCurKeyboard);
        mInputView.closing();
        final InputMethodSubtype subtype = mInputMethodManager.getCurrentInputMethodSubtype();
        mInputView.setSubtypeOnSpaceKey(subtype);
    }

    @Override
    public void onCurrentInputMethodSubtypeChanged(InputMethodSubtype subtype) {
        mInputView.setSubtypeOnSpaceKey(subtype);
    }

    /**
     * Deal with the editor reporting movement of its cursor.
     */
    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd,
                                  int newSelStart, int newSelEnd,
                                  int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
                candidatesStart, candidatesEnd);

        // If the current selection in the text view changes, we should
        // clear whatever candidate text we have.
        if (mComposing.length() > 0 && (newSelStart != candidatesEnd
                || newSelEnd != candidatesEnd)) {
            mComposing.setLength(0);
            updateCandidates();
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }

    /**
     * This tells us about completions that the editor has determined based
     * on the current text in it.  We want to use this in fullscreen mode
     * to show the completions ourself, since the editor can not be seen
     * in that situation.
     */
    @Override
    public void onDisplayCompletions(CompletionInfo[] completions) {
        if (mCompletionOn) {
            mCompletions = completions;
            if (completions == null) {
                setSuggestions(null, false, false);
                return;
            }

            List<String> stringList = new ArrayList<String>();
            for (int i = 0; i < completions.length; i++) {
                CompletionInfo ci = completions[i];
                if (ci != null) stringList.add(ci.getText().toString());
            }
            setSuggestions(stringList, true, true);
        }
    }

    /**
     * This translates incoming hard key events in to edit operations on an
     * InputConnection.  It is only needed when using the
     * PROCESS_HARD_KEYS option.
     */
    private boolean translateKeyDown(int keyCode, KeyEvent event) {
        mMetaState = MetaKeyKeyListener.handleKeyDown(mMetaState,
                keyCode, event);
        int c = event.getUnicodeChar(MetaKeyKeyListener.getMetaState(mMetaState));
        mMetaState = MetaKeyKeyListener.adjustMetaAfterKeypress(mMetaState);
        InputConnection ic = getCurrentInputConnection();
        if (c == 0 || ic == null) {
            return false;
        }

        boolean dead = false;

        if ((c & KeyCharacterMap.COMBINING_ACCENT) != 0) {
            dead = true;
            c = c & KeyCharacterMap.COMBINING_ACCENT_MASK;
        }

        if (mComposing.length() > 0) {
            char accent = mComposing.charAt(mComposing.length() - 1);
            int composed = KeyEvent.getDeadChar(accent, c);

            if (composed != 0) {
                c = composed;
                mComposing.setLength(mComposing.length() - 1);
            }
        }

        onKey(c, null);

        return true;
    }

    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
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

            case KeyEvent.KEYCODE_ENTER:
                // Let the underlying text editor always handle these.
                return false;
            default:
                // For all other keys, if we want to do transformations on
                // text being entered with a hard keyboard, we need to process
                // it and do the appropriate action.
                /*
                if (PROCESS_HARD_KEYS) {
                    if (keyCode == KeyEvent.KEYCODE_SPACE
                            && (event.getMetaState()&KeyEvent.META_ALT_ON) != 0) {
                        // A silly example: in our input method, Alt+Space
                        // is a shortcut for 'android' in lower case.
                        InputConnection ic = getCurrentInputConnection();
                        if (ic != null) {
                            // First, tell the editor that it is no longer in the
                            // shift state, since we are consuming this.
                            ic.clearMetaKeyStates(KeyEvent.META_ALT_ON);
                            keyDownUp(KeyEvent.KEYCODE_A);
                            keyDownUp(KeyEvent.KEYCODE_N);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            keyDownUp(KeyEvent.KEYCODE_R);
                            keyDownUp(KeyEvent.KEYCODE_O);
                            keyDownUp(KeyEvent.KEYCODE_I);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            // And we consume this event.
                            return true;
                        }
                    }
                    if (mPredictionOn && translateKeyDown(keyCode, event)) {
                        return true;
                    }
                }*/
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // If we want to do transformations on text being entered with a hard
        // keyboard, we need to process the up events to update the meta key
        // state we are tracking.
        if (PROCESS_HARD_KEYS) {
            if (mPredictionOn) {
                mMetaState = MetaKeyKeyListener.handleKeyUp(mMetaState,
                        keyCode, event);
            }
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
            updateCandidates();
        }
    }

    /**
     * Helper to update the shift state of our keyboard based on the initial
     * editor state.
     */
    private void updateShiftKeyState(EditorInfo attr) {
        if (attr != null && mInputView != null && (mQwertyKeyboard == mInputView.getKeyboard() || mQwertyMoreKeyboard == mInputView.getKeyboard())) {
            int caps = 0;
            EditorInfo ei = getCurrentInputEditorInfo();
            if (ei != null && ei.inputType != InputType.TYPE_NULL) {
                caps = getCurrentInputConnection().getCursorCapsMode(attr.inputType);
            }
            mInputView.setShifted(mCapsLock || caps != 0);
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

    /**
     * Helper to send a key down / key up pair to the current editor.
     */
    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }

    /**
     * Helper to send a character to the editor as raw key events.
     */
    private void sendKey(int keyCode) {
        switch (keyCode) {
            case '\n':
                keyDownUp(KeyEvent.KEYCODE_ENTER);
                break;
            default:
                if (keyCode >= '0' && keyCode <= '9') {
                    keyDownUp(keyCode - '0' + KeyEvent.KEYCODE_0);
                } else {
                    getCurrentInputConnection().commitText(String.valueOf((char) keyCode), 1);
                }
                break;
        }
    }

    // Implementation of KeyboardViewListener

    public void onKey(int primaryCode, int[] keyCodes) {
        if (isWordSeparator(primaryCode)) {
            MLog.e("1");
            // Handle separator
            if (mComposing.length() > 0) {
                MLog.e(mComposing.toString());
                commitTyped(getCurrentInputConnection());
            }
            sendKey(primaryCode);
            updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
            handleBackspace();
        } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
            handleShift();
        } else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
            handleClose();
            return;
        } else if (primaryCode == LatinKeyboardView.KEYCODE_LANGUAGE_SWITCH) {
            handleLanguageSwitch();
            return;
        } else if (primaryCode == LatinKeyboardView.KEYCODE_OPTIONS) {
            // Show a menu or somethin'
        } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE && mInputView != null) {
            Keyboard current = mInputView.getKeyboard();
            if (current == mSymbolsKeyboard || current == mSymbolsMoreKeyboard || current == mSymbolsShiftedKeyboard || current == mSymbolsShiftedMoreKeyboard) {
                if (GlobalFunction.getExtraRow(context)) {
                    setLatinKeyboard(mQwertyMoreKeyboard);
                } else {
                    setLatinKeyboard(mQwertyKeyboard);
                }
            } else {
                if (GlobalFunction.getExtraRow(context)) {
                    setLatinKeyboard(mSymbolsMoreKeyboard);
                    mSymbolsMoreKeyboard.setShifted(false);
                } else {
                    setLatinKeyboard(mSymbolsKeyboard);
                    mSymbolsKeyboard.setShifted(false);
                }

            }
        } else {
            MLog.e("8");
            handleCharacter(primaryCode, keyCodes);
        }
    }

    public void onText(CharSequence text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        ic.beginBatchEdit();
        if (mComposing.length() > 0) {
            commitTyped(ic);
        }
        ic.commitText(text, 0);
        ic.endBatchEdit();
        updateShiftKeyState(getCurrentInputEditorInfo());
    }

    /**
     * Update the list of available candidates from the current composing
     * text.  This will need to be filled in by however you are determining
     * candidates.
     */
    private void updateCandidates() {
        if (!mCompletionOn) {
            if (mComposing.length() > 0) {
                ArrayList<String> list = new ArrayList<String>();
                //list.add(mComposing.toString());
                Log.d("SoftKeyboard", "REQUESTING: " + mComposing.toString());
                if (mScs != null) {
                    mScs.getSentenceSuggestions(new TextInfo[]{new TextInfo(mComposing.toString())}, 5);
                }
                setSuggestions(list, true, true);
            } else {
                setSuggestions(null, false, false);
            }
        }
    }

    public void setSuggestions(List<String> suggestions, boolean completions,
                               boolean typedWordValid) {
        if (suggestions != null && suggestions.size() > 0) {
            setCandidatesViewShown(true);
        } else if (isExtractViewShown()) {
            setCandidatesViewShown(true);
        }
        mSuggestions = suggestions;
        if (mCandidateView != null) {
            mCandidateView.setSuggestions(suggestions, completions, typedWordValid);
        }
    }

    private void handleBackspace() {
        final int length = mComposing.length();
        if (length > 1) {
            mComposing.delete(length - 1, length);
            getCurrentInputConnection().setComposingText(mComposing, 1);
            updateCandidates();
        } else if (length > 0) {
            mComposing.setLength(0);
            getCurrentInputConnection().commitText("", 0);
            updateCandidates();
        } else {
            keyDownUp(KeyEvent.KEYCODE_DEL);
        }
        updateShiftKeyState(getCurrentInputEditorInfo());
    }

    private void handleShift() {
        if (mInputView == null) {
            return;
        }

        Keyboard currentKeyboard = mInputView.getKeyboard();
        if (mQwertyKeyboard == currentKeyboard || mQwertyMoreKeyboard == currentKeyboard) {
            // Alphabet keyboard
            checkToggleCapsLock();
            mInputView.setShifted(mCapsLock || !mInputView.isShifted());
        } else if (currentKeyboard == mSymbolsKeyboard || currentKeyboard == mSymbolsMoreKeyboard) {

            if (GlobalFunction.getExtraRow(context)) {
                mSymbolsMoreKeyboard.setShifted(true);
            } else {
                mSymbolsKeyboard.setShifted(true);
            }

            if (GlobalFunction.getExtraRow(context)) {
                setLatinKeyboard(mSymbolsShiftedMoreKeyboard);
                mSymbolsShiftedMoreKeyboard.setShifted(true);
            } else {
                setLatinKeyboard(mSymbolsShiftedKeyboard);
                mSymbolsShiftedKeyboard.setShifted(true);
            }

        } else if (currentKeyboard == mSymbolsShiftedKeyboard || currentKeyboard == mSymbolsShiftedMoreKeyboard) {
            if (GlobalFunction.getExtraRow(context)) {
                mSymbolsShiftedMoreKeyboard.setShifted(false);

            } else {
                mSymbolsShiftedKeyboard.setShifted(false);

            }

            if (GlobalFunction.getExtraRow(context)) {
                setLatinKeyboard(mSymbolsMoreKeyboard);
                mSymbolsMoreKeyboard.setShifted(false);
            } else {
                setLatinKeyboard(mSymbolsKeyboard);
                mSymbolsKeyboard.setShifted(false);
            }

        }
    }

    private void handleCharacter(int primaryCode, int[] keyCodes) {
        if (isLongClick) {
            isLongClick = false;
            MLog.e("isLongClick");
            return;
        }
        if (isInputViewShown()) {
            if (mInputView.isShifted()) {
                primaryCode = Character.toUpperCase(primaryCode);
            }
        }
        if (mPredictionOn) {
            mComposing.append((char) primaryCode);
            getCurrentInputConnection().setComposingText(mComposing, 1);
            updateShiftKeyState(getCurrentInputEditorInfo());
            updateCandidates();
        } else {
            MLog.e(String.valueOf((char) primaryCode));
            getCurrentInputConnection().commitText(
                    String.valueOf((char) primaryCode), 1);
        }
    }

    private void handleCharacter2(int primaryCode, String key) {

        if (mPredictionOn) {
            mComposing.append(key);
            getCurrentInputConnection().setComposingText(mComposing, 1);
            updateShiftKeyState(getCurrentInputEditorInfo());
            updateCandidates();
        } else {
            getCurrentInputConnection().commitText(key, 1);
        }
    }

    private void handleClose() {
        commitTyped(getCurrentInputConnection());
        requestHideSelf(0);
        mInputView.closing();
    }

    private IBinder getToken() {
        final Dialog dialog = getWindow();
        if (dialog == null) {
            return null;
        }
        final Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return window.getAttributes().token;
    }

    private void handleLanguageSwitch() {
        mInputMethodManager.switchToNextInputMethod(getToken(), false /* onlyCurrentIme */);
    }

    private void checkToggleCapsLock() {
        long now = System.currentTimeMillis();
//        mCapsLock = mInputView.isShifted();
        if (mLastShiftTime + 500 > now) {
            mCapsLock = !mCapsLock;
            mLastShiftTime = 0;
            mInputView.isOnecaps = false;
        } else {
            mInputView.isOnecaps = mInputView.isShifted();
//            mCapsLock = !mCapsLock;
            mLastShiftTime = now;
        }

    }

    private String getWordSeparators() {
        return mWordSeparators;
    }

    public boolean isWordSeparator(int code) {
        String separators = getWordSeparators();
        return separators.contains(String.valueOf((char) code));
    }

    public void pickDefaultCandidate() throws Exception {
        pickSuggestionManually(0);
    }

    public void pickSuggestionManually(int index) {
        if (mCompletionOn && mCompletions != null && index >= 0
                && index < mCompletions.length) {
            CompletionInfo ci = mCompletions[index];
            getCurrentInputConnection().commitCompletion(ci);
            if (mCandidateView != null) {
                mCandidateView.clear();
            }
            updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (mComposing.length() > 0) {

            if (mPredictionOn && mSuggestions != null && index >= 0) {
                mComposing.replace(0, mComposing.length(), mSuggestions.get(index));
            }
            commitTyped(getCurrentInputConnection());

        }
    }

    public void swipeRight() {
        Log.d("SoftKeyboard", "Swipe right");
        if (mCompletionOn || mPredictionOn) {
            try {
                pickDefaultCandidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void swipeLeft() {
        Log.d("SoftKeyboard", "Swipe left");
        handleBackspace();
    }

    public void swipeDown() {
        handleClose();
    }

    public void swipeUp() {
    }

    public void onPress(int primaryCode) {
        mInputView.keyPress = primaryCode;
        if (primaryCode == 0) {
            return;
        }
        if (longClickTimer != null) {
            longClickTimer.cancel();
        }
        isLongClick = false;
        longClickTimer = new CountDownTimer(800, 800) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isLongClick = true;
                List<Keyboard.Key> keys = mInputView.getKeyboard().getKeys();
                for (Keyboard.Key key : keys) {
                    if (key.codes[0] == primaryCode) {
                        if (key.popupCharacters != null) {
                            handleCharacter2(primaryCode, key.popupCharacters.toString());
                            onRelease(primaryCode);
                        }
                        break;
                    }
                }
            }
        }.start();


        if (GlobalFunction.getSounds(this)) {
            SoundsUtils.play(this, R.raw.iphone);
        }
        if (GlobalFunction.getVibrate(this)) {
            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(40);
        }
        if (GlobalFunction.getPopup(context)) {


            if (mCurKeyboard == mNumberKeyboard || primaryCode == -2 || primaryCode == 10 || primaryCode == 32 || primaryCode == -5 || primaryCode == -1) {
            } else {
                List<Keyboard.Key> keys = mInputView.getKeyboard().getKeys();
                for (Keyboard.Key key : keys) {
                    if (key.codes[0] == primaryCode) {
                        MLog.e(key.x);
                        MLog.e(key.y);
                        if (key.y < 10) {
                            if (key.x < 10) {
                                showPopup3(primaryCode, key, true, false);
                            } else if (key.x > width - (width / 10) - 10) {
                                showPopup3(primaryCode, key, false, false);
                            } else {
                                showPopup2(primaryCode, key);
                            }

                        } else {
                            boolean extraRow = GlobalFunction.getExtraRow(context);

                            if ((primaryCode == 113 || primaryCode == 126) && extraRow) {
                                showPopup3(primaryCode, key, true, true);
                            } else if ((primaryCode == 112 || primaryCode == 9651) && extraRow) {
                                showPopup3(primaryCode, key, false, true);
                            } else {
                                showPopup(primaryCode, key);
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    HashMap<Integer, PopupWindow> dm = new HashMap<>();

    private void showPopup(int primaryCode, Keyboard.Key key) {
        View custom = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
        TextView tvTitle = (TextView) custom.findViewById(R.id.tv_title);

        PopupWindow popup;
        popup = new PopupWindow(this);
        popup.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popup.setSplitTouchEnabled(false);
        popup.setWidth((width / 10 * 2) + width / 200);
        popup.setHeight((int) (DeviceUtils.convertDpToPixel(context, 52 * 2) + width / 100 * 1.5));
        popup.setContentView(custom);
        popup.setOutsideTouchable(true);
        popup.setTouchable(false);
        if (key.label != null) {
            CharSequence text = key.label;
            if (mInputView.isShifted()) {
                tvTitle.setText(text.toString().toUpperCase());
            } else {
                tvTitle.setText(text.toString().toLowerCase());
            }
            popup.showAtLocation(mInputView, Gravity.NO_GRAVITY, key.x - width / 20 - width / 400, key.y);
            dm.put(primaryCode, popup);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (popup.isShowing()) {
                        popup.dismiss();
                    }
                }
            }, 800);
        }
    }

    private void test(View view, int x, int y, int width, int height) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("ADAS");
        dialog.getWindow().setLayout(100, 200);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.x = 100;   //x position
        wmlp.y = 200;   //y position

        dialog.show();
    }

    private void showPopup2(int primaryCode, Keyboard.Key key) {

        View custom2 = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
        TextView tvTitle2 = (TextView) custom2.findViewById(R.id.tv_title);
        tvTitle2.setPadding(0, DeviceUtils.convertDpToPixel(context, 5), 0, 0);
        PopupWindow popup2 = new PopupWindow(this);
        popup2.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        popup2.setWidth((width / 10 * 2) + width / 200);
        popup2.setHeight((int) (DeviceUtils.convertDpToPixel(context, 52 * 2) + width / 100 * 1.5));
        popup2.setContentView(custom2);
        popup2.setOutsideTouchable(true);
        popup2.setTouchable(false);

        if (key.label != null) {
            CharSequence text = key.label;
            if (mInputView.isShifted()) {
                tvTitle2.setText(text.toString().toUpperCase());
            } else {
                tvTitle2.setText(text.toString().toLowerCase());
            }
            popup2.showAtLocation(mInputView, Gravity.NO_GRAVITY, key.x - width / 20 - width / 400, key.y);
            dm.put(primaryCode, popup2);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (popup2.isShowing()) {
                        popup2.dismiss();
                    }
                }
            }, 800);
        }
    }

    private void showPopup3(int primaryCode, Keyboard.Key key, boolean isLeft, boolean isRow2) {

        View custom3 = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
        TextView tvTitle3 = (TextView) custom3.findViewById(R.id.tv_title);
        ImageView imvBack = (ImageView) custom3.findViewById(R.id.imv_back);
        tvTitle3.setPadding(0, DeviceUtils.convertDpToPixel(context, 5), 0, 0);
        PopupWindow popup3 = new PopupWindow(this);
        popup3.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popup3.setWidth((width / 10 * 2) + width / 200);
        popup3.setHeight((int) (DeviceUtils.convertDpToPixel(context, 52 * 2) + width / 100 * 1.5));
        popup3.setContentView(custom3);
        popup3.setOutsideTouchable(true);
        popup3.setTouchable(false);
        if (isLeft) {
            imvBack.setBackgroundResource(R.drawable.keyboard_key_feedback_left_background);
        } else {
            imvBack.setBackgroundResource(R.drawable.keyboard_key_feedback_right_background);
        }
        if (key.label != null) {
            CharSequence text = key.label;
            if (mInputView.isShifted()) {
                tvTitle3.setText(text.toString().toUpperCase());
            } else {
                tvTitle3.setText(text.toString().toLowerCase());
            }
            boolean extraRow = GlobalFunction.getExtraRow(context);
            if (extraRow) {
                popup3.showAtLocation(mInputView, Gravity.NO_GRAVITY, key.x - width / 20 - width / 400, key.y);
            } else {
                popup3.showAtLocation(mInputView, Gravity.NO_GRAVITY, key.x - width / 20 - width / 400, key.y);
            }
            dm.put(primaryCode, popup3);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (popup3.isShowing()) {
                        popup3.dismiss();
                    }
                }
            }, 800);
        }
    }

    public void onRelease(int primaryCode) {
        if (GlobalFunction.getPopup(context)) {


            if (dm.containsKey(primaryCode)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dm.get(primaryCode).isShowing()) {
                            dm.get(primaryCode).dismiss();
                        }
                    }
                }, 200);
            }
        }
        if (longClickTimer != null) {
            longClickTimer.cancel();
        }
    }

    /**
     * http://www.tutorialspoint.com/android/android_spelling_checker.htm
     *
     * @param results results
     */
    @Override
    public void onGetSuggestions(SuggestionsInfo[] results) {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < results.length; ++i) {
            // Returned suggestions are contained in SuggestionsInfo
            final int len = results[i].getSuggestionsCount();
            sb.append('\n');

            for (int j = 0; j < len; ++j) {
                sb.append("," + results[i].getSuggestionAt(j));
            }

            sb.append(" (" + len + ")");
        }
        Log.d("SoftKeyboard", "SUGGESTIONS: " + sb.toString());
    }

    private static final int NOT_A_LENGTH = -1;

    private void dumpSuggestionsInfoInternal(
            final List<String> sb, final SuggestionsInfo si, final int length, final int offset) {
        // Returned suggestions are contained in SuggestionsInfo
        final int len = si.getSuggestionsCount();
        for (int j = 0; j < len; ++j) {
            sb.add(si.getSuggestionAt(j));
        }
    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
        Log.d("SoftKeyboard", "onGetSentenceSuggestions");
        final List<String> sb = new ArrayList<>();
        for (int i = 0; i < results.length; ++i) {
            final SentenceSuggestionsInfo ssi = results[i];
            for (int j = 0; j < ssi.getSuggestionsCount(); ++j) {
                dumpSuggestionsInfoInternal(
                        sb, ssi.getSuggestionsInfoAt(j), ssi.getOffsetAt(j), ssi.getLengthAt(j));
            }
        }
        Log.d("SoftKeyboard", "SUGGESTIONS: " + sb.toString());
        setSuggestions(sb, true, true);
    }
}
