package dheeraj.com.trafficsolution.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dheeraj.com.trafficsolution.FeedsActivity;
import dheeraj.com.trafficsolution.R;

public class SignIn extends AppCompatActivity {

    EditText email, password;
    Button bt_signIn;

    FirebaseAuth firebaseAuth;

    String st_displayname, st_city;
    String photourl;

    public static final String FireBaseSharedPref = "FireBaseSharedPref";
    public static final String FireBaseShared_KEY = "FireBaseShared_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        Firebase.setAndroidContext(this);
        firebaseAuth = FirebaseAuth.getInstance();

        bt_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailid = email.getText().toString();
                String passwordtext = password.getText().toString();

                if (emailid == null) {
                    email.setError("You can not leave it blank.");
                }
                if (passwordtext == null) {
                    password.setError("You can not leave it blank.");
                }

                // Force user to fill up the form
                if (emailid.equals("") && passwordtext.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please complete the sign up form", Toast.LENGTH_LONG).show();
                    email.setError("You can not leave it blank.");
                    password.setError("You can not leave it blank.");
                }
                else {
                    final ProgressDialog rd = new ProgressDialog(SignIn.this);
                    rd.setTitle("Please Wait!");
                    rd.setMessage("Logging Into Your Account...");
                    rd.setCancelable(false);
                    rd.show();

                    firebaseAuth.signInWithEmailAndPassword(emailid, passwordtext).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getApplicationContext(), "Incorrect password or email !", Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                            rd.cancel();
                        }
                    });
                    firebaseAuth.signInWithEmailAndPassword(emailid, passwordtext).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(final AuthResult authResult) {

                            SharedPreferences sharedpreferences = getSharedPreferences(FireBaseSharedPref, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(FireBaseShared_KEY,true);
                            editor.commit();

                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("people").child(authResult.getUser().getUid());

                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    st_displayname = String.valueOf(dataSnapshot.child("displayNames").getValue());
                                    photourl = String.valueOf(dataSnapshot.child("photoUrls").getValue());
                                    st_city = String.valueOf(dataSnapshot.child("village").getValue());

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(st_displayname)
                                            .setPhotoUri(Uri.parse(photourl))
                                            .build();

                                    authResult.getUser().updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                @Override
                                                public void onComplete(Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("profileupdated", "User profile updated.");
                                                    }
                                                }
                                            }
                                    );

                                    Toast.makeText(SignIn.this, st_displayname + "-" + st_city, Toast.LENGTH_LONG).show();

                                    //TODO: SAVE USER DATA HERE IN SHARED PREFS

                                    rd.cancel();
                                    startActivity(new Intent(getApplicationContext(), FeedsActivity.class));
                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    rd.cancel();
                                    Toast.makeText(SignIn.this, "Oops! Database Error. Please Try Again!", Toast.LENGTH_SHORT).show();
                                }

                            });
                        }
                    });
                };
            }
        });
    }

    void init() {
        setContentView(R.layout.activity_sign_in);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        bt_signIn = (Button) findViewById(R.id.signin1);

        Typeface MontReg = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Regular.otf");
        Typeface MontBold = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Bold.otf");
        //Typeface MontHair = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Hairline.otf");

        email.setTypeface(MontReg);
        password.setTypeface(MontReg);
        bt_signIn.setTypeface(MontBold);
    }
}