-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- ones_compliment.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an N-bit One's Complimenter using
-- structural VHDL
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity ones_complimenter is
	generic(N : integer := 32);
	port(
		i_A : in std_logic_vector(N-1 downto 0);
		o_F : out std_logic_vector(N-1 downto 0)
		);
end ones_complimenter;

architecture structure of ones_complimenter is

component inv
	port(
		i_A : in std_logic;
		o_F : out std_logic
		);
end component;

begin

-- We loop through and instantiate and connect N inv modules
G1: for i in 0 to N-1 generate
	inv_i: inv 
		port map(
				i_A  => i_A(i),
				o_F  => o_F(i)
				);
end generate;

end structure;
