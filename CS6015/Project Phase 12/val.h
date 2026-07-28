#pragma once
#include <string>
#include "pointer.h"

class Expr;
class Env;

CLASS(Val) {
public:
    virtual ~Val() { };
    virtual bool equals(PTR(Val) e) = 0;
    virtual PTR(Expr) to_expr() = 0;
    virtual PTR(Val) add_to(PTR(Val) other_val) = 0;
    virtual PTR(Val) mult_with(PTR(Val) other_val) = 0;
    virtual void printExp(std::ostream &ot) = 0;
    std::string to_string();
    virtual bool is_true() = 0;
    virtual PTR(Val) call(PTR(Val) actual_arg) = 0;
};

class NumVal : public Val {
public:
    int val;
    NumVal(int val);
    bool equals(PTR(Val) e) override;
    PTR(Expr) to_expr() override;
    PTR(Val) add_to(PTR(Val) other_val) override;
    PTR(Val) mult_with(PTR(Val) other_val) override;
    bool is_true() override;
    PTR(Val) call(PTR(Val) actual_arg) override;
    void printExp(std::ostream &ot) override;
};

class BoolVal : public Val {
public:
    bool val;
    BoolVal(bool val);
    bool equals(PTR(Val) e) override;
    PTR(Expr) to_expr() override;
    PTR(Val) add_to(PTR(Val) other_val) override;
    PTR(Val) mult_with(PTR(Val) other_val) override;
    bool is_true() override;
    PTR(Val) call(PTR(Val) actual_arg) override;
    void printExp(std::ostream &ot) override;
};

class FunVal : public Val {
public:
    std::string arg;
    PTR(Expr) body;
    PTR(Env) env;
    FunVal(std::string arg, PTR(Expr) body, PTR(Env) env);
    bool equals(PTR(Val) e) override;
    PTR(Expr) to_expr() override;
    PTR(Val) add_to(PTR(Val) other_val) override;
    PTR(Val) mult_with(PTR(Val) other_val) override;
    bool is_true() override;
    PTR(Val) call(PTR(Val) actual_arg) override;
    void printExp(std::ostream &ot) override;
};