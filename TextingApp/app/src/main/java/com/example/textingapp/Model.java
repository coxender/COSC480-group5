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



    public String encrypt(String input)
    {
        String output = "";
            for (int i = 0; i < input.length(); i++) {
                if (Character.isLetter(input.charAt(i)))
                    output = output + encrypt(input.charAt(i));
                else
                    output = output + input.charAt(i);
            }

        return output;
    }

    public String decrypt(String input)
    {
        String output = "";

        for (int i = 0; i < input.length(); i++) {
                output = output + decrypt(input.charAt(i));
        }

        return output;
    }
    public String encryptDiffie(String input, long key, long prime){
        String output = "";
        for (int i = 0; i < input.length(); i++) {
            if (Character.isLetter(input.charAt(i))) {
                long code = (long) input.charAt(i);
                output += (code + key) + ",";
            }
            else
                output = output + input.charAt(i);
        }
        return (output);
    }
    public String decryptDiffie(String code, long key, long prime){
        String output="";
        String token;
        Long number;
        System.out.println("code:" + code);
        code=code.replaceAll(" ","");
        Scanner scanner=new Scanner(code);
        scanner.useDelimiter(",");
        while(scanner.hasNext()){
            token=scanner.next();
            try {
                number = Long.parseLong(token);
                number = (number-key);
                System.out.print(number);
                output += (char) number.longValue();
            }catch(NumberFormatException e) {

                System.out.print(e);
                output += token;
            }
        }
        return output;
    }






    private char encrypt(char letter)
    {
        int code = code(letter);

        code = (code + key%52)%52;

        return character(code);
    }

    private char decrypt(char letter)
    {
        int code = code(letter);

        code = (code - key%52 + 52)%52;

        return character(code);
    }

    private int code(char letter)
    {
        if (Character.isLowerCase(letter))
            return (int)letter - (int)'a';
        else
            return (int)letter - (int)'A' + 26;
    }

    private char character(int code)
    {
        if (code < 26)
            return (char)((int)'a' + code);
        else
            return (char)((int)'A' + code - 26);
    }

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