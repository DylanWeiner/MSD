#include "env.h"
#include "val.h"

PTR(Env) Env::empty = NEW(EmptyEnv)();

PTR(Val) EmptyEnv::lookup(std::string var_name) {
    throw std::runtime_error("free variable: " + var_name);
}

ExtendedEnv::ExtendedEnv(std::string lhs, PTR(Val) rhs_val, PTR(Env) env) {
    this->lhs = lhs;
    this->rhs_val = rhs_val;
    this->env = env;
}

PTR(Val) ExtendedEnv::lookup(std::string find_name) {
    if (find_name == lhs)
        return rhs_val;
    else
        return env->lookup(find_name);
}
