-------------------------------------------------------------------------
-- Group 9
-- Project Part C
-- Due December 8th, 2011
-------------------------------------------------------------------------

-- tb_hazardetection.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This tests the unit that deals with control hazards, and
--              detects hazards.
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;


entity tb_hazardetection is

end tb_hazardetection;

architecture behavior of tb_hazardetection is



Component HazardDetection_Unit_new is
  port( i_EXslashMEM_RegWrite       :   in  std_logic;
        i_EXslashMEM_rd             :   in  std_logic_vector(4 downto 0);
        i_IFslashID_rt              :   in  std_logic_vector(4 downto 0);
        i_IFslashID_rs              :   in  std_logic_vector(4 downto 0);
        i_MEMslashWB_MemRead        :   in  std_logic;
        i_MEMslashWB_RegWrite       :   in  std_logic;
        i_MEMslashWB_rd             :   in  std_logic_vector(4 downto 0);
        i_IDslashEX_rs              :   in  std_logic_vector(4 downto 0);
        i_IDslashEX_rt              :   in  std_logic_vector(4 downto 0);
        i_IFslashID_Isbranch        :   in  std_logic;
        o_PCWriteEnable             :   out std_logic;
        o_IFslashID_stall           :   out std_logic;
        o_IDslashEX_Flush           :   out std_logic);
end Component;


signal s_EXslashMEM_RegWrite, s_MEMslashWB_RegWrite, s_IFslashID_Isbranch,
       s_MEMslashWB_MemRead, so_PCWriteEnable, so_IFslashID_stall, so_IDslashEX_Flush  : std_logic;
signal s_EXslashMEM_rd, s_IFslashID_rt, s_IFslashID_rs, s_MEMslashWB_rd,
       s_IDslashEX_rs, s_IDslashEX_rt                                 : std_logic_vector(4 downto 0);


begin
  testme: HazardDetection_Unit_new
   port map(  i_EXslashMEM_RegWrite       =>   s_EXslashMEM_RegWrite,
              i_EXslashMEM_rd             =>   s_EXslashMEM_rd,
              i_IFslashID_rt              =>   s_IFslashID_rt,
              i_IFslashID_rs              =>   s_IFslashID_rs,
              i_MEMslashWB_MemRead        =>   s_MEMslashWB_MemRead,
              i_MEMslashWB_RegWrite       =>   s_MEMslashWB_RegWrite,
              i_MEMslashWB_rd             =>   s_MEMslashWB_rd,
              i_IDslashEX_rs              =>   s_IDslashEX_rs,
              i_IDslashEX_rt              =>   s_IDslashEX_rt,
              i_IFslashID_Isbranch        =>   s_IFslashID_Isbranch,
              o_PCWriteEnable             =>   so_PCWriteEnable,
              o_IFslashID_stall           =>   so_IFslashID_stall,
              o_IDslashEX_Flush           =>   so_IDslashEX_Flush);
process
begin
  
  s_IFslashID_Isbranch  <=  '0';
  s_EXslashMEM_RegWrite <=  '0';
  s_EXslashMEM_rd       <=  "00000";
  s_IFslashID_rs        <=  "00000";
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
 