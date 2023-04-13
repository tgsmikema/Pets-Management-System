cmake_minimum_required(VERSION 3.13)

include(pico_sdk_import.cmake)

project(test_project C CXX ASM)

set(CMAKE_C_STANDARD 11)
set(CMAKE_CXX_STANDARD 17)

pico_sdk_init()
if (TARGET tinyusb_device)
    add_executable(test
    test.c
    )

    # pull in common dependencies
    target_link_libraries(test pico_cyw43_arch_lwip_threadsafe_background pico_stdlib)
    target_link_libraries(test pico_lwip_http)

    # enable usb output, disable uart output
    pico_enable_stdio_usb(test 1)
    pico_enable_stdio_uart(test 1)

    # create map/bin/hex/uf2 file etc.
    pico_add_extra_outputs(test)

    # add url via pico_set_program_url
    #example_auto_set_url(hello_usb)

    target_include_directories(test PRIVATE ${CMAKE_CURRENT_LIST_DIR} )
elseif(PICO_ON_DEVICE)
    message(WARNING "not building hello_usb because TinyUSB submodule is not initialized in the SDK")
endif()