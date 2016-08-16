package com.dalexiv.rssreader.presentation.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import com.dalexiv.rssreader.R;

/**
 * Created by dalexiv on 8/15/16.
 */

public class DialogNewRssLink extends DialogFragment {
    public static final String TAG_LINK_ENTERED = "LINK";

    public static DialogNewRssLink newInstance() {
        return new DialogNewRssLink();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_text_input, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setView(v).setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    Intent intent = new Intent();
                    intent.putExtra(TAG_LINK_ENTERED,
                            ((EditText) v.findViewById(R.id.linkEditText)).getText().toString());
                    getTargetFragment().onActivityResult(getTargetRequestCode(),
                            Activity.RESULT_OK, intent);
                }).create();
    }
}
