package com.example.trainticket;

public class Train {
    String train_no,train_name,train_departure,train_arrival,train_2s,train_sl,train_3a,train_2a,train_1a,train_path;
    Train(){}
    Train(String train_no,String train_name,String train_departure,String train_arrival,String train_2s,String train_sl,String train_3a,String train_2a,String train_1a,String train_path){
        this.train_no = train_no;
        this.train_name = train_name;
        this.train_departure = train_departure;
        this.train_arrival = train_arrival;
        this.train_2s = train_2s;
        this.train_sl = train_sl;
        this.train_3a = train_3a;
        this.train_2a = train_2a;
        this.train_1a = train_1a;
        this.train_path = train_path;
    }

    public String getTrain_no() {
        return train_no;
    }

    public void setTrain_no(String train_no) {
        this.train_no = train_no;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getTrain_departure() {
        return train_departure;
    }

    public void setTrain_departure(String train_departure) {
        this.train_departure = train_departure;
    }

    public String getTrain_arrival() {
        return train_arrival;
    }

    public void setTrain_arrival(String train_arrival) {
        this.train_arrival = train_arrival;
    }

    public String getTrain_2s() {
        return train_2s;
    }

    public void setTrain_2s(String train_2s) {
        this.train_2s = train_2s;
    }

    public String getTrain_sl() {
        return train_sl;
    }

    public void setTrain_sl(String train_sl) {
        this.train_sl = train_sl;
    }

    public String getTrain_3a() {
        return train_3a;
    }

    public void setTrain_3a(String train_3a) {
        this.train_3a = train_3a;
    }

    public String getTrain_2a() {
        return train_2a;
    }

    public void setTrain_2a(String train_2a) {
        this.train_2a = train_2a;
    }

    public String getTrain_1a() {
        return train_1a;
    }

    public void setTrain_1a(String train_1a) {
        this.train_1a = train_1a;
    }

    public String getTrain_path() {
        return train_path;
    }

    public void setTrain_path(String train_from) {
        this.train_path = train_path;
    }
}
