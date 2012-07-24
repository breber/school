-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- full_adder_nbit_df.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an N-bit Full Adder using dataflow VHDL
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.std_logic_unsigned.all;

entity nbit_full_adder_df is
	generic(N : integer := 16);
	port(
		i_X    : in std_logic_vector(N-1 downto 0);
		i_Y    : in std_logic_vector(N-1 downto 0);
		i_Cin  : in std_logic;
		o_Cout : out std_logic;
		o_S	   : out std_logic_vector(N-1 downto 0)
		);
end nbit_full_adder_df;

architecture dataflow of nbit_full_adder_df is

begin

	o_S <= i_X + i_Y + i_Cin;

end dataflow;
