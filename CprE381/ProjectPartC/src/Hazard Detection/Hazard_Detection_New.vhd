-------------------------------------------------------------------------
-- Group 9
-- Project Part C
-- Due December 8th, 2011
-------------------------------------------------------------------------

-- HazardDetection_Unit_New.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is the Unit that detects hazards and handles them
--              
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;
entity HazardDetection_Unit_New is

--  port( i_EXslashMEM_RegWrite       :   in  std_logic;
--        i_EXslashMEM_rd             :   in  std_logic_vector(4 downto 0);
--        i_IFslashID_rt              :   in  std_logic_vector(4 downto 0);
--        i_IFslashID_rs              :   in  std_logic_vector(4 downto 0);
--        i_MEMslashWB_MemRead        :   in  std_logic;
--        i_MEMslashWB_RegWrite       :   in  std_logic;
--        i_MEMslashWB_rd             :   in  std_logic_vector(4 downto 0);
--        i_IDslashEX_rs              :   in  std_logic_vector(4 downto 0);
--        i_IDslashEX_rt              :   in  std_logic_vector(4 downto 0);
--        i_IFslashID_Isbranch        :   in  std_logic;
--        o_PCWriteEnable             :   out std_logic;
--        o_IFslashID_stall           :   out std_logic;
--        o_IDslashEX_Flush           :   out std_logic);


  port( i_IDEX_MemRead    :     in    std_logic;
        i_IDEX_Rt         :     in    std_logic_vector( 4 downto 0 );
        i_IFID_Rs         :     in    std_logic_vector( 4 downto 0 );
        i_IFID_Rt         :     in    std_logic_vector( 4 downto 0 );
        o_PCWriteEnable   :     out   std_logic;
        o_IFID_stall      :     out   std_logic;
        o_IDEX_Flush      :     out   std_logic         );

end HazardDetection_Unit_New;

architecture dataflow of HazardDetection_Unit_New is

  begin   
    
    
    
  o_PCWriteEnable   <=  '0' when i_IDEX_MemRead = '1' and ((i_IDEX_Rt = i_IFID_Rs) or (i_IDEX_Rt = i_IFID_Rt))
                  else  '1';
    
  o_IFID_stall <=  '1' when i_IDEX_MemRead = '1' and ((i_IDEX_Rt = i_IFID_Rs) or (i_IDEX_Rt = i_IFID_Rt))
                  else  '0';
    
  o_IDEX_Flush <=  '1' when i_IDEX_MemRead = '1' and ((i_IDEX_Rt = i_IFID_Rs) or (i_IDEX_Rt = i_IFID_Rt))
                  else  '0';      
    
    
--MEM/ID

--o_IFslashID_stall <= '1' after 1 ns 
-- when  (i_IFslashID_Isbranch = '1' and i_MEMslashWB_MemRead = '1' and i_MEMslashWB_RegWrite = '1' and i_MEMslashWB_rd /= "00000" and i_MEMslashWB_rd = i_IFslashID_rs)
--    or (i_IFslashID_Isbranch = '1' and i_MEMslashWB_MemRead = '1' and i_MEMslashWB_RegWrite = '1' and i_MEMslashWB_rd /= "00000" and i_MEMslashWB_rd = i_IFslashID_rt)
--    or ((i_MEMslashWB_RegWrite ='1' and i_MEMslashWB_RD /= "00000" and not (i_EXslashMEM_RegWrite = '1' and i_EXslashMEM_rd /= "00000" and (i_EXslashMEM_rd = i_IDslashEX_rs or i_EXslashMEM_rd = i_IDslashEX_rt)) and i_MEMslashWB_rd = i_IDslashEX_rs)
--      and (i_MEMslashWB_RegWrite ='1' and i_MEMslashWB_RD /= "00000" and i_EXslashMEM_RegWrite = '1' and i_EXslashMEM_rd /= "00000" and (i_EXslashMEM_rd = i_IDslashEX_rs or i_EXslashMEM_rd = i_IDslashEX_rt) and i_MEMslashWB_rd = i_IDslashEX_rt))
-- else '0' after 1 ns;
--
-- o_PCWriteEnable <= '0' after 1 ns 
-- when  (i_IFslashID_Isbranch = '1' and i_MEMslashWB_MemRead = '1' and i_MEMslashWB_RegWrite = '1' and i_MEMslashWB_rd /= "00000" and i_MEMslashWB_rd = i_IFslashID_rs)
--    or (i_IFslashID_Isbranch = '1' and i_MEMslashWB_MemRead = '1' and i_MEMslashWB_RegWrite = '1' and i_MEMslashWB_rd /= "00000" and i_MEMslashWB_rd = i_IFslashID_rt)
--    or ((i_MEMslashWB_RegWrite ='1' and i_MEMslashWB_RD /= "00000" and not (i_EXslashMEM_RegWrite = '1' and i_EXslashMEM_rd /= "00000" and (i_EXslashMEM_rd = i_IDslashEX_rs or i_EXslashMEM_rd = i_IDslashEX_rt)) and i_MEMslashWB_rd = i_IDslashEX_rs)
--      and (i_MEMslashWB_RegWrite ='1' and i_MEMslashWB_RD /= "00000" and i_EXslashMEM_RegWrite = '1' and i_EXslashMEM_rd /= "00000" and (i_EXslashMEM_rd = i_IDslashEX_rs or i_EXslashMEM_rd = i_IDslashEX_rt) and i_MEMslashWB_rd = i_IDslashEX_rt))
-- else '1' after 1 ns;
-- 
--  o_IDslashEX_Flush <= '1' after 1 ns
--  when  (i_IFslashID_Isbranch = '1' and i_MEMslashWB_MemRead = '1' and i_MEMslashWB_RegWrite = '1' and i_MEMslashWB_rd /= "00000" and i_MEMslashWB_rd = i_IFslashID_rs)
--     or (i_IFslashID_Isbranch = '1' and i_MEMslashWB_MemRead = '1' and i_MEMslashWB_RegWrite = '1' and i_MEMslashWB_rd /= "00000" and i_MEMslashWB_rd = i_IFslashID_rt)
--     or ((i_MEMslashWB_RegWrite ='1' and i_MEMslashWB_RD /= "00000" and not (i_EXslashMEM_RegWrite = '1' and i_EXslashMEM_rd /= "00000" and (i_EXslashMEM_rd = i_IDslashEX_rs or i_EXslashMEM_rd = i_IDslashEX_rt)) and i_MEMslashWB_rd = i_IDslashEX_rs)
--      and (i_MEMslashWB_RegWrite ='1' and i_MEMslashWB_RD /= "00000" and i_EXslashMEM_RegWrite = '1' and i_EXslashMEM_rd /= "00000" and (i_EXslashMEM_rd = i_IDslashEX_rs or i_EXslashMEM_rd = i_IDslashEX_rt) and i_MEMslashWB_rd = i_IDslashEX_rt))
-- else '0' after 1 ns;
  

