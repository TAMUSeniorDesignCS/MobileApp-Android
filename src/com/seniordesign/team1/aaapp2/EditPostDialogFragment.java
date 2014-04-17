package com.seniordesign.team1.aaapp2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class EditPostDialogFragment extends DialogFragment {
	private EditPostDialogFragment _this = null;
	public EditPostDialogFragment(){
		super();
		this._this = this;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		final int postId = getArguments().getInt("postId");
		final String username = getArguments().getString("username");
		final String pw = getArguments().getString("pw");
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.editPostDialogOptions, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(which == 0){
					//edit post
					Log.w("AAApp2", "Edit Post doesn't work yet.");
				}
				else if(which == 1){
					//delete post
					NetworkAsyncTask network = new NetworkAsyncTask(_this.getActivity());
					network.execute(NetworkAsyncTask.serverLit + "post/remove?postid=" + Integer.toString(postId) + "&rusername=" + username + "&rpassword=" + pw);
				}
				else{
					Log.e("AAApp2", "Imposible value of which in EditPostDialogFragment DialogInterface.OnClickListener");
				}
			}
		});
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
