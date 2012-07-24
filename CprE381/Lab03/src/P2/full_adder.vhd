-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- full_adder.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an full adder using structural VHDL
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity full_adder is
	port(
		i_X    : in std_logic;
		i_Y    : in std_logic;
		i_Cin  : in std_logic;
		o_Cout : out std_logic;
		o_S	   : out std_logic
		);
end full_adder;

architecture structure of full_adder is

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

component xor2 is
	port(
		i_A : in std_logic;
		i_B : in std_logic;
		o_F : out std_logic
		);
end component;

signal x_xor_y, x_xor_y_and_c, x_and_y : std_logic;

begin

	m_Xor1 : xor2
		port map(
				i_A  => i_X,
				i_B  => i_Y,
				o_F  => x_xor_y
				);

	m_And1 : and2
		port map(
				i_A  => x_xor_y,
				i_B  => i_Cin,
				o_F  => x_xor_y_and_c
				);

	m_And2 : and2
		port map(
				i_A  => i_X,
				i_B  => i_Y,
				o_F  => x_and_y
				);
				
	m_Or1 : or2
		port map(
				i_A  => x_xor_y_and_c,
				i_B  => x_and_y,
				o_F  => o_Cout
				);	
				
	m_Xor2 : xor2
		port map(
				i_A  => x_xor_y,
				i_B  => i_Cin,
				o_F  => o_S
				);			

end structure;
