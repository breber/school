-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- tb_shifter.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a simple VHDL testbench for our
-- 32-bit shifter
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

-- This is an empty entity so we don't need to declare ports
entity tb_shifter is
	generic(gCLK_HPER   : time := 50 ns);
end tb_shifter;

architecture behavior of tb_shifter is
  
-- Calculate the clock period as twice the half-period
constant cCLK_PER  : time := gCLK_HPER * 2;

-- Declare the components we are going to instantiate
component barrel_shifter
  
  port(i_in							    : in  std_logic_vector(31 downto 0);
       shiftAmount		  : in  std_logic_vector(4 downto 0);
       log_arith				  : in 	std_logic;				-- 0 = logical, 1 = arithmetic
       leftOrRight			 : in	std_logic;				-- 0 = left, 1 = right
       o_F	    				   : out std_logic_vector(31 downto 0)
       );

end component;


-- One bit signals
signal s_CLK, s_ArithLogic, s_LeftRight : std_logic;

-- Input/Output Signals
signal s_X, s_F : std_logic_vector( 31 downto 0 );

-- Shift amount signal
signal s_shiftAmount : std_logic_vector( 4 downto 0 );

begin

SHIFTER1 : barrel_shifter
	port map(	i_in           => s_X,
       			  shiftAmount    => s_shiftAmount,
       			  log_arith			   => s_ArithLogic,
       			  leftOrRight		  => s_LeftRight,
		        o_F 					      => s_F   );
					
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

		-------------------------------------
		--				TEST LEFT SHIFT					       --
		-------------------------------------
		
		s_LeftRight <= '0';

		-- Testing sll 0, 0 = 0
		s_X 					     <= x"00000000";
		s_shiftAmount  <=  "00000";
		s_ArithLogic	  <=  '0';
		-- s_F	== x"00000000"
    wait for cCLK_PER;
    
    -- Testing sla 0, 0 = 0
		s_X 					     <= x"00000000";
		s_shiftAmount  <=  "00000";
		s_ArithLogic  	<=  '1';
		-- s_F	== x"00000000"
    wait for cCLK_PER;

		-- Testing sll 5, 0 = 5
		s_X 					     <= x"00000005";
		s_shiftAmount  <=  "00000";
		s_ArithLogic  	<=  '0';
		-- s_F	== x"00000005"
    wait for cCLK_PER;
    
		-- Testing sla 5, 0 = 5
		s_X 					     <= x"00000005";
		s_shiftAmount  <=  "00000";
		s_ArithLogic  	<=  '1';
		-- s_F	== x"00000005"
    wait for cCLK_PER;
    
    -- Testing sll 5, 1 = 10
		s_X 					     <= x"00000005";
		s_shiftAmount  <=  "00001";
		s_ArithLogic  	<=  '0';
		-- s_F	== x"0000000A"
    wait for cCLK_PER;
    
    -- Testing sla 5, 1 = 10
		s_X 					     <= x"00000005";
		s_shiftAmount  <=  "00001";
		s_ArithLogic  	<=  '1';
		-- s_F	== x"0000000A"
    wait for cCLK_PER;
    
    -- Testing sll 1, 4 = 0x10
		s_X 					     <= x"00000001";
		s_shiftAmount  <=  "00100";
		s_ArithLogic	  <=  '0';
		-- s_F	== x"00000100"
    wait for cCLK_PER;
    
    -- Testing sla 1, 4 = 0x10
		s_X 					     <= x"00000001";
		s_shiftAmount  <=  "00100";
		s_ArithLogic  	<=  '1';
		-- s_F	== x"00000100"
    wait for cCLK_PER;
    
    -- Testing sll 1, 8 = 0x100
		s_X 					     <= x"00000001";
		s_shiftAmount  <=  "01000";
		s_ArithLogic  	<=  '0';
		-- s_F	== x"00000100"
    wait for cCLK_PER;
    
    -- Testing sla 1, 8 = 0x100
		s_X 					     <= x"00000001";
		s_shiftAmount  <=  "01000";
		s_ArithLogic  	<=  '1';
		-- s_F	== x"00000100"
    wait for cCLK_PER;
    
    -- Testing sll 1, 16 = 0x10000
		s_X 					     <= x"00000001";
		s_shiftAmount  <=  "10000";
		s_ArithLogic  	<=  '0';
		-- s_F	== x"00010000"
    wait for cCLK_PER;
    
    -- Testing sla 1, 16 = 0x10000
		s_X 					     <= x"00000001";
		s_shiftAmount  <=  "10000";
		s_ArithLogic  	<=  '1';
		-- s_F	== x"00010000"
    wait for cCLK_PER;
    
    -- Testing sll 1, 31 = 0x80000000
		s_X 					     <= x"00000001";
		s_shiftAmount  <=  "11111";
		s_ArithLogic  	<=  '0';
		-- s_F	== x"80000000"
    wait for cCLK_PER;
    
    -- Testing sla 1, 31 = 0x80000000
		s_X 					     <= x"00000001";
		s_shiftAmount  <=  "11111";
		s_ArithLogic  	<=  '1';
		-- s_F	== x"80000000"
    wait for cCLK_PER;
    
    
    -------------------------------------
		--				TEST RIGHT SHIFT				 --
		-------------------------------------
		
		s_LeftRight <= '1';

		-- Testing srl 0, 0 = 0
		s_X           <= x"00000000";
		s_shiftAmount <=  "00000";
		s_ArithLogic  <=  '0';
		-- s_F	== x"00000000"
    wait for cCLK_PER;
    
    -- Testing sra 0, 0 = 0
		s_X           <= x"00000000";
		s_shiftAmount <=  "00000";
		s_ArithLogic	 <=  '1';
		-- s_F	== x"00000000"
    wait for cCLK_PER;
    
		-- Testing srl 1, 1 = 0
		s_X 					    <= x"00000001";
		s_shiftAmount <=  "00001";
		s_ArithLogic	 <=  '0';
		-- s_F	== x"00000000"
    wait for cCLK_PER;
    
    -- Testing sra 1, 1 = 0
		s_X           <= x"00000001";
		s_shiftAmount <=  "00001";
		s_ArithLogic	 <=  '1';
		-- s_F	== x"00000000"
    wait for cCLK_PER;
    
		-- Testing srl 0x80010001, 1 = 0x40008000
		s_X           <= x"80010001";
		s_shiftAmount <=  "00001";
		s_ArithLogic	 <=  '0';
		-- s_F	== x"40008000"
    wait for cCLK_PER;
    
    -- Testing sra 0x80010001, 1 = 0xC0008000
		s_X           <= x"80010001";
		s_shiftAmount <=  "00001";
		s_ArithLogic	 <=  '1';
		-- s_F	== x"C0008000"
    wait for cCLK_PER;
    
		-- Testing srl 0x80010001, 8 = 0x00800100
		s_X           <= x"80010001";
		s_shiftAmount <=  "01000";
		s_ArithLogic  <=  '0';
		-- s_F	== x"00800100"
    wait for cCLK_PER;
    
    -- Testing sra 0x80010001, 8 = 0xFF800100
		s_X           <= x"80010001";
		s_shiftAmount <=  "01000";
		s_ArithLogic  <=  '1';
		-- s_F	== x"FF800100"
    wait for cCLK_PER;
    
		-- Testing srl 0x80010001, 31 = 0x00000001
		s_X           <= x"80010001";
		s_shiftAmount <=  "11111";
		s_ArithLogic  <=  '0';
		-- s_F	== x"00000001"
    wait for cCLK_PER;
    
    -- Testing sra 0x80010001, 31 = 0xFFFFFFFF
		s_X           <= x"80010001";
		s_shiftAmount <=  "11111";
		s_ArithLogic  <=  '1';
		-- s_F	== x"FFFFFFFF"
    wait for cCLK_PER;
    
  end process;

end behavior;
