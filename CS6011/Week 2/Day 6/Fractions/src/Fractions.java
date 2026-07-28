import java.io.Serializable;

public class Fractions implements Comparable<Fractions> {
    private long numerator;
    private long denominator;

    Fractions() {
        numerator = 0;
        denominator = 1;
    }

    public void reduce() {
        long val = GCD();
        numerator /= val;
        denominator /= val;
    }

    public void normalizeSign() {
        if(denominator < 0) {
            numerator *= -1;
            denominator *= -1;
        }
    }

    public void NonZero() throws ArithmeticException {
            if(denominator == 0 ) {
                throw new ArithmeticException("A number cannot be divided by zero!");
            }
        }

    public Fractions(int numerator, int denominator) {
        try {
            this.numerator = numerator;
            this.denominator = denominator;
            NonZero();
            reduce();
            normalizeSign();
        }
        catch (ArithmeticException e) {
            System.out.println("That won't work!");
            System.out.println("Error Message: " + e.getMessage());
            System.exit(-1);
        }
    }

    public Fractions plus(Fractions other) {
        Fractions result = new Fractions();
        result.numerator = (this.numerator* other.denominator) + (other.numerator * this.denominator);
        result.denominator = this.denominator * other.denominator;
        reduce();
        return result;
    }

    public Fractions minus(Fractions other) {
        Fractions result = new Fractions();
        result.numerator = (this.numerator * other.denominator) - (other.numerator * this.denominator);
        result.denominator = this.denominator * other.denominator;
        reduce();
        return result;
    }

    public Fractions times(Fractions other) {
        Fractions result = new Fractions();
        result.numerator = this.numerator * other.numerator;
        result.denominator = this.denominator * other.denominator;
        reduce();
        return result;
    }

    public Fractions dividedBy(Fractions other) {
        Fractions result = new Fractions();
        result.numerator = this.numerator * other.denominator;
        result.denominator = this.denominator * other.numerator;
        result.reduce();
        result.normalizeSign();
        return result;
    }

    public Fractions reciprocal() {
        Fractions result = new Fractions();
        result.numerator = this.denominator;
        result.denominator = this.numerator;
        result.normalizeSign();
        return result;
    }

    @Override
    public int compareTo(Fractions other) {
        int result = 0;
        if(this.toDouble() < other.toDouble()) {
            result = -1;
        }
        else if(this.toDouble() > other.toDouble()) {
            result = 1;
        }
        return result;
    }


    double toDouble() {
        double d = denominator;
        double n = numerator;
        return n / d;
    }

    public String toString() {
        return numerator + "/" + denominator;
    }

    public long GCD() {
        long gcd = numerator;
        long remainder = denominator;
        while (remainder != 0) {
            long temp = remainder;
            remainder = gcd % remainder;
            gcd = temp;
        }
        return gcd;
    }
}

