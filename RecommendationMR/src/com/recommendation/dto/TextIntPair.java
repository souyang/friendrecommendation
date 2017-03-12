package com.recommendation.dto;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TextIntPair implements WritableComparable<TextIntPair> {

    private Text first;
    private IntWritable second;

    public void set(Text first, IntWritable second){
        this.first=first;
        this.second=second;
    }

    public Text getFirst() {
        return first;
    }
    public IntWritable getSecond() {
        return second;
    }


    public TextIntPair(){
        set(new Text(),new IntWritable());
    }

    public TextIntPair(String first, Integer second){
        set(new Text(first),new IntWritable(second));
    }

    public TextIntPair(Text first, IntWritable second){
        set(first,second);
    }

    public TextIntPair(TextIntPair tp){
        set(tp.getFirst(),tp.getSecond());
    }

    @Override
    public void write(DataOutput out) throws IOException {
        first.write(out);
        second.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException{
        first.readFields(in);
        second.readFields(in);
    }

    @Override
    public int compareTo(TextIntPair tp){

        return second.compareTo(tp.second); // for sorting on the number of common friends
    }


    @Override
    public int hashCode(){
        return first.hashCode()*31 + second.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if( o instanceof TextIntPair){
            TextIntPair tp = (TextIntPair) o;
            return first.equals(tp.first) && second.equals(tp.second);
        }
        return  false;
    }

    @Override
    public String toString(){
        return first +"\t" +second.toString();
    }


}
