package com.seniordesign.team1.aaapp2;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

public class NumberPickerPreference extends DialogPreference {
	private int mNewValue;
	private int mCurrentValue;
	private NumberPicker mNumberPicker = null;
	
	private final int DEFAULT_VALUE = 24;
	
	private static class SavedState extends BaseSavedState {
	    // Member that holds the setting's value
	    // Change this data type to match the type saved by your Preference
	    int value;

	    public SavedState(Parcelable superState) {
	        super(superState);
	    }

	    public SavedState(Parcel source) {
	        super(source);
	        // Get the current preference's value
	        value = source.readInt();  // Change this to read the appropriate data type
	    }

	    @Override
	    public void writeToParcel(Parcel dest, int flags) {
	        super.writeToParcel(dest, flags);
	        // Write the preference's value
	        dest.writeInt(value);  // Change this to write the appropriate data type
	    }

	    // Standard creator object using an instance of this class
	    public static final Parcelable.Creator<SavedState> CREATOR =
	            new Parcelable.Creator<SavedState>() {

	        public SavedState createFromParcel(Parcel in) {
	            return new SavedState(in);
	        }

	        public SavedState[] newArray(int size) {
	            return new SavedState[size];
	        }
	    };
	}
	
    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        setDialogLayoutResource(R.layout.numberpicker_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        
        setDialogIcon(null);
 
    }
    
    @Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		this.mNumberPicker = (NumberPicker)view.findViewById(R.id.pref_num_picker);
		// TODO this should be an XML parameter:
		//mNumberPicker.setRange(1, 7);
		//if ( this.initialValue != null ) picker.setCurrent(initialValue);
		this.mNumberPicker.setMinValue(1);
	    this.mNumberPicker.setMaxValue(96);
	}
    
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            persistInt(mNewValue);
        }
    }
    
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            mCurrentValue = this.getPersistedInt(DEFAULT_VALUE);
        } else {
            // Set default state from the XML attribute
            mCurrentValue = (Integer) defaultValue;
            persistInt(mCurrentValue);
        }
    }
    
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }
    
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) {
            // No need to save instance state since it's persistent, use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current setting value
        myState.value = mNewValue;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        
        // Set this Preference's widget to reflect the restored state
        mNumberPicker.setValue(myState.value);
    }
}