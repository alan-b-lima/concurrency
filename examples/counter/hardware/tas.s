bits 64

section .text

global test_and_set
global test_and_reset

test_and_set:
    lock bts dword [rdi], 0
    setc al
    ret

test_and_reset:
    lock btr dword [rdi], 0
    setc al
    ret

section .note.GNU-stack noexec nowrite progbits