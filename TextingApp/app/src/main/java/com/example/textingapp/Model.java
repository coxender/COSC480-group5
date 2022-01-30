package com.example.textingapp;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class Model
{
    private int key;
    public void set(int key)
    {
        this.key = key;
    }
    public int get(){return this.key;}
    // Get an instance of the RSA key generator


    //Option 1
    public String encrypt(String input)
    {
        final int ASCII_RANGE=255;
        String output = "";
            for (int i = 0; i < input.length(); i++) {
                int code =(int) input.charAt(i);
                code = (code + (key))%ASCII_RANGE;
                output+=(char) code;
            }

        return  output;
    }
    public String decrypt(String input)
    {
        final int ASCII_RANGE=255;
        String output = "";

        for (int i = 0; i < input.length(); i++) {
            int code =(int) input.charAt(i);
            code = (code %ASCII_RANGE - (key));
            output+=(char) code;
        }
        System.out.print("NONE: "+ output);
        return output;
    }


    //Option 2: Diffie Hellman
    public String encryptDiffie(String input, long key){
        String output = "";
        for (int i = 0; i < input.length(); i++) {
                long code = (long) input.charAt(i);
                output += (code + key) + ",";
        }
        return (output);
    }

    public String decryptDiffie(String code, long key){
        String output="";
        String token;
        Long number;
        Scanner scanner=new Scanner(code);
        scanner.useDelimiter(",");
        while(scanner.hasNext()){
            token=scanner.next();
            try {
                number = Long.parseLong(token);
                number = (number-key);
                output += (char) number.longValue();
            }catch(NumberFormatException e) {

                System.out.print(e);
                output += token;
            }
        }
        return output;
    }
    //Option 3 AES is not calculated in model, it is in AESTEST solely




    //Option OLD: RSA
    public String encryptRSA(String stringToEncrypt, PublicKey publicKey) {

        //creates a cipher instance using RSA
        Cipher cipher = null;
        byte[] encrypted = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
            //set up the mode to encrypt with the public key
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //do the encryption
            encrypted = cipher.doFinal(stringToEncrypt.getBytes());
            //convert to string and return encrypted string
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(encrypted);

    }

    public static String decryptRSA(String encryptedString, PrivateKey privateKey) {

        //creates a cipher instance using RSA
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        //set up the mode to decrypt and use the private key
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        //do the decryption
        byte[] original = new byte[0];
        try {
            original = cipher.doFinal(Base64.getDecoder().decode(encryptedString));
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        //convert to string and return
        return new String(original);

    }
}