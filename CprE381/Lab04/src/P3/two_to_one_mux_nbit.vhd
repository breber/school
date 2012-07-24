-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- two_to_one_mux_nbit.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an N-bit 2:1 Mux using structural VHDL
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity nbit_2_1_mux is
	generic(N : integer := 32);
	port(
		i_A  : in std_logic_vector(N-1 downto 0);
		i_B  : in std_logic_vector(N-1 downto 0);
		i_S  : in std_logic;
		o_F  : out std_logic_vector(N-1 downto 0)
		);
end nbit_2_1_mux;

architecture structure of nbit_2_1_mux is

component two_to_one_mux
	port(
		i_A  : in std_logic;
		i_B  : in std_logic;
		i_S  : in std_logic;
		o_F  : out std_logic
		);
end component;

begin

-- We loop through and instantiate and connect N mux modules
M1: for i in 0 to N-1 generate
	mux_i: two_to_one_mux 
		port map(
				i_A => i_A(i),
				i_B	=> i_B(i),
				i_S => i_S,
				o_F => o_F(i)
				);
end generate;

end structure;
