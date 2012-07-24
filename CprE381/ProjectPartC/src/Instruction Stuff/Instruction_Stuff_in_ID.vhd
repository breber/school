-------------------------------------------------------------------------
-- Group 9
-- Project Part C 
-- Due December 8th, 2011
-------------------------------------------------------------------------

-- Instruction_Stuff_in_ID.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: In our single-cycle processor, we put all of the instruction
--              handling as a single component.  Now we cannot do this as
--              we need it to be in 2 parts in both the IF and the ID
--              stages.  This is the component that will go in the ID
--              stage of the pipeline.
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

entity Instruction_Stuff_in_ID is
  
  port( RegValue    :     in  std_logic_vector( 31 downto 0 );
        zeroFromALU :     in  std_logic;
        isBranch    :     in  std_logic;
        isJump      :     in  std_logic;
        ALUResult   :     in  std_logic_vector( 31 downto 0 );
        whichJB     :     in  std_logic_vector( 3  downto 0 );
        PCplus4     :     in  std_logic_vector( 31 downto 0 );
        instruction :     in  std_logic_vector( 31 downto 0 );
        o_nextPC    :     out std_logic_vector( 31 downto 0 )
        );
  
end Instruction_Stuff_in_ID;

architecture mixed of Instruction_Stuff_in_ID is

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
  signal s_jump, s_jumpReg, s_beq, s_bgez, s_bgtz, s_blez, s_bltz, s_bne :   std_logic_vector( 31 downto 0 );
  signal PCPlus8                                              :   std_logic_vector ( 31 downto 0 );
    
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
  
  -- PC + 8 Adder
  PCPlus8aDDER : fullAdder_n
  port MAP( i_A       =>    PCPlus4,
            i_B       =>    x"00000000",
            i_CryIn   =>    '0',
            o_Sum     =>    PCPlus8,
            o_CryOut  =>    s_StillDontCare    ); 
  
  -- The big instruciton fetch mux at the end. (right before PC Register)
  IFMux : Instruction_Fetch_Mux
  port MAP( normal_increment    =>    PCPlus8,
            jump                =>    s_jump,
            jumpReg             =>    s_jumpReg,
            beq                 =>    s_beq,
            bgez                =>    s_bgez,
            bgtz                =>    s_bgtz,
            blez                =>    s_blez,
            bltz                =>    s_bltz,
            bne                 =>    s_bne,
            WhichJB             =>    WhichJB,
            next_instruction    =>    o_NextPC    );
  
  -- Sign-extends the immediate value of the instruction
  signExtender  :   sixteen_to_32_extender
  port MAP( i_in    =>    instruction( 15 downto 0 ),
            i_S     =>    '1', -- sign extending
            o_F     =>    s_shifted   );
            
            
  -- Adds PC+4 to immediate value        
  s_toAddPC <=  s_shifted( 29 downto 0 ) & "00";
  BranchAdder : fullAdder_n
  port MAP( i_A       =>    s_toAddPC,
            i_B       =>    PCplus8,
            i_CryIn   =>    '0',
            o_Sum     =>    s_BranchOffset,
            o_CryOut  =>    s_dontcare    );
  
  ------------------------------------------------------------------------
  --               Inputs to the giant mux at the end                   --    
  ------------------------------------------------------------------------    
  
  -- s_jump
  s_jump    <=  PCplus8( 31 downto 28 ) & instruction( 25 downto 0 ) & "00";
  
  -- s_jumpReg
  s_jumpReg <=  RegValue;  
  
  s_equality  <=  isBranch and ZeroFromALU;
  
  -- s_beq
  beqMux  :  mux_2to1n
  port MAP( i_A   =>  PCplus8,
            i_B   =>  s_BranchOffset,
            sel   =>  s_equality,
            o_R   =>  s_beq   );
            
  -- s_bne
  bneMux  : mux_2to1n
  port MAP( i_A   =>  s_BranchOffset,
            i_B   =>  PCplus8,
            sel   =>  s_equality,
            o_R   =>  s_bne   );
            
  -- s_bgez
  bgezMux : mux_2to1n
  port MAP( i_A   =>  s_BranchOffset,
            i_B   =>  PCplus8,
            sel   =>  ALUResult( 0 ),
            o_R   =>  s_bgez    );

  -- s_bltz
  bltzMux : mux_2to1n
  port MAP( i_A   =>  PCplus8,
            i_B   =>  s_BranchOffset,
            sel   =>  ALUResult( 0 ),
            o_R   =>  s_bltz    );
          
  s_notZeroAndNotALURESULT31  <=  ( not zeroFromALU ) and ( not ALUResult( 31 ) );
          
  -- s_bgtz  
  bgtzMux : mux_2to1n
  port MAP( i_A   =>  PCplus8,
            i_B   =>  s_BranchOffset,
            sel   =>  s_notZeroAndNotALURESULT31,
            o_R   =>  s_bgtz    );
           
  s_zeroOrALURESULT31   <=    ( zeroFromALU ) or ( ALUResult( 31 ) );
     
  -- s_blez
  blezMux : mux_2to1n
  port MAP( i_A   =>  PCplus8,
            i_B   =>  s_BranchOffset,
            sel   =>  s_zeroOrALURESULT31,
            o_R   =>  s_blez    );
  
end mixed;
