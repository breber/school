-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- two_to_one_mux.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an N-bit One's Complimenter using
-- structural VHDL
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity two_to_one_mux is
	port(
		i_A : in std_logic;
		i_B : in std_logic;
		i_S : in std_logic;
		o_F : out std_logic
		);
end two_to_one_mux;

architecture structure of two_to_one_mux is

component and2
	port(
		i_A : in std_logic;
		i_B : in std_logic;
		o_F : out std_logic
		);
end component;

component or2 is
	port(
		i_A : in std_logic;
		i_B : in std_logic;
		o_F : out std_logic
		);
end component;

component inv is
	port(
		i_A : in std_logic;
		o_F : out std_logic
		);
end component;

signal out_Not, out_And1, out_And2 : std_logic;

begin
	m_Inv1 : inv
		port map(
				i_A  => i_S,
				o_F  => out_Not
				);

	m_And1 : and2
		port map(
				i_A  => i_A,
				i_B  => out_Not,
				o_F  => out_And1
				);

	m_And2 : and2
		port map(
				i_A  => i_B,
				i_B  => i_S,
				o_F  => out_And2
				);

	m_Or : or2
		port map(
				i_A  => out_And1,
				i_B  => out_And2,
				o_F  => o_F
				);

end structure;