--o_IFslashID_stall <= '1'   
-- when  (i_IFslashID_Isbranch = '1' and i_MEMslashWB_MemRead = '1' and i_MEMslashWB_RegWrite = '1' and i_MEMslashWB_rd /= "00000" and i_MEMslashWB_rd = i_IFslashID_rs)
--    or (i_IFslashID_Isbranch = '1' and i_MEMslashWB_MemRead = '1' and i_MEMslashWB_RegWrite = '1' and i_MEMslashWB_rd /= "00000" and i_MEMslashWB_rd = i_IFslashID_rt)
--    or ((i_MEMslashWB_RegWrite ='1' and i_MEMslashWB_RD /= "00000" and not (i_EXslashMEM_RegWrite = '1' and i_EXslashMEM_rd /= "00000" and (i_EXslashMEM_rd = i_IDslashEX_rs or i_EXslashMEM_rd = i_IDslashEX_rt)) and i_MEMslashWB_rd = i_IDslashEX_rs)
--      and (i_MEMslashWB_RegWrite ='1' and i_MEMslashWB_RD /= "00000" and i_EXslashMEM_RegWrite = '1' and i_EXslashMEM_rd /= "00000" and (i_EXslashMEM_rd = i_IDslashEX_rs or i_EXslashMEM_rd = i_IDslashEX_rt) and i_MEMslashWB_rd = i_IDslashEX_rt))
-- else '0'  ;
--
-- o_PCWriteEnable <= '0'   
-- when  (i_IFslashID_Isbranch = '1' and i_MEMslashWB_MemRead = '1' and i_MEMslashWB_RegWrite = '1' and i_MEMslashWB_rd /= "00000" and i_MEMslashWB_rd = i_IFslashID_rs)
--    or (i_IFslashID_Isbranch = '1' and i_MEMslashWB_MemRead = '1' and i_MEMslashWB_RegWrite = '1' and i_MEMslashWB_rd /= "00000" and i_MEMslashWB_rd = i_IFslashID_rt)
--    or ((i_MEMslashWB_RegWrite ='1' and i_MEMslashWB_RD /= "00000" and not (i_EXslashMEM_RegWrite = '1' and i_EXslashMEM_rd /= "00000" and (i_EXslashMEM_rd = i_IDslashEX_rs or i_EXslashMEM_rd = i_IDslashEX_rt)) and i_MEMslashWB_rd = i_IDslashEX_rs)
--      and (i_MEMslashWB_RegWrite ='1' and i_MEMslashWB_RD /= "00000" and i_EXslashMEM_RegWrite = '1' and i_EXslashMEM_rd /= "00000" and (i_EXslashMEM_rd = i_IDslashEX_rs or i_EXslashMEM_rd = i_IDslashEX_rt) and i_MEMslashWB_rd = i_IDslashEX_rt))
-- else '1'  ;
-- 
--  o_IDslashEX_Flush <= '1'  
--  when  (i_IFslashID_Isbranch = '1' and i_MEMslashWB_MemRead = '1' and i_MEMslashWB_RegWrite = '1' and i_MEMslashWB_rd /= "00000" and i_MEMslashWB_rd = i_IFslashID_rs)
--     or (i_IFslashID_Isbranch = '1' and i_MEMslashWB_MemRead = '1' and i_MEMslashWB_RegWrite = '1' and i_MEMslashWB_rd /= "00000" and i_MEMslashWB_rd = i_IFslashID_rt)
--     or ((i_MEMslashWB_RegWrite ='1' and i_MEMslashWB_RD /= "00000" and not (i_EXslashMEM_RegWrite = '1' and i_EXslashMEM_rd /= "00000" and (i_EXslashMEM_rd = i_IDslashEX_rs or i_EXslashMEM_rd = i_IDslashEX_rt)) and i_MEMslashWB_rd = i_IDslashEX_rs)
--      and (i_MEMslashWB_RegWrite ='1' and i_MEMslashWB_RD /= "00000" and i_EXslashMEM_RegWrite = '1' and i_EXslashMEM_rd /= "00000" and (i_EXslashMEM_rd = i_IDslashEX_rs or i_EXslashMEM_rd = i_IDslashEX_rt) and i_MEMslashWB_rd = i_IDslashEX_rt))
-- else '0'  ;

end dataflow;