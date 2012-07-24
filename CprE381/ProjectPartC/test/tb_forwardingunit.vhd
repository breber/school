-------------------------------------------------------------------------
-- Group 9
-- Project Part C
-- Due December 8th, 2011
-------------------------------------------------------------------------

-- tb_ForwardingUnit.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is the unit that deals with control hazards, and
--              detects hazards.
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;


entity tb_ForwardingUnit is
  generic(  gCLK_HPER   : time := 50 ns  );
end tb_ForwardingUnit;

architecture behavior of tb_ForwardingUnit is

  -- Calculate the clock period as twice the half-period
  constant cCLK_PER  : time := gCLK_HPER * 2;

Component Forwarding_Unit is
  port( i_clk                       :   in  std_logic;
        i_EXslashMEM_RegWrite       :   in  std_logic;
        i_EXslashMEM_rd             :   in  std_logic_vector(4 downto 0);
        i_EXslashMEM_rs             :   in  std_logic_vector(4 downto 0);
        i_EXslashMEM_rt             :   in  std_logic_vector(4 downto 0);
        i_IFslashID_rt              :   in  std_logic_vector(4 downto 0);
        i_IFslashID_rs              :   in  std_logic_vector(4 downto 0);
        i_MEMslashWB_RegWrite       :   in  std_logic;
        i_MEMslashWB_rd             :   in  std_logic_vector(4 downto 0);
        i_MEMslashWB_MemRead        :   in  std_logic;
        i_IDslashEX_rs              :   in  std_logic_vector(4 downto 0);
        i_IDslashEX_rt              :   in  std_logic_vector(4 downto 0); 
        i_IFslashID_Isbranch        :   in  std_logic;
        o_EXdataOne                 :   out std_logic_vector(1 downto 0);--rs
        o_EXdataTwo                 :   out std_logic_vector(1 downto 0);--rt
        o_IDdataOne                 :   out std_logic_vector(1 downto 0);--rs
        o_IDdataTwo                 :   out std_logic_vector(1 downto 0));--rt
end Component;


signal s_EXslashMEM_RegWrite, s_MEMslashWB_RegWrite, s_IFslashID_Isbranch,
       s_MEMslashWB_MemRead, s_clk                                        : std_logic;
signal s_EXslashMEM_rd, s_IFslashID_rt, s_IFslashID_rs, s_MEMslashWB_rd,
       s_IDslashEX_rs, s_IDslashEX_rt, s_EXslashMEM_rs, s_EXslashMEM_rt   : std_logic_vector(4 downto 0);
signal s_EXdataOne, s_EXdataTwo, s_IDdataOne, s_IDdataTwo                 : std_logic_vector(1 downto 0);


