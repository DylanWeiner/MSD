#include <iostream>
#include <stdlib.h>
#include "exec.h"

int depth_count = 0;

static std::string random_expr_string_add();
static std::string random_expr_string_mult();
static std::string random_expr_string_let_add();
static std::string random_expr_string_let_mult();

int main(int argc, char **argv) {
    const char * const interp_argv[] = {argv[1], "--interp" };
    const char * const print_argv[] = { argv[1], "--print" };
    for (int i = 0; i < 100; i++) {
        std::string in = random_expr_string_add();
        std::cout << "Trying "<< in << "\n";
        
        ExecResult interp_result = exec_program(2, interp_argv, in);
        ExecResult print_result = exec_program(2, print_argv, in);

        ExecResult interp_again_result = exec_program(2, interp_argv, print_result.out);

        if (interp_again_result.out != interp_result.out)
            throw std::runtime_error("different result for printed");
            
    }

    for (int i = 0; i < 100; i++) {
        std::string in = random_expr_string_mult();
        std::cout << "Trying "<< in << "\n";
        
        ExecResult interp_result = exec_program(2, interp_argv, in);
        ExecResult print_result = exec_program(2, print_argv, in);

        ExecResult interp_again_result = exec_program(2, interp_argv, print_result.out);

        if (interp_again_result.out != interp_result.out)
            throw std::runtime_error("different result for printed");
            
    }

    for (int i = 0; i < 100; i++) {
        std::string in = random_expr_string_let_add();
        std::cout << "Trying "<< in << "\n";
        
        ExecResult interp_result = exec_program(2, interp_argv, in);
        ExecResult print_result = exec_program(2, print_argv, in);

        ExecResult interp_again_result = exec_program(2, interp_argv, print_result.out);

        if (interp_again_result.out != interp_result.out)
            throw std::runtime_error("different result for printed");
            
    }

    for (int i = 0; i < 100; i++) {
        std::string in = random_expr_string_let_mult();
        std::cout << "Trying "<< in << "\n";
        
        ExecResult interp_result = exec_program(2, interp_argv, in);
        ExecResult print_result = exec_program(2, print_argv, in);

        ExecResult interp_again_result = exec_program(2, interp_argv, print_result.out);

        if (interp_again_result.out != interp_result.out)
            throw std::runtime_error("different result for printed");
            
    }

std::cout << "Comparison successful!\n";
return 0;
}

std::string random_expr_string_add() {
    if ((rand() % 10) < 6)
        return std::to_string(rand());
    else
        return random_expr_string_add() + "+" + random_expr_string_add();
}

std::string random_expr_string_mult() {
    if ((rand() % 10) < 6)
        return std::to_string(rand());
    else
        return random_expr_string_mult() + "*" + random_expr_string_mult();
}

std::string random_expr_string_let_add() {
    if(depth_count == 0) {
        depth_count++;
        return "_let x = " + random_expr_string_add() + " _in x + " + random_expr_string_let_add();
    }
    else if(depth_count > 0 && depth_count < 3) {
        depth_count++;
        return random_expr_string_add() + " + _let x = " + random_expr_string_add() + " _in x + " + random_expr_string_let_add();
    }
    else
        depth_count = 0;
        return random_expr_string_add();
}

std::string random_expr_string_let_mult() {
    if(depth_count == 0) {
        depth_count++;
        return "_let x = " + random_expr_string_mult() + " _in x * " + random_expr_string_let_mult();
    }
    else if(depth_count > 0 && depth_count < 3) {
        depth_count++;
        return random_expr_string_mult() + " * _let x = " + random_expr_string_mult() + " _in x * " + random_expr_string_let_mult();
    }
    else
        depth_count = 0;
        return random_expr_string_mult();
}