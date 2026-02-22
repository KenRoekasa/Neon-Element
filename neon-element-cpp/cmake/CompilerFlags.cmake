# Compiler warning flags for all targets
if(CMAKE_CXX_COMPILER_ID MATCHES "GNU|Clang")
    add_compile_options(
        -Wall
        -Wextra
        -Wpedantic
        -Wshadow
        -Wnon-virtual-dtor
        -Wold-style-cast
        -Wcast-align
        -Wunused
        -Woverloaded-virtual
        -Wconversion
        -Wsign-conversion
        -Wnull-dereference
        -Wformat=2
    )
    if(CMAKE_CXX_COMPILER_ID MATCHES "GNU")
        add_compile_options(-Wduplicated-cond -Wduplicated-branches -Wlogical-op)
    endif()
elseif(MSVC)
    add_compile_options(/W4 /permissive-)
endif()

# Sanitizer support for debug builds
option(ENABLE_SANITIZERS "Enable ASan + UBSan" OFF)
if(ENABLE_SANITIZERS)
    add_compile_options(-fsanitize=address,undefined -fno-omit-frame-pointer)
    add_link_options(-fsanitize=address,undefined)
endif()
