package dheeraj.com.trafficsolution.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dheeraj.com.trafficsolution.FeedsActivity;
import dheeraj.com.trafficsolution.R;
import dheeraj.com.trafficsolution.Utils.SharedPreferenceMethods;

public class LoginRegisterChoose extends Activity {

    Button bt_signUp, bt_signIn;
    TextView tv_travis, tv_message, tv_termsConditions, tv_forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        // Check if user is logged in
        String is_logged_in = SharedPreferenceMethods.getString(getApplicationContext(), SharedPreferenceMethods.IS_LOGGED_IN);

        if (is_logged_in.equals("yes")) {
            startActivity(new Intent(getApplicationContext(), FeedsActivity.class));
            finish();
        }
        else {
            bt_signUp.setVisibility(View.VISIBLE);
            bt_signIn.setVisibility(View.VISIBLE);
            tv_termsConditions.setVisibility(View.VISIBLE);
            tv_forgotPassword.setVisibility(View.VISIBLE);
        }

        bt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });

        bt_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignIn.class));
            }
        });

        tv_termsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginRegisterChoose.this, "Terms & Conditions", Toast.LENGTH_SHORT).show();
            }
        });

        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginRegisterChoose.this, "Forgot Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void init() {
        setContentView(R.layout.activity_login_register_choose);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        bt_signUp = (Button)findViewById(R.id.bt_regChoose_signUp);
        bt_signIn = (Button)findViewById(R.id.bt_regChoose_signIn);
        tv_termsConditions = (TextView)findViewById(R.id.tv_regChoose_termsConditions);
        tv_forgotPassword = (TextView)findViewById(R.id.tv_regChoose_forgotPass);
        bt_signUp.setVisibility(View.INVISIBLE);
        bt_signIn.setVisibility(View.INVISIBLE);
        tv_termsConditions.setVisibility(View.INVISIBLE);
        tv_forgotPassword.setVisibility(View.INVISIBLE);

        tv_travis = (TextView)findViewById(R.id.tv_regChoose_travis);
        tv_message = (TextView)findViewById(R.id.tv_regChoose_travis_full_form);

        Typeface MontReg = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Regular.otf");
        Typeface MontBold = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Bold.otf");
        //Typeface MontHair = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Hairline.otf");

        bt_signUp.setTypeface(MontBold);
        bt_signIn.setTypeface(MontBold);
        tv_travis.setTypeface(MontBold);
        tv_message.setTypeface(MontReg);
        tv_forgotPassword.setTypeface(MontReg);
        tv_termsConditions.setTypeface(MontReg);
    }
}
