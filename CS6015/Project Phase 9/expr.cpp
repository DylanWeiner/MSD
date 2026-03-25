#include <string>
#include <sstream>
#include <iostream>
#include "expr.h"
#include "catch.h"
#include "val.h"


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

Val* VarExpr::equals(Expr* e) {
    VarExpr *other = dynamic_cast<VarExpr*>(e);
    if(other == nullptr) {
        return new BoolVal(false);
    } ///Checks if the two values are the same.
    return new BoolVal(val == other->val);
} /// Checks if the two values are the same.

/**
* \brief Takes a VarExpr and turns it into a readable format
 */
Val* VarExpr::interp() {
    throw std::runtime_error("no value for variable");
}

/**
* \brief Checks if a NumExpr has a variable in it but returns true because a VarExpr cannot be an int.
 */
Val* VarExpr::has_variable() {
    return new BoolVal(true);
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
* \brief Sets NumExpr value.
 */
NumExpr::NumExpr(int val) {
    this->val = val;
}

/**
* \brief Compares Nums
 * \param other is the value to be compared
 * \param Returns false if there is a null value.
 */
Val* NumExpr::equals(Expr* e) {
    NumExpr *other = dynamic_cast<NumExpr*>(e);
    if(other == nullptr) {
        return new BoolVal(false);
    } ///  Returns false if there is a null value.
    return new BoolVal(val == other->val);
} /// Checks if the two values are the same.

/**
* \brief Takes a NumExpr and turns it into a readable format
 */
Val* NumExpr::interp() {
    return new NumVal(this->val);
}

/**
* \brief Checks if a NumExpr has a variable in it but returns false bc a NumExpr is always an int.
 */
Val* NumExpr::has_variable() {
    return new BoolVal(false);
}

/**
* \brief Substitutes a provided value with an alternative value.
 * \param name is the val we desire to substitute
 * \param newVal is the value we will be replacing name with.
 */
Expr* NumExpr::subst(std::string name, Expr* newVal) {
    return new NumExpr(val);
}

/**
* \brief Prints a provided number.
 */
void NumExpr::printExp(std::ostream &ot) {
    ot << this->interp()->num_to_string();
}

void NumExpr::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
    ot << this->interp()->num_to_string();
}

/**
* \brief Sets NumExpr values.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
AddExpr::AddExpr(Expr *lhs, Expr *rhs) {
    this->lhs = lhs;
    this->rhs = rhs;
}

/**
* \brief Compares Addition equations
 * \param other is the value to be compared
 * \param Returns false if there is a null value.
 */
