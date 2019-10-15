package swoop.com.swoop.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import swoop.com.swoop.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Help extends Fragment {


    private View view;
    private EditText email;
    private Button submit;

    public Help() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_help, container, false);

        //initialize variables
        email = (EditText)view.findViewById(R.id.email);
        submit = (Button) view.findViewById(R.id.submit);

        //handle click events
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        return view;
    }

    //launches explicit intent for email
    private void sendEmail(){

        String to_send = email.getText().toString();

        if (!to_send.isEmpty())
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("email"));
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{getResources().getString(R.string.swoop_email)});
            intent.putExtra(Intent.EXTRA_TEXT,to_send);
            intent.setType("message/rfc822");
            Intent chooser = Intent.createChooser(intent,"Complete action with");
            startActivity(chooser);
        }
        else
        {
            email.setError("Write something first");
        }

    }

}
