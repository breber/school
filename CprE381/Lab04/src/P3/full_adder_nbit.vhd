-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- full_adder_nbit.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an N-bit Full Adder using structural VHDL
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity nbit_full_adder is
	generic(N : integer := 32);
	port(
		i_X    : in std_logic_vector(N-1 downto 0);
		i_Y    : in std_logic_vector(N-1 downto 0);
		i_Cin  : in std_logic;
		o_Cout : out std_logic;
		o_S	   : out std_logic_vector(N-1 downto 0)
		);
end nbit_full_adder;

architecture structure of nbit_full_adder is

component full_adder
	port(
		i_X    : in std_logic;
		i_Y    : in std_logic;
		i_Cin  : in std_logic;
		o_Cout : out std_logic;
		o_S	   : out std_logic
		);
end component;

signal carry : std_logic_vector(N downto 0);

begin

carry(0) <= i_Cin;
o_Cout   <= carry(N);

FA_t: for i in 0 to N-1 generate
	mux_i: full_adder 
		port map(
				i_X    => i_X(i),
				i_Y	   => i_Y(i),
				i_Cin  => carry(i),
				o_Cout => carry(i + 1),
				o_S    => o_S(i)
				);
end generate;

end structure;
