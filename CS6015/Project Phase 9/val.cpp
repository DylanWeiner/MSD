#include "val.h"
#include "expr.h"

NumVal::NumVal(int val) {
    this->val = val;
}

bool NumVal::equals(Val *e) {
    NumVal *other = dynamic_cast<NumVal*>(e);
    if(other == nullptr) {
        return false;
    } ///  Returns false if there is a null value.
    return val == other->val;
}

Expr* NumVal::to_expr() {
    return new NumExpr(val);
}

Val* NumVal::add_to(Val *other_val) {
    NumVal *other_num = dynamic_cast<NumVal*>(other_val);
    if (other_num == NULL)
        throw std::runtime_error("add of non-number");
    return new NumVal(val + other_num->val);
}

Val* NumVal::mult_with(Val *other_val) {
    NumVal *other_num = dynamic_cast<NumVal*>(other_val);
    if (other_num == NULL)
        throw std::runtime_error("add of non-number");
    return new NumVal(val * other_num->val);   
}

std::string NumVal::num_to_string() {
    return std::to_string(this->val);
}

bool NumVal::is_true() {
    throw std::runtime_error("num cannot be interpreted as bool");
}

BoolVal::BoolVal(bool val) {
    this->val = val;
}

bool BoolVal::equals(Val *e) {
    BoolVal *other = dynamic_cast<BoolVal*>(e);
    if(other == nullptr) {
        return false;
    } ///  Returns false if there is a null value.
    return val == other->val;
}

Expr* BoolVal::to_expr() {
    return new BoolExpr(val);
}

Val* BoolVal::add_to(Val *other_val) {
    BoolVal *other_bool = dynamic_cast<BoolVal*>(other_val);
    if (other_bool == NULL)
        throw std::runtime_error("add of non-number");
    return new BoolVal(val || other_bool->val);
}

Val* BoolVal::mult_with(Val *other_val) {
    BoolVal *other_bool = dynamic_cast<BoolVal*>(other_val);
    if (other_bool == NULL)
        throw std::runtime_error("add of non-number");
    return new BoolVal(val && other_bool->val);   
}

std::string BoolVal::num_to_string() {
    return std::to_string(this->val);
}

bool BoolVal::is_true() {
    return val;
}