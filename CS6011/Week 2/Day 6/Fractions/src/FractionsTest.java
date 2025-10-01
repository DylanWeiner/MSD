import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Collections;

class FractionsTest {
    @Test
    public void TestZero() {
        Fractions f1 = new Fractions(1,2 );
        Fractions f2 = new Fractions( 1, 3 );

        Fractions f3 = f1.times( f2 );

        Fractions f4 = f1.plus( f2 );
        Assertions.assertEquals( f4.toString(), "5/6" );

        Fractions f5 = new Fractions(-1,7 );
        Fractions f6 = new Fractions( 2, -3 );

        Assertions.assertEquals( f6.toString(), "-2/3" );

        Fractions f7 = f5.reciprocal();
        Assertions.assertEquals( f7.toString(), "-7/1" );

        Fractions f8 = f5.dividedBy( f6 );
        Assertions.assertEquals( f8.toString(), "3/14" );

        Fractions f9 = new Fractions(2,-4 );
        Fractions f10 = new Fractions( 4, -2 );
        Fractions f11 = f10.dividedBy( f9 );
        Assertions.assertEquals( f11.toString(), "4/1" );

        Assertions.assertEquals( f1.toDouble(), 0.5 );
        Assertions.assertEquals( f11.toDouble(), 4.0 );


    }

    //This will test the denominator cannot be zero:
//    @Test
//    public void TestOne() {
//        Fractions f1 = new Fractions(1,0 );
//        System.out.println( f1.toDouble() );
//    }

    @Test
    public void TestTwo() {
        ArrayList<Fractions> frac = new ArrayList<>();
        frac.add(new Fractions(2,-3));
        frac.add(new Fractions(-1,7));
        frac.add(new Fractions(2,-4));

        Collections.sort(frac);

        for (int i = 0; i < frac.size(); i++) {
            System.out.println(frac.get(i));
        }
    }
}