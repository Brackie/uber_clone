package swoop.com.swoop.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import swoop.com.swoop.R;

public class Login extends AppCompatActivity {

    Button login;
    TextView register,resetPassword;
    private final static int PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set up action bar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initializing variables
        login = (Button)findViewById(R.id.login);
        register = (TextView)findViewById(R.id.register);
        resetPassword = (TextView)findViewById(R.id.reset_password);

        //handle click events
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permissionsGranted())
                {
                    startActivity(new Intent(Login.this,Home.class));
                    finish();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permissionsGranted())
                {
                    startActivity(new Intent(Login.this,Register.class));
                }
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ResetPassword.class));
            }
        });
    }

    //ask for permissions
    private boolean permissionsGranted(){

        int fine_location_permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
        if (fine_location_permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},PERMISSIONS_REQUEST_CODE);
            return false;
        }
        return true;

    }

}
