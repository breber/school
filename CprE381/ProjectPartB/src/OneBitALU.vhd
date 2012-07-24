-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Lab Date: 09/28/2011 + 10/5/2011
-------------------------------------------------------------------------


-- OneBitALU.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is a one-bit ALU that supports the operations:
--              
--              and   000
--              or    001
--              xor   010
--              nand  011
--              nor   100
--              add   101
--              slt   110
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

entity OneBitALU is
  
  port(  i_Aalu   :   in  std_logic;
         i_Balu   :   in  std_logic;
         i_CryIn  :   in  std_logic;
         i_OpCode :   in  std_logic_vector( 2 downto 0 );
         i_Ainv   :   in  std_logic;
         i_Binv   :   in  std_logic;
         o_Result :   out std_logic;
         o_CryOut :   out std_logic   );
         
end OneBitALU;


architecture structrual of OneBitALU is

  -----------------------------------------
  --Components:
  
  -- 2 input and
  component and2 is
      port(i_A          : in std_logic;
           i_B          : in std_logic;
           o_F          : out std_logic );
  end component;
  
  -- 2 input or
  component or2 is
      port(i_A          : in std_logic;
           i_B          : in std_logic;
           o_F          : out std_logic );
  end component;
  
  -- 2 input xor
  component xor2 is
      port(i_A          : in std_logic;
           i_B          : in std_logic;
           o_F          : out std_logic );
  end component;
  
  -- 2 input nand
  component nand2 is
      port(i_A          : in std_logic;
           i_B          : in std_logic;
           o_F          : out std_logic );
  end component;
  
  -- 2 input nor
  component nor2 is
      port(i_A          : in std_logic;
           i_B          : in std_logic;
           o_F          : out std_logic );
  end component;
  
  -- Full Adder
  component fullAdder is
      port(i_A       :   in   std_logic;
           i_B       :   in   std_logic;
           i_CryIn   :   in   std_logic;
           o_Sum     :   out  std_logic;
           o_CryOut  :   out  std_logic  );
  end component;
  
  -- Set less than
  component slt is
      port(i_A      :   in   std_logic;
           i_B      :   in   std_logic;
           o_F      :   out  std_logic  );
  end component;
       
  -- 7 to 1 mux
  component mux7 is
      port(i_A      :   in  std_logic;
           i_B      :   in  std_logic;
           i_C      :   in  std_logic;
           i_D      :   in  std_logic;
           i_E      :   in  std_logic;
           i_F      :   in  std_logic;
           i_G      :   in  std_logic;
           i_Sel    :   in  std_logic_vector( 2 downto 0 );
           o_F      :   out std_logic   );
  end component;
  
  ------------------------------------------------
  -- The following 2 components are for inversion
  
  -- inverter
  component inv is
    port(i_A          : in std_logic;
         o_F          : out std_logic);
  end component;
  
  -- 2 to 1 mux
  component mux_2to1 is
    port(i_A   :   in   std_logic;
         i_B   :   in   std_logic;
         sel   :   in   std_logic;
         o_R   :   out  std_logic);
  end component;
  
  -------------------------------------------------
  -- Signals:
  signal s_Anot, s_Bnot   :   std_logic;  -- these are A and B inverted
  signal s_Amux, s_Bmux   :   std_logic;  -- these are for selecting inverted bits or not (after mux)
  signal s_Fand, s_For, s_Fxor, s_Fnand, s_Fnor, s_Fadd, s_Fslt   :   std_logic;  -- outputs


begin
  
  -- invert A
  g_invA : inv
  port MAP( i_A   =>  i_Aalu,
            o_F   =>  s_Anot  );
  
  -- invert B
  g_invB  : inv
  port MAP( i_A   =>   i_Balu,
            o_F   =>   s_Bnot );
  
  -- select A or Anot
  g_muxA  : mux_2to1
  port MAP( i_A   =>   i_Aalu,
            i_B   =>   s_Anot,
            sel   =>   i_Ainv,
            o_R   =>   s_Amux );
  
  -- select B or Bnot
  g_muxB  : mux_2to1
  port MAP( i_A   =>   i_Balu,
            i_B   =>   s_Bnot,
            sel   =>   i_Binv,
            o_R   =>   s_Bmux );
  
  -- and
  g_and   : and2
  port MAP( i_A   =>    s_Amux,
            i_B   =>    s_Bmux,
            o_F   =>    s_Fand );
  
  -- or
  g_or    : or2
  port MAP( i_A   =>    s_Amux,
            i_B   =>    s_Bmux,
            o_F   =>    S_For );
            
  -- xor                     
  g_xor   : xor2
  port MAP( i_A   =>    s_Amux,
            i_B   =>    s_Bmux,
            o_F   =>    S_Fxor );
  -- nand                      
  g_nand  : nand2
  port MAP( i_A   =>    s_Amux,
            i_B   =>    s_Bmux,
            o_F   =>    S_Fnand );
  -- nor                     
  g_nor  : nor2
  port MAP( i_A   =>    s_Amux,
            i_B   =>    s_Bmux,
            o_F   =>    s_Fnor );
  -- add                      
  g_Add   : fullAdder
  port MAP( i_A   =>    s_Amux,
            i_B   =>    s_Bmux,
            i_CryIn =>  i_CryIn,
            o_Sum =>    s_Fadd,
            o_CryOut => o_CryOut  );

  -- set less than                      
  g_slt   : slt
  port MAP( i_A   =>    s_Amux,
            i_B   =>    s_Bmux,
            o_F   =>    s_Fslt );
  
  -- This mux selects between the 7 of the above options          
  g_7mux1 : mux7
  port MAP( i_A   =>    s_Fand,
            i_B   =>    s_For,
            i_C   =>    s_Fxor,
            i_D   =>    s_Fnand,
            i_E   =>    s_Fnor,
            i_F   =>    s_Fadd,
            i_G   =>    s_Fslt,
            i_sel =>    i_OpCode,
            o_F   =>    o_Result  );
          
  
end structrual;