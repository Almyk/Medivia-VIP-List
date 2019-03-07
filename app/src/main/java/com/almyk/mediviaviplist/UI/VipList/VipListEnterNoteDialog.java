package com.almyk.mediviaviplist.UI.VipList;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.Repository.DataRepository;

public class VipListEnterNoteDialog extends DialogFragment implements View.OnClickListener {
    private DataRepository mRepository;

    private EditText mNoteView;
    private Button mDeleteBtn;
    private Button mSaveBtn;

    private static PlayerEntity player;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.enter_note, container, false);
        mRepository = ((MediviaVipListApp) getActivity().getApplication()).getRepository();

        mNoteView = rootView.findViewById(R.id.et_note);
        mDeleteBtn = rootView.findViewById(R.id.btn_delete);
        mSaveBtn = rootView.findViewById(R.id.btn_save);

        mDeleteBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);

        String note = player.getNote();
        if(!TextUtils.isEmpty(note)) {
            mNoteView.setText(note);
        }
        return rootView;
    }

    public VipListEnterNoteDialog() {
    }

    public static VipListEnterNoteDialog newInstance() {

        Bundle args = new Bundle();

        VipListEnterNoteDialog fragment = new VipListEnterNoteDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_delete:
                player.setNote("");
                mRepository.updatePlayerDB(player);
                Toast.makeText(getActivity(), "Deleted note for " + player.getName(), Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.btn_save:
                String note = mNoteView.getText().toString();
                player.setNote(note);
                mRepository.updatePlayerDB(player);
                Toast.makeText(getActivity(), "Added note to " + player.getName(), Toast.LENGTH_SHORT).show();
                dismiss();
                break;
        }
    }

    public static void setPlayer(PlayerEntity player) {
        VipListEnterNoteDialog.player = player;
    }
}
