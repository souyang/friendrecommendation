package com.recommendation.dto;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
/** 
 * Class for Bigram
 * 
 * **/
public class TextPair implements WritableComparable<TextPair>{

    private Text first;
    private Text second;

    public void set(Text first, Text second){
        this.first=first;
        this.second=second;
    }

    public Text getFirst() {
        return first;
    }
    public Text getSecond() {
        return second;
    }


    public TextPair(){
        set(new Text(),new Text());
    }

    public TextPair(String first, String second){
        set(new Text(first),new Text(second));
    }

    public TextPair(Text first, Text second){
        set(first,second);
    }

    @Override
    public void write(DataOutput out) throws IOException{
        first.write(out);
        second.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException{
        first.readFields(in);
        second.readFields(in);
    }

    @Override
    public int compareTo(TextPair tp){
        int cmp = first.compareTo(tp.first);
        if (cmp!=0) {
            return cmp;
        }
        return second.compareTo(tp.second);
    }


    @Override
    public int hashCode(){
        return first.hashCode()*31 + second.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if( o instanceof TextPair){
            TextPair tp = (TextPair) o;
            return first.equals(tp.first) && second.equals(tp.second);
        }
        return  false;
    }

    @Override
    public String toString(){
        return first +"\t" +second;
    }


}
