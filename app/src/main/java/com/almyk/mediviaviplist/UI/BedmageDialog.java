package com.almyk.mediviaviplist.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.almyk.mediviaviplist.Database.Entities.BedmageEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.Repository.DataRepository;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BedmageDialog extends DialogFragment implements View.OnClickListener {
    private DataRepository mRepository;

    private EditText mBedmageTextView;
    private EditText mTimerTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_add_bedmage, container, false);
        mRepository = ((MediviaVipListApp) getActivity().getApplication()).getRepository();

        mBedmageTextView = rootView.findViewById(R.id.et_name);
        mTimerTextView = rootView.findViewById(R.id.et_timer);
        rootView.findViewById(R.id.btn_add).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                addBedmage();
                break;
        }

    }

    private void addBedmage() {
        String name = mBedmageTextView.getText().toString();
        String timer = mTimerTextView.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(timer)) {
            BedmageEntity bedmage = new BedmageEntity();
            bedmage.setName(name);
            long timerMilli = Long.parseLong(timer);
            timerMilli = TimeUnit.MINUTES.toMillis(timerMilli);
            bedmage.setTimer(timerMilli);
            bedmage.setLogoutTime(0);
            bedmage.setTimeLeft(timerMilli);
            bedmage.setNotified(false);
            bedmage.setOnline(false);

            mRepository.addBedmage(bedmage);
            Toast.makeText(getActivity(), "Added bedmage " + name, Toast.LENGTH_SHORT).show();
            dismiss();
        } else {
            Toast.makeText(getActivity(), "Error!\nMake sure that the input was correct and try again.", Toast.LENGTH_LONG).show();
        }
    }
}
