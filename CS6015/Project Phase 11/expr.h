#include <string>
#include <sstream>
#include <iostream>
#include "pointer.h"
// #include <fstream>

// CLASS(Val) {};
class Val;

typedef enum {
  prec_none,      // = 0
  prec_add,       // = 1
  prec_mult,       // = 2
  prec_start
} precedence_t;

CLASS(Expr) {
public:
    virtual ~Expr() { };
    virtual PTR(Val) equals(PTR(Expr) e) = 0;
    virtual PTR(Val) interp() = 0;
    // virtual PTR(Val) has_variable() = 0;
    virtual PTR(Expr) subst(std::string name, PTR(Expr) newVal) = 0;
    virtual void printExp(std::ostream &ot) = 0;
    std::string to_string_p();
    void pretty_print(std::ostream &ot);
    virtual void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) = 0;
    std::string to_pretty_string();
};

class NumExpr : public Expr {
public:
    int val;
    NumExpr(int val);
    PTR(Val) equals(PTR(Expr) e) override;
    PTR(Val) interp() override;
    // PTR(Val) has_variable() override;
    PTR(Expr) subst(std::string name, PTR(Expr) newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;
};

class VarExpr : public Expr {
public:
    std::string val;
    VarExpr(std::string val);
    PTR(Val) equals(PTR(Expr) e) override;
    PTR(Val) interp() override;
    // PTR(Val) has_variable() override;
    PTR(Expr) subst(std::string name, PTR(Expr) newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;

};

class AddExpr : public Expr {
public:
    PTR(Expr) lhs;
    PTR(Expr) rhs;
    AddExpr(PTR(Expr) lhs, PTR(Expr) rhs);
    PTR(Val) equals(PTR(Expr) e) override;
    PTR(Val) interp() override;
    // PTR(Val) has_variable() override;
    PTR(Expr) subst(std::string name, PTR(Expr) newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;
};

class MultExpr : public Expr {
public:
    PTR(Expr) lhs;
    PTR(Expr) rhs;
    MultExpr(PTR(Expr) lhs, PTR(Expr) rhs);
    PTR(Val) equals(PTR(Expr) e) override;
    PTR(Val) interp() override;
    // PTR(Val) has_variable() override;
    PTR(Expr) subst(std::string name, PTR(Expr) newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;
};

class LetExpr : public Expr {
public:
    std::string var;
    PTR(Expr) rhs;
    PTR(Expr) body;
    LetExpr(std::string var, PTR(Expr) rhs, PTR(Expr) body);
    PTR(Val) equals(PTR(Expr) e) override;
    PTR(Val) interp() override;
    // PTR(Val) has_variable() override;
    PTR(Expr) subst(std::string name, PTR(Expr) newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;
};

class BoolExpr : public Expr {
public:
    bool val;
    BoolExpr(bool val);
    PTR(Val) equals(PTR(Expr) e) override;
    PTR(Val) interp() override;
    // PTR(Val) has_variable() override;
    PTR(Expr) subst(std::string name, PTR(Expr) newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;
};

class IfExpr : public Expr {
public:
    PTR(Expr) cond;
    PTR(Expr) then_body;
    PTR(Expr) else_body;
    IfExpr(PTR(Expr) cond, PTR(Expr) then_body, PTR(Expr) else_body);
    PTR(Val) equals(PTR(Expr) e) override;
    PTR(Val) interp() override;
    // PTR(Val) has_variable() override;
    PTR(Expr) subst(std::string name, PTR(Expr) newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;
};

class EqExpr : public Expr {
public:
    PTR(Expr) lhs;
    PTR(Expr) rhs;
    EqExpr(PTR(Expr) lhs, PTR(Expr) rhs);
    PTR(Val) equals(PTR(Expr) e) override;
    PTR(Val) interp() override;
    // PTR(Val) has_variable() override;
    PTR(Expr) subst(std::string name, PTR(Expr) newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;
};

class FunExpr : public Expr {
public:
    std::string arg;
    PTR(Expr) body;
    FunExpr(std::string arg, PTR(Expr) body);
    PTR(Val) equals(PTR(Expr) e) override;
    PTR(Val) interp() override;
    PTR(Expr) subst(std::string name, PTR(Expr) newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;
};

class CallExpr : public Expr {
public:
    PTR(Expr) fun;
    PTR(Expr) arg;
    CallExpr(PTR(Expr) fun, PTR(Expr) arg);
    PTR(Val) equals(PTR(Expr) e) override;
    PTR(Val) interp() override;
    PTR(Expr) subst(std::string name, PTR(Expr) newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;
};