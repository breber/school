-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- tb_simple_proc.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a simple VHDL testbench for our
-- simple processor which contains an adder/subtractor and a register file.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

-- This is an empty entity so we don't need to declare ports
entity tb_simple_proc is
  generic(gCLK_HPER   : time := 50 ns);
end tb_simple_proc;

architecture behavior of tb_simple_proc is
  
  -- Calculate the clock period as twice the half-period
  constant cCLK_PER  : time := gCLK_HPER * 2;

-- Declare the components we are going to instantiate
component simple_proc
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
end component;

-- Address signals
signal s_AddrIn1, s_AddrIn2, s_AddrOut : std_logic_vector(4 downto 0);

-- Immediate Value signal
signal s_Immediate : std_logic_vector(31 downto 0);

-- Control Signals
signal s_CLK, s_AddSub, s_SImm, s_WE : std_logic;


begin

MPROC : simple_proc
  port map(
					i_CLK    => s_CLK,
					i_AdIn1  => s_AddrIn1,
					i_AdIn2  => s_AddrIn2,
					i_AdOut  => s_AddrOut,
					i_Imm    => s_Immediate,
					i_AddSub => s_AddSub,
					i_SImm   => s_SImm,
					i_WE     => s_WE
					);
					
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

		-- addi $1, $0, 1 # place "1" in $1
    s_AddrOut   <=  "00001";
		s_AddrIn1   <=  "00000";
		s_AddrIn2   <=  "00001";
		s_Immediate <= x"00000001";
		s_AddSub    <=  '0';
		s_SImm      <=  '1';
		s_WE        <=  '1';
    wait for cCLK_PER;
    
    -- addi $2, $0, 2 # place "2" in $2
    s_AddrOut   <=  "00010";
		s_AddrIn2   <=  "00010";
		s_Immediate <= x"00000002";
    wait for cCLK_PER;
    
    -- addi $3, $0, 3 # place "3" in $3
    s_AddrOut   <=  "00011";
		s_Immediate <= x"00000003";
    wait for cCLK_PER;
    
    -- addi $4, $0, 4 # place "4" in $4
    s_AddrOut   <=  "00100";
		s_Immediate <= x"00000004";
    wait for cCLK_PER;
    
    -- addi $5, $0, 5 # place "5" in $5
    s_AddrOut   <=  "00101";
		s_Immediate <= x"00000005";
    wait for cCLK_PER;

		-- addi $6, $0, 6 # place "6" in $6
    s_AddrOut   <=  "00110";
		s_Immediate <= x"00000006";
    wait for cCLK_PER;
    
    -- addi $7, $0, 7 # place "7" in $7
    s_AddrOut   <=  "00111";
		s_Immediate <= x"00000007";
    wait for cCLK_PER;
    
    -- addi $8, $0, 8 # place "8" in $8
    s_AddrOut   <=  "01000";
		s_Immediate <= x"00000008";
    wait for cCLK_PER;
    
    -- addi $9, $0, 9 # place "9" in $9
    s_AddrOut   <=  "01001";
		s_Immediate <= x"00000009";
    wait for cCLK_PER;
    
    -- addi $10, $0, 10 # place "10" in $10
    s_AddrOut   <=  "01010";
		s_Immediate <= x"0000000A";
    wait for cCLK_PER;
    
    -- add $11, $1, $2 # $11 = $1 + $2
    s_AddrOut   <=  "01011";
		s_AddrIn1   <=  "00001";
		s_AddrIn2   <=  "00010";
		s_Immediate <= x"00000000";
		s_AddSub    <=  '0';
		s_SImm      <=  '0';
    wait for cCLK_PER;
    
    -- sub $12, $11, $3 # $12 = $11 - $3
    s_AddrOut   <=  "01100";
		s_AddrIn1   <=  "01011";
		s_AddrIn2   <=  "00011";
		s_AddSub    <=  '1';
    wait for cCLK_PER;

		-- add $13, $12, $4 # $13 = $12 + $4
    s_AddrOut   <=  "01101";
		s_AddrIn1   <=  "01100";
		s_AddrIn2   <=  "00100";
		s_AddSub    <=  '0';
    wait for cCLK_PER;
    
    -- sub $14, $13, $5 # $14 = $13 - $5
    s_AddrOut   <=  "01110";
		s_AddrIn1   <=  "01101";
		s_AddrIn2   <=  "00101";
		s_AddSub    <=  '1';
    wait for cCLK_PER;
    
    -- add $15, $14, $6 # $15 = $14 + $6
    s_AddrOut   <=  "01111";
		s_AddrIn1   <=  "01110";
		s_AddrIn2   <=  "00110";
		s_AddSub    <=  '0';
    wait for cCLK_PER;
    
    -- sub $16, $15, $7 # $16 = $15 - $7
    s_AddrOut   <=  "10000";
		s_AddrIn1   <=  "01111";
		s_AddrIn2   <=  "00111";
		s_AddSub    <=  '1';
    wait for cCLK_PER;
    
    -- add $17, $16, $8 # $17 = $16 + $8
    s_AddrOut   <=  "10001";
		s_AddrIn1   <=  "10000";
		s_AddrIn2   <=  "01000";
		s_AddSub    <=  '0';
    wait for cCLK_PER;
    
    -- sub $18, $17, $9 # $18 = $17 - $9
    s_AddrOut   <=  "10010";
		s_AddrIn1   <=  "10001";
		s_AddrIn2   <=  "01001";
		s_AddSub    <=  '1';
    wait for cCLK_PER;
    
    -- add $19, $18, $10 # $19 = $18 + $10
    s_AddrOut   <=  "10011";
		s_AddrIn1   <=  "10010";
		s_AddrIn2   <=  "01010";
		s_AddSub    <=  '0';
    wait for cCLK_PER;
    
    -- addi $20, $0, 35 # Place "35" in $20
    s_AddrOut   <=  "10100";
		s_AddrIn1   <=  "00000";
		s_AddrIn2   <=  "00000";
		s_Immediate <= x"00000023";
		s_AddSub    <=  '0';
		s_SImm      <=  '1';
    wait for cCLK_PER;
    
    -- add $21, $19, $20 # $21 = $19 + $20
    s_AddrOut   <=  "10101";
		s_AddrIn1   <=  "10011";
		s_AddrIn2   <=  "10100";
		s_Immediate <= x"00000000";
		s_SImm      <=  '0';
    wait for cCLK_PER;

  end process;

end behavior;
