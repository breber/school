-------------------------------------------------------------------------
-- Group 9
-- Project Part C
-- Due December 8th, 2011
-------------------------------------------------------------------------

-- Hazard_Detection.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is the Unit that detects hazards and handles them
--              
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;
entity HazardDetection_Unit is

  port( i_clk                       :   in  std_logic;
        i_EXslashMEM_RegWrite       :   in  std_logic;
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

end HazardDetection_Unit;

architecture dataflow of HazardDetection_Unit is


begin
--process (i_EXslashMEM_RegWrite, i_EXslashMEM_rd, i_IFslashID_rt, i_IFslashID_rs, 
  --        i_MEMslashWB_RegWrite, i_MEMslashWB_rd, i_IDslashEX_rt, i_IDslashEX_rs,
    --     i_IFslashID_Isbranch )
process ( i_clk )
  begin
  
  --RisingEdgeIF: if ( rising_edge( i_clk ) ) then  
  RisingEdgeIF: if ( i_clk = '1' ) then 
  
  o_IFslashID_stall <= '0';
  o_IDslashEX_Flush <= '0';
  o_PCWriteEnable <= '1';     
    
--MEM/ID

            o_IFslashID_stall <= '1' after 1 ns when (i_IFslashID_isbranch = '1' and i
                            else  '0' after 1 ns
            o_PCWriteEnable   <= '0'
          else  
            o_IDslashEX_Flush <= '1' 

if i_IFslashID_Isbranch = '1' then
  if i_MEMslashWB_MemRead = '1' then
    if i_EXslashMEM_RegWrite = '1' then
      if i_EXslashMEM_rd /= "00000" then
        if i_EXslashMEM_rd = i_IFslashID_rs then
            o_IFslashID_stall <= '1';
            o_PCWriteEnable <= '0'; 
            o_IDslashEX_Flush <= '1'; 
        end if;
      end if;
    end if;
  end if;
end if;
if i_IFslashID_Isbranch = '1' then
  if i_MEMslashWB_MemRead = '1' then
    if i_EXslashMEM_RegWrite = '1' then
      if i_EXslashMEM_rd /= "00000" then
        if i_EXslashMEM_rd = i_IFslashID_rt then
            o_IFslashID_stall <= '1';
            o_PCWriteEnable <= '0'; 
            o_IDslashEX_Flush <= '1';
        end if;
      end if;
    end if;
  end if;
end if; 

--MEM/EX          
        
      if i_MEMslashWB_RegWrite ='1' then
    if i_MEMslashWB_RD /= "00000" then
      if not (i_EXslashMEM_RegWrite = '1' and 
              i_EXslashMEM_rd /= "00000" and
              (i_EXslashMEM_rd = i_IDslashEX_rs or 
               i_EXslashMEM_rd = i_IDslashEX_rt)) then -- CHANGE 1
        if i_MEMslashWB_rd = i_IDslashEX_rs then --CHANGE 3
                o_IFslashID_stall <= '1';
                o_PCWriteEnable <= '0'; 
                o_IDslashEX_Flush <= '1';
        end if;
      end if;
    end if;
  end if;
  if i_MEMslashWB_RegWrite ='1' then
    if i_MEMslashWB_RD /= "00000" then
      if not (i_EXslashMEM_RegWrite = '1' and 
              i_EXslashMEM_rd /= "00000" and
              (i_EXslashMEM_rd = i_IDslashEX_rs or 
               i_EXslashMEM_rd = i_IDslashEX_rt)) then -- CHANGE 1
        if i_MEMslashWB_rd = i_IDslashEX_rt then --CHANGE 3
                o_IFslashID_stall <= '1';
                o_PCWriteEnable <= '0'; 
                o_IDslashEX_Flush <= '1';
        end if;
      end if;
    end if;
  end if;
  
end if RisingEdgeIF;
  
end process;
end dataflow;