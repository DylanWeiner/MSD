#include "expr.h"
#include "catch.h"

VarExpr::VarExpr(std::string val) {
    this->val = val;
    
} // Sets Num value.

bool VarExpr::equals(Expr* e) {
    VarExpr *other = dynamic_cast<VarExpr*>(e);
    if(other == nullptr) {
        return false;
    } //  Returns false if there is a null value.
    return val == other->val;
} // Checks if the two values are the same.

Num::Num(int val) {
    this->val = val;
} // Sets Num value.

bool Num::equals(Expr* e) {
    Num *other = dynamic_cast<Num*>(e);
    if(other == nullptr) {
        return false;
    } //  Returns false if there is a null value.
    return val == other->val;
} // Checks if the two values are the same.

Add::Add(Num *lhs, Num *rhs) {
    this->lhs = lhs;
    this->rhs = rhs;
}  // Sets Num values.

bool Add::equals(Expr* e) {
    Add *other = dynamic_cast<Add*>(e);
    if(other == nullptr) {
        return false;
    }
    return lhs->equals(other->lhs) && rhs->equals(other->rhs);
} // Checks if left and right value of the equations are the same.

Mult::Mult(Expr *lhs, Expr *rhs) {
    this->lhs = lhs;
    this->rhs = rhs;
} // Sets Num values.

bool Mult::equals(Expr* e) {
    Mult *other = dynamic_cast<Mult*>(e);
    if(other == nullptr) {
        return false;
    }
    return lhs->equals(other->lhs) && rhs->equals(other->rhs);
} // Checks if left and right value of the equations are the same.


TEST_CASE("Num Equals", "Tests for Comparison") {
    VarExpr *val1 = new VarExpr("x");
    VarExpr *val2 = new VarExpr("y");
    Num *numVal1 = new Num(1);
    Num *numVal2 = new Num(2);
    Num *numVal3 = new Num(3);
    Num *numVal4 = new Num(-3);
    Add *sumVal1 = new Add(numVal2, numVal3);
    Add *sumVal2 = new Add(numVal3, numVal2);
    Mult *multVal1 = new Mult(numVal3, numVal2);
    Mult *multVal2 = new Mult(numVal2, numVal3);



    CHECK(val1->equals(val2) == false); // Returns different strings return false.
    CHECK(val1->equals(val1) == true ); // Tests same strings return true.
    CHECK(val1->equals(NULL) == false); // Tests null value returns false.
    CHECK(numVal1->equals(val1) == false); // Returns different ints return false.
    CHECK(sumVal1->equals(sumVal1)== true); // Returns same lhs and rhs in addition return true.
    CHECK(sumVal1->equals(sumVal2)== false); // Returns different lhs and rhs in addition return false.
    CHECK(multVal1->equals(multVal1)== true); // Returns same lhs and rhs in multiplication return true.
    CHECK(multVal2->equals(multVal1)== false); // Returns different lhs and rhs in multiplication return false.
    CHECK(sumVal2->equals(multVal1)== false); // Returns same values with a different sign return false.
    CHECK(multVal2->equals(sumVal1)== false); // Returns same values with a different modifier return false.
    CHECK(numVal4->equals(numVal3)== false); // Returns same values with a different sign return false.
}