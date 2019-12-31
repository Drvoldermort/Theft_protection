package com.example.pavan.theftprotection.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pavan.theftprotection.Common.Constants;
import com.example.pavan.theftprotection.Common.CustomLoader;
import com.example.pavan.theftprotection.Common.Preferences;
import com.example.pavan.theftprotection.Fragment.HomeFragment;
import com.example.pavan.theftprotection.Fragment.MapHomeFragment;
import com.example.pavan.theftprotection.Fragment.TrackPhoneFragment;
import com.example.pavan.theftprotection.NeedyClass.LocUpdateService;
import com.example.pavan.theftprotection.NeedyClass.ReadMessageService;
import com.example.pavan.theftprotection.NeedyClass.SmsListener;
import com.example.pavan.theftprotection.NeedyClass.SmsReceiver;
import com.example.pavan.theftprotection.NeedyClass.UpdateLocationService;
import com.example.pavan.theftprotection.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private android.support.v7.widget.Toolbar mainToolbar;

    Preferences pref;
    GoogleSignInClient mGoogleSignInClient;
    TextView maindrawer_username;
    String user_id;
    TextView maindrawer_email;
    CircleImageView maindrawer_profile;
    private NavigationView mainLeftNav;
    private DrawerLayout maindrawerLayout;
    private ActionBarDrawerToggle mainToggle;
    private FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    CustomLoader customLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {

                Toast.makeText(getApplicationContext(),"Message: "+messageText,Toast.LENGTH_LONG).show();

                startService(new Intent(getApplicationContext(), UpdateLocationService.class));


            }
        });


        mAuth = FirebaseAuth.getInstance();

        customLoader = new CustomLoader(this,R.style.Theme_Transparent);



        loadfragment(new HomeFragment());

        pref = new Preferences(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Left navigation viewer
        mainLeftNav = findViewById(R.id.nav_main_view);
        mainLeftNav.setNavigationItemSelectedListener(this);

        //drawer Layout
        maindrawerLayout = findViewById(R.id.drawer_layout);

        firebaseFirestore = FirebaseFirestore.getInstance();
        user_id = mAuth.getCurrentUser().getUid();


        //toolbar
        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Theft Protection");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.action_menu);

        mainToggle = new ActionBarDrawerToggle(HomeActivity.this, maindrawerLayout, mainToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        maindrawerLayout.addDrawerListener(mainToggle);


        maindrawer_username = (TextView) mainLeftNav.getHeaderView(0).findViewById(R.id.drawer_username);

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_action_home:
                displayview(0);
                break;
            case R.id.drawer_Logout:
                mAuth.signOut();
                mGoogleSignInClient.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.drawer_TrackmyPhone:
                displayview(2);
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //on back
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        String position = pref.get(Constants.fragment_position);
        if(position.equals("") || position.equals("0"))
        {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                moveTaskToBack(true);
                                finish();
                                System.exit(0);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
        }
        else if(position.equals("1"))
            displayview(0);
        else
            displayview(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        customLoader.show();
        NavigationView navigationView = findViewById(R.id.nav_main_view);
        View header = navigationView.findViewById(R.id.main_navigation_header);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            maindrawer_username = (TextView) mainLeftNav.getHeaderView(0).findViewById(R.id.drawer_username);
            maindrawer_email = (TextView) mainLeftNav.getHeaderView(0).findViewById(R.id.drawer_email);
            maindrawer_profile = (CircleImageView) mainLeftNav.getHeaderView(0).findViewById(R.id.profile_image);
            maindrawer_username.setText(personName);
            maindrawer_email.setText(personEmail);
            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.mipmap.profile_view);
            Glide.with(this).applyDefaultRequestOptions(placeholderOption).load(personPhoto).into(maindrawer_profile);
            firebaseFirestore.collection("userlocationdata").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists())
                        displayview(1);
                    else
                        displayview(0);

                }
            });
        }
        customLoader.cancel();

    }

    private void loadfragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container,fragment).commit();

    }


    public void displayview(int position)
    {
        switch(position){
            case 0:
                loadfragment(new HomeFragment());
                break;
            case 1:
                loadfragment(new MapHomeFragment());
                break;
            case 2:
                loadfragment(new TrackPhoneFragment());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(getApplicationContext(),ReadMessageService.class));



    }

}
