package com.example.pavan.theftprotection.NeedyClass;

import android.support.annotation.NonNull;

//import com.google.firebase.database.Exclude;

import com.google.firebase.firestore.Exclude;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class PhoneLoactorId {
    @Exclude
    public String PhoneLoactorId;

    public <T extends PhoneLoactorId> T withId(@NonNull final String id){
     this.PhoneLoactorId=id;
     return(T) this;
    }
}
