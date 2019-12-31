package com.example.pavan.theftprotection.Fragment;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pavan.theftprotection.Common.Constants;
import com.example.pavan.theftprotection.Common.Preferences;
import com.example.pavan.theftprotection.NeedyClass.LocUpdateService;
import com.example.pavan.theftprotection.NeedyClass.UpdateLocationService;
import com.example.pavan.theftprotection.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import static android.support.v4.content.ContextCompat.getSystemService;

public class TrackPhoneFragment extends Fragment {


    Preferences pref;
    View view;
    EditText phonenumber;
    Button sendMessage,checkPhoneNumber,stopLocationUpdate,fetchnumber,track;
    String destinationAddress,scAddress,smsMessage;
    PendingIntent sentIntent = null, deliveryIntent = null;
    private FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    String user_id;
    String alternatePhone= null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup containers, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_track_my_phone, containers, false);


        mAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        user_id = mAuth.getCurrentUser().getUid();

        pref = new Preferences(getContext());
        pref.set(Constants.fragment_position,"1").commit();

        smsMessage = "Your Phone is being Tracked. Leave it at the nearest police station!";
        phonenumber =  view.findViewById(R.id.editTextPhone);
        stopLocationUpdate = view.findViewById(R.id.buttonstopUpdateLocation);
        //fetchnumber = view.findViewById(R.id.buttonfetchnumber);
        //checkPhoneNumber = view.findViewById(R.id.buttonCheckNumber);
        track = view.findViewById(R.id.ButtonTrack);
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Inside Click",Toast.LENGTH_SHORT).show();
                getActivity().startService(new Intent(getContext(), LocUpdateService.class));
//                firebaseFirestore.collection("userlocationdata").document(user_id).collection("devicelocation").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                         QuerySnapshot result = task.getResult();
//
//                    }
//                });
            }
        });
        sendMessage = view.findViewById(R.id.buttonSendMessage);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinationAddress = phonenumber.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage
                        (destinationAddress, scAddress, smsMessage,
                                sentIntent, deliveryIntent);


                Toast.makeText(getContext(),"Message Sent Successfully",Toast.LENGTH_LONG).show();



            }
        });


        stopLocationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().stopService(new Intent(getContext(), UpdateLocationService.class));

            }
        });




        return view;
    }


}
