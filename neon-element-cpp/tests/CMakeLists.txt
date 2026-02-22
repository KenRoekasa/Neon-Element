include(FetchContent)
FetchContent_Declare(
    Catch2
    GIT_REPOSITORY https://github.com/catchorg/Catch2.git
    GIT_TAG v3.5.2
)
FetchContent_MakeAvailable(Catch2)

list(APPEND CMAKE_MODULE_PATH ${catch2_SOURCE_DIR}/extras)
include(CTest)
include(Catch)

# Helper function to add a test executable
function(add_neon_test TEST_NAME)
    add_executable(${TEST_NAME} ${ARGN})
    target_include_directories(${TEST_NAME} PRIVATE ${CMAKE_SOURCE_DIR}/src)
    target_link_libraries(${TEST_NAME} PRIVATE
        Catch2::Catch2WithMain
        sfml-graphics
        sfml-window
        sfml-system
        sfml-audio
        sfml-network
    )
    catch_discover_tests(${TEST_NAME})
endfunction()

# Phase 1 tests
add_neon_test(test_core test_core.cpp)
