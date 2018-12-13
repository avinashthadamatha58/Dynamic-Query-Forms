package org.glasser.swing.text;

import javax.swing.text.*;

public class NumericDocument extends ValidatingDocument 
{  

    protected double minValue = Double.MIN_VALUE;

    protected double maxValue = Double.MAX_VALUE;

    protected boolean floatingPointAllowed = false;

    public void setMinValue(double minValue) {
        if(minValue > 0) throw new IllegalArgumentException("setMinValue(): minValue argument must be 0 or less.");
        this.minValue = minValue;
    }

    public void setMaxValue(double maxValue) {
        if(maxValue < 0) throw new IllegalArgumentException("setMaxValue(): maxValue argument must be 0 or greater.");
        this.maxValue = maxValue;
    }

    public void setFloatingPointAllowed(boolean floatingPointAllowed) {
        this.floatingPointAllowed = floatingPointAllowed;
    }



    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public boolean isFloatingPointAllowed() {
        return floatingPointAllowed;
    }


    protected boolean isValidValue(String s) {
        s = s.trim();

        // check for the possibility that this is the the
        // first character of a negative value (the minus sign.)
        if(s.equals("-") && minValue < 0) return true;

        try {
            if(floatingPointAllowed) {
                double d = Double.parseDouble(s);
                if(d >= minValue && d <= maxValue) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                long l = Long.parseLong(s);
                if(l >= minValue && l <=maxValue) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        catch(NumberFormatException ex) {
            return false;
        }
    }
                

}

