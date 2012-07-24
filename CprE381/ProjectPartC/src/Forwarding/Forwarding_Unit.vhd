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
entity Forwarding_Unit is

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
end Forwarding_Unit;

architecture dataflow of Forwarding_Unit is
begin
 
--process ( i_EXslashMEM_RegWrite, i_EXslashMEM_rd, i_IFslashID_rt, i_IFslashID_rs, 
  --        i_MEMslashWB_RegWrite, i_MEMslashWB_rd, i_IDslashEX_rt, i_IDslashEX_rs,
   --       i_IFslashID_Isbranch, i_MEMslashWB_MemRead, i_EXslashMEM_rs, i_EXslashMEM_rt )
process( i_clk  )

  begin
    
--  RisingEdgeIF: if ( rising_edge( i_clk ) ) then
  RisingEdgeIF: if ( i_clk = '1' ) then

  o_EXdataOne <= "00";
  o_EXdataTwo <= "00";
  o_IDdataOne <= "00";
  o_IDdataTwo <= "00";
  
  --EX/ID
  if i_IFslashID_Isbranch = '1' then
    if i_EXslashMEM_RegWrite = '1' then
      if i_EXslashMEM_rd /= "00000" then
        if i_EXslashMEM_rd = i_IFslashID_rs then
          o_IDdataOne <= "01";
        end if;
      end if;
    end if;
  end if;
         
         
  if i_IFslashID_Isbranch = '1' then  
    if i_EXslashMEM_RegWrite = '1' then
      if i_EXslashMEM_rd /= "00000" then
        if i_EXslashMEM_rd = i_IFslashID_rt then
          o_IDdataTwo <= "01";
        end if;
      end if;
    end if;
  end if;  


  --MEM/ID
  if i_IFslashID_Isbranch = '1' then
    if i_MEMslashWB_MemRead = '1' then
      if i_MEMslashWB_RegWrite = '1' then
        if i_MEMslashWB_rd /= "00000" then
          if i_MEMslashWB_rd = i_IFslashID_rs then
            o_IDdataOne <= "10";
          end if;
        end if;
      end if;
    end if;
  end if;
         
         
  if i_IFslashID_Isbranch = '1' then  
    if i_MEMslashWB_MemRead = '1' then
      if i_MEMslashWB_RegWrite = '1' then
       if i_MEMslashWB_rd /= "00000" then
          if i_MEMslashWB_rd = i_IFslashID_rt then
            o_IDdataTwo <= "10";
          end if;
        end if;
      end if;
    end if;
  end if; 

  --EX/EX
  if i_EXslashMEM_RegWrite = '1'  then
    if i_EXslashMEM_rd /= "00000" then
      if i_EXslashMEM_rd = i_IDslashEX_rs then 
        o_EXdataOne <= "01";
      end if;
    end if;
  end if;  
  if i_EXslashMEM_RegWrite = '1'  then
    if i_EXslashMEM_rd /= "00000" then
      if i_EXslashMEM_rd = i_IDslashEX_rt then
        o_EXdataTwo <= "01";
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
          o_EXdataOne <= "10";
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
          o_EXdataTwo <= "10";
        end if;
      end if;
    end if;
  end if;
  
  
  --MEM/MEM
  if i_MEMslashWB_MemRead = '1' then
    if i_MEMslashWB_rd /= "00000" then
      if i_MEMslashWB_rd = i_EXslashMEM_rs then
        o_MEMdataOne <= "1";
      end if;
    end if;
  end if;
   
  if i_MEMslashWB_MemRead = '1' then
    if i_MEMslashWB_rd /= "00000" then 
      if i_MEMslashWB_rd = i_EXslashMEM_rt then
        o_MEMdataOne <= "1";
      end if;
    end if;
  end if;

end if RisingEdgeIF;
  
end process;
end dataflow;
