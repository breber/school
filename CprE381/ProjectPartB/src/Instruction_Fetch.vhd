-------------------------------------------------------------------------
-- Group 9
-- Project Part B 
-- Due November 9th, 2011
-------------------------------------------------------------------------

-- Instruction_Fetch.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is the overall instruction fetch component.  This does
--              all of the actual calculations to determine where it should
--              point to next.  It uses the Instruction_Fetch_Mux to then
--              tell which output shall be followed.
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;

entity Instruction_Fetch is
  
  port( clock       :     in  std_logic;
        RegValue    :     in  std_logic_vector( 31 downto 0 );
        zeroFromALU :     in  std_logic;
        isBranch    :     in  std_logic;
        isJump      :     in  std_logic;
        ALUResult   :     in  std_logic_vector( 31 downto 0 );
        whichJB     :     in  std_logic_vector( 3  downto 0 );
        reset       :     in  std_logic;
        PCplus4     :     out std_logic_vector( 31 downto 0 );
        instruction :     out std_logic_vector( 31 downto 0 ) 
        );
  
end Instruction_Fetch;

architecture mixed of Instruction_Fetch is

  -------------------------------------------------------------------
  --                         Components                            --  
  -------------------------------------------------------------------

  -- This is the Instruction_Fetch_Mux.
  -- It takes in all of the calculated possible outputs and chooses which one.
  component Instruction_Fetch_Mux is
    port( normal_increment    :   in  std_logic_vector( 31 downto 0 );
          jump                :   in  std_logic_vector( 31 downto 0 );
          jumpReg             :   in  std_logic_vector( 31 downto 0 );
          beq                 :   in  std_logic_vector( 31 downto 0 );
          bgez                :   in  std_logic_vector( 31 downto 0 );
          bgtz                :   in  std_logic_vector( 31 downto 0 );
          blez                :   in  std_logic_vector( 31 downto 0 );
          bltz                :   in  std_logic_vector( 31 downto 0 );
          bne                 :   in  std_logic_vector( 31 downto 0 );
          WhichJB             :   in  std_logic_vector( 3  downto 0 );      
          next_instruction    :   out std_logic_vector( 31 downto 0 )  );
  end component;

  -- Memory for Instruction Memory
  component mem is
	 generic(depth_exp_of_2 	: integer := 10;
			 mif_filename 	: string := "merge.mif");
	 port   (address			: IN STD_LOGIC_VECTOR (depth_exp_of_2-1 DOWNTO 0) := (OTHERS => '0');
			 byteena			: IN STD_LOGIC_VECTOR (3 DOWNTO 0) := (OTHERS => '1');
			 clock			: IN STD_LOGIC := '1';
			 data			: IN STD_LOGIC_VECTOR (31 DOWNTO 0) := (OTHERS => '0');
			 wren			: IN STD_LOGIC := '0';
			 q				: OUT STD_LOGIC_VECTOR (31 DOWNTO 0));         
  end component;

  -- 2-to-1 mux
  component mux_2to1n is
  
    generic( N  :   integer := 32 );
    port( i_A   :   in   std_logic_vector( N-1 downto 0);
          i_B   :   in   std_logic_vector( N-1 downto 0);
          sel   :   in   std_logic;
          o_R   :   out  std_logic_vector( N-1 downto 0));
        
  end component;
  
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
  
  component sixteen_to_32_extender is
    port(
		      i_In : in std_logic_vector(15 downto 0);
		      i_S	 : in std_logic;										-- 0 = zero extend, 1 = sign extend
		      o_F  : out std_logic_vector(31 downto 0)
		     );
  end component;



  ----------------------------------------------------------------------------
  --                                Signals                                 --
  ----------------------------------------------------------------------------
  
  -- These signals are for the inputs to the giant Instruction_Fetch_Mux
  signal s_jump, s_jumpReg, s_beq, s_bgez, s_bgezal, s_bgtz, s_blez, s_bltz, s_bltzal, s_bne, s_normal  :   std_logic_vector( 31 downto 0 );
  
  -- The current PC
  signal  s_CurrentPC  :   std_logic_vector( 31 downto 0 );
  
  -- Output of the big Instruction_Fetch_Mux. This will go back into the PC register
  signal  s_NextPC     :   std_logic_vector( 31 downto 0 );
  
  -- The output of the memory
  signal  s_MemOutput   :   std_logic_vector( 31 downto 0 );
  
  -- An intermediate signal for branch statements
  -- Essentially the Branch Target
  signal  s_BranchOffset  :   std_logic_vector( 31 downto 0 );

  -- Other intermediates
  signal  s_shifted   :   std_logic_vector( 31 downto 0 );
  signal  s_dontCare, s_stillDontCare  :   std_logic; -- we don't care what this is.
  signal  s_toAddPC  :   std_logic_vector( 31 downto 0 );
  signal  s_equality, s_zeroOrALURESULT31, s_notZeroAndNotALURESULT31   :   std_logic;

