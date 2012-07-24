-------------------------------------------------------------------------
-- Group 9
-- Project Part C
-- Due December 8th, 2011
-------------------------------------------------------------------------

-- Forwarding_Unit.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is the unit that deals with data forwarding given
--              that there are data hazards that need to be forwarded.
--
--              "Mux control" for forwarding muxes
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;
entity Forwarding_Unit_New is

  port( i_EXMEM_RegWrite       :   in  std_logic;
        i_EXMEM_rd             :   in  std_logic_vector(4 downto 0);
        i_EXMEM_rs             :   in  std_logic_vector(4 downto 0);
        i_EXMEM_rt             :   in  std_logic_vector(4 downto 0);
        i_IFID_rt              :   in  std_logic_vector(4 downto 0);
        i_IFID_rs              :   in  std_logic_vector(4 downto 0);
        i_IDEX_RegWrite        :   in  std_logic;
        i_MEMWB_RegWrite       :   in  std_logic;
        i_MEMWB_rd             :   in  std_logic_vector(4 downto 0);
        i_MEMWB_MemRead        :   in  std_logic;
        i_IDEX_rs              :   in  std_logic_vector(4 downto 0);
        i_IDEX_rt              :   in  std_logic_vector(4 downto 0);
        i_IDEX_rd              :   in  std_logic_vector(4 downto 0); 
        i_IFID_Isbranch        :   in  std_logic;
        o_EXdataOne            :   out std_logic_vector(1 downto 0);--rs
        o_EXdataTwo            :   out std_logic_vector(1 downto 0);--rt
        o_IDdataOne            :   out std_logic_vector(2 downto 0);--rs
        o_IDdataTwo            :   out std_logic_vector(2 downto 0);--rt
        o_MEMdataOne           :   out std_logic        );
end Forwarding_Unit_New;

architecture dataflow of Forwarding_Unit_New is


begin
  
--  o_IDdataOne <=  "001" after 1 ns when i_IFID_isBranch = '1' and i_EXMEM_RegWrite = '1' and i_EXMEM_rd /= "00000"
--                                   and  i_EXMEM_rd = i_IFID_rs
--             else "010" after 1 ns when i_IFID_isBranch = '1' and i_MEMWB_MemRead = '1'
--                                   and  i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000" and i_MEMWB_rd = i_IFID_rs
--             else "011" after 1 ns when i_IFID_isBranch = '1' and i_MEMWB_MemRead = '0'
--                                   and  i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000" and i_MEMWB_rd = i_IFID_rs
--             else "100" after 1 ns when i_IFID_isBranch = '1' and i_IDEX_RegWrite = '1' and i_IDEX_rd /= "00000"
--                                   and  i_IDEX_rd = i_IFID_rs
--             else "000" after 1 ns;
--  
--  o_IDdataTwo <=  "001" after 1 ns when i_IFID_isBranch = '1' and i_EXMEM_RegWrite = '1' and i_EXMEM_rd /= "00000"
--                                   and  i_EXMEM_rd = i_IFID_rt
--             else "010" after 1 ns when i_IFID_isBranch = '1' and i_MEMWB_MemRead = '1'
--                                   and  i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000" and i_MEMWB_rd = i_IFID_rt
--             else "011" after 1 ns when i_IFID_isBranch = '1' and i_MEMWB_MemRead = '0'
--                                   and  i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000" and i_MEMWB_rd = i_IFID_rt
--             else "100" after 1 ns when i_IFID_isBranch = '1' and i_IDEX_RegWrite = '1' and i_IDEX_rd /= "00000"
--                                   and  i_IDEX_rd = i_IFID_rt
--             else "000" after 1 ns;
--  
--  o_EXDataOne <=  "01" after 1 ns when i_EXMEM_RegWrite = '1' and i_EXMEM_rd = i_IDEX_rs and i_EXMEM_rd /= "00000"
--            else  "10" after 1 ns when i_MEMWB_MemRead = '1' and i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000"
--                                  and  not(i_EXMEM_RegWrite = '1'
--                                  and  i_EXMEM_rd /= "00000" and (i_EXMEM_Rd = i_IDEX_rs or i_EXMEM_rd = i_IDEX_rt))
--                                  and  i_MEMWB_rd = i_IDEX_rs
--            else  "11" after 1 ns when i_MEMWB_MemRead = '0' and i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000" 
--                                  and  not(i_EXMEM_RegWrite = '1'
--                                  and  i_EXMEM_rd /= "00000" and (i_EXMEM_Rd = i_IDEX_rs or i_EXMEM_rd = i_IDEX_rt))
--                                  and  i_MEMWB_rd = i_IDEX_rs 
--            else  "00" after 1 ns;
--          
--  o_EXDataTwo <=  "01" after 1 ns when i_EXMEM_RegWrite = '1' and i_EXMEM_rd = i_IDEX_rt and i_EXMEM_rd /= "00000"
--            else  "10" after 1 ns when i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000" and not(i_EXMEM_RegWrite = '1'
--                                  and  i_EXMEM_rd /= "00000" and (i_EXMEM_Rd = i_IDEX_rs or i_EXMEM_rd = i_IDEX_rt))
--                                  and  i_MEMWB_rd = i_IDEX_rt
--            else  "00" after 1 ns;
--
--  o_MEMdataOne  <=  '1' after 1 ns when i_MEMWB_MemRead = '1' and i_MEMWB_rd /= "00000" and i_MEMWB_rd = i_EXMEM_rt
--              else  '0' after 1 ns;
--              





  o_IDdataOne <=  "001"   when i_IFID_isBranch = '1' and i_EXMEM_RegWrite = '1' and i_EXMEM_rd /= "00000"
                                   and  i_EXMEM_rd = i_IFID_rs
             else "010"   when i_IFID_isBranch = '1' and i_MEMWB_MemRead = '1'
                                   and  i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000" and i_MEMWB_rd = i_IFID_rs
             else "011"   when i_IFID_isBranch = '1' and i_MEMWB_MemRead = '0'
                                   and  i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000" and i_MEMWB_rd = i_IFID_rs
             else "100"   when i_IFID_isBranch = '1' and i_IDEX_RegWrite = '1' and i_IDEX_rd /= "00000"
                                   and  i_IDEX_rd = i_IFID_rs
             else "000"  ;
  
  
  
  
  
  
  o_IDdataTwo <=  "001"   when i_IFID_isBranch = '1' and i_EXMEM_RegWrite = '1' and i_EXMEM_rd /= "00000"
                                   and  i_EXMEM_rd = i_IFID_rt
             else "010"   when i_IFID_isBranch = '1' and i_MEMWB_MemRead = '1'
                                   and  i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000" and i_MEMWB_rd = i_IFID_rt
             else "011"   when i_IFID_isBranch = '1' and i_MEMWB_MemRead = '0'
                                   and  i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000" and i_MEMWB_rd = i_IFID_rt
             else "100"   when i_IFID_isBranch = '1' and i_IDEX_RegWrite = '1' and i_IDEX_rd /= "00000"
                                   and  i_IDEX_rd = i_IFID_rt
             else "000"  ;
  
  
  
  
  o_EXDataOne <=  "01"  when i_EXMEM_RegWrite = '1' and i_EXMEM_rd = i_IDEX_rs  and i_EXMEM_rd  /= "00000"
             else "10"  when ((i_MEMWB_RegWrite = '1' and i_MEMWB_RD /= "00000") and not(i_EXMEM_RegWrite = '1' and i_EXMEM_rd /= "00000" and
                                    i_EXMEM_Rd = i_IDEX_Rs) and (i_MEMWB_Rd = i_IDEX_Rs))
             else "00";
                        
                        
                        
  o_EXDataTwo <=  "01"  when i_EXMEM_RegWrite = '1' and i_EXMEM_rd = i_IDEX_rt  and i_EXMEM_rd  /= "00000"
             else "10"  when ((i_MEMWB_RegWrite = '1' and i_MEMWB_RD /= "00000") and not(i_EXMEM_RegWrite = '1' and i_EXMEM_rd /= "00000" and
                                    i_EXMEM_Rd = i_IDEX_Rt) and(i_MEMWB_Rd = i_IDEX_Rt))
             else "00";

  
  
  o_MEMdataOne  <=  '1'   when i_MEMWB_MemRead = '1' and i_MEMWB_rd /= "00000" and i_MEMWB_rd = i_EXMEM_rt
              else  '0'  ;
                
                
                
                
                
                
  
