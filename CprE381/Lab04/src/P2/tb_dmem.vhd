-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- tb_dmem.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a simple VHDL testbench for our
-- memory module.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

-- This is an empty entity so we don't need to declare ports
entity tb_dmem is
  generic(gCLK_HPER   : time := 50 ns);
end tb_dmem;

architecture behavior of tb_dmem is
  
  -- Calculate the clock period as twice the half-period
  constant cCLK_PER  : time := gCLK_HPER * 2;

-- Declare the components we are going to instantiate
component mem
	generic(
			   depth_exp_of_2 : integer := 10;
			   mif_filename 	: string := "dmem.mif"
				 );
				 
	port(
			address	: IN STD_LOGIC_VECTOR (depth_exp_of_2-1 DOWNTO 0) := (OTHERS => '0');
			byteena	: IN STD_LOGIC_VECTOR (3 DOWNTO 0) := (OTHERS => '1');
			clock		: IN STD_LOGIC := '1';
			data		: IN STD_LOGIC_VECTOR (31 DOWNTO 0) := (OTHERS => '0');
			wren		: IN STD_LOGIC := '0';
			q				: OUT STD_LOGIC_VECTOR (31 DOWNTO 0)
			);
end component;


-- Address signal
signal s_Address : std_logic_vector(9 downto 0);

-- Byte Enable
signal s_ByteEna : std_logic_vector(3 downto 0);

-- Width 32 bit signals
signal s_QVal, s_Data : std_logic_vector(31 downto 0);

-- Control Signals
signal s_CLK, s_WE : std_logic;

begin

MMEM : mem
  port map(
					address => s_Address,
					byteena => s_ByteEna,
					clock   => s_CLK,
					data    => s_Data,
					wren    => s_WE,
					q       => s_QVal
					);
					
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

		-- Read value 1 from memory
		s_Address <= "0000000000";
		s_ByteEna <= "1111";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for cCLK_PER;
    
    -- Write value 1 to memory
		s_Address <= "0100000000";
		s_Data    <= s_QVal;
		s_WE			<= '1';
    wait for 2 * cCLK_PER;
    
    -- Read value 1 from memory after write
		s_Address <= "0100000000";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for 2 * cCLK_PER;
    
    
		-- Read value 2 from memory
		s_Address <= "0000000001";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for cCLK_PER;
    
    -- Write value 2 to memory
		s_Address <= "0100000001";
		s_Data    <= s_QVal;
		s_WE			<= '1';
    wait for 2 * cCLK_PER;
    
    -- Read value 2 from memory after write
		s_Address <= "0100000001";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for 2 * cCLK_PER;
    
    
    -- Read value 3 from memory
		s_Address <= "0000000010";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for cCLK_PER;
    
    -- Write value 3 to memory
		s_Address <= "0100000010";
		s_Data    <= s_QVal;
		s_WE			<= '1';
    wait for 2 * cCLK_PER;
    
    -- Read value 3 from memory after write
		s_Address <= "0100000010";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for 2 * cCLK_PER;
    
    
    -- Read value 4 from memory
		s_Address <= "0000000011";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for cCLK_PER;
    
    -- Write value 4 to memory
		s_Address <= "0100000011";
		s_Data    <= s_QVal;
		s_WE			<= '1';
    wait for 2 * cCLK_PER;
    
    -- Read value 4 from memory after write
		s_Address <= "0100000011";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for 2 * cCLK_PER;
    
    
    -- Read value 5 from memory
		s_Address <= "0000000100";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for cCLK_PER;
    
    -- Write value 5 to memory
		s_Address <= "0100000100";
		s_Data    <= s_QVal;
		s_WE			<= '1';
    wait for 2 * cCLK_PER;
    
    -- Read value 5 from memory after write
		s_Address <= "0100000100";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for 2 * cCLK_PER;
    
    
		-- Read value 6 from memory
		s_Address <= "0000000101";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for cCLK_PER;
    
    -- Write value 6 to memory
		s_Address <= "0100000101";
		s_Data    <= s_QVal;
		s_WE			<= '1';
    wait for 2 * cCLK_PER;
    
    -- Read value 6 from memory after write
		s_Address <= "0100000101";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for 2 * cCLK_PER;
    
    
		-- Read value 7 from memory
		s_Address <= "0000000110";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for cCLK_PER;
    
    -- Write value 7 to memory
		s_Address <= "0100000110";
		s_Data    <= s_QVal;
		s_WE			<= '1';
    wait for 2 * cCLK_PER;
    
    -- Read value 7 from memory after write
		s_Address <= "0100000110";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for 2 * cCLK_PER;
    
    
    -- Read value 8 from memory
		s_Address <= "0000000111";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for cCLK_PER;
    
    -- Write value 8 to memory
		s_Address <= "0100000111";
		s_Data    <= s_QVal;
		s_WE			<= '1';
    wait for 2 * cCLK_PER;
    
    -- Read value 8 from memory after write
		s_Address <= "0100000111";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for 2 * cCLK_PER;
    
    
    -- Read value 9 from memory
		s_Address <= "0000001000";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for cCLK_PER;
    
    -- Write value 9 to memory
		s_Address <= "0100001000";
		s_Data    <= s_QVal;
		s_WE			<= '1';
    wait for 2 * cCLK_PER;
    
    -- Read value 9 from memory after write
		s_Address <= "0100001000";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for 2 * cCLK_PER;
    
    
		-- Read value 10 from memory
		s_Address <= "0000001001";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for cCLK_PER;
    
    -- Write value 10 to memory
		s_Address <= "0100001001";
		s_Data    <= s_QVal;
		s_WE			<= '1';
    wait for 2 * cCLK_PER;
    
    -- Read value 10 from memory after write
		s_Address <= "0100001001";
		s_Data    <= x"00000000";
		s_WE			<= '0';
    wait for 2 * cCLK_PER;    

  end process;

end behavior;
