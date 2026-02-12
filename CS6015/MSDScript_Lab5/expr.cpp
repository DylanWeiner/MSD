#include <string>
#include <sstream>
#include <iostream>
#include "headers/expr.h"
#include "headers/catch.h"


// precedence_t prevPrec = prec_none; /// Sets a starting precedence level.


/**
* \brief Sets Expressions to strings.
 */
std::string Expr::to_string_p() {
    std::stringstream st("");
    this->printExp(st);
    return st.str();
}

/**
* \brief prints non overridden classes in a way that is compatible with pretty print.
 */
void Expr::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
    printExp(ot);
}

/**
* \brief Prints values in an easily readable format.
 */
void Expr::pretty_print(std::ostream &ot) {
    std::streampos pos = ot.tellp();
    bool paren = false;
    pretty_print_at(ot, prec_none, pos, paren);
}

/**
* \brief Outputs out pretty print as a string.
 *\param st is an output string that gets modified by pretty print.
 */
std::string Expr::to_pretty_string() {
    std::stringstream st("");
    this->pretty_print(st);
    return st.str();
}

/**
* \brief Sets VarExpr value.
 */
VarExpr::VarExpr(std::string val) {
    this->val = val;
    
}

/**
* \brief Compares VarExprs
 * \param other is the value to be compared
 * \param Returns false if there is a null value.
 */

bool VarExpr::equals(Expr* e) {
    VarExpr *other = dynamic_cast<VarExpr*>(e);
    if(other == nullptr) {
        return false;
    } ///Checks if the two values are the same.
    return val == other->val;
} /// Checks if the two values are the same.

/**
* \brief Takes a VarExpr and turns it into a readable format
 */
int VarExpr::interp() {
    throw std::runtime_error("no value for variable");
}

/**
* \brief Checks if a Num has a variable in it but returns true because a VarExpr cannot be an int.
 */
bool VarExpr::has_variable() {
    return true;
}

/**
* \brief Substitutes a provided value with an alternative value.
 * \param name is the val we desire to substitute
 * \param newVal is the value we will be replacing name with.
 */
Expr* VarExpr::subst(std::string name, Expr* newVal) {
    if(name == val) {
        return newVal;
    }
    return new VarExpr(name);
}

/**
* \brief Prints a provided VarExpr.
 */
void VarExpr::printExp(std::ostream &ot) {
    ot << val;
}

void VarExpr::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
    ot << val;
}

/**
* \brief Sets Num value.
 */
Num::Num(int val) {
    this->val = val;
}

/**
* \brief Compares Nums
 * \param other is the value to be compared
 * \param Returns false if there is a null value.
 */
bool Num::equals(Expr* e) {
    Num *other = dynamic_cast<Num*>(e);
    if(other == nullptr) {
        return false;
    } ///  Returns false if there is a null value.
    return val == other->val;
} /// Checks if the two values are the same.

/**
* \brief Takes a Num and turns it into a readable format
 */
int Num::interp() {
    return this->val;
}

/**
* \brief Checks if a Num has a variable in it but returns false bc a Num is always an int.
 */
bool Num::has_variable() {
    return false;
}

/**
* \brief Substitutes a provided value with an alternative value.
 * \param name is the val we desire to substitute
 * \param newVal is the value we will be replacing name with.
 */
Expr* Num::subst(std::string name, Expr* newVal) {
    return new Num(val);
}

/**
* \brief Prints a provided number.
 */
void Num::printExp(std::ostream &ot) {
    ot << std::to_string(val);
}

void Num::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
    ot << std::to_string(val);
}

/**
* \brief Sets Num values.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Add::Add(Expr *lhs, Expr *rhs) {
    this->lhs = lhs;
    this->rhs = rhs;
}

/**
* \brief Compares Addition equations
 * \param other is the value to be compared
 * \param Returns false if there is a null value.
 */
