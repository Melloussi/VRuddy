package com.app.vruddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.novoda.merlin.Merlin;

public class MainActivity extends AppCompatActivity {

    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Merlin merlin = new Merlin.Builder().withConnectableCallbacks().build(this);




        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
//        System.out.println("nInfo: "+nInfo+
//                "\nnInfo.isAvailable(): "+nInfo.isAvailable()+
//                "\nnInfo.isConnected(): "+nInfo.isConnected());

        //System.out.println("is connected: "+connected);



//        merlin.registerConnectable(new Connectable() {
//            @Override
//            public void onConnect() {
//                // Do something you haz internet!
//                isConnected = true;
//                System.out.println("Cool, You'r online :)");
//            }
//        });
//
//        if(connected){
//            System.out.println("Cool, You'r online :)");
//            Intent intent = new Intent(MainActivity.this, SplachScreen.class);
//                        startActivity(intent);
//                        finish();
//        }else if(connected){
//            System.out.println("You'r offline Please Check you internet connection!!");
//            Intent intent = new Intent(MainActivity.this, checkCnx.class);
//            startActivity(intent);
//            finish();
//        }


        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
//                        System.out.println("-------- Test -------");
//                        Intent intent = new Intent(MainActivity.this, SplachScreen.class);
//                        startActivity(intent);
//                        finish();

                        if(connected == true){
                            System.out.println("Cool, You'r online :)");
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else if(connected == false){
                            System.out.println("You'r offline Please Check you internet connection!!");
                            //Intent intent = new Intent(MainActivity.this, checkCnx.class);
                            //startActivity(intent);
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                           finish();
                        }
                    }
                },
                2000
        );
    }
}