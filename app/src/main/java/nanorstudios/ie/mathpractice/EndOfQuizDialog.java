package nanorstudios.ie.mathpractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Dialog Fragment to show the end of the quiz to the user.
 */

public class EndOfQuizDialog extends DialogFragment {

    public static final String TAG = "EndOfQuizDialog";
    private EndOfQuizCallbacks mCallbacks;

    public static EndOfQuizDialog newInstance(int chosenNumber) {
        EndOfQuizDialog dialogFrag = new EndOfQuizDialog();
        Bundle args = new Bundle();
        args.putInt(Constants.CHOSEN_NUMBER, chosenNumber);
        dialogFrag.setArguments(args);
        return dialogFrag;
    }

    @Override
    public void onAttach(Context context) {
        mCallbacks = (EndOfQuizCallbacks) context;
        super.onAttach(context);
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int chosenNum = getArguments().getInt(Constants.CHOSEN_NUMBER);
        return new AlertDialog.Builder(getActivity())
                .setTitle("Congratulations")
                .setMessage("Well done, you've beaten " + chosenNum + ", keep up the great work!")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallbacks.closeActivity();
                    }
                })
                .create();
    }

    public interface EndOfQuizCallbacks {
        void closeActivity();
    }
}