bool Add::equals(Expr* e) {
    Add *other = dynamic_cast<Add*>(e);
    if(other == nullptr) {
        return false;
    }
    return lhs->equals(other->lhs) && rhs->equals(other->rhs);
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes an Addition equation and turns it into a readable format
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
int Add::interp() {
    return (this->lhs->interp() + this->rhs->interp());
}

/**
* \brief Checks if the nested values of an addition have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
bool Add::has_variable() {
    return this->lhs->has_variable() || this->rhs->has_variable();
}

/**
* \brief Substitutes a provided value with an alternative value.
 * \param name is the val we desire to substitute
 * \param newVal is the value we will be replacing name with.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Expr* Add::subst(std::string name, Expr* newVal) {
    return new Add(lhs->subst(name, newVal), rhs->subst(name, newVal));
} /// Uses recursion to substitute nested values.

/**
* \brief Prints a provided addition set.
 * \param lhs represents the lefthand side.
 * \param rhs represents the righthand side.
 */
void Add::printExp(std::ostream &ot) {
    ot << "(";
    this->lhs->printExp(ot);
    ot << "+";
    this->rhs->printExp(ot);
    ot << ")";
} /// Uses recursion to substitute nested values.

/**
* \brief Prints a provided addition set in a way that is easily readable.
 * \param prec is the precedence level addition takes
 * \param prevPrec is the precedence level from the preceding value we read.
 * \param lhs represents the lefthand side.
 * \param rhs represents the righthand side.
 */
void Add::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
    if(prec >= prec_add) {
        ot << "(";
    }
    LetExpr* lhs = dynamic_cast<LetExpr*>(lhs);
    if (lhs != nullptr) {
        this->lhs->pretty_print_at(ot, prec_add, pos, true);
    }
    else{
        this->lhs->pretty_print_at(ot, prec_add, pos, false);
    }
    ot << " + ";
    this->rhs->pretty_print_at(ot, prec_none, pos, false);

    if(prec >= prec_add) {
        ot << ")";
    } /// Only places both parentheses if their is a greater precedence preceding this precedence.
}

/**
* \brief Sets Num values.
  * \param lhs is our lefthand side
  * \param rhs is our righthand side
  */
Mult::Mult(Expr *lhs, Expr *rhs) {
    this->lhs = lhs;
    this->rhs = rhs;
}

/**
* \brief Compares Multiplication equations
 * \param other is the value to be compared
 * \param lhs represents the lefthand side.
 * \param rhs represents the righthand side.
 */
