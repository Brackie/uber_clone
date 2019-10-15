 package swoop.com.swoop.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import swoop.com.swoop.R;

 public class ChangePaymentMethod extends AppCompatActivity {

     TextView fareEstimate;
     LinearLayout creditLayout,cashLayout,mpesaLayout;
     ImageView creditChecked,cashChecked,mpesaChecked;
     Button changePaymentMethod;
     String paymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_payment_method);

        //setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initializing variables
        fareEstimate = (TextView) findViewById(R.id.fare_estimate);
        creditLayout = (LinearLayout) findViewById(R.id.debit_credit_layout);
        cashLayout = (LinearLayout) findViewById(R.id.cash_layout);
        mpesaLayout = (LinearLayout) findViewById(R.id.m_pesa_layout);
        creditChecked = (ImageView) findViewById(R.id.credit_chosen);
        cashChecked = (ImageView) findViewById(R.id.cash_chosen);
        mpesaChecked = (ImageView) findViewById(R.id.m_pesa_chosen);
        changePaymentMethod = (Button) findViewById(R.id.change_payment_method);

        //handle click events
        creditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod = "Credit/Debit";
                creditChecked.setVisibility(View.VISIBLE);
                cashChecked.setVisibility(View.GONE);
                mpesaChecked.setVisibility(View.GONE);
            }
        });

        cashLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod = "Cash";
                creditChecked.setVisibility(View.GONE);
                cashChecked.setVisibility(View.VISIBLE);
                mpesaChecked.setVisibility(View.GONE);
            }
        });

        mpesaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod = "M-Pesa";
                creditChecked.setVisibility(View.GONE);
                cashChecked.setVisibility(View.GONE);
                mpesaChecked.setVisibility(View.VISIBLE);
            }
        });

        changePaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paymentMethod != null)
                {
                    Intent back = new Intent(ChangePaymentMethod.this,PickUp.class);
                    back.putExtra("payment_method",paymentMethod);
                    setResult(Activity.RESULT_OK,back);
                    finish();
                }
                else
                {
                    Toast.makeText(ChangePaymentMethod.this, "Choose Payment Method", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

     //handle click events on options menu
     @Override
     public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            finish();
        }

         return true;
     }

 }
