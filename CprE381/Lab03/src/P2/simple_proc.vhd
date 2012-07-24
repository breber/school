-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- simple_proc.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a simple processor design using 
-- structural VHDL. It contains a register file and an adder/subtractor.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity simple_proc is
  port(
			i_CLK 	 : in std_logic;     -- Clock input
      i_AdIn1  : in std_logic_vector(4 downto 0);
      i_AdIn2  : in std_logic_vector(4 downto 0);
      i_AdOut  : in std_logic_vector(4 downto 0);
      i_Imm    : in std_logic_vector(31 downto 0);
      i_AddSub : in std_logic;
      i_SImm   : in std_logic;
      i_WE     : in std_logic
			);
end simple_proc;

architecture structure of simple_proc is

component five_to_thirtytwo_decoder
  port(i_A : in std_logic_vector(4 downto 0);
       o_F : out std_logic_vector(31 downto 0));
end component;

component nbit_add_sub
	generic(N : integer := 32);
	port(
		i_X      : in std_logic_vector(N-1 downto 0);
		i_Y      : in std_logic_vector(N-1 downto 0);
		i_Imm    : in std_logic_vector(N-1 downto 0);
		nAdd_Sub : in std_logic;
		ALUSrc   : in std_logic;
		o_Cout   : out std_logic;
		o_S	     : out std_logic_vector(N-1 downto 0)
		);
end component;

component register_file
  port(
			i_CLK 	 : in std_logic;     -- Clock input
      i_AdIn1  : in std_logic_vector(4 downto 0);
      i_AdIn2  : in std_logic_vector(4 downto 0);
      i_AdOut  : in std_logic_vector(4 downto 0);
      i_WData  : in std_logic_vector(31 downto 0);
      i_WE     : in std_logic;
      o_RData1 : out std_logic_vector(31 downto 0);-- Data value input
      o_RData2 : out std_logic_vector(31 downto 0)-- Data value input
			);
end component;

signal o_RAdd1, o_RAdd2, o_AddSub : std_logic_vector(31 downto 0);
signal o_Cout : std_logic;

begin

	RF : register_file
		port map(
				    i_CLK    => i_CLK,
    				i_AdIn1  => i_AdIn1,
    				i_AdIn2  => i_AdIn2,
    				i_AdOut  => i_AdOut,
    				i_WData  => o_AddSub,
    				i_WE     => i_WE,
    				o_RData1 => o_RAdd1,
    				o_RData2 => o_RAdd2
    				);

	ADDSUB : nbit_add_sub
		port map(
        		i_X      => o_RAdd1,
        		i_Y      => o_RAdd2,
        		i_Imm    => i_Imm,
		        nAdd_Sub => i_AddSub,
        		ALUSrc   => i_SImm,
	        	o_Cout   => o_Cout,
	        	o_S	     => o_AddSub
	        	);

end structure;
