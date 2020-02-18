package org.chaos.spikyfy.spiky.meta;

import java.util.Random;

public class RandomCountry {
	private String[] countries;
    private double[] probs;
    private Random rand;
    
    public RandomCountry(String[] countries, double[] probs){
        this.countries = countries;
        this.probs = probs;
        rand = new Random();
    }
    
    public String random(){
        double p = rand.nextDouble();
        double sum = 0.0;
        int i = 0;
        while(sum < p){
            sum += probs[i];
            i++;
        }
        return countries[i-1];
    }
}
