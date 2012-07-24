-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- tb_simpleproc.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a simple VHDL testbench for our
-- simple processor.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

-- This is an empty entity so we don't need to declare ports
entity tb_simpleproc is
  generic(gCLK_HPER   : time := 50 ns);
end tb_simpleproc;

architecture behavior of tb_simpleproc is
  
  -- Calculate the clock period as twice the half-period
  constant cCLK_PER  : time := gCLK_HPER * 2;

-- Declare the components we are going to instantiate
component simple_proc
  port(
			i_CLK 	 			: in std_logic;     -- Clock input
      i_AdIn1  			: in std_logic_vector(4 downto 0);
      i_AdIn2  			: in std_logic_vector(4 downto 0);
      i_AdOut  			: in std_logic_vector(4 downto 0);
      i_Imm    			: in std_logic_vector(15 downto 0);
      i_AddSub 			: in std_logic;		-- Whether we are adding or subtracting
      i_SImm   			: in std_logic;		-- Whether to use R2 data or the immediate
      i_WE     			: in std_logic;		-- Whether we should enable writing for the RF
      i_WEMem  			: in std_logic;		-- Whether we should enable writing for the Memory module
      i_SelMemAdder : in std_logic;		-- 0 = choose data from adder/subtractor, 1 = data from memory module
      i_Signed			: in std_logic;		-- Whether to sign extend the result or not
      i_Size  			: in std_logic_vector(1 downto 0)		-- 00 = 32 bit, 01 = 16-bit, 10 = 8-bit
			);
end component;


-- Address signal
signal s_AddrIn1, s_AddrIn2, s_AddrOut : std_logic_vector(4 downto 0);

signal s_Size : std_logic_vector(1 downto 0);

-- Byte Enable
signal s_ByteEna : std_logic_vector(3 downto 0);

-- 16 bit Immediate signal
signal s_Imm : std_logic_vector(15 downto 0);

-- Width 32 bit signals
signal s_QVal, s_Data : std_logic_vector(31 downto 0);

-- Control Signals
signal s_CLK, s_AddSub, s_SImm, s_WE, s_WEMem, s_SelMemAdder, s_Signed : std_logic;

begin

