#include "catch.h"
#include "cmdline.h"
#include "parse.hpp"
#include "val.h"

int main(int argc, char **argv) {
  try {
    int action = use_arguments(argc, argv); /// Reads arguments from the commandline into the use_arguments method.

    if(action == do_interp) {
      Expr *e = parse_expr(std::cin); /// Parses user input.

      std::cout << e->interp()->to_string() << "\n";
    }
    else if(action == do_print) {
      Expr *e = parse_expr(std::cin); /// Parses user input.

      // std::cout << "Expression: \n";
      e->printExp(std::cout);
      std::cout << "\n";
    }
    else if(action == do_pretty_print) {
      Expr *e = parse_expr(std::cin); /// Parses user input.

      // std::cout << "Expression: \n";
      e->pretty_print(std::cout);
      std::cout << "\n";
    }
    else { //if(action == do_nothing)
      return 0;
    }
  }
  catch(std::runtime_error exn) {
    std::cerr << exn.what() << "\n";
    return 1;
  }
}
