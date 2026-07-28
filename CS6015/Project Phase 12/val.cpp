#include "val.h"
#include "expr.h"
#include "env.h"

std::string Val::to_string() {
    std::stringstream st("");
    THIS->printExp(st);
    return st.str();
}

NumVal::NumVal(int val) {
    this->val = val;
}

bool NumVal::equals(PTR(Val) e) {
    PTR(NumVal) other = CAST(NumVal)(e);
    if(other == nullptr) {
        return false;
    } ///  Returns false if there is a null value.
    return val == other->val;
}

PTR(Expr) NumVal::to_expr() {
    return NEW(NumExpr)(val);
}

PTR(Val) NumVal::add_to(PTR(Val) other_val) {
    PTR(NumVal) other_num = CAST(NumVal)(other_val);
    if (other_num == nullptr)
        throw std::runtime_error("add of non-number");
    return NEW(NumVal)(val + other_num->val);
}

PTR(Val) NumVal::mult_with(PTR(Val) other_val) {
    PTR(NumVal) other_num = CAST(NumVal)(other_val);
    if (other_num == nullptr)
        throw std::runtime_error("add of non-number");
    return NEW(NumVal)(val * other_num->val);   
}



bool NumVal::is_true() {
    throw std::runtime_error("num cannot be interpreted as bool");
}

PTR(Val) NumVal::call(PTR(Val) actual_arg) {
    throw std::runtime_error("number cannot be called");
}

void NumVal::printExp(std::ostream &ot) {
    ot << val;
}

BoolVal::BoolVal(bool val) {
    this->val = val;
}

bool BoolVal::equals(PTR(Val) e) {
    PTR(BoolVal) other = CAST(BoolVal)(e);
    if(other == nullptr) {
        return false;
    } ///  Returns false if there is a null value.
    return val == other->val;
}

PTR(Expr) BoolVal::to_expr() {
    return NEW(BoolExpr)(val);
}

PTR(Val) BoolVal::add_to(PTR(Val) other_val) {
    PTR(BoolVal) other_bool = CAST(BoolVal)(other_val);
    if (other_bool == nullptr)
        throw std::runtime_error("add of non-number");
    return NEW(BoolVal)(val || other_bool->val);
}

PTR(Val) BoolVal::mult_with(PTR(Val) other_val) {
    PTR(BoolVal) other_bool = CAST(BoolVal)(other_val);
    if (other_bool == nullptr)
        throw std::runtime_error("add of non-number");
    return NEW(BoolVal)(val && other_bool->val);   
}

bool BoolVal::is_true() {
    return val;
}

PTR(Val) BoolVal::call(PTR(Val) actual_arg) {
    throw std::runtime_error("boolean cannot be called");
}

void BoolVal::printExp(std::ostream &ot) {
    ot << val;
}

FunVal::FunVal(std::string arg, PTR(Expr) body, PTR(Env) env) {
    this->arg = arg;
    this->body = body;
    this->env = env;
}

bool FunVal::equals(PTR(Val) e) {
    PTR(FunVal) other = CAST(FunVal)(e);
    if(other == nullptr) {
        return false;
    } ///  Returns false if there is a null value.
    return arg == other->arg && body == other->body;
}

PTR(Expr) FunVal::to_expr() {
    return NEW(FunExpr)(arg, body);
}

PTR(Val) FunVal::add_to(PTR(Val) other_val) {
    throw std::runtime_error("function cannot be added");
}

PTR(Val) FunVal::mult_with(PTR(Val) other_val) {
    throw std::runtime_error("function cannot be multiplied");
}

void FunVal::printExp(std::ostream &ot) {
    ot << to_expr()->to_string_p();
}

bool FunVal::is_true() {
    throw std::runtime_error("function cannot be interpreted as bool");
}

PTR(Val) FunVal::call(PTR(Val) actual_arg) {
    PTR(Env) new_env = NEW(ExtendedEnv)(arg, actual_arg, env);
    return body->interp(new_env);
}