MPROC : simple_proc
  port map(
					i_CLK 	 			=> s_CLK,
		      i_AdIn1  		  => s_AddrIn1,
		      i_AdIn2  			=> s_AddrIn2,
		      i_AdOut  			=> s_AddrOut,
		      i_Imm    			=> s_Imm,
		      i_AddSub 			=> s_AddSub,
  		    i_SImm   			=> s_SImm,
		      i_WE     			=> s_WE,
    		  i_WEMem  			=> s_WEMem,
		      i_SelMemAdder => s_SelMemAdder,
		      i_Signed			=> s_Signed,
		      i_Size				=> s_Size
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

		-- addi $25, $0, 0	# Load &A into $25
		s_AddrIn1 		<=  "00000";
		s_AddrIn2 		<=  "00000";
		s_AddrOut 		<=  "11001";
		s_Imm 				<= x"0000";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for cCLK_PER;
    
		-- addi $26, $0, 256	# Load &B into $26
		s_AddrIn1 		<=  "00000";
		s_AddrIn2 		<=  "00000";
		s_AddrOut 		<=  "11010";
		s_Imm 				<= x"0100";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for cCLK_PER;
    
		-- lw $1, 0($25)	# Load A[0] into $1
		s_AddrIn1 		<=  "11001";
		s_AddrIn2 		<=  "00000";
		s_AddrOut 		<=  "00001";
		s_Imm 				<= x"0000";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '1';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for cCLK_PER;
    
    -- lhu $2, 4($25)	# Load A[1] into $2
		s_AddrIn1 		<=  "11001";
		s_AddrIn2 		<=  "00000";
		s_AddrOut 		<=  "00010";
		s_Imm 				<= x"0004";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '1';
		s_Signed			<=  '0';
		s_Size				<=  "01";
    wait for cCLK_PER;
    
    -- add $1, $1, $2	# $1 = $1 + $2
		s_AddrIn1 		<=  "00001";
		s_AddrIn2 		<=  "00010";
		s_AddrOut 		<=  "00001";
		s_Imm 				<= x"0000";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '0';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for cCLK_PER;
    
    -- sw $1, 0($26)	# Store $1 into B[0]
		s_AddrIn1 		<=  "11010";
		s_AddrIn2 		<=  "00001";
		s_AddrOut 		<=  "00000";
		s_Imm 				<= x"0000";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '0';
		s_WEMem				<=  '1';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for 2 * cCLK_PER;
    
    -- lb $2, 8($25)	# Load A[2] into $2
		s_AddrIn1 		<=  "11001";
		s_AddrIn2 		<=  "00000";
		s_AddrOut 		<=  "00010";
		s_Imm 				<= x"000A";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '1';
		s_Signed			<=  '1';
		s_Size				<=  "10";
    wait for cCLK_PER;
    
    -- add $1, $1, $2	# $1 = $1 + $2
		s_AddrIn1 		<=  "00001";
		s_AddrIn2 		<=  "00010";
		s_AddrOut 		<=  "00001";
		s_Imm 				<= x"0000";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '0';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for cCLK_PER;
    
    -- sw $1, 4($26)	# Store $1 into B[1]
		s_AddrIn1 		<=  "11010";
		s_AddrIn2 		<=  "00001";
		s_AddrOut 		<=  "00000";
		s_Imm 				<= x"0004";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '0';
		s_WEMem				<=  '1';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for 2 * cCLK_PER;
    
    -- lbu $2, 12($25)	# Load A[3] into $2
		s_AddrIn1 		<=  "11001";
		s_AddrIn2 		<=  "00000";
		s_AddrOut 		<=  "00010";
		s_Imm 				<= x"000C";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '1';
		s_Signed			<=  '0';
		s_Size				<=  "10";
    wait for cCLK_PER;
    
    -- add $1, $1, $2	# $1 = $1 + $2
		s_AddrIn1 		<=  "00001";
		s_AddrIn2 		<=  "00010";
		s_AddrOut 		<=  "00001";
		s_Imm 				<= x"0000";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '0';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for cCLK_PER;
    
    -- sw $1, 8($26)	# Store $1 into B[2]
		s_AddrIn1 		<=  "11010";
		s_AddrIn2 		<=  "00001";
		s_AddrOut 		<=  "00000";
		s_Imm 				<= x"0008";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '0';
		s_WEMem				<=  '1';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for 2 * cCLK_PER;
    
    -- lw $2, 16($25)	# Load A[4] into $2
		s_AddrIn1 		<=  "11001";
		s_AddrIn2 		<=  "00000";
		s_AddrOut 		<=  "00010";
		s_Imm 				<= x"0010";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '1';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for cCLK_PER;
    
    -- add $1, $1, $2	# $1 = $1 + $2
		s_AddrIn1 		<=  "00001";
		s_AddrIn2 		<=  "00010";
		s_AddrOut 		<=  "00001";
		s_Imm 				<= x"0000";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '0';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for cCLK_PER;
    
    -- sw $1, 12($26)	# Store $1 into B[3]
		s_AddrIn1 		<=  "11010";
		s_AddrIn2 		<=  "00001";
		s_AddrOut 		<=  "00000";
		s_Imm 				<= x"000C";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '0';
		s_WEMem				<=  '1';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for 2 * cCLK_PER;
    
    -- lw $2, 20($25)	# Load A[5] into $2
		s_AddrIn1 		<=  "11001";
		s_AddrIn2 		<=  "00000";
		s_AddrOut 		<=  "00010";
		s_Imm 				<= x"0014";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '1';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for cCLK_PER;
    
    -- add $1, $1, $2	# $1 = $1 + $2
		s_AddrIn1 		<=  "00001";
		s_AddrIn2 		<=  "00010";
		s_AddrOut 		<=  "00001";
		s_Imm 				<= x"0000";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '0';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for cCLK_PER;
    
    -- sw $1, 16($26)	# Store $1 into B[4]
		s_AddrIn1 		<=  "11010";
		s_AddrIn2 		<=  "00001";
		s_AddrOut 		<=  "00000";
		s_Imm 				<= x"0010";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '0';
		s_WEMem				<=  '1';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for 2 * cCLK_PER;
    
    -- lw $2, 24($25)	# Load A[6] into $2
		s_AddrIn1 		<=  "11001";
		s_AddrIn2 		<=  "00000";
		s_AddrOut 		<=  "00010";
		s_Imm 				<= x"0018";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '1';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for cCLK_PER;
    
    -- add $1, $1, $2	# $1 = $1 + $2
		s_AddrIn1 		<=  "00001";
		s_AddrIn2 		<=  "00010";
		s_AddrOut 		<=  "00001";
		s_Imm 				<= x"0000";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '0';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for cCLK_PER;
    
    -- addi $27, $0, 512	# Load &B[256] into $27
		s_AddrIn1 		<=  "00000";
		s_AddrIn2 		<=  "00000";
		s_AddrOut 		<=  "11011";
		s_Imm 				<= x"0200";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '1';
		s_WEMem				<=  '0';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for cCLK_PER;
    
    -- sw $1, -4($27)	# Store $1 into B[255]
		s_AddrIn1 		<=  "11011";
		s_AddrIn2 		<=  "00001";
		s_AddrOut 		<=  "00000";
		s_Imm 				<= x"FFFC";
		s_AddSub  		<=  '0';
		s_SImm	  		<=  '1';
		s_WE					<=  '0';
		s_WEMem				<=  '1';
		s_SelMemAdder	<=  '0';
		s_Signed			<=  '1';
		s_Size				<=  "00";
    wait for 2 * cCLK_PER;
    
  end process;

end behavior;
