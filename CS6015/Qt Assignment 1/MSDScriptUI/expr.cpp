#include <string>
#include <sstream>
#include <iostream>
#include "expr.h"
#include "val.h"
#include "env.h"


// precedence_t prevPrec = prec_none; /// Sets a starting precedence level.


/**
* \brief Sets Expressions to strings.
 */
std::string Expr::to_string_p() {
    std::stringstream st("");
    THIS->printExp(st);
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
    THIS->pretty_print(st);
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

PTR(Val) VarExpr::equals(PTR(Expr) e) {
    PTR(VarExpr) other = CAST(VarExpr)(e);
    if(other == nullptr) {
        return NEW(BoolVal)(false);
    } ///Checks if the two values are the same.
    return NEW(BoolVal)(val == other->val);
} /// Checks if the two values are the same.

/**
* \brief Takes a VarExpr and turns it into a readable format
 */
PTR(Val) VarExpr::interp(PTR(Env) env) {
    return env->lookup(val);
}

/**
* \brief Checks if a NumExpr has a variable in it but returns true because a VarExpr cannot be an int.
 */
// Val* VarExpr::has_variable() {
//     return new BoolVal(true);
// }

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
PTR(Val) NumExpr::equals(PTR(Expr) e) {
    PTR(NumExpr) other = CAST(NumExpr)(e);
    if(other == nullptr) {
        return NEW(BoolVal)(false);
    } ///  Returns false if there is a null value.
    return NEW(BoolVal)(val == other->val);
} /// Checks if the two values are the same.

/**
* \brief Takes a NumExpr and turns it into a readable format
 */
PTR(Val) NumExpr::interp(PTR(Env) env) {
    return NEW(NumVal)(CAST(NumExpr)(THIS)->val);
}

/**
* \brief Checks if a NumExpr has a variable in it but returns false bc a NumExpr is always an int.
 */
// Val* NumExpr::has_variable() {
//     return new BoolVal(false);
// }

/**
* \brief Prints a provided number.
 */
void NumExpr::printExp(std::ostream &ot) {
    ot << CAST(NumVal)(CAST(NumExpr)(THIS)->interp(Env::empty))->to_string();
}

void NumExpr::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
    ot << CAST(NumVal)(CAST(NumExpr)(THIS)->interp(Env::empty))->to_string();
}

/**
* \brief Sets NumExpr values.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
AddExpr::AddExpr(PTR(Expr) lhs, PTR(Expr) rhs) {
    this->lhs = lhs;
    this->rhs = rhs;
}

/**
* \brief Compares Addition equations
 * \param other is the value to be compared
 * \param Returns false if there is a null value.
 */