Val* AddExpr::equals(Expr* e) {
    AddExpr *other = dynamic_cast<AddExpr*>(e);
    if(other == nullptr) {
        return new BoolVal(false);
    }
    return new BoolVal(lhs->equals(other->lhs) && rhs->equals(other->rhs));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes an Addition equation and turns it into a readable format
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Val* AddExpr::interp() {
    return (this->lhs->interp()->add_to(this->rhs->interp()));
}

/**
* \brief Checks if the nested values of an addition have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Val* AddExpr::has_variable() {
    return new BoolVal(this->lhs->has_variable() || this->rhs->has_variable());
}

/**
* \brief Substitutes a provided value with an alternative value.
 * \param name is the val we desire to substitute
 * \param newVal is the value we will be replacing name with.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Expr* AddExpr::subst(std::string name, Expr* newVal) {
    return new AddExpr(lhs->subst(name, newVal), rhs->subst(name, newVal));
} /// Uses recursion to substitute nested values.

/**
* \brief Prints a provided addition set.
 * \param lhs represents the lefthand side.
 * \param rhs represents the righthand side.
 */
void AddExpr::printExp(std::ostream &ot) {
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
void AddExpr::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
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
* \brief Sets NumExpr values.
  * \param lhs is our lefthand side
  * \param rhs is our righthand side
  */
MultExpr::MultExpr(Expr *lhs, Expr *rhs) {
    this->lhs = lhs;
    this->rhs = rhs;
}

/**
* \brief Compares Multiplication equations
 * \param other is the value to be compared
 * \param lhs represents the lefthand side.
 * \param rhs represents the righthand side.
 */
Val* MultExpr::equals(Expr* e) {
    MultExpr *other = dynamic_cast<MultExpr*>(e);
    if(other == nullptr) {
        return new BoolVal(false);
    } /// Returns false if there's a null value.
    return new BoolVal(lhs->equals(other->lhs) && rhs->equals(other->rhs));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Multiplication equation and turns it into a readable format
 */
Val* MultExpr::interp() {
    return (this->lhs->interp()->mult_with(this->rhs->interp()));
}

/**
* \brief Checks if the nested values of a multiplication have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Val* MultExpr::has_variable() {
    return new BoolVal(this->lhs->has_variable() || this->rhs->has_variable());
}

/**
* \brief Substitutes a provided value with an alternative value.
 * \param name is the val we desire to substitute
 * \param newVal is the value we will be replacing name with.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Expr* MultExpr::subst(std::string name, Expr* newVal) {
    return new MultExpr(lhs->subst(name, newVal), rhs->subst(name, newVal));
}

/**
* \brief Prints a provided addition set.
 * \param lhs represents the lefthand side.
 * \param rhs represents the righthand side.
 */
void MultExpr::printExp(std::ostream &ot) {
    ot << "(";
        this->lhs->printExp(ot);
    ot << "*";
        this->rhs->printExp(ot);
    ot << ")";
}

/**
* \brief Prints a provided multiplication set in a way that is easily readable.
 * \param prec is the precedence level addition takes
 * \param prevPrec is the precedence level from the preceding value we read.
 */
void MultExpr::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
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
Val* LetExpr::equals(Expr* e) {
    LetExpr *other = dynamic_cast<LetExpr*>(e);
    if(other == nullptr) {
        return new BoolVal(false);
    } /// Returns false if there's a null value.
    return new BoolVal(var==(other->var) && rhs->equals(other->rhs) && body->equals(other->body));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Let expression and turns it into a readable format
 */
Val* LetExpr::interp() {
    // int n = rhs->interp();
    return body->subst(var, rhs)->interp();
}

/**
* \brief Checks if the nested values of a multiplication have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Val* LetExpr::has_variable() {
    return new BoolVal(true);
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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
* \brief Sets Bool values.
  * \param val is our boolean value
  */
BoolExpr::BoolExpr(bool val) {
    this->val = val;
}

/**
* \brief Compares Let values
 * \param other is the value to be compared
 * \param var is our variable
 * \param rhs is our righthand side
 * \param body is our replacement
 */
Val* BoolExpr::equals(Expr* e) {
    BoolExpr *other = dynamic_cast<BoolExpr*>(e);
    if(other == nullptr) {
        return new BoolVal(false);
    } /// Returns false if there's a null value.
    return new BoolVal(val==(other->val));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Let expression and turns it into a readable format
 */
Val* BoolExpr::interp() {
    // int n = rhs->interp();
    return new BoolVal(this->val);
}

/**
* \brief Checks if the nested values of a multiplication have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Val* BoolExpr::has_variable() {
    return new BoolVal(false);
}

/**
* \brief Substitutes a provided value with an alternative value.
 * \param name is the val we desire to substitute
 * \param newVal is the value we will be replacing name with.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Expr* BoolExpr::subst(std::string name, Expr* newVal) {
    return new BoolExpr(this->val);
}


/**
* \brief Prints a provided number.
 */
void BoolExpr::printExp(std::ostream &ot) {
    ot << this->interp();
}

void BoolExpr::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
    ot << this->interp();
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
* \brief Sets If values.
  * \param val is our boolean value
  */
IfExpr::IfExpr(Expr *cond, Expr *then_body, Expr *else_body) {
    this->cond = cond;
    this->then_body = then_body;
    this->else_body = else_body;
}

/**
* \brief Compares Let values
 * \param other is the value to be compared
 * \param var is our variable
 * \param rhs is our righthand side
 * \param body is our replacement
 */
Val* IfExpr::equals(Expr* e) {
    IfExpr *other = dynamic_cast<IfExpr*>(e);
    if(other == nullptr) {
        return new BoolVal(false);
    } /// Returns false if there's a null value.
    return new BoolVal(cond->equals(other->cond) && then_body->equals(other->then_body) && else_body->equals(other->else_body));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Let expression and turns it into a readable format
 */
Val* IfExpr::interp() {
    // int n = rhs->interp();
    // EqExpr* cond_eq = dynamic_cast<EqExpr*>(cond);
    if(cond != nullptr && cond->interp()->equals(new BoolVal(true))) {
        return this->then_body->interp();
    }
    else {
        return this->else_body->interp();
    }
}

/**
* \brief Checks if the nested values of a multiplication have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Val* IfExpr::has_variable() {
    return new BoolVal(this->cond->has_variable() || this->then_body->has_variable() || this->else_body->has_variable());
}

/**
* \brief Substitutes a provided value with an alternative value.
 * \param name is the val we desire to substitute
 * \param newVal is the value we will be replacing name with.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Expr* IfExpr::subst(std::string name, Expr* newVal) {
    return new IfExpr(this->cond->subst(name, newVal), this->then_body->subst(name, newVal), this->else_body->subst(name, newVal));
}

/**
* \brief Prints a provided number.
 */
void IfExpr::printExp(std::ostream &ot) {
    ot << "(_if ";
    ot << cond->to_string_p();
    ot << "\n_then ";
        this->then_body->printExp(ot);
    ot << "\n_else ";
        this->else_body->printExp(ot);
    ot << ")";
}

void IfExpr::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
    // if(paren) {
    //     ot << "(";
    // }

    // std::streampos letPos = ot.tellp();

    // ot << "_if ";
    // ot << cond->to_string_p();
    // ot << "\n ";

    // // Use pos for indentation.
    
    // std::streampos curr_pos = ot.tellp();

    // int num_space = letPos - pos;

    // for(int i = 0; i < num_space; i++) {
    //     ot << " ";
    // }

    // ot << "_then  ";
 
    // this->then_body->pretty_print_at(ot, prec_none, curr_pos, false);
    
    // ot << "_else  ";
 
    // this->else_body->pretty_print_at(ot, prec_none, curr_pos, false);

    // if(paren) {
    //     ot << ")";
    // } /// Only places both parentheses if their is a greater precedence preceding this precedence.
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
* \brief Sets If values.
  * \param val is our boolean value
  */
EqExpr::EqExpr(Expr *lhs, Expr *rhs) {
    this->lhs = lhs;
    this->rhs = rhs;
}

/**
* \brief Compares Let values
 * \param other is the value to be compared
 * \param var is our variable
 * \param rhs is our righthand side
 * \param body is our replacement
 */
Val* EqExpr::equals(Expr* e) {
    EqExpr *other = dynamic_cast<EqExpr*>(e);
    if(other == nullptr) {
        return new BoolVal(false);
    } /// Returns false if there's a null value.
    return new BoolVal(lhs->equals(other->lhs) && rhs->equals(other->rhs));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Let expression and turns it into a readable format
 */
Val* EqExpr::interp() {
    // int n = rhs->interp();
    if(this->lhs->interp()->equals(this->rhs->interp())) {
        return new BoolVal(true);
    }
    else {
        return new BoolVal(false);
    }
}

/**
* \brief Checks if the nested values of a multiplication have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Val* EqExpr::has_variable() {
    return new BoolVal(this->lhs->has_variable() || this->rhs->has_variable());
}

/**
* \brief Substitutes a provided value with an alternative value.
 * \param name is the val we desire to substitute
 * \param newVal is the value we will be replacing name with.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
Expr* EqExpr::subst(std::string name, Expr* newVal) {
    return new EqExpr(this->lhs->subst(name, newVal), this->rhs->subst(name, newVal));
}

/**
* \brief Prints a provided number.
 */
void EqExpr::printExp(std::ostream &ot) {
    ot << "(";
    ot << lhs->to_string_p();
    ot << " = ";
    ot << rhs->to_string_p();
    ot << ")";
}

void EqExpr::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
}

TEST_CASE("NumExpr Equals", "Tests for Comparison") {
    VarExpr *val1 = new VarExpr("x");
    VarExpr *val2 = new VarExpr("y");
    NumExpr *numVal1 = new NumExpr(1);
    NumExpr *numVal2 = new NumExpr(2);
    NumExpr *numVal3 = new NumExpr(3);
    NumExpr *numVal4 = new NumExpr(-3);
    AddExpr *sumVal1 = new AddExpr(numVal2, numVal3);
    AddExpr *sumVal2 = new AddExpr(numVal3, numVal2);
    MultExpr *multVal1 = new MultExpr(numVal3, numVal2);
    MultExpr *multVal2 = new MultExpr(numVal2, numVal3);

    CHECK(val1->equals(val2) == new BoolVal(false)); // Returns different strings return false.
    CHECK(val1->equals(val1) == new BoolVal(true)); // Tests same strings return true.
    CHECK(val1->equals(NULL) == new BoolVal(false)); // Tests null value returns false.
    CHECK(numVal1->equals(val1) == new BoolVal(false)); // Returns different ints return false.
    CHECK(sumVal1->equals(sumVal1)== new BoolVal(true)); // Returns same lhs and rhs in addition return true.
    CHECK(sumVal1->equals(sumVal2)== new BoolVal(false)); // Returns different lhs and rhs in addition return false.
    CHECK(multVal1->equals(multVal1)== new BoolVal(true)); // Returns same lhs and rhs in multiplication return true.
    CHECK(multVal2->equals(multVal1)== new BoolVal(false)); // Returns different lhs and rhs in multiplication return false.
    CHECK(sumVal2->equals(multVal1)== new BoolVal(false)); // Returns same values with a different sign return false.
    CHECK(multVal2->equals(sumVal1)== new BoolVal(false)); // Returns same values with a different modifier return false.
    CHECK(numVal4->equals(numVal3)== new BoolVal(false)); // Returns same values with a different sign return false.

    CHECK_THROWS_WITH( (new VarExpr("x"))->interp(), "no value for variable" );
}

TEST_CASE("Mult & Add Checks", "Tests for Comparison") {
    CHECK((new MultExpr(new NumExpr(3), new NumExpr(2)))
        ->interp()->equals(new NumVal(6)) );
    CHECK((new MultExpr(new NumExpr(3), new NumExpr(2)))
        ->interp()->equals(new NumVal(6)) );

    CHECK((new AddExpr(new VarExpr("x"), new NumExpr(1)))->has_variable() == new BoolVal(true));
    CHECK((new MultExpr(new NumExpr(2), new NumExpr(1)))->has_variable() == new BoolVal(false));

    CHECK( (new AddExpr(new VarExpr("x"), new NumExpr(7)))
       ->subst("x", new VarExpr("y"))
       ->equals(new AddExpr(new VarExpr("y"), new NumExpr(7))) );

    CHECK( (new VarExpr("x"))
       ->subst("x", new AddExpr(new VarExpr("y"),new NumExpr(7)))
       ->equals(new AddExpr(new VarExpr("y"),new NumExpr(7))) );

    CHECK((new VarExpr("x")) -> subst("x", new AddExpr(new NumExpr(3),new NumExpr(5))) -> equals(new AddExpr(new NumExpr(3),new NumExpr(5))));

    CHECK( (new AddExpr(new VarExpr("x"), new NumExpr(9)))
        -> subst("x", (new AddExpr(new NumExpr(2), new NumExpr(3))))
        -> interp() ->equals(new NumVal(14))
        );

    CHECK( (new AddExpr(new VarExpr("x"), new NumExpr(9)))
        -> subst("x", (new AddExpr(new NumExpr(2), new NumExpr(3))))
        -> equals(new AddExpr(new AddExpr(new NumExpr(2), new NumExpr(3)), new NumExpr(9)))
        == new BoolVal(true));

    CHECK( (new AddExpr(new VarExpr("x"), new NumExpr(9)))
        -> subst("x", (new AddExpr(new NumExpr(2), new NumExpr(3))))
        -> equals(new AddExpr(new AddExpr(new NumExpr(2), new NumExpr(3)), new NumExpr(9))) == new BoolVal(true));

    CHECK( (new AddExpr(new VarExpr("x"), new NumExpr(9)))
        -> subst("x", (new AddExpr(new NumExpr(2), new NumExpr(3))))
        -> interp()->equals(new NumVal(14)) );

    CHECK( (new MultExpr(new VarExpr("x"), new NumExpr(9)))
        -> subst("x", (new MultExpr(new NumExpr(4), new NumExpr(3))))
        -> interp()->equals(new NumVal(108)) );

    CHECK( (new MultExpr(new VarExpr("x"), new NumExpr(9)))
        -> subst("x", (new MultExpr(new NumExpr(4), new NumExpr(3))))
        -> equals(new MultExpr(new MultExpr(new NumExpr(4), new NumExpr(3)), new NumExpr(9))) == new BoolVal(true));
}

TEST_CASE("Pretty Print", "Tests for Pretty Print Compatability") {
    // CHECK( (new NumExpr(10))->to_string_p() == "10" );
    CHECK ( (new MultExpr(new NumExpr(1), new AddExpr(new NumExpr(2), new NumExpr(3))))->to_pretty_string() ==  "1 * (2 + 3)" );
    CHECK ( (new MultExpr(new MultExpr(new NumExpr(8), new NumExpr(1)), new VarExpr("y")))->to_pretty_string() ==  "(8 * 1) * y" );
    CHECK ( (new MultExpr(new AddExpr(new NumExpr(3), new NumExpr(5)), new MultExpr(new NumExpr(6), new NumExpr(1))))->to_pretty_string() ==  "(3 + 5) * 6 * 1" );
    CHECK ( (new MultExpr(new MultExpr(new NumExpr(7), new NumExpr(7)), new AddExpr(new NumExpr(9), new NumExpr(2))) )->to_pretty_string() ==  "(7 * 7) * (9 + 2)" );
    CHECK ( (new MultExpr(new AddExpr(new NumExpr(7), new NumExpr(7)), new MultExpr(new NumExpr(9), new NumExpr(2))) )->to_pretty_string() ==  "(7 + 7) * 9 * 2" );
}

TEST_CASE("Let Expressions", "Tests for All _let Expressions") {
    CHECK(((new LetExpr("x", new NumExpr(5), (new AddExpr(new LetExpr("y", new NumExpr(3), (new AddExpr(new VarExpr("y"), new NumExpr(2)))), new VarExpr("x")))))->to_string_p()) == "(_let x=5 _in ((_let y=3 _in (y+2))+x))");
    CHECK((new LetExpr("x", new NumExpr(5), new AddExpr(new LetExpr("y", new NumExpr(3), new AddExpr(new VarExpr("y"), new NumExpr(2))), new VarExpr("x"))))->to_pretty_string() == "_let x = 5\n_in  (_let y = 3\n      _in  y + 2) + x");
    CHECK ( (new AddExpr(new LetExpr("x", new NumExpr (5), new LetExpr("y", new NumExpr(3), new AddExpr(new VarExpr("y"), new NumExpr(2)))), new VarExpr("x"))) -> to_pretty_string() == "(_let x = 5\n  _in  _let y = 3\n       _in  y + 2) + x");
    CHECK ( (new AddExpr(new MultExpr(new NumExpr (2), new LetExpr("x", new NumExpr(5), new AddExpr(new VarExpr("x") , new NumExpr(1)) )), new NumExpr(3) )) ->to_pretty_string()== "2 * (_let x = 5\n      _in  x + 1) + 3");
}