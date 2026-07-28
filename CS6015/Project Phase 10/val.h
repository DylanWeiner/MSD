#include <string>
class Expr;

class Val {
public:
    virtual bool equals(Val *e) = 0;
    virtual Expr* to_expr() = 0;
    virtual Val* add_to(Val *other_val) = 0;
    virtual Val* mult_with(Val *other_val) = 0;
    virtual void printExp(std::ostream &ot) = 0;
    std::string to_string();
    virtual bool is_true() = 0;
    virtual Val *call(Val *actual_arg) = 0;
};

class NumVal : public Val {
public:
    int val;
    NumVal(int val);
    bool equals(Val *e) override;
    Expr* to_expr() override;
    Val* add_to(Val *other_val) override;
    Val* mult_with(Val *other_val) override;
    bool is_true() override;
    Val *call(Val *actual_arg) override;
    void printExp(std::ostream &ot) override;
};

class BoolVal : public Val {
public:
    bool val;
    BoolVal(bool val);
    bool equals(Val *e) override;
    Expr* to_expr() override;
    Val* add_to(Val *other_val) override;
    Val* mult_with(Val *other_val) override;
    bool is_true() override;
    Val *call(Val *actual_arg) override;
    void printExp(std::ostream &ot) override;
};

class FunVal : public Val {
public:
    std::string arg;
    Expr *body;
    FunVal(std::string arg, Expr *body);
    bool equals(Val *e) override;
    Expr* to_expr() override;
    Val* add_to(Val *other_val) override;
    Val* mult_with(Val *other_val) override;
    bool is_true() override;
    Val *call(Val *actual_arg) override;
    void printExp(std::ostream &ot) override;
};