--  o_EXDataOne <=  "001"   when i_EXMEM_RegWrite = '1' and i_EXMEM_rd = i_IDEX_rs and i_EXMEM_rd /= "00000"
--            else  "010"   when i_MEMWB_MemRead = '1' and i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000"
--                                  and  not(i_EXMEM_RegWrite = '1'
--                                  and  i_EXMEM_rd /= "00000" and (i_EXMEM_Rd = i_IDEX_rs or i_EXMEM_rd = i_IDEX_rt))
--                                  and  i_MEMWB_rd = i_IDEX_rs
--            else  "011"   when i_MEMWB_MemRead = '0' and i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000" 
--                                  and  not(i_EXMEM_RegWrite = '1'
--                                  and  i_EXMEM_rd /= "00000" and (i_EXMEM_Rd = i_IDEX_rs or i_EXMEM_rd = i_IDEX_rt))
--                                  and  i_MEMWB_rd = i_IDEX_rs
--            else  "100"   when i_EXMEM  
--            else  "000"  ;
--          
--          
--          
--          
--          
--  o_EXDataTwo <=  "001"   when i_EXMEM_RegWrite = '1' and i_EXMEM_rd = i_IDEX_rt and i_EXMEM_rd /= "00000"
--            else  "010"   when i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000" and not(i_EXMEM_RegWrite = '1'
--                                  and  i_EXMEM_rd /= "00000" and (i_EXMEM_Rd = i_IDEX_rs or i_EXMEM_rd = i_IDEX_rt))
--                                  and  i_MEMWB_rd = i_IDEX_rt
--            else  "011"   when i_MEMWB_MemRead = '0' and i_MEMWB_RegWrite = '1' and i_MEMWB_rd /= "00000" 
--                                  and  not(i_EXMEM_RegWrite = '1'
--                                  and  i_EXMEM_rd /= "00000" and (i_EXMEM_Rd = i_IDEX_rs or i_EXMEM_rd = i_IDEX_rt))
--                                  and  i_MEMWB_rd = i_IDEX_rs 
--            else  "000"  ;







end dataflow;