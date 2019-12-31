package com.example.pavan.theftprotection.NeedyClass;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SmsReceiver extends BroadcastReceiver {

    private FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    String user_id;
    String alternatePhone= null;
    //interface
    private static SmsListener mListener;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        mAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        user_id = mAuth.getCurrentUser().getUid();



        firebaseFirestore.collection("userlocationdata").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    alternatePhone = documentSnapshot.getString("altphone");
                    continuejob(context,intent);
                }
                else{
                    Toast.makeText(context,"Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }



        });





    }
    private void continuejob(Context context,Intent intent){
        Bundle data  = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //Check the sender to filter messages which we require to read


            Toast.makeText(context,"sender = "+sender, Toast.LENGTH_SHORT).show();

            if (sender.equals("+919108455178")) {

                Toast.makeText(context, sender, Toast.LENGTH_SHORT).show();


                String messageBody = smsMessage.getMessageBody();
                if (messageBody.equals("Your Phone is being Tracked. Leave it at the nearest police station!")) {

                    //Pass the message text to interface
                    mListener.messageReceived(messageBody);
                }
            }


        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
