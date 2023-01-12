package com.app.vruddy.Views.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import androidx.appcompat.app.AppCompatActivity;

public class InsertDataToSharedPreferences extends AppCompatActivity {
    private String func = "";
    private String var = "";
    //getter setter


    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }
    //--------------------------------

    public void insert(String func, String var, Context contextt){


        //startActivity(new Intent(InsertDataToSharedPreferences.this, InsertDataToSharedPreferences.class));
                System.out.println("----- insert(); ----------");


                try {

                    System.out.println("----------- My SharedPreferences -----------");


                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(contextt);
                    System.out.println("----------- After My SharedPreferences -----------");

                    //SharedPreferences sharedPref = getSharedPreferences("JAVASCRIPT", 0);
                    //sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();

                    System.out.println("put function: "+func);
                    System.out.println("put var: "+var);

                    editor.putString("function", func);
                    editor.putString("var", var);
                    editor.apply();
                }catch (Exception e){
                    System.out.println("Error in (insert()): "+e);
                }

    }
    public void read(Context context){

        System.out.println("----- read(); ----------");
        //SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        setFunc(sharedPref.getString("function", ""));
        setVar(sharedPref.getString("var", ""));

        System.out.println("function from read(): "+sharedPref.getString("function", ""));
        System.out.println("var from read(): "+sharedPref.getString("var", ""));
    }

}
