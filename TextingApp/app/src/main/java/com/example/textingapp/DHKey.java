package com.example.textingapp;

public class DHKey {
    long prime,primRoot, privkey,genkey;

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
    private static long power(long pr, long privkey, long p){
        if(privkey==1) {
            return pr;
        }
        else{
            long answer= ((long)Math.pow(pr, privkey))%p;
            return answer;
        }
    }
    public long getSecKey(){
        return power(this.primRoot,this.privkey,this.prime);
    }
    public long getPrime(){
        return this.prime;
    }
    public void setPrime(long prime){this.prime=prime;}

}
