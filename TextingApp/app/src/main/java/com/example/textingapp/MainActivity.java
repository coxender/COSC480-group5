package com.example.textingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    public static Model model;
    public DHKey keygen;
    private EditText editText;
    private EditText sendEmail;
    private ListView listView;
    private Spinner spinner;
    private static final String[] paths = {"Codebook", "Diffie Hellman", "AES"};


    PublicKey publicKey;
    PrivateKey privateKey;

    private DatabaseReference databaseReference;

    FirebaseUser fbUser;
    private FirebaseAuth FBA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        sendEmail = findViewById(R.id.email);
        listView = findViewById(R.id.listView);

        databaseReference = FirebaseDatabase.getInstance().getReference("Message");
        FBA = FirebaseAuth.getInstance();
        fbUser = FBA.getCurrentUser();
        model = new Model();
        model.set(17);
        KeyPairGenerator keyPairGenerator = null;

        {
            try {
                keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(4096);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }


        // Generate the KeyPair
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Get the public and private key
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();


        spinner = (Spinner)findViewById(R.id.method);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> stringFinal = new ArrayList<>();
                TreeMap<String, HashMap> map1 = new TreeMap<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    map1.put(childSnapshot.getKey(), (HashMap) childSnapshot.getValue());
                }

                map1.entrySet().stream().filter(x -> x.getValue().get("usrReceive").equals(fbUser.getEmail()) || x.getValue().get("usrSent").equals(fbUser.getEmail())).forEach(x -> {
                    String msgEncrypted = x.getValue().get("msg").toString();
                    int method = Integer.parseInt(x.getValue().get("method").toString());
                    String decrypted="";
                    //codebook
                    if (method == 1) {

                        decrypted = model.decrypt(msgEncrypted);
                    //diffieHellman
                    }else if(method == 2){
                        DHKey keygen=new DHKey(model.get());
                        long key = keygen.getSecKey();

                        decrypted=model.decryptDiffie(msgEncrypted,key);
                    //AES
                    }else if(method ==3){
                        int key=model.get();
                        String secret_key=String.valueOf(key);
                        decrypted=AESTEST.decrypt(msgEncrypted,secret_key);
                    }
                    stringFinal.add(x.getValue().get("usrSent").toString() + " => " + x.getValue().get("usrReceive").toString() +":\n"+ decrypted);
                });

                Collections.reverse(stringFinal);//puts new messages at the top
                listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stringFinal));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FBA = FirebaseAuth.getInstance();

    }
    //send messages
    public void sendButton(View view){
        //sends the string and information to the DB

        if(fbUser != null) {
            //get message information
            String userEmail = fbUser.getEmail().toLowerCase();
            Date date = new Date();

            String text = editText.getText().toString();
            String email = sendEmail.getText().toString();

            //find what option chosen and encrypt
            String encrypted="";
            String s = spinner.getSelectedItem().toString();
            System.out.println("Selected Method: " + s);

            int method = 1;
            switch(s){
                case "Codebook": method = 1;
                    break;
                case "Diffie Hellman": method = 2;
                    break;
                case "AES": method = 3;
            }

            //encrypt using chosen method
            if (method == 1){
                encrypted = model.encrypt(text);
            }else if(method == 2){
                keygen=new DHKey(model.get());

                long key = keygen.getSecKey();
                long prime=keygen.getPrime();
                encrypted=model.encryptDiffie(text,key);
                keygen.setPrime(key);

            }else if(method == 3){
                int key=model.get();
                String secret_key=String.valueOf(key);
                encrypted= AESTEST.encrypt(text, secret_key);
            }

            Message msg = new Message(encrypted, userEmail, email, method);
            //send data
            databaseReference.child(Long.toString(date.getTime())).setValue(msg);

            //reset text box
            editText.setText("");
        } else {
            startActivity(new Intent(this, Login.class));
            finish();
        }


    }

}