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
end simple_proc;

architecture structure of simple_proc is

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

-- Declare the components we are going to instantiate
component mem
	generic(
			   depth_exp_of_2 : integer := 12;
			   mif_filename 	: string := "dmem.mif"
				 );
				 
	port(
			address	: IN STD_LOGIC_VECTOR (depth_exp_of_2-1 DOWNTO 0) := (OTHERS => '0');
			byteena	: IN STD_LOGIC_VECTOR (3 DOWNTO 0) := (OTHERS => '1');
			clock		: IN STD_LOGIC := '1';
			data		: IN STD_LOGIC_VECTOR (31 DOWNTO 0) := (OTHERS => '0');
			wren		: IN STD_LOGIC := '0';
			q				: OUT STD_LOGIC_VECTOR (31 DOWNTO 0)
			);
end component;

component eight_to_32_extender
	port(
		  i_In : in std_logic_vector(7 downto 0);
  		i_S	 : in std_logic;										-- 0 = zero extend, 1 = sign extend
  		o_F  : out std_logic_vector(31 downto 0)
  		);
end component;

component sixteen_to_32_extender
	port(
		  i_In : in std_logic_vector(15 downto 0);
  		i_S	 : in std_logic;										-- 0 = zero extend, 1 = sign extend
  		o_F  : out std_logic_vector(31 downto 0)
  		);
end component;

component nbit_2_1_mux
	port(
		  i_A  : in std_logic_vector(31 downto 0);
  		i_B  : in std_logic_vector(31 downto 0);
  		i_S  : in std_logic;
  		o_F  : out std_logic_vector(31 downto 0)
  		);
end component;

component nbit_4_1_mux
	generic(N : integer := 32);
	port(
		i_A  : in std_logic_vector(N-1 downto 0);
		i_B  : in std_logic_vector(N-1 downto 0);
		i_C  : in std_logic_vector(N-1 downto 0);
		i_D  : in std_logic_vector(N-1 downto 0);
		i_S  : in std_logic_vector(1 downto 0);
		o_F  : out std_logic_vector(N-1 downto 0)
		);
end component;

signal o_RAdd1, o_RAdd2, o_AddSub, o_QVal, o_MemData, 
			 o_WDataRF, o_Imm, o_16Extend, o_8Extend : std_logic_vector(31 downto 0);
signal o_Cout : std_logic;

begin
	IMMEXT : sixteen_to_32_extender
		port map(
						i_In => i_Imm,
						i_S  => '1',
						o_F  => o_Imm
						);

	RF : register_file
		port map(
				    i_CLK    => i_CLK,
    				i_AdIn1  => i_AdIn1,
    				i_AdIn2  => i_AdIn2,
    				i_AdOut  => i_AdOut,
    				i_WData  => o_WDataRF,
    				i_WE     => i_WE,
    				o_RData1 => o_RAdd1,
    				o_RData2 => o_RAdd2
    				);

	ADDSUB : nbit_add_sub
		port map(
        		i_X      => o_RAdd1,
        		i_Y      => o_RAdd2,
        		i_Imm    => o_Imm,
		        nAdd_Sub => i_AddSub,
        		ALUSrc   => i_SImm,
	        	o_Cout   => o_Cout,
	        	o_S	     => o_AddSub
	        	);
	        	
	WDATARF : nbit_2_1_mux
	  port map(
	          i_A => o_AddSub,
	          i_B => o_MemData,
	          i_S => i_SelMemAdder,
	          o_F => o_WDataRF
	          );
	        	
	DMEM : mem
	  port map(
	          address => o_AddSub(13 downto 2), -- The lower 12 bits act as address
	          																	-- Since our memory module is word-addressable,
	          																	-- we need to divide by 4 (shift by 2) to get the
	          																	-- correct word address
	          byteena => "1111", -- TODO: byteena input
	          clock   => i_CLK,
	          data    => o_RAdd2,
	          wren    => i_WEMem,
	          q       => o_QVal
	          );
	          
	EIGHTEXTEND : eight_to_32_extender
		port map(
						i_In => o_QVal(7 downto 0), -- TODO: which bits?
						i_S  => i_Signed,
						o_F  => o_8Extend
						);   

	SIXTEENEXTEND : sixteen_to_32_extender
		port map(
						i_In => o_QVal(15 downto 0), -- TODO: which bits?
						i_S  => i_Signed,
						o_F  => o_16Extend
						);
						
	OUTMEM : nbit_4_1_mux
		port map(
						i_A => o_QVal,
						i_B => o_8Extend,
						i_C => o_16Extend,
						i_D => x"00000000",
						i_S => i_Size,
						o_F => o_MemData
						);

end structure;
