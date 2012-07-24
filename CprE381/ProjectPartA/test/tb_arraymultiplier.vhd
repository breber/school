-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- tb_arraymultiplier.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a simple VHDL testbench for our
-- 32-bit array multiplier.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

-- This is an empty entity so we don't need to declare ports
entity tb_arraymultiplier is
	generic(gCLK_HPER   : time := 50 ns);
end tb_arraymultiplier;

architecture behavior of tb_arraymultiplier is
  
  -- Calculate the clock period as twice the half-period
  constant cCLK_PER  : time := gCLK_HPER * 2;

-- Declare the components we are going to instantiate
component ArrayMult
  
  port(i_X 			: in  std_logic_vector(31 downto 0);
       i_Y		  : in  std_logic_vector(31 downto 0);
       o_F	    : out std_logic_vector(31 downto 0)
       );

end component;


-- One bit signals
signal s_CLK : std_logic;

-- Operands Signals
signal s_X, s_Y, s_F, s_Expected : std_logic_vector( 31 downto 0 );

begin

ARRMULT : ArrayMult
	port map(	i_X => s_X,
       			i_Y => s_Y,
		        o_F => s_F   );
					
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

	-- The expected result will always be X * Y
	-- s_Expected <= s_X * s_Y;

  process
  begin

		-- Testing 0 x 0
		s_X <= x"00000000";
		s_Y <= x"00000000";
		-- s_F	== x"00000000"
    wait for cCLK_PER;

		-- Testing 5 x 0
		s_X <= x"00000005";
		s_Y <= x"00000000";
		-- s_F	== x"00000000"
    wait for cCLK_PER;
    
    -- Testing 0 x 5
		s_X <= x"00000000";
		s_Y <= x"00000005";
		-- s_F	== x"00000000"
    wait for cCLK_PER;
    
		-- Testing -5 x 0
		s_X <= x"FFFFFFFB";
		s_Y <= x"00000000";
		-- s_F	== x"00000000"
    wait for cCLK_PER;
    
    -- Testing 0 x -5
		s_X <= x"00000000";
		s_Y <= x"FFFFFFFB";
		-- s_F	== x"00000000"
    wait for cCLK_PER;
    
    
    -- Testing 5 x 1
		s_X <= x"00000005";
		s_Y <= x"00000001";
		-- s_F	== x"00000005"
    wait for cCLK_PER;
    
    -- Testing 1 x 5
		s_X <= x"00000001";
		s_Y <= x"00000005";
		-- s_F	== x"00000005"
    wait for cCLK_PER;
    
    -- Testing -5 x 1
		s_X <= x"FFFFFFFB";
		s_Y <= x"00000001";
		-- s_F	== x"FFFFFFFB"
    wait for cCLK_PER;
    
    -- Testing 1 x -5
		s_X <= x"00000001";
		s_Y <= x"FFFFFFFB";
		-- s_F	== x"FFFFFFFB"
    wait for cCLK_PER;
    
    
    -- Testing 5 x 5
		s_X <= x"00000005";
		s_Y <= x"00000005";
		-- s_F	== x"00000019"
    wait for cCLK_PER;
    
    -- Testing -5 x 5
		s_X <= x"FFFFFFFB";
		s_Y <= x"00000005";
		-- s_F	== x"FFFFFFC7"
    wait for cCLK_PER;
    
    -- Testing 5 x -5
		s_X <= x"00000005";
		s_Y <= x"FFFFFFFB";
		-- s_F	== x"FFFFFFC7"
    wait for cCLK_PER;
    
    -- Testing -5 x -5
		s_X <= x"FFFFFFFB";
		s_Y <= x"FFFFFFFB";
		-- s_F	== x"00000019"
    wait for cCLK_PER;
    
  end process;

end behavior;