bool Mult::equals(Expr* e) {
    Mult *other = dynamic_cast<Mult*>(e);
    if(other == nullptr) {
        return false;
    } /// Returns false if there's a null value.
    return lhs->equals(other->lhs) && rhs->equals(other->rhs);
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Multiplication equation and turns it into a readable format
 */
int Mult::interp() {
    return (lhs->interp() * rhs->interp());
}

/**
* \brief Checks if the nested values of a multiplication have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
bool Mult::has_variable() {
    return this->lhs->has_variable() || this->rhs->has_variable();
}

/**
* \brief Substitutes a provided value with an alternative value.
 * \param name is the val we desire to substitute
 * \param newVal is the value we will be replacing name with.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Expr* Mult::subst(std::string name, Expr* newVal) {
    return new Mult(lhs->subst(name, newVal), rhs->subst(name, newVal));
}

/**
* \brief Prints a provided addition set.
 * \param lhs represents the lefthand side.
 * \param rhs represents the righthand side.
 */
void Mult::printExp(std::ostream &ot) {
    ot << "(";
        this->lhs->to_string_p();
    ot << "*";
        this->rhs->to_string_p();
    ot << ")";
}

/**
* \brief Prints a provided multiplication set in a way that is easily readable.
 * \param prec is the precedence level addition takes
 * \param prevPrec is the precedence level from the preceding value we read.
 */
void Mult::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
    if(prec >= prec_mult) {
        ot << "(";
    }

    this->lhs->pretty_print_at(ot, prec_mult, pos, false);
    ot << " * ";
    LetExpr* rhs = dynamic_cast<LetExpr*>(rhs);
    if (rhs != nullptr &&  prec == prec_add) {
        this->rhs->pretty_print_at(ot, prec_add, pos, true);
    }
    else{
        this->rhs->pretty_print_at(ot, prec_add, pos, false);
    }
    
    if(prec >= prec_mult) {
        ot << ")";
    } /// Only places both parentheses if their is a greater precedence preceding this precedence.
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
* \brief Sets Let values.
  * \param var is our variable
  * \param rhs is our righthand side
  * \param body is our replacement
  */
LetExpr::LetExpr(std::string var, Expr *rhs, Expr *body) {
    this->var = var;
    this->rhs = rhs;
    this->body = body;
}

/**
* \brief Compares Let values
 * \param other is the value to be compared
 * \param var is our variable
 * \param rhs is our righthand side
 * \param body is our replacement
 */
bool LetExpr::equals(Expr* e) {
    LetExpr *other = dynamic_cast<LetExpr*>(e);
    if(other == nullptr) {
        return false;
    } /// Returns false if there's a null value.
    return var==(other->var) && rhs->equals(other->rhs) && body->equals(other->body);
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Let expression and turns it into a readable format
 */
int LetExpr::interp() {
    return body->subst(var, rhs)->interp();
}

/**
* \brief Checks if the nested values of a multiplication have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
bool LetExpr::has_variable() {
    return true;
}

/**
* \brief Substitutes a provided value with an alternative value.
 * \param name is the val we desire to substitute
 * \param newVal is the value we will be replacing name with.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Expr* LetExpr::subst(std::string name, Expr* newVal) {
    return new LetExpr(var, rhs, body->subst(name, newVal));
}

/**
* \brief Prints a provided addition set.
 * \param lhs represents the lefthand side.
 * \param rhs represents the righthand side.
 */
void LetExpr::printExp(std::ostream &ot) {
    ot << "(_let ";
    ot<< var;
    ot << "=";
        this->rhs->printExp(ot);
    ot << " _in ";
        this->body->printExp(ot);
    ot << ")";
}

/**
* \brief Prints a provided multiplication set in a way that is easily readable.
 * \param prec is the precedence level addition takes
 * \param prevPrec is the precedence level from the preceding value we read.
 */
void LetExpr::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
    if(paren) {
        ot << "(";
    }

    std::streampos letPos = ot.tellp();

    ot << "_let ";
    ot << var;
    ot << " = ";
    this->rhs->pretty_print_at(ot, prec_none, letPos, false);
    ot << "\n ";

    // Use pos for indentation.
    
    std::streampos curr_pos = ot.tellp();

    int num_space = letPos - pos;

    for(int i = 0; i < num_space; i++) {
        ot << " ";
    }

    ot << "_in  ";
 
    this->body->pretty_print_at(ot, prec_none, curr_pos, false);
    
    if(paren) {
        ot << ")";
    } /// Only places both parentheses if their is a greater precedence preceding this precedence.
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
}

TEST_CASE("Mult & Add Checks", "Tests for Comparison") {
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

TEST_CASE("Pretty Print", "Tests for Pretty Print Compatability") {
    // CHECK( (new Num(10))->to_string_p() == "10" );
    CHECK ( (new Mult(new Num(1), new Add(new Num(2), new Num(3))))->to_pretty_string() ==  "1 * (2 + 3)" );
    CHECK ( (new Mult(new Mult(new Num(8), new Num(1)), new VarExpr("y")))->to_pretty_string() ==  "(8 * 1) * y" );
    CHECK ( (new Mult(new Add(new Num(3), new Num(5)), new Mult(new Num(6), new Num(1))))->to_pretty_string() ==  "(3 + 5) * 6 * 1" );
    CHECK ( (new Mult(new Mult(new Num(7), new Num(7)), new Add(new Num(9), new Num(2))) )->to_pretty_string() ==  "(7 * 7) * (9 + 2)" );
    CHECK ( (new Mult(new Add(new Num(7), new Num(7)), new Mult(new Num(9), new Num(2))) )->to_pretty_string() ==  "(7 + 7) * 9 * 2" );
}

TEST_CASE("Let Expressions", "Tests for All _let Expressions") {
    CHECK(((new LetExpr("x", new Num(5), (new Add(new LetExpr("y", new Num(3), (new Add(new VarExpr("y"), new Num(2)))), new VarExpr("x")))))->to_string_p()) == "(_let x=5 _in ((_let y=3 _in (y+2))+x))");
    CHECK((new LetExpr("x", new Num(5), new Add(new LetExpr("y", new Num(3), new Add(new VarExpr("y"), new Num(2))), new VarExpr("x"))))->to_pretty_string() == "_let x = 5\n _in  (_let y = 3\n       _in  y + 2) + x");
    CHECK ( (new Add(new LetExpr("x", new Num (5), new LetExpr("y", new Num(3), new Add(new VarExpr("y"), new Num(2)))), new VarExpr("x"))) -> to_pretty_string() == "(_let x = 5\n  _in  _let y = 3\n       _in  y + 2) + x");
    CHECK ( (new Add(new Mult(new Num (2), new LetExpr("x", new Num(5), new Add(new VarExpr("x") , new Num(1)) )), new Num(3) )) ->to_pretty_string()== "2 * (_let x = 5\n      _in  x + 1) + 3");
}
