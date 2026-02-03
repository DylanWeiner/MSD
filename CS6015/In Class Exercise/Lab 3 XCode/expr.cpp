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

int VarExpr::interp() {
    throw std::runtime_error("no value for variable");
}

bool VarExpr::has_variable() {
    return true;
}

Expr* VarExpr::subst(std::string name, Expr* newVal) {
    if(name == val) {
        return newVal;
    }
    return new VarExpr(name);
}

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

int Num::interp() {
    return this->val;
}

bool Num::has_variable() {
    return false;
}

Expr* Num::subst(std::string name, Expr* newVal) {
    return new Num(val);
}

Add::Add(Expr *lhs, Expr *rhs) {
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

int Add::interp() {
    return (this->lhs->interp() + this->rhs->interp());
}

bool Add::has_variable() {
    return this->lhs->has_variable() || this->rhs->has_variable();
}

Expr* Add::subst(std::string name, Expr* newVal) {
    return new Add(lhs->subst(name, newVal), rhs->subst(name, newVal));
}

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

int Mult::interp() {
    return (lhs->interp() * rhs->interp());
}

bool Mult::has_variable() {
    return this->lhs->has_variable() || this->rhs->has_variable();
}

Expr* Mult::subst(std::string name, Expr* newVal) {
    return new Mult(lhs->subst(name, newVal), rhs->subst(name, newVal));
}

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

    CHECK_THROWS_WITH( (new VarExpr("x"))->interp(), "no value for variable" );

    CHECK((new Mult(new Num(3), new Num(2)))
        ->interp()==6 );
    CHECK((new Mult(new Num(3), new Num(2)))
        ->interp()==6 );

    CHECK((new Add(new VarExpr("x"), new Num(1)))->has_variable() == true );
    CHECK((new Mult(new Num(2), new Num(1)))->has_variable() == false );

    CHECK( (new Add(new VarExpr("x"), new Num(7)))
       ->subst("x", new VarExpr("y"))
       ->equals(new Add(new VarExpr("y"), new Num(7))) );

    CHECK( (new VarExpr("x"))
       ->subst("x", new Add(new VarExpr("y"),new Num(7)))
       ->equals(new Add(new VarExpr("y"),new Num(7))) );

    CHECK((new VarExpr("x")) -> subst("x", new Add(new Num(3),new Num(5))) -> equals(new Add(new Num(3),new Num(5))));

    CHECK( (new Add(new VarExpr("x"), new Num(9)))
        -> subst("x", (new Add(new Num(2), new Num(3))))
        -> interp() == 14
        );

    CHECK( (new Add(new VarExpr("x"), new Num(9)))
        -> subst("x", (new Add(new Num(2), new Num(3))))
        -> equals(new Add(new Add(new Num(2), new Num(3)), new Num(9)))
        == true);

    CHECK( (new Add(new VarExpr("x"), new Num(9)))
        -> subst("x", (new Add(new Num(2), new Num(3))))
        -> equals(new Add(new Add(new Num(2), new Num(3)), new Num(9))) == true);

    CHECK( (new Add(new VarExpr("x"), new Num(9)))
        -> subst("x", (new Add(new Num(2), new Num(3))))
        -> interp() == 14 );

    CHECK( (new Mult(new VarExpr("x"), new Num(9)))
        -> subst("x", (new Mult(new Num(4), new Num(3))))
        -> interp() == 108 );

    CHECK( (new Mult(new VarExpr("x"), new Num(9)))
        -> subst("x", (new Mult(new Num(4), new Num(3))))
        -> equals(new Mult(new Mult(new Num(4), new Num(3)), new Num(9))) == true);
}