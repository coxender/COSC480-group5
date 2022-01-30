package com.example.textingapp;

public class DHKey {
    long prime,primRoot, privkey,genkey;
    //constructors
    public DHKey(long privkey){
        this.prime=557;
        this.primRoot=72;
        this.privkey=privkey;
    }
    public DHKey(long p, long pr, long privkey){
        this.prime=p;
        this.primRoot=pr;
        this.privkey=privkey;
    }

    //modular arithmetic function
    private static long power(long pr, long privkey, long p){
        if(privkey==1) {
            return pr;
        }
        else{
            long answer= ((long)Math.pow(pr, privkey))%p;
            return answer;
        }
    }
    //calculates secret key
    public long getSecKey(){
        return power(this.primRoot,this.privkey,this.prime);
    }
    //get and set
    public long getPrime(){
        return this.prime;
    }
    public void setPrime(long prime){this.prime=prime;}

}
