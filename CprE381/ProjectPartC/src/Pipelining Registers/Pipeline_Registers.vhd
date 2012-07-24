-------------------------------------------------------------------------
-- Group 9
-- Project Part C 
-- Due December 8th, 2011
-------------------------------------------------------------------------

-- Pipeline_Registers.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is the pipelining register.  We have implemented it
--              so that all of the pipelining registers are the same-that
--              is that they will all have the same controls. All of the
--              controls that these registers deal with are both inputs
--              and outputs because they will both take in and store the
--              values, as registers do.
--              to Flush, just set all resets to 1
--              (  i_rst  =>  i_flush  )
--              to Stall, just set i_WE to 0
--              (  i_WE   =>  not i_stall )
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

entity Pipeline_Registers is
  
  port(   i_clk             :     in std_logic;
         -- i_flushORIDEXFlush  :     in std_logic;
          i_stall           :     in std_logic;
          i_flush           :     in std_logic;
          i_instruction     :     in std_logic_vector( 31 downto 0 );
          i_ALU_op          :     in std_logic_vector( 2 downto 0 );
          i_ALU_src         :     in std_logic;
          i_log_arith       :     in std_logic;
          i_leftOrRight     :     in std_logic;
          i_IsJump          :     in std_logic;
          i_IsBranch        :     in std_logic;
          i_RegWrite        :     in std_logic;
          i_MemWrite        :     in std_logic;
          i_MemRead         :     in std_logic;
          i_MemToReg        :     in std_logic_vector( 1 downto 0 );
          i_RegDest         :     in std_logic_vector( 1 downto 0 );
          i_W_H_B           :     in std_logic_vector( 2 downto 0 );
          i_WhichJB         :     in std_logic_vector( 3 downto 0 );
          i_shftAmount      :     in std_logic_vector( 1 downto 0 );
          i_isLui           :     in std_logic;
          i_NeedsLink       :     in std_logic;
          i_RegValue        :     in std_logic_vector( 31 downto 0 );
          i_RF_op_2         :     in std_logic_vector( 31 downto 0 );
          i_sextend_op      :     in std_logic_vector( 31 downto 0 );
          i_RS              :     in std_logic_vector( 4  downto 0 );
          i_RT              :     in std_logic_vector( 4  downto 0 );
          i_RD              :     in std_logic_vector( 4  downto 0 );
          i_EXResult        :     in std_logic_vector( 31 downto 0 );
          i_fromLoad        :     in std_logic_vector( 31 downto 0 );
          i_nextPC          :     in std_logic_vector( 31 downto 0 );
          o_instruction     :     out std_logic_vector( 31 downto 0 );
          o_ALU_op          :     out std_logic_vector( 2 downto 0 );
          o_ALU_src         :     out std_logic;
          o_log_arith       :     out std_logic;
          o_leftOrRight     :     out std_logic;
          o_IsJump          :     out std_logic;
          o_IsBranch        :     out std_logic;
          o_RegWrite        :     out std_logic;
          o_MemWrite        :     out std_logic;
          o_MemRead         :     out std_logic;
          o_MemToReg        :     out std_logic_vector( 1 downto 0 );
          o_RegDest         :     out std_logic_vector( 1 downto 0 );
          o_W_H_B           :     out std_logic_vector( 2 downto 0 );
          o_WhichJB         :     out std_logic_vector( 3 downto 0 );
          o_shftAmount      :     out std_logic_vector( 1 downto 0 );
          o_isLui           :     out std_logic;
          o_NeedsLink       :     out std_logic;
          o_RegValue        :     out std_logic_vector( 31 downto 0 );
          o_RF_op_2         :     out std_logic_vector( 31 downto 0 );
          o_sextend_op      :     out std_logic_vector( 31 downto 0 );
          o_RS              :     out std_logic_vector( 4  downto 0 );
          o_RT              :     out std_logic_vector( 4  downto 0 );
          o_RD              :     out std_logic_vector( 4  downto 0 );
          o_EXResult        :     out std_logic_vector( 31 downto 0 );
          o_fromLoad        :     out std_logic_vector( 31 downto 0 );
          o_nextPC          :     out std_logic_vector( 31 downto 0 )     );

end Pipeline_Registers;


architecture structural of Pipeline_Registers is

  ----------------------------------------------------------
  --                     Components                       --
  component nbit_register
    generic(N : integer := 32);
    port(
			   i_CLK : in std_logic;     -- Clock input
         i_RST : in std_logic;     -- Reset input
         i_WE  : in std_logic;     -- Write enable input
         i_D   : in std_logic_vector(N-1 downto 0);     -- Data value input
         o_Q   : out std_logic_vector(N-1 downto 0)
			   );
  end component;
  
  -- Every std_logic only needs a flip-flop, not an entire n-bit register
  component dff
  port(i_CLK        : in std_logic;     -- Clock input
       i_RST        : in std_logic;     -- Reset input
       i_WE         : in std_logic;     -- Write enable input
       i_D          : in std_logic;     -- Data value input
       o_Q          : out std_logic);   -- Data value output
  end component;

  ----------------------------------------------------------
  --                      Signals                         --
  signal s_notStall   :   std_logic;
  signal s_flushORIDEXFlush : std_logic;


