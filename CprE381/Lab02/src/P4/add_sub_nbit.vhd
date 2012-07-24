-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- add_sub_nbit.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an N-bit Adder/Subtractor using structural VHDL
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity nbit_add_sub is
	generic(N : integer := 32);
	port(
		i_X      : in std_logic_vector(N-1 downto 0);
		i_Y      : in std_logic_vector(N-1 downto 0);
		nAdd_Sub : in std_logic;
		o_Cout   : out std_logic;
		o_S	     : out std_logic_vector(N-1 downto 0)
		);
end nbit_add_sub;

architecture structure of nbit_add_sub is

component nbit_full_adder
	port(
		i_X    : in std_logic_vector(N-1 downto 0);
		i_Y    : in std_logic_vector(N-1 downto 0);
		i_Cin  : in std_logic;
		o_Cout : out std_logic;
		o_S	   : out std_logic_vector(N-1 downto 0)
		);
end component;

component nbit_2_1_mux
	port(
		i_A  : in std_logic_vector(N-1 downto 0);
		i_B  : in std_logic_vector(N-1 downto 0);
		i_S  : in std_logic;
		o_F  : out std_logic_vector(N-1 downto 0)
		);
end component;

component ones_complimenter
	port(
		i_A : in std_logic_vector(N-1 downto 0);
		o_F : out std_logic_vector(N-1 downto 0)
		);
end component;

signal out_Not, out_Mux : std_logic_vector(N-1 downto 0);

begin

	oneCmplmnt : ones_complimenter
		port map(
				i_A  => i_X,
				o_F  => out_Not
				);
				
	selecter : nbit_2_1_mux
		port map(
				i_A  => i_X,
				i_B  => out_Not,
				i_S  => nAdd_Sub,
				o_F  => out_Mux
				);
			
	adder : nbit_full_adder
		port map(
				i_X    => out_Mux,
				i_Y    => i_Y,
				i_Cin  => nAdd_Sub,
				o_Cout => o_Cout,
				o_S	   => o_S
				);

end structure;
