package app.android.meal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sucess extends AppCompatActivity {
  TextView User_ID,Del_Name,Del_Country,Status;
    private DatabaseReference ret;
    String UID,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucess);
        User_ID=findViewById(R.id.Userid);
        Del_Name=findViewById(R.id.Name);
        Del_Country=findViewById(R.id.Country);
        Status=findViewById(R.id.Status);
        Bundle extras=getIntent().getExtras();
        User_ID.setText(extras.getString("UID"));
        //Status.setText(extras.getString("status"));
        UID= String.valueOf(User_ID.getText());
        status=extras.getString("status");
        String NR="Not Registered";
        String Done="Already had this meal";
        String x="Allowed";
        if(status.equals("Not Registered"))
        {
            Status.setText(NR);
        }
        else if(status.equals("Done"))
        {
            Status.setText(Done);
        }
        else {
            Status.setText(x);
            ret = FirebaseDatabase.getInstance().getReference().child("MUN").child("Delegates").child("UNHRC").child(extras.getString("UID"));
            ret.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data: dataSnapshot.getChildren()) {
                        Delegates detail = dataSnapshot.getValue(Delegates.class);

                        String name = detail.getName();
                        String Country = detail.getCountry();


                        Del_Name.setText(name);
                        Del_Country.setText(Country);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // [START_EXCLUDE]
                    System.out.println("The read failed: " + databaseError.getMessage());
                }
            });

        }
    }
    public void Scan(View v)
    {
        Intent myIntent = new Intent(sucess.this,MainActivity.class);
        startActivity(myIntent);


    }
    public void Meals(View v)
    {
        Intent myIntent = new Intent(sucess.this,Meal_Options.class);
        startActivity(myIntent);
    }
}
