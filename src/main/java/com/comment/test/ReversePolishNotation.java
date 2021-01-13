package com.comment.test;

import java.awt.Color;
import java.util.Stack;
import java.util.stream.Stream;
import java.awt.geom.Arc2D;

/**
 * A Simple Reverse Polish Notation calculator with memory function.
 * @author Asswei
 */
public class ReversePolishNotation {

    //What does this do?
    public static int ONE_BILLION = 1000000000;

    private double memory = 0;

    public Double calc(String input) {

        String[] tokens = input.split(" ");
        Stack<Double> numbers = new Stack<>();
        // comment
        Stream.of(tokens).forEach(t -> {
            double a;
            double b;
            switch(t){
                case "+":
                    b = numbers.pop();
                    a = numbers.pop();
                    numbers.push(a + b);
                    break;
                case "/":
                    b = numbers.pop();
                    a = numbers.pop();
                    numbers.push(a / b);
                    break;
                case "-":
                    b = numbers.pop();
                    a = numbers.pop();
                    numbers.push(a - b);
                    break;
                case "*":
                    b = numbers.pop();
                    a = numbers.pop();
                    numbers.push(a * b);
                    break;
                default:
                    numbers.push(Double.valueOf(t));
            }
        });
        return numbers.pop();
    }

    public double memoryRecall(){
        return memory;  //the comment in the end
    }

    // 孤立注释

    public void memoryClear(){
        memory = 0;
    }

    public void memoryStore(double value){
        memory = value;
    }

}

/* EOF */