begin
  
  ------------------------------------------------------------------------
  --                       Components mapped out                        --    
  ------------------------------------------------------------------------
  
  -- PC Register
  PCReg : nbit_register
  port MAP( i_CLK   =>    clock,
            i_RST   =>    reset,
            i_WE    =>    '1', --this is a single-cycle processor
            i_D     =>    s_NextPC,
            o_Q     =>    s_CurrentPC   );
  
  -- Instruction memory
  Instr_Mem:  mem
  port MAP( 	address		=>   s_CurrentPC( 11 downto 2 ),
			       byteena		=>   "0000",   -- doesn't matter for this! :)
			       clock			 =>   clock,
			       data			  =>   x"00000000",  -- this doesn't matter either!
			       wren			  =>   '0',
			       q				    =>   s_MemOutput   );
  
  -- The big instruciton fetch mux at the end. (right before PC Register)
  IFMux : Instruction_Fetch_Mux
  port MAP( normal_increment    =>    s_normal,
            jump                =>    s_jump,
            jumpReg             =>    s_jumpReg,
            beq                 =>    s_beq,
            bgez                =>    s_bgez,
            bgtz                =>    s_bgtz,
            blez                =>    s_blez,
            bltz                =>    s_bltz,
            bne                 =>    s_bne,
            WhichJB             =>    WhichJB,
            next_instruction    =>    s_NextPC    );
  
  -- Sign-extends the immediate value of the instruction
  signExtender  :   sixteen_to_32_extender
  port MAP( i_in    =>    s_MemOutput( 15 downto 0 ),
            i_S     =>    '1', -- sign extending
            o_F     =>    s_shifted   );
    
  -- Adds PC+4 to immediate value        
  s_toAddPC <=  s_shifted( 29 downto 0 ) & "00";
  BranchAdder : fullAdder_n
  port MAP( i_A       =>    s_toAddPC,
            i_B       =>    s_normal,
            i_CryIn   =>    '0',
            o_Sum     =>    s_BranchOffset,
            o_CryOut  =>    s_dontcare    );
            
  
  ------------------------------------------------------------------------
  --               Inputs to the giant mux at the end                   --    
  ------------------------------------------------------------------------
        
  -- s_normal.  This just means that it is not a branch or a jump
  NormalAdder : fullAdder_n
  port MAP( i_A       =>    s_CurrentPC,
            i_B       =>    x"00000004",
            i_CryIn   =>    '0',
            o_Sum     =>    s_normal,
            o_CryOut  =>    s_StillDontCare    );        
  
  -- s_jump
  s_jump    <=  s_normal( 31 downto 28 ) & s_MemOutput( 25 downto 0 ) & "00";
  
  -- s_jumpReg
  s_jumpReg <=  RegValue;  
  
  s_equality  <=  isBranch and ZeroFromALU;
  
  -- s_beq
  beqMux  :  mux_2to1n
  port MAP( i_A   =>  s_normal,
            i_B   =>  s_BranchOffset,
            sel   =>  s_equality,
            o_R   =>  s_beq   );
            
  -- s_bne
  bneMux  : mux_2to1n
  port MAP( i_A   =>  s_BranchOffset,
            i_B   =>  s_normal,
            sel   =>  s_equality,
            o_R   =>  s_bne   );
            
  -- s_bgez
  bgezMux : mux_2to1n
  port MAP( i_A   =>  s_BranchOffset,
            i_B   =>  s_normal,
            sel   =>  ALUResult( 0 ),
            o_R   =>  s_bgez    );

  -- s_bltz
  bltzMux : mux_2to1n
  port MAP( i_A   =>  s_normal,
            i_B   =>  s_BranchOffset,
            sel   =>  ALUResult( 0 ),
            o_R   =>  s_bltz    );
          
  s_notZeroAndNotALURESULT31  <=  ( not zeroFromALU ) and ( not ALUResult( 31 ) );
          
  -- s_bgtz  
  bgtzMux : mux_2to1n
  port MAP( i_A   =>  s_normal,
            i_B   =>  s_BranchOffset,
            sel   =>  s_notZeroAndNotALURESULT31,
            o_R   =>  s_bgtz    );
           
  s_zeroOrALURESULT31   <=    ( zeroFromALU ) or ( ALUResult( 31 ) );
     
  -- s_blez
  blezMux : mux_2to1n
  port MAP( i_A   =>  s_normal,
            i_B   =>  s_BranchOffset,
            sel   =>  s_zeroOrALURESULT31,
            o_R   =>  s_blez    );


  -----------------------------------------------------------------------------
  -- This is the final outputs of this component! From the Instruction Memory --
  -----------------------------------------------------------------------------
	instruction  <=  s_MemOutput;
  PCPlus4      <=  s_normal;
  
end mixed;