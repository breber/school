# Start up the simulation
vsim -novopt work.tb_simpleproc 

# Set binary strings to be hex values (not needed for now)
set binopt {-logic}
set hexopt {-literal -hex}

# Divide up the wave window so signals are well labelled
eval add wave -noupdate -divider {"Processor"}
eval add wave sim:/tb_simpleproc/mproc/i_clk

eval add wave -noupdate -divider {"Register File"}
eval add wave sim:/tb_simpleproc/mproc/rf/o_r

eval add wave -noupdate -divider {"DMEM"}
eval add wave sim:/tb_simpleproc/mproc/dmem/mem