begin 
  
  ----------------------------------------------------------
  --                   Signal Setting                     --
  s_notStall        <=    not i_stall;  -- Set the s_notStall
 -- s_flushORIDEXFlush    <=    i_flush or i_flushORIDEXFlush;
  
  -------------------------------------------------------------------------
  --   This is all of the registers.  They are named r_#name, from the   --
  --   inputs and outputs i_#name and o_#name, respectively.             --
  
  r_instruction   :   nbit_register
  generic map(  N =>  32  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_instruction,
              o_Q     =>    o_instruction   );
           
  r_ALU_op   :   nbit_register
  generic map(  N =>  3  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_ALU_op,
              o_Q     =>    o_ALU_op   );
           
  r_ALU_src   :   dff
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_ALU_src,
              o_Q     =>    o_ALU_Src   );
           
  r_log_arith   :   dff
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_log_arith,
              o_Q     =>    o_log_arith   );
           
  r_leftOrRight   :   dff
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_leftOrRight  ,
              o_Q     =>    o_leftOrRight    );
           
  r_IsJump   :   dff
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_IsJump ,
              o_Q     =>    o_IsJump    );
           
  r_IsBranch   :   dff
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_IsBranch ,
              o_Q     =>    o_IsBranch   );
           
  r_RegWrite   :   dff
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_RegWrite ,
              o_Q     =>    o_RegWrite    );
           
  r_MemWrite   :   dff
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_MemWrite ,
              o_Q     =>    o_MemWrite    );
           
  r_MemRead   :   dff
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_MemRead  ,
              o_Q     =>    o_MemRead    );
           
  r_MemToReg   :   nbit_register
  generic map(  N =>  2  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_MemToReg  ,
              o_Q     =>    o_MemToReg     );
           
  r_RegDest   :   nbit_register
  generic map(  N =>  2  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_RegDest ,
              o_Q     =>    o_RegDest    );
           
  r_W_H_B   :   nbit_register
  generic map(  N =>  3  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_W_H_B ,
              o_Q     =>    o_W_H_B   );
           
  r_WhichJB   :   nbit_register
  generic map(  N =>  4  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_WhichJB ,
              o_Q     =>    o_WhichJB   );
           
  r_shftAmount   :   nbit_register
  generic map(  N =>  2  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_shftAmount ,
              o_Q     =>    o_shftAmount    );
           
  r_isLui   :   dff
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_isLui ,
              o_Q     =>    o_isLui    );
           
  r_NeedsLink   :   dff
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_NeedsLink ,
              o_Q     =>    o_NeedsLink    );
           
  r_RegValue   :   nbit_register
  generic map(  N =>  32  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_RegValue ,
              o_Q     =>    o_RegValue    );
           
  r_RF_op_2   :   nbit_register
  generic map(  N =>  32  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_RF_op_2 ,
              o_Q     =>    o_RF_op_2    );
           
  r_sextend_op   :   nbit_register
  generic map(  N =>  32  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_sextend_op,
              o_Q     =>    o_sextend_op    );
           
  r_RS   :   nbit_register
  generic map(  N =>  5  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_RS ,
              o_Q     =>    o_RS    );
           
  r_RT   :   nbit_register
  generic map(  N =>  5  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_RT ,
              o_Q     =>    o_RT    );
           
  r_RD   :   nbit_register
  generic map(  N =>  5  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_RD ,
              o_Q     =>    o_RD   );
           
  r_EXResult  :   nbit_register
  generic map(  N =>  32  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_EXResult ,
              o_Q     =>    o_EXResult    );
           
           
  r_fromLoad   :   nbit_register
  generic map(  N =>  32  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_fromLoad ,
              o_Q     =>    o_fromLoad    );
           
  r_nextPC   :   nbit_register
  generic map(  N =>  32  )
  port map(   i_CLK   =>    i_clk,
              i_RST   =>    i_flush,
              i_WE    =>    s_notStall,
              i_D     =>    i_nextPC  ,
              o_Q     =>    o_nextPC     );
              
              
                            
--  -- need to flush controls of IDEX
--  s_IDEX_ALU_Op        <=  "000" when s_hazard_IDEX_flush = '1';
--  s_IDEX_ALU_src       <=   '0' when s_hazard_IDEX_flush = '1';
--  s_IDEX_log_arith     <=   '0' when s_hazard_IDEX_flush = '1';
--  s_IDEX_leftOrRight   <=   '0' when s_hazard_IDEX_flush = '1';
--  s_IDEX_isJump        <=   '0' when s_hazard_IDEX_flush = '1';
--  s_IDEX_isBranch      <=   '0' when s_hazard_IDEX_flush = '1';
--  s_IDEX_RegWrite      <=   '0' when s_hazard_IDEX_flush = '1';
--  s_IDEX_MemWrite      <=   '0' when s_hazard_IDEX_flush = '1';
--  s_IDEX_MemRead       <=   '0' when s_hazard_IDEX_flush = '1';
--  s_IDEX_MemToReg      <=   "00" when s_hazard_IDEX_flush = '1';
--  s_IDEX_RegDest       <=   "00" when s_hazard_IDEX_flush = '1';
--  s_IDEX_W_H_B         <=   "000" when s_hazard_IDEX_flush = '1';
--  s_IDEX_WhichJB       <=   "0000" when s_hazard_IDEX_flush = '1';
--  s_IDEX_shiftAmount   <=   "00" when s_hazard_IDEX_flush = '1';
--  s_IDEX_isLui         <=   '0' when s_hazard_IDEX_flush = '1';
--  s_IDEX_NeedsLink    <=   '0' when s_hazard_IDEX_flush = '1';
--  s_IDEX_RegValue      <=   x"00000000" when s_hazard_IDEX_flush = '1';
--  s_IDEX_RF_op_2       <=   x"00000000" when s_hazard_IDEX_flush = '1';              
              
              
       
              
              
              
              
           
end structural;