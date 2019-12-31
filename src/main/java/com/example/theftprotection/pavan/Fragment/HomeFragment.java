package com.example.pavan.theftprotection.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavan.theftprotection.Activity.HomeActivity;
import com.example.pavan.theftprotection.Common.Constants;
import com.example.pavan.theftprotection.Common.CustomLoader;
import com.example.pavan.theftprotection.Common.Preferences;
import com.example.pavan.theftprotection.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    Preferences pref;
    View view;
    String user_id, user_email;
    String deviceID = null;
    String imei = null;
    Button getImei;
    Button save_btn;
    String alt_phone, alt_email, org_phone;
    TextView IMEIView;
    EditText altphone, altemail, orgPhone;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    CustomLoader customLoader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup containers, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, containers, false);
//        getid();


        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        pref = new Preferences(getContext());
        pref.set(Constants.fragment_position, "0").commit();

        user_email = mAuth.getCurrentUser().getEmail();
        customLoader = new CustomLoader(getContext(),R.style.Theme_Transparent);


        altphone = view.findViewById(R.id.editTextaltphone);
        altemail = view.findViewById(R.id.editTextaltemail);
        orgPhone = view.findViewById(R.id.editTextphonenum);

        IMEIView = view.findViewById(R.id.textViewIMEI);
        getImei = view.findViewById(R.id.buttongetIMEI);
        getImei.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }

                imei = telephonyManager.getImei();
                deviceID = Build.MANUFACTURER + "" + Build.MODEL;
                Toast.makeText(getContext(), deviceID, Toast.LENGTH_SHORT).show();
                IMEIView.setText(deviceID);


            }
        });


        save_btn = view.findViewById(R.id.btn_save);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateandupload();

            }
        });


        return view;
    }

    private void validateandupload() {
        alt_phone = altphone.getText().toString();
        alt_email = altemail.getText().toString();
        org_phone = orgPhone.getText().toString();
        if (!alt_email.isEmpty() && !alt_phone.isEmpty() && !org_phone.isEmpty()) {
            Map<String, Object> userloc = new HashMap<>();
            userloc.put("altemail", alt_email);
            userloc.put("altphone", alt_phone);
            userloc.put("phonenumber", org_phone);
            userloc.put("usermailid", user_email);
            userloc.put("imei",imei);
            userloc.put("device_id",deviceID);
            userloc.put("timestamp", FieldValue.serverTimestamp());
            firebaseFirestore.collection("userlocationdata").document(user_id).set(userloc).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "User Data Registered Successfully", Toast.LENGTH_SHORT).show();
                        ((HomeActivity) getActivity()).displayview(1);
                    } else
                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        customLoader.show();

        firebaseFirestore.collection("userlocationdata").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists() && documentSnapshot!=null)
                    ((HomeActivity)getActivity()).displayview(1);

            }
        });
        customLoader.cancel();
    }
}
