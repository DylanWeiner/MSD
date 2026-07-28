; By Dylan Weiner
; Assembly Code Assignment saved in CADE Lab

extern puts

section .rodata
helloString:  db        "hello", 0

section .text

global sayHello ; this says that "sayHello" is a symbol that the linker needs to know about

sayHello: mov       rax, 1                  ; system call for write
            mov       rdi, helloString            ; address of string to puts
            call      puts                     ; invoke operating system to do the write
            ret

global myPuts

myPuts:
          mov     rax, 1
          mov       rdx, rsi
          mov       rsi, rdi
          mov       rdi, 1              ; states stdout
          syscall
          ret

          section   .rodata
myHello:  db        "hello", 10

global myGTOD

myGTOD:
          mov     rax, 96
          sub     rsp, 16
          mov        rdi, rsp
          mov        rsi, 0
          syscall
          mov        rax, [rsp]
          mov        rdx, [rsp + 8]
          add       rsp, 16
          ret
