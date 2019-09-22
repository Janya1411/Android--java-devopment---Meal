package app.android.meal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.time.LocalDate;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Meal_Options extends AppCompatActivity {
    TextView date,Status;
    LocalDate d = LocalDate.now();
    String x = d.toString();
    String Day1="2019-10-04";
    String Day2="2019-10-05";
    String Day3="2019-10-06";
    String nt="Come back On the 4th of Oct";



    String day;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal__options);
        date = findViewById(R.id.currdate);
        Status = findViewById(R.id.Status);
        date.setText(x);
        if(x.equals(Day1))
        {
            day="Day1";
        }
        else if(x.equals(Day2))
        {
            day="Day2";
        }
        else
        {
            day="Day3";
        }

    }
    public void Lunch(View v)
    {
        if(day.equals("Day1")||day.equals("Day2")||day.equals("Day3")) {
            Intent myIntent = new Intent(Meal_Options.this, MainActivity.class);
            myIntent.putExtra("Day", day);
            myIntent.putExtra("Meal", "Lunch");
            startActivity(myIntent);
        }
        else
            {
                Status.setText(nt);
            }
    }
    public void Dinner(View v)
    {
        if(day.equals("Day1")||day.equals("Day2")||day.equals("Day3")) {
            Intent myIntent = new Intent(Meal_Options.this, MainActivity.class);
            myIntent.putExtra("Day", day);
            myIntent.putExtra("Meal", "Dinner");
            startActivity(myIntent);
        }
        else
        {
            Status.setText(nt);
        }
    }
    public void Breakfast(View v)
    {
        if(day.equals("Day1")||day.equals("Day2")||day.equals("Day3")) {
            Intent myIntent = new Intent(Meal_Options.this, MainActivity.class);
            myIntent.putExtra("Day", day);
            myIntent.putExtra("Meal", "Breakfast");
            startActivity(myIntent);
        }
        else
        {
            Status.setText(nt);
        }
    }

}