begin  
  
  testme: Forwarding_Unit
   port map(  i_clk                       =>   s_clk,
              i_EXslashMEM_RegWrite       =>   s_EXslashMEM_RegWrite,
              i_EXslashMEM_rd             =>   s_EXslashMEM_rd,
              i_EXslashMEM_rs             =>   s_EXslashMEM_rs,
              i_EXslashMEM_rt             =>   s_EXslashMEM_rt,
              i_IFslashID_rt              =>   s_IFslashID_rt,
              i_IFslashID_rs              =>   s_IFslashID_rs,
              i_MEMslashWB_RegWrite       =>   s_MEMslashWB_RegWrite,
              i_MEMslashWB_rd             =>   s_MEMslashWB_rd,
              i_MEMslashWB_MemRead        =>   s_MEMslashWB_MemRead,
              i_IDslashEX_rs              =>   s_IDslashEX_rs,
              i_IDslashEX_rt              =>   s_IDslashEX_rt,
              i_IFslashID_Isbranch        =>   s_IFslashID_Isbranch,
              o_EXdataOne                 =>   s_EXdataOne,
              o_EXdataTwo                 =>   s_EXdataTwo,
              o_IDdataOne                 =>   s_IDdataOne,
              o_IDdataTwo                 =>   s_IDdataTwo);
              
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
  
  s_IFslashID_Isbranch  <=  '0';
  s_EXslashMEM_RegWrite <=  '0';
  s_EXslashMEM_rd       <=  "00000";
  s_IFslashID_rs        <=  "00000";
  s_EXslashMEM_rs       <=  "00000";
  s_EXslashMEM_rt       <=  "00000";
  s_MEMslashWB_MemRead  <=  '0';
  s_IFslashID_rt        <=  "00000";
  s_MEMslashWB_RegWrite <=  '0';
  s_MEMslashWB_rd       <=  "00000";
  s_IDslashEX_rs        <=  "00000";
  s_IDslashEX_rt        <=  "00000";
  
  -------------------------
  -- EX --> ID
  -------------------------
  report "EX->ID";
  -- EX/MEM.RD == IF/ID.RS
  s_IFslashID_Isbranch  <=  '1';
  s_EXslashMEM_RegWrite <=  '1';
  s_EXslashMEM_rd       <=  "00001";
  s_IFslashID_rs        <=  "00001";
  wait for 100 ns;
  
  -- EX/MEM.RD == IF/ID.RT
  s_IFslashID_Isbranch  <=  '1';
  s_EXslashMEM_RegWrite <=  '1';
  s_EXslashMEM_rd       <=  "00001";
  s_IFslashID_rt        <=  "00001";
  wait for 100 ns;
  
  -- IF/ID.IsBranch == 0
  s_IFslashID_Isbranch  <=  '0';
  s_EXslashMEM_RegWrite <=  '1';
  s_EXslashMEM_rd       <=  "00001";
  s_IFslashID_rs        <=  "00001";
  wait for 100 ns;
  
  -- IF/ID.RegWrite == 0
  s_IFslashID_Isbranch  <=  '1';
  s_EXslashMEM_RegWrite <=  '0';
  s_EXslashMEM_rd       <=  "00001";
  s_IFslashID_rs        <=  "00001";
  wait for 100 ns;
  
  -- EX/MEM.RD != IF/ID.RS
  s_IFslashID_Isbranch  <=  '1';
  s_EXslashMEM_RegWrite <=  '1';
  s_EXslashMEM_rd       <=  "00001";
  s_IFslashID_rs        <=  "00010";
  wait for 100 ns;
  
  -- EX/MEM.RD != IF/ID.RT
  s_IFslashID_Isbranch  <=  '1';
  s_EXslashMEM_RegWrite <=  '1';
  s_EXslashMEM_rd       <=  "00001";
  s_IFslashID_rt        <=  "00010";
  wait for 100 ns;
  
  
  -------------------------
  -- RESET
  -------------------------
  s_IFslashID_Isbranch  <=  '0';
  s_EXslashMEM_RegWrite <=  '0';
  s_EXslashMEM_rd       <=  "00000";
  s_IFslashID_rs        <=  "00000";
  s_EXslashMEM_rs       <=  "00000";
  s_EXslashMEM_rt       <=  "00000";
  s_MEMslashWB_MemRead  <=  '0';
  s_IFslashID_rt        <=  "00000";
  s_MEMslashWB_RegWrite <=  '0';
  s_MEMslashWB_rd       <=  "00000";
  s_IDslashEX_rs        <=  "00000";
  s_IDslashEX_rt        <=  "00000";
  
  -------------------------
  -- MEM --> ID
  -------------------------
  report "MEM->ID";
  -- MEM/ID.RD == IF/ID.RS
  s_IFslashID_Isbranch  <=  '1';
  s_MEMslashWB_RegWrite <=  '1';
  s_MEMslashWB_rd       <=  "00001";
  s_IFslashID_rs        <=  "00001";
  s_MEMslashWB_MemRead  <=  '1';
  wait for 100 ns;
  
  -- MEM/ID.RD == IF/ID.RT
  s_IFslashID_Isbranch  <=  '1';
  s_MEMslashWB_RegWrite <=  '1';
  s_MEMslashWB_rd       <=  "00001";
  s_IFslashID_rt        <=  "00001";
  wait for 100 ns;
  
  -- IF/ID.IsBranch == 0
  s_IFslashID_Isbranch  <=  '0';
  s_MEMslashWB_RegWrite <=  '1';
  s_MEMslashWB_rd       <=  "00001";
  s_IFslashID_rs        <=  "00001";
  wait for 100 ns;
  
  -- IF/ID.RegWrite == 0
  s_IFslashID_Isbranch  <=  '1';
  s_MEMslashWB_RegWrite <=  '0';
  s_MEMslashWB_rd       <=  "00001";
  s_IFslashID_rs        <=  "00001";
  wait for 100 ns;
  
  -- MEM/ID.RD != IF/ID.RS
  s_IFslashID_Isbranch  <=  '1';
  s_MEMslashWB_RegWrite <=  '1';
  s_MEMslashWB_rd       <=  "00001";
  s_IFslashID_rs        <=  "00010";
  wait for 100 ns;
  
  -- MEM/ID.RD != IF/ID.RT
  s_IFslashID_Isbranch  <=  '1';
  s_MEMslashWB_RegWrite <=  '1';
  s_MEMslashWB_rd       <=  "00001";
  s_IFslashID_rt        <=  "00010";
  wait for 100 ns;


  -------------------------
  -- RESET
  -------------------------
  s_IFslashID_Isbranch  <=  '0';
  s_EXslashMEM_RegWrite <=  '0';
  s_EXslashMEM_rd       <=  "00000";
  s_IFslashID_rs        <=  "00000";
  s_EXslashMEM_rs       <=  "00000";
  s_EXslashMEM_rt       <=  "00000";
  s_MEMslashWB_MemRead  <=  '0';
  s_IFslashID_rt        <=  "00000";
  s_MEMslashWB_RegWrite <=  '0';
  s_MEMslashWB_rd       <=  "00000";
  s_IDslashEX_rs        <=  "00000";
  s_IDslashEX_rt        <=  "00000";

  -------------------------
  -- EX --> EX
  -------------------------
  report "EX->EX";
  -- EX/MEM.RD == ID/EX.RS
  s_EXslashMEM_RegWrite <=  '1';
  s_EXslashMEM_rd       <=  "00001";
  s_IDslashEX_rs        <=  "00001";
  s_IDslashEX_rt        <=  "00000";
  wait for 100 ns;
  
  -- MEM/ID.RD == IF/ID.RT
  s_EXslashMEM_RegWrite <=  '1';
  s_EXslashMEM_rd       <=  "00001";
  s_IDslashEX_rt        <=  "00001";
  s_IDslashEX_rs        <=  "00000";
  wait for 100 ns;
  
  -- MEM/ID.RD == IF/ID.RT && MEM/ID.RD == IF/ID.RS
  s_EXslashMEM_RegWrite <=  '1';
  s_EXslashMEM_rd       <=  "00001";
  s_IDslashEX_rt        <=  "00001";
  s_IDslashEX_rs        <=  "00001";
  wait for 100 ns;
  
  -- IF/ID.RegWrite == 0
  s_EXslashMEM_RegWrite <=  '0';
  s_EXslashMEM_rd       <=  "00001";
  s_IDslashEX_rs        <=  "00001";
  s_IDslashEX_rt        <=  "00000";
  wait for 100 ns;
  
  -- MEM/ID.RD != IF/ID.RS
  s_EXslashMEM_RegWrite <=  '1';
  s_EXslashMEM_rd       <=  "00001";
  s_IDslashEX_rs        <=  "00010";
  wait for 100 ns;
  
  -- MEM/ID.RD != IF/ID.RT
  s_EXslashMEM_RegWrite <=  '1';
  s_EXslashMEM_rd       <=  "00001";
  s_IDslashEX_rs        <=  "00010";
  wait for 100 ns;
  
  
  -------------------------
  -- RESET
  -------------------------
  s_IFslashID_Isbranch  <=  '0';
  s_EXslashMEM_RegWrite <=  '0';
  s_EXslashMEM_rd       <=  "00000";
  s_IFslashID_rs        <=  "00000";
  s_EXslashMEM_rs       <=  "00000";
  s_EXslashMEM_rt       <=  "00000";
  s_MEMslashWB_MemRead  <=  '0';
  s_IFslashID_rt        <=  "00000";
  s_MEMslashWB_RegWrite <=  '0';
  s_MEMslashWB_rd       <=  "00000";
  s_IDslashEX_rs        <=  "00000";
  s_IDslashEX_rt        <=  "00000";
  
  -------------------------
  -- MEM --> EX
  -------------------------
  report "MEM->EX";
  
  -- MEM/WB.Rd == ID/EX.Rs (should forward EXData1 --> 10)
  s_MEMslashWB_RegWrite <=  '1';
  s_MEMslashWB_rd       <=  "00001";  
  s_EXslashMEM_RegWrite <=  '0';
  s_EXslashMEM_rd       <=  "00000";
  s_IDslashEX_rs        <=  "00001";
  s_IDslashEX_rt        <=  "00000";
  wait for 100 ns;
  
  -- MEM/WB.Rd == ID/EX.Rt (should forward EXData2 --> 10)
  s_MEMslashWB_RegWrite <=  '1';
  s_MEMslashWB_rd       <=  "00001";  
  s_EXslashMEM_RegWrite <=  '0';
  s_EXslashMEM_rd       <=  "00000";
  s_IDslashEX_rs        <=  "00000";
  s_IDslashEX_rt        <=  "00001";
  wait for 100 ns;
  
  -- MEM/WB.Rd == ID/EX.Rs && EX/MEM.Rd == ID/ED.Rs (Should forward EXData1 --> 10)
  s_MEMslashWB_RegWrite <=  '1';
  s_MEMslashWB_rd       <=  "00001";  
  s_EXslashMEM_RegWrite <=  '0';
  s_EXslashMEM_rd       <=  "00001";
  s_IDslashEX_rs        <=  "00001";
  s_IDslashEX_rt        <=  "00000";
  wait for 100 ns; 
  
  -- MEM/WB.Rd == ID/EX.Rt && EX/MEM.Rd == ID/ED.Rs (Should forward EXData2 --> 10)
  s_MEMslashWB_RegWrite <=  '1';
  s_MEMslashWB_rd       <=  "00001";  
  s_EXslashMEM_RegWrite <=  '0';
  s_EXslashMEM_rd       <=  "00001";
  s_IDslashEX_rs        <=  "00000";
  s_IDslashEX_rt        <=  "00001";
  wait for 100 ns;
  
  -- MEM/WB.Rd == ID/EX.Rs && EX/MEM.Rd == ID/ED.Rs (Should forward EXData1 --> 01)
  s_MEMslashWB_RegWrite <=  '1';
  s_MEMslashWB_rd       <=  "00001";  
  s_EXslashMEM_RegWrite <=  '1';
  s_EXslashMEM_rd       <=  "00001";
  s_IDslashEX_rs        <=  "00001";
  s_IDslashEX_rt        <=  "00000";
  wait for 100 ns; 
  
  -- MEM/WB.Rd == ID/EX.Rt && EX/MEM.Rd == ID/ED.Rs (Should forward EXData2 --> 01)
  s_MEMslashWB_RegWrite <=  '1';
  s_MEMslashWB_rd       <=  "00001";  
  s_EXslashMEM_RegWrite <=  '1';
  s_EXslashMEM_rd       <=  "00001";
  s_IDslashEX_rs        <=  "00000";
  s_IDslashEX_rt        <=  "00001";
  wait for 100 ns;  

end process;
  
end behavior;
