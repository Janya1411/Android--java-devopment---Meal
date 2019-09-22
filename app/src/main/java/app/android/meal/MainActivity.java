package app.android.meal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.barcode.BarcodeDetector.Builder;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import static com.google.android.gms.vision.barcode.Barcode.DATA_MATRIX;
import static com.google.android.gms.vision.barcode.Barcode.QR_CODE;

public class MainActivity extends AppCompatActivity {

    SurfaceView camprev;
    CameraSource camerasrc;
    TextView Uid_display;
    BarcodeDetector barcodeDetector;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private DatabaseReference mDatabase2;
    int counter,c;
    String Day;
    String Meal;


    private static final int REQUEST_CAMERA_PERMISSION = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camprev = findViewById(R.id.CameraPreview);
        Uid_display=findViewById(R.id.output);
        Bundle extras=getIntent().getExtras();
         Day=extras.getString("Day");
         Meal=extras.getString("Meal");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("MUN").child("Meal").child(Day).child(Meal);
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("MUN").child("Delegates").child("UNHRC");


        barcodeDetector = new Builder(this)
                .setBarcodeFormats(DATA_MATRIX | QR_CODE).build();

        if(!barcodeDetector.isOperational())
        {
            Uid_display.setText("");
        }
        camerasrc= new  CameraSource.Builder(this,barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920,1080).build();
        //you should add this feature

        camprev.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        camerasrc.start(camprev.getHolder());
                        camprev.setVisibility(View.VISIBLE);
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {


            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                camerasrc.stop();

            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                 final SparseArray<Barcode> qrcodes = detections.getDetectedItems();

                if (qrcodes.size() != 0) {
                    Uid_display.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                            camerasrc.stop();

                            final String intentdata = qrcodes.valueAt(0).displayValue;
                            final String y="Done";
                            final String x="Not Registered";
                            final String z="eligible";
                            mDatabase2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        if (dataSnapshot.child(intentdata).exists()) {
                                            c=0;

                                        } else {
                                            c=1;
                                        }
                                    }
                                    if(c==1) {
                                        Intent intent = new Intent(MainActivity.this, sucess.class);
                                        intent.putExtra("status", x);
                                        intent.putExtra("UID", intentdata);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        mDatabase1.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                    if (dataSnapshot.child(intentdata).exists()) {
                                                        counter = 0;
                                                    } else {
                                                        counter = 1;

                                                    }
                                                }
                                                if(counter==1)
                                                {
                                                    User user = new User(intentdata);
                                                    mDatabase1.child(intentdata).setValue("1");
                                                    Intent intent = new Intent(MainActivity.this, sucess.class);
                                                    intent.putExtra("UID", intentdata);
                                                    intent.putExtra("status",z);
                                                    startActivity(intent);
                                                }
                                                else
                                                {
                                                    Intent intent = new Intent(MainActivity.this, sucess.class);
                                                    intent.putExtra("status", y);
                                                    intent.putExtra("UID", intentdata);
                                                    startActivity(intent);
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });

                        }
                    });
                }
            }
        });
    }
}

