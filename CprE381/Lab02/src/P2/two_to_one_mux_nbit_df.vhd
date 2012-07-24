-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- two_to_one_mux_nbit_df.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an N-bit 2:1 Mux using dataflow VHDL
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity nbit_2_1_mux_df is
	generic(N : integer := 16);
	port(
		i_A  : in std_logic_vector(N-1 downto 0);
		i_B  : in std_logic_vector(N-1 downto 0);
		i_S  : in std_logic;
		o_F  : out std_logic_vector(N-1 downto 0)
		);
end nbit_2_1_mux_df;

architecture dataflow of nbit_2_1_mux_df is

begin

	o_F <= i_A when i_S = '0'	else
		      i_B;

end dataflow;
