-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- tb_opcodetranslator.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a simple VHDL testbench for our
-- opcodetranslator for our 32 bit ALU
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

-- This is an empty entity so we don't need to declare ports
entity tb_opcodetranslator is
	generic(gCLK_HPER   : time := 50 ns);
end tb_opcodetranslator;

architecture behavior of tb_opcodetranslator is
  
  -- Calculate the clock period as twice the half-period
  constant cCLK_PER  : time := gCLK_HPER * 2;

-- Declare the components we are going to instantiate
component opcodetranslator
  
  port(i_OpCode : in  std_logic_vector(2 downto 0);
       o_CryIn  : out std_logic;
       o_AInv   : out std_logic;
       o_BInv   : out std_logic;
       o_OpCode : out std_logic_vector(2 downto 0)
       );

end component;


-- One bit signals
signal s_CLK, s_AInv, s_BInv, s_CryIn : std_logic;

-- OpCode Signal
signal s_OpCodeIn, s_OpCodeOut : std_logic_vector( 2 downto 0 );

begin

OPTRANS : opcodetranslator
	port map(	i_OpCode => s_OpCodeIn,
       			o_CryIn  => s_CryIn,
		        o_AInv   => s_AInv,
		        o_BInv   => s_BInv,
		        o_OpCode => s_OpCodeOut   );
					
	-- This process sets the clock value (low for gCLK_HPER, then high
  -- for gCLK_HPER). Absent a "wait" command, processes restart 
  -- at the beginning once they have reached the final statement.
  P_CLK: process
  begin
    s_CLK <= '0';
    wait for gCLK_HPER;
    s_CLK <= '1';
    wait for gCLK_HPER;
  end process;

  process
  begin

		-- Testing AND translation
		s_OpCodeIn <= "000";
		-- s_CryIn			== '0'
		-- s_AInv	 			== '0'
		-- s_BInv	 			== '0'
		-- s_OpCodeOut	== "000"
    wait for cCLK_PER;
    
		-- Testing OR translation
		s_OpCodeIn <= "001";
		-- s_CryIn			== '0'
		-- s_AInv	 			== '0'
		-- s_BInv	 			== '0'
		-- s_OpCodeOut	== "001"
    wait for cCLK_PER;
    
		-- Testing XOR translation
		s_OpCodeIn <= "010";
		-- s_CryIn			== '0'
		-- s_AInv	 			== '0'
		-- s_BInv	 			== '0'
		-- s_OpCodeOut	== "010"
    wait for cCLK_PER;
    
		-- Testing NAND translation
		s_OpCodeIn <= "011";
		-- s_CryIn			== '0'
		-- s_AInv	 			== '0'
		-- s_BInv	 			== '0'
		-- s_OpCodeOut	== "011"
    wait for cCLK_PER;
    
    -- Testing NOR translation
		s_OpCodeIn <= "100";
		-- s_CryIn			== '0'
		-- s_AInv	 			== '0'
		-- s_BInv	 			== '0'
		-- s_OpCodeOut	== "100"
    wait for cCLK_PER;
    
    -- Testing ADD translation
		s_OpCodeIn <= "101";
		-- s_CryIn			== '0'
		-- s_AInv	 			== '0'
		-- s_BInv	 			== '0'
		-- s_OpCodeOut	== "101"
    wait for cCLK_PER;
    
		-- Testing SLT translation (should translate to ADD with carryIn = 1 and BInv = 1)
		s_OpCodeIn <= "110";
		-- s_CryIn			== '1'
		-- s_AInv	 			== '0'
		-- s_BInv	 			== '1'
		-- s_OpCodeOut	== "101"
    wait for cCLK_PER;
    
		-- Testing SUB translation (should translate to ADD with carryIn = 1 and BInv = 1)
		s_OpCodeIn <= "111";
		-- s_CryIn			== '1'
		-- s_AInv	 			== '0'
		-- s_BInv	 			== '1'
		-- s_OpCodeOut	== "101"
    wait for cCLK_PER;
    
  end process;

end behavior;
