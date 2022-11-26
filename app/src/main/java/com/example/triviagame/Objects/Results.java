package com.example.triviagame.Objects;

import java.util.ArrayList;

public class Results {

    private int response_code;
    private ArrayList<Question> results;

    public Results(int response_code, ArrayList<Question> results) {
        this.response_code = response_code;
        this.results = results;
    }

    public Results(){
        results = new ArrayList<>();
    }

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public ArrayList<Question> getResults() {
        return results;
    }

    public void setResults(ArrayList<Question> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "MyJson{" +
                "response_code=" + response_code +
                ", results=" + results.toString() +
                '}';
    }
}