PTR(Val) AddExpr::equals(PTR(Expr) e) {
    PTR(AddExpr) other = CAST(AddExpr)(e);
    if(other == nullptr) {
        return NEW(BoolVal)(false);
    }
    return NEW(BoolVal)(lhs->equals(other->lhs) && rhs->equals(other->rhs));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes an Addition equation and turns it into a readable format
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
PTR(Val) AddExpr::interp(PTR(Env) env) {
    return (CAST(AddExpr)(THIS)->lhs->interp(env)->add_to(CAST(AddExpr)(THIS)->rhs->interp(env)));
}

/**
* \brief Checks if the nested values of an addition have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
// Val* AddExpr::has_variable() {
//     return new BoolVal(THIS->lhs->has_variable() || THIS->rhs->has_variable());
// }

/**
* \brief Prints a provided addition set.
 * \param lhs represents the lefthand side.
 * \param rhs represents the righthand side.
 */
void AddExpr::printExp(std::ostream &ot) {
    ot << "(";
    CAST(AddExpr)(THIS)->lhs->printExp(ot);
    ot << "+";
    CAST(AddExpr)(THIS)->rhs->printExp(ot);
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
    PTR(LetExpr) lhs = CAST(LetExpr)(CAST(AddExpr)(THIS)->lhs);
    if (lhs != nullptr) {
        CAST(AddExpr)(THIS)->lhs->pretty_print_at(ot, prec_add, pos, true);
    }
    else{
        CAST(AddExpr)(THIS)->lhs->pretty_print_at(ot, prec_add, pos, false);
    }
    ot << " + ";
    CAST(AddExpr)(THIS)->rhs->pretty_print_at(ot, prec_none, pos, false);

    if(prec >= prec_add) {
        ot << ")";
    } /// Only places both parentheses if their is a greater precedence preceding THIS precedence.
}

/**
* \brief Sets NumExpr values.
  * \param lhs is our lefthand side
  * \param rhs is our righthand side
  */
MultExpr::MultExpr(PTR(Expr) lhs, PTR(Expr) rhs) {
    this->lhs = lhs;
    this->rhs = rhs;
}

/**
* \brief Compares Multiplication equations
 * \param other is the value to be compared
 * \param lhs represents the lefthand side.
 * \param rhs represents the righthand side.
 */
PTR(Val) MultExpr::equals(PTR(Expr) e) {
    PTR(MultExpr) other = CAST(MultExpr)(e);
    if(other == nullptr) {
        return NEW(BoolVal)(false);
    } /// Returns false if there's a null value.
    return NEW(BoolVal)(lhs->equals(other->lhs) && rhs->equals(other->rhs));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Multiplication equation and turns it into a readable format
 */
PTR(Val) MultExpr::interp(PTR(Env) env) {
    return (CAST(MultExpr)(THIS)->lhs->interp(env)->mult_with(CAST(MultExpr)(THIS)->rhs->interp(env)));
}

/**
* \brief Checks if the nested values of a multiplication have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
// Val* MultExpr::has_variable() {
//     return new BoolVal(THIS->lhs->has_variable() || THIS->rhs->has_variable());
// }

/**
* \brief Prints a provided addition set.
 * \param lhs represents the lefthand side.
 * \param rhs represents the righthand side.
 */
void MultExpr::printExp(std::ostream &ot) {
    ot << "(";
    CAST(MultExpr)(THIS)->lhs->printExp(ot);
    ot << "*";
    CAST(MultExpr)(THIS)->rhs->printExp(ot);
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

    CAST(MultExpr)(THIS)->lhs->pretty_print_at(ot, prec_mult, pos, false);
    ot << " * ";
    PTR(LetExpr) rhs = CAST(LetExpr)(CAST(MultExpr)(THIS)->rhs);
    if (rhs != nullptr &&  prec == prec_add) {
        CAST(MultExpr)(THIS)->rhs->pretty_print_at(ot, prec_add, pos, true);
    }
    else{
        CAST(MultExpr)(THIS)->rhs->pretty_print_at(ot, prec_add, pos, false);
    }

    if(prec >= prec_mult) {
        ot << ")";
    } /// Only places both parentheses if their is a greater precedence preceding THIS precedence.
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
* \brief Sets Let values.
  * \param var is our variable
  * \param rhs is our righthand side
  * \param body is our replacement
  */
LetExpr::LetExpr(std::string var, PTR(Expr) rhs, PTR(Expr) body) {
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
PTR(Val) LetExpr::equals(PTR(Expr) e) {
    PTR(LetExpr) other = CAST(LetExpr)(e);
    if(other == nullptr) {
        return NEW(BoolVal)(false);
    } /// Returns false if there's a null value.
    return NEW(BoolVal)(var==(other->var) && rhs->equals(other->rhs) && body->equals(other->body));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Let expression and turns it into a readable format
 */
PTR(Val) LetExpr::interp(PTR(Env) env) {
    PTR(Val) rhs_val = rhs->interp(env);
    PTR(Env) new_env = NEW(ExtendedEnv)(var, rhs_val, env);
    return body->interp(new_env);
}

/**
* \brief Checks if the nested values of a multiplication have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
// Val* LetExpr::has_variable() {
//     return new BoolVal(true);
// }

/**
* \brief Prints a provided addition set.
 * \param lhs represents the lefthand side.
 * \param rhs represents the righthand side.
 */
void LetExpr::printExp(std::ostream &ot) {
    ot << "(_let ";
    ot<< var;
    ot << "=";
    CAST(LetExpr)(THIS)->rhs->printExp(ot);
    ot << " _in ";
    CAST(LetExpr)(THIS)->body->printExp(ot);
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
    CAST(LetExpr)(THIS)->rhs->pretty_print_at(ot, prec_none, letPos, false);
    ot << "\n ";

    // Use pos for indentation.

    std::streampos curr_pos = ot.tellp();

    int num_space = letPos - pos;

    for(int i = 0; i < num_space; i++) {
        ot << " ";
    }

    ot << "_in  ";

    CAST(LetExpr)(THIS)->body->pretty_print_at(ot, prec_none, curr_pos, false);

    if(paren) {
        ot << ")";
    } /// Only places both parentheses if their is a greater precedence preceding THIS precedence.
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
PTR(Val) BoolExpr::equals(PTR(Expr) e) {
    PTR(BoolExpr) other = CAST(BoolExpr)(e);
    if(other == nullptr) {
        return NEW(BoolVal)(false);
    } /// Returns false if there's a null value.
    return NEW(BoolVal)(val==(other->val));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Let expression and turns it into a readable format
 */
PTR(Val) BoolExpr::interp(PTR(Env) env) {
    // int n = rhs->interp();
    if(CAST(BoolExpr)(THIS)->val != true && CAST(BoolExpr)(THIS)->val != false) {
        throw std::runtime_error("Not a boolean value!");
    }
    return NEW(BoolVal)(CAST(BoolExpr)(THIS)->val);
}

/**
* \brief Checks if the nested values of a multiplication have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
// Val* BoolExpr::has_variable() {
//     return new BoolVal(false);
// }

/**
* \brief Prints a provided number.
 */
void BoolExpr::printExp(std::ostream &ot) {
    ot << CAST(BoolExpr)(THIS)->interp(Env::empty);
}

void BoolExpr::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
    ot << CAST(BoolExpr)(THIS)->interp(Env::empty);
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
* \brief Sets If values.
  * \param val is our boolean value
  */
IfExpr::IfExpr(PTR(Expr) cond, PTR(Expr) then_body, PTR(Expr) else_body) {
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
PTR(Val) IfExpr::equals(PTR(Expr) e) {
    PTR(IfExpr) other = CAST(IfExpr)(e);
    if(other == nullptr) {
        return NEW(BoolVal)(false);
    } /// Returns false if there's a null value.
    return NEW(BoolVal)(cond->equals(other->cond) && then_body->equals(other->then_body) && else_body->equals(other->else_body));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Let expression and turns it into a readable format
 */
PTR(Val) IfExpr::interp(PTR(Env) env) {
    PTR(Val) cond_val = cond->interp(env);
    if(!CAST(BoolVal)(cond_val)) {
        throw std::runtime_error("condition is not a boolean");
    }
    if(cond_val->is_true()) {
        return CAST(IfExpr)(THIS)->then_body->interp(env);
    } else {
        return CAST(IfExpr)(THIS)->else_body->interp(env);
    }
}

/**
* \brief Checks if the nested values of a multiplication have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
// Val* IfExpr::has_variable() {
//     return new BoolVal(THIS->cond->has_variable() || THIS->then_body->has_variable() || THIS->else_body->has_variable());
// }

/**
* \brief Prints a provided number.
 */
void IfExpr::printExp(std::ostream &ot) {
    ot << "(_if ";
    ot << cond->to_string_p();
    ot << "\n_then ";
    CAST(IfExpr)(THIS)->then_body->printExp(ot);
    ot << "\n_else ";
    CAST(IfExpr)(THIS)->else_body->printExp(ot);
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

    // THIS->then_body->pretty_print_at(ot, prec_none, curr_pos, false);

    // ot << "_else  ";

    // THIS->else_body->pretty_print_at(ot, prec_none, curr_pos, false);

    // if(paren) {
    //     ot << ")";
    // } /// Only places both parentheses if their is a greater precedence preceding THIS precedence.
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
* \brief Sets If values.
  * \param val is our boolean value
  */
EqExpr::EqExpr(PTR(Expr) lhs, PTR(Expr) rhs) {
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
PTR(Val) EqExpr::equals(PTR(Expr) e) {
    PTR(EqExpr) other = CAST(EqExpr)(e);
    if(other == nullptr) {
        return NEW(BoolVal)(false);
    } /// Returns false if there's a null value.
    return NEW(BoolVal)(lhs->equals(other->lhs) && rhs->equals(other->rhs));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Let expression and turns it into a readable format
 */
PTR(Val) EqExpr::interp(PTR(Env) env) {
    // int n = rhs->interp();
    if(CAST(EqExpr)(THIS)->lhs->interp(env)->equals(CAST(EqExpr)(THIS)->rhs->interp(env))) {
        return NEW(BoolVal)(true);
    }
    else {
        return NEW(BoolVal)(false);
    }
}

/**
* \brief Checks if the nested values of a multiplication have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
// Val* EqExpr::has_variable() {
//     return new BoolVal(THIS->lhs->has_variable() || THIS->rhs->has_variable());
// }

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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
* \brief Sets If values.
  * \param val is our boolean value
  */
FunExpr::FunExpr(std::string arg, PTR(Expr) body) {
    this->arg = arg;
    this->body = body;
}

/**
* \brief Compares Let values
 * \param other is the value to be compared
 * \param var is our variable
 * \param rhs is our righthand side
 * \param body is our replacement
 */
PTR(Val) FunExpr::equals(PTR(Expr) e) {
    PTR(FunExpr) other = CAST(FunExpr)(e);
    if(other == nullptr) {
        return NEW(BoolVal)(false);
    } /// Returns false if there's a null value.
    return NEW(BoolVal)(arg==(other->arg) && body->equals(other->body));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Let expression and turns it into a readable format
 */
PTR(Val) FunExpr::interp(PTR(Env) env) {
    return NEW(FunVal)(arg, body, env);
}

/**
* \brief Prints a provided number.
 */
void FunExpr::printExp(std::ostream &ot) {
    ot << "_fun (";
    ot << arg;
    ot << ") ";
    ot << body->to_string_p();
    ot << "";
}

void FunExpr::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
* \brief Sets If values.
  * \param val is our boolean value
  */
CallExpr::CallExpr(PTR(Expr) fun, PTR(Expr) arg) {
    this->fun = fun;
    this->arg = arg;
}

/**
* \brief Compares Let values
 * \param other is the value to be compared
 * \param var is our variable
 * \param rhs is our righthand side
 * \param body is our replacement
 */
PTR(Val) CallExpr::equals(PTR(Expr) e) {
    PTR(CallExpr) other = CAST(CallExpr)(e);
    if(other == nullptr) {
        return NEW(BoolVal)(false);
    } /// Returns false if there's a null value.
    return NEW(BoolVal)(fun->equals(other->fun) && arg->equals(other->arg));
} /// Checks if left and right value of the equations are the same.

/**
* \brief Takes a Let expression and turns it into a readable format
 */
PTR(Val) CallExpr::interp(PTR(Env) env) {
    // int n = body->interp();
    return fun->interp(env)->call(arg->interp(env));
}

/**
* \brief Checks if the nested values of a multiplication have a variable.
 * \param lhs is our lefthand side
 * \param rhs is our righthand side
 */
// Val* EqExpr::has_variable() {
//     return new BoolVal(THIS->lhs->has_variable() || THIS->rhs->has_variable());
// }

/**
* \brief Prints a provided number.
 */
void CallExpr::printExp(std::ostream &ot) {
    ot << "(";
    ot << fun->to_string_p();
    ot << ") (";
    ot << arg->to_string_p();
    ot << ")";
}

void CallExpr::pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) {
}
