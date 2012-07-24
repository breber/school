-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- tb_add_sub.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a simple VHDL testbench for an Adder/Subtracter
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

-- This is an empty entity so we don't need to declare ports
entity tb_add_sub is

end tb_add_sub;

architecture behavior of tb_add_sub is

-- Declare the components we are going to instantiate
component nbit_add_sub
  generic(N : integer := 32);
	port(
			i_X      : in std_logic_vector(N-1 downto 0);
			i_Y      : in std_logic_vector(N-1 downto 0);
			nAdd_Sub : in std_logic;
			o_Cout   : out std_logic;
			o_S	     : out std_logic_vector(N-1 downto 0)
			);
end component;

signal s_X, s_Y, s_OutStructural : std_logic_vector(31 downto 0);
signal s_nAdd_Sub, s_Cout : std_logic;


begin

Bob: nbit_add_sub
  port map(
					i_X    		=> s_X,
					i_Y    		=> s_Y,
					nAdd_Sub  => s_nAdd_Sub,
					o_Cout 		=> s_Cout,
  	      o_S    		=> s_OutStructural
					);

  process
  begin

		-- Add 1
    s_X   <= "01110111011101110111011101110111";
		s_Y   <= "00000000000000000000000000000001";
		s_nAdd_Sub <= '0'; -- perform the addition
    wait for 200 ns;
		-- Output should be 01110111011101110111011101111000

		s_nAdd_Sub <= '1'; -- perform the subtraction
    wait for 200 ns;
		-- Output should be 01110111011101110111011101110110

		-- Add the number to itself
    s_X   <= "01110111011101110111011101110111";
		s_Y   <= "01110111011101110111011101110111";
		s_nAdd_Sub <= '0';
    wait for 200 ns;
		-- Output should be 11101110111011101110111011101110

		-- Subtract the number from itself
    s_X   <= "01110111011101110111011101110111";
		s_Y   <= "01110111011101110111011101110111";
		s_nAdd_Sub <= '1';
    wait for 200 ns;
		-- Output should be 00000000000000000000000000000000

		-- Test an overflow
    s_X   <= "11111111111111111111111111111111";
		s_Y   <= "00000000000000000000000000000001";
		s_nAdd_Sub <= '0';
    wait for 200 ns;
		-- Output should be 11101110111011101110111011101110

		-- Test another overflow
    s_X   <= "11111111111111111111111111111111";
		s_Y   <= "00000000000000000000000000000000";
		s_nAdd_Sub <= '1';
    wait for 200 ns;
		-- Output should be 11101110111011101110111011101110

  end process;

end behavior;
