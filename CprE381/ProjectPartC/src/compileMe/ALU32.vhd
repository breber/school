-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Lab Date: 09/28/2011 + 10/5/2011
-------------------------------------------------------------------------


-- ALU32.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is a thirty-two bit ALU that supports the operations:
--              
--              and   000
--              or    001
--              xor   010
--              nand  011
--              nor   100
--              add   101
--              slt   110
--              sub   111
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

entity ALU32 is

  port( i_A       :   in  std_logic_vector( 31 downto 0 );
        i_B       :   in  std_logic_vector( 31 downto 0 );
        i_Opcode  :   in  std_logic_vector( 2  downto 0 );
        o_F       :   out std_logic_vector( 31 downto 0 );
        o_CryOut  :   out std_logic;
        o_Overflow:   out std_logic;
        o_Zero    :   out std_logic  );

end ALU32;


architecture structural of ALU32 is

  ------------------------------------------------------------
  --    Components:

  -- 1-bit ALU
  component OneBitALU is
    port(   i_Aalu   :   in  std_logic;
            i_Balu   :   in  std_logic;
            i_CryIn  :   in  std_logic;
            i_Ainv   :   in  std_logic;
            i_Binv   :   in  std_logic;
            i_OpCode :   in  std_logic_vector( 2 downto 0 );
            o_Result :   out std_logic;
            o_CryOut :   out std_logic   );
  end component;
            
  -- Opcode Translator
  component opcodetranslator is
    port(   i_Opcode   :    in  std_logic_vector( 2 downto 0 );
            o_Ainv     :    out std_logic;
            o_Binv     :    out std_logic;
            o_CryIn    :    out std_logic;
            o_OpCode   :    out std_logic_vector( 2 downto 0 ) );
  end component;
            
  -- 2-bit xor
  component xor2 is
      port(i_A          : in std_logic;
           i_B          : in std_logic;
           o_F          : out std_logic );
  end component;
  
  -- 32-bit nor
  component nor32 is
      port(i_In      : in  std_logic_vector( 31 downto 0 );
           o_F      : out std_logic );
  end component;
  
  -- SLT "mux"
  component slt_mux is
      port(i_Sel 	  : in  std_logic_vector(2 downto 0);
           i_Input  : in  std_logic_vector(31 downto 0);
           o_F			   : out std_logic_vector(31 downto 0)  );
  end component;

  ------------------------------------------------------------
  --    Signals:
  signal  s_Ainv, s_Binv    :   std_logic;
  signal  s_CryIn    :   std_logic_vector( 32 downto 0 );
  signal  s_Opcode   :   std_logic_vector( 2  downto 0 );  -- 1-bit level opcode
  signal  s_R        :   std_logic_vector( 31 downto 0 );

begin
  
  g_opcodeTranslator  :   opcodetranslator
  port MAP(   i_Opcode    =>    i_Opcode,
              o_Ainv      =>    s_Ainv,
              o_Binv      =>    s_Binv,
              o_CryIn     =>    s_CryIn( 0 ),
              o_OpCode    =>    s_Opcode  );
  
  
  I1: for i in 0 to 31 generate 
    g_oneBitALU_i : OneBitALU
    port MAP(   i_Aalu    =>    i_A( i ),
                i_Balu    =>    i_B( i ),
                i_CryIn   =>    s_CryIn( i ),
                i_Ainv    =>    s_Ainv,
                i_Binv    =>    s_Binv,
                i_Opcode  =>    s_Opcode,
                o_Result  =>    s_R( i ),
                o_CryOut  =>    s_CryIn( i + 1 ) );
  end generate;
  
  o_CryOut  <=  s_Cryin( 32 );
  
  -- o_Zero
  g_nor : nor32
  port MAP( i_In   =>    s_R,
            o_F    =>    o_Zero  );
  
  -- o_Overflow
  g_xor : xor2
  port MAP( i_A   =>    s_Cryin( 31 ),
            i_B   =>    s_CryIn( 32 ),
            o_F   =>    o_Overflow  );
            
  -- if SLT opcode is selected
  g_sltMux  : slt_mux
  port MAP( i_Sel   =>    i_Opcode,
            i_Input =>    s_R,
            o_F     =>    o_F );

end structural;