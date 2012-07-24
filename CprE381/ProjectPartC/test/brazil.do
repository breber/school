vsim -novopt work.mips_pipelined_processor
force -freeze sim:/mips_pipelined_processor/i_clk 1 0, 0 {50 ns} -r 100
add wave sim:/mips_pipelined_processor/*

add wave sim:/mips_pipelined_processor/ifpart1/*
add wave sim:/mips_pipelined_processor/ifpart2/*

add wave \
{sim:/mips_pipelined_processor/datamem/mem(0) } \
{sim:/mips_pipelined_processor/datamem/mem(1) } \
{sim:/mips_pipelined_processor/datamem/mem(2) } \
{sim:/mips_pipelined_processor/datamem/mem(3) } \
{sim:/mips_pipelined_processor/datamem/mem(4) } \
{sim:/mips_pipelined_processor/datamem/mem(5) } \
{sim:/mips_pipelined_processor/datamem/mem(6) } \
{sim:/mips_pipelined_processor/datamem/mem(7) } \
{sim:/mips_pipelined_processor/datamem/mem(8) } 
add wave \
{sim:/mips_pipelined_processor/regfile/o_r } 

add wave sim:/mips_pipelined_processor/regfile/*

force -freeze sim:/mips_pipelined_processor/i_rst 1 0
run 500
force -freeze sim:/mips_pipelined_processor/i_rst 0 0