package com.example.nexustutor.Model;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.nexustutor.Fragment.ProfileTutorFragment;
import com.example.nexustutor.MainActivity;
import com.example.nexustutor.R;

public class MyCustomDialogSubject extends DialogFragment {

    private static final String TAG = "MyCustomDialogSubject";
    public interface OnSubjectSelected{
        void sendInput(String input);
    }
    public OnSubjectSelected mOnSubjectSelected;

    private ImageButton imgBtnClose;
    private Button btnAdd, btnCancel;
    private EditText etAddSubject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_subject,container,false);

        imgBtnClose = view.findViewById(R.id.img_btn_close_subject);
        btnAdd = view.findViewById(R.id.btn_add_subject);
        btnCancel = view.findViewById(R.id.btn_cancel_subject);
        etAddSubject = view.findViewById(R.id.et_add_subject);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });
        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: capturing input");
                String subject = etAddSubject.getText().toString();

                if (!subject.equals("")){
                   /* ProfileTutorFragment fragment = getActivity().getFragmentManager().findFragmentByTag("ProfileTutorFragment");
                    Toast.makeText(getActivity(),subject, Toast.LENGTH_LONG);*/
                    mOnSubjectSelected.sendInput(subject);
                }
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnSubjectSelected = (OnSubjectSelected) getTargetFragment();

        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException :" + e.getMessage());
        }
    }
}
