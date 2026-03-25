#include <string>
class Expr;

class Val {
public:
    virtual bool equals(Val *e) = 0;
    virtual Expr* to_expr() = 0;
    virtual Val* add_to(Val *other_val) = 0;
    virtual Val* mult_with(Val *other_val) = 0;
    virtual std::string num_to_string() = 0;
    virtual bool is_true() = 0;
};

class NumVal : public Val {
public:
    int val;
    NumVal(int val);
    bool equals(Val *e) override;
    Expr* to_expr() override;
    Val* add_to(Val *other_val) override;
    Val* mult_with(Val *other_val) override;
    std::string num_to_string() override;
    bool is_true() override;
};

class BoolVal : public Val {
public:
    bool val;
    BoolVal(bool val);
    bool equals(Val *e) override;
    Expr* to_expr() override;
    Val* add_to(Val *other_val) override;
    Val* mult_with(Val *other_val) override;
    std::string num_to_string() override;
    bool is_true() override;
};