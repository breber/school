-------------------------------------------------------------------------
-- Group 9
-- Project Part C 
-- Due December 8th, 2011
-------------------------------------------------------------------------

-- Instruction_Stuff_in_IF.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: In our single-cycle processor, we put all of the instruction
--              handling as a single component.  Now we cannot do this as
--              we need it to be in 2 parts in both the IF and the ID
--              stages.  This is the component that will go in the IF
--              stage of the pipeline.
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

entity Instruction_Stuff_in_IF is
  
  port (  nextPC      :   in  std_logic_vector( 31 downto 0 );
          WE          :   in  std_logic;
          clock       :   in  std_logic;
          reset       :   in  std_logic;
          instruction :   out std_logic_vector( 31 downto 0 );
          PCPlus4     :   out std_logic_vector( 31 downto 0 )   );
          
end Instruction_Stuff_in_IF;


architecture structure of Instruction_Stuff_in_IF is

  -- Full Adder
  component fullAdder_n is
    generic( N      :   integer := 32 );
    port( i_A       :   in   std_logic_vector( N-1 downto 0 );
          i_B       :   in   std_logic_vector( N-1 downto 0 );
          i_CryIn   :   in   std_logic;
          o_Sum     :   out  std_logic_vector( N-1 downto 0 );
          o_CryOut  :   out  std_logic);
  end component;

  -- Register File to store PC
  component nbit_register is
    generic(N : integer := 32);
    port(
			    i_CLK : in std_logic;     -- Clock input
          i_RST : in std_logic;     -- Reset input
          i_WE  : in std_logic;     -- Write enable input
          i_D   : in std_logic_vector(N-1 downto 0);     -- Data value input
          o_Q   : out std_logic_vector(N-1 downto 0)
			   );
  end component;
  
  -- Memory for Instruction Memory
  component mem is
	 generic(depth_exp_of_2 	: integer := 10;
			 --mif_filename 	   : string  :=  "merge.mif");     -- IMEM FILE HERE! merge.mif, bubblesort_if.mif, allinstructions.mif
			 --mif_filename     : string  :=  "mergenops.mif" );
			 --mif_filename     : string  :=  "bubblesort_if.mif");
			 --mif_filename     : string  :=  "allinstructions.mif");
			 --mif_filename     : string  :=  "taylorsin.mif");
			 --mif_filename     : string  :=  "allinstructionsNoDepend.mif");
			 --mif_filename     : string  :=  "bubbleLOLS.mif"  );
			 --mif_filename     : string  :=  "lolol.mif"   );
       --mif_filename     : string  :=  "bubblenopsafterbranches.mif" );
       --mif_filename     : string  :=  "bubblebriannops.mif"   );
       --mif_filename     : string  :=  "bubblebriannonops.mif"    );
       mif_filename      :  string  :=  "MergeNops.mif"   );
	 port   (address			: IN STD_LOGIC_VECTOR (depth_exp_of_2-1 DOWNTO 0) := (OTHERS => '0');
			 byteena			: IN STD_LOGIC_VECTOR (3 DOWNTO 0) := (OTHERS => '1');
			 clock			: IN STD_LOGIC := '1';
			 data			: IN STD_LOGIC_VECTOR (31 DOWNTO 0) := (OTHERS => '0');
			 wren			: IN STD_LOGIC := '0';
			 q				: OUT STD_LOGIC_VECTOR (31 DOWNTO 0));         
  end component;

  signal s_CurrentPC :  std_logic_vector( 31 downto 0 );
  signal s_StillDontCare  : std_logic;

begin

  -- PC Register
  PCReg : nbit_register
  port MAP( i_CLK   =>    clock,
            i_RST   =>    reset,
            i_WE    =>    WE,
            i_D     =>    nextPC,
            o_Q     =>    s_CurrentPC   );
  
  -- PC + 4 Adder
  PCPlus4aDDER : fullAdder_n
  port MAP( i_A       =>    s_CurrentPC,
            i_B       =>    x"00000004",
            i_CryIn   =>    '0',
            o_Sum     =>    PCplus4,
            o_CryOut  =>    s_StillDontCare    );  
  
  -- Instruction memory
  Instr_Mem:  mem
  port MAP( 	address		=>   s_CurrentPC( 11 downto 2 ),
			       byteena		=>   "0000",   -- doesn't matter for this! :)
			       clock			 =>   clock,
			       data			  =>   x"00000000",  -- this doesn't matter either!
			       wren			  =>   '0',
			       q				    =>   instruction   );
  
end structure;
