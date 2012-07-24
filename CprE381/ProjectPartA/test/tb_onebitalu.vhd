-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- tb_onebitalu.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a simple VHDL testbench for our
-- one bit ALU
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

-- This is an empty entity so we don't need to declare ports
entity tb_onebitalu is
	generic(gCLK_HPER   : time := 50 ns);
end tb_onebitalu;

architecture behavior of tb_onebitalu is
  
  -- Calculate the clock period as twice the half-period
  constant cCLK_PER  : time := gCLK_HPER * 2;

-- Declare the components we are going to instantiate
component OneBitALU
	port(  i_Aalu   :   in  std_logic;
         i_Balu   :   in  std_logic;
         i_CryIn  :   in  std_logic;
         i_OpCode :   in  std_logic_vector( 2 downto 0 );
         i_Ainv   :   in  std_logic;
         i_Binv   :   in  std_logic;
         o_Result :   out std_logic;
         o_CryOut :   out std_logic   );
end component;


-- One bit signals
signal s_CLK, s_Aalu, s_Balu, s_CryIn, s_Result, s_CryOut, s_Ainv, s_Binv : std_logic;

-- OpCode Signal
signal s_OpCode : std_logic_vector( 2 downto 0 );

begin

ALU : OneBitALU
	port map(  i_Aalu   =>   s_Aalu,
         	 	 i_Balu   =>   s_Balu,
		         i_CryIn  =>   s_CryIn,
		         i_OpCode =>   s_OpCode,
		         i_Ainv   =>   s_Ainv,
		         i_Binv   =>   s_Binv,
		         o_Result =>   s_Result,
		         o_CryOut =>   s_CryOut   );
					
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
    
    --start off with no inversion at all
    s_Ainv  <=  '0';
    s_Binv  <=  '0';
    
    --------------------------------------------------
    --                   AND                        --
    --------------------------------------------------

		-- Testing 'and' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "000";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'and' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "000";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'and' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "000";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
        
    
    --------------------------------------------------
    --                   OR                         --
    --------------------------------------------------
    
    -- Testing 'or' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'or' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'or' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'or' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                   XOR                        --
    --------------------------------------------------   
    
    -- Testing 'xor' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'xor' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'xor' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'xor' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                  NAND                        --
    --------------------------------------------------
    
    -- Testing 'nand' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nand' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nand' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nand' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                   NOR                        --
    --------------------------------------------------
    
    -- Testing 'nor' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nor' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nor' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nor' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                   ADD                        --
    --------------------------------------------------
    
    -- Testing 'add' operation with '0' and '0' inputs, '0' carry in
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '0' and '0' inputs, '1' carry in
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '0' and '1' inputs, '0' carry in
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '0' and '1' inputs, '1' carry in
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '0' inputs, '0' carry in
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '0' inputs, '1' carry in
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '1' inputs, '0' carry in
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '1' inputs, '1' carry in
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --              INVERSION CHECKS                --
    --------------------------------------------------
    -- Now invert A, keep B the same.
    --------------------------------------------------
    s_Ainv  <=  '1';
    s_Binv  <=  '0';
    
    --------------------------------------------------
    --                   AND                        --
    --------------------------------------------------

		-- Testing 'and' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "000";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'and' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "000";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'and' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "000";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
        
    
    --------------------------------------------------
    --                   OR                         --
    --------------------------------------------------
    
    -- Testing 'or' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'or' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'or' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'or' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                   XOR                        --
    --------------------------------------------------   
    
    -- Testing 'xor' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'xor' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'xor' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'xor' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                  NAND                        --
    --------------------------------------------------
    
    -- Testing 'nand' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nand' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nand' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nand' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                   NOR                        --
    --------------------------------------------------
    
    -- Testing 'nor' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nor' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nor' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nor' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                   ADD                        --
    --------------------------------------------------
    
    -- Testing 'add' operation with '0' and '0' inputs, '0' carry in
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '0' and '0' inputs, '1' carry in
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '0' and '1' inputs, '0' carry in
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '0' and '1' inputs, '1' carry in
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '0' inputs, '0' carry in
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '0' inputs, '1' carry in
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '1' inputs, '0' carry in
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '1' inputs, '1' carry in
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '1'
    wait for cCLK_PER;

    --------------------------------------------------
    --              INVERSION CHECKS                --
    --------------------------------------------------
    -- Now invert B, keep A the same.
    --------------------------------------------------
    s_Ainv  <=  '0';
    s_Binv  <=  '1';
    
    --------------------------------------------------
    --                   AND                        --
    --------------------------------------------------

		-- Testing 'and' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "000";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'and' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "000";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'and' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "000";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
        
    
    --------------------------------------------------
    --                   OR                         --
    --------------------------------------------------
    
    -- Testing 'or' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'or' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'or' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'or' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                   XOR                        --
    --------------------------------------------------   
    
    -- Testing 'xor' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'xor' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'xor' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'xor' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                  NAND                        --
    --------------------------------------------------
    
    -- Testing 'nand' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nand' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nand' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nand' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                   NOR                        --
    --------------------------------------------------
    
    -- Testing 'nor' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nor' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nor' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nor' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                   ADD                        --
    --------------------------------------------------
    
    -- Testing 'add' operation with '0' and '0' inputs, '0' carry in
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '0' and '0' inputs, '1' carry in
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '0' and '1' inputs, '0' carry in
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '0' and '1' inputs, '1' carry in
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '0' inputs, '0' carry in
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '0' inputs, '1' carry in
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '1' inputs, '0' carry in
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '1' inputs, '1' carry in
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    --------------------------------------------------
    --              INVERSION CHECKS                --
    --------------------------------------------------
    -- Now invert A and B both
    --------------------------------------------------
    s_Ainv  <=  '1';
    s_Binv  <=  '1';
    
    --------------------------------------------------
    --                   AND                        --
    --------------------------------------------------

		-- Testing 'and' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "000";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'and' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "000";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'and' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "000";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
        
    
    --------------------------------------------------
    --                   OR                         --
    --------------------------------------------------
    
    -- Testing 'or' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'or' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'or' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'or' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "001";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                   XOR                        --
    --------------------------------------------------   
    
    -- Testing 'xor' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'xor' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'xor' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'xor' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "010";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                  NAND                        --
    --------------------------------------------------
    
    -- Testing 'nand' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nand' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nand' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nand' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "011";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                   NOR                        --
    --------------------------------------------------
    
    -- Testing 'nor' operation with '0' and '0' inputs
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nor' operation with '0' and '1' inputs
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nor' operation with '1' and '0' inputs
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'nor' operation with '1' and '1' inputs
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "100";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    
    --------------------------------------------------
    --                   ADD                        --
    --------------------------------------------------
    
    -- Testing 'add' operation with '0' and '0' inputs, '0' carry in
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '0' and '0' inputs, '1' carry in
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '0' and '1' inputs, '0' carry in
		s_Aalu   <= '0';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '0' and '1' inputs, '1' carry in
		s_Aalu   <= '0';
		s_Balu   <= '0';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '0' inputs, '0' carry in
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '0'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '0' inputs, '1' carry in
		s_Aalu   <= '1';
		s_Balu   <= '0';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '1' inputs, '0' carry in
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '0';
		s_OpCode <= "101";
		-- s_Result == '0'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    -- Testing 'add' operation with '1' and '1' inputs, '1' carry in
		s_Aalu   <= '1';
		s_Balu   <= '1';
		s_CryIn  <= '1';
		s_OpCode <= "101";
		-- s_Result == '1'
		-- s_CryOut == '1'
    wait for cCLK_PER;
    
    
  end process;

end behavior;
