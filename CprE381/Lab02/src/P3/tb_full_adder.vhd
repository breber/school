-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- tb_full_adder.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a simple VHDL testbench for a Full Adder
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

-- This is an empty entity so we don't need to declare ports
entity tb_full_adder is

end tb_full_adder;

architecture behavior of tb_full_adder is

-- Declare the components we are going to instantiate
component nbit_full_adder
  generic(N : integer := 32);
	port(
			i_X    : in std_logic_vector(N-1 downto 0);
			i_Y    : in std_logic_vector(N-1 downto 0);
			i_Cin  : in std_logic;
			o_Cout : out std_logic;
			o_S	   : out std_logic_vector(N-1 downto 0)
			);
end component;

component nbit_full_adder_df
	generic(N : integer := 32);
	port(
		i_X    : in std_logic_vector(N-1 downto 0);
		i_Y    : in std_logic_vector(N-1 downto 0);
		i_Cin  : in std_logic;
		o_Cout : out std_logic;
		o_S	   : out std_logic_vector(N-1 downto 0)
		);
end component;

signal s_X, s_Y, s_OutStructural, s_OutDataflow : std_logic_vector(31 downto 0);
signal s_Cin, s_CoutStructural, s_CoutDataflow : std_logic;


begin

Structural: nbit_full_adder
  port map(
					i_X    => s_X,
					i_Y    => s_Y,
					i_Cin  => s_Cin,
					o_Cout => s_CoutStructural,
  	      o_S    => s_OutStructural
					);

Dataflow: nbit_full_adder_df
  port map(
					i_X    => s_X,
					i_Y    => s_Y,
					i_Cin  => s_Cin,
					o_Cout => s_CoutDataflow,
  	      o_S    => s_OutDataflow
					);

  process
  begin

		-- Add 1
    s_X   <= "01110111011101110111011101110111";
		s_Y   <= "00000000000000000000000000000001";
		s_Cin <= '0';
    wait for 200 ns;
		-- Output should be 01110111011101110111011101111000
		if (s_OutStructural = s_OutDataflow) then
			report "Structural matches dataflow";
		else 
			report "Structural does not match dataflow";
		end if;

		-- Add the number to itself
    s_X   <= "01110111011101110111011101110111";
		s_Y   <= "01110111011101110111011101110111";
		s_Cin <= '0';
    wait for 200 ns;
		-- Output should be 11101110111011101110111011101110
		if (s_OutStructural = s_OutDataflow) then
			report "Structural matches dataflow";
		else 
			report "Structural does not match dataflow";
		end if;

		-- Test an overflow
    s_X   <= "11111111111111111111111111111111";
		s_Y   <= "00000000000000000000000000000001";
		s_Cin <= '0';
    wait for 200 ns;
		-- Output should be 11101110111011101110111011101110
		if (s_OutStructural = s_OutDataflow) then
			report "Structural matches dataflow";
		else 
			report "Structural does not match dataflow";
		end if;

		-- Test another overflow
    s_X   <= "11111111111111111111111111111111";
		s_Y   <= "00000000000000000000000000000000";
		s_Cin <= '1';
    wait for 200 ns;
		-- Output should be 11101110111011101110111011101110
		if (s_OutStructural = s_OutDataflow) then
			report "Structural matches dataflow";
		else 
			report "Structural does not match dataflow";
		end if;

  end process;

end behavior;
