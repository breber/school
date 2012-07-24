-------------------------------------------------------------------------
-- Group 9
-- Project Part B 
-- Due November 9th, 2011
-------------------------------------------------------------------------

-- load_mux.vhd
-------------------------------------------------------------------------
-- DESCRIPTION:  This is our logic for loading data from DMEM. It takes
--               in the lowest 2 bits of the memory address, a code from
--               our control logic indicating which instruction is being
--               performed, and the full word of data from the data memory.
--
--               It will output the data to be stored (extended
--               appropriately) into the register file.
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;

entity load_mux is
  port(
			i_Addr 	 			: in std_logic_vector(1 downto 0);
			i_SelW_HW_B	: in std_logic_vector(2 downto 0);
			i_Word				  : in std_logic_vector(31 downto 0);
			o_S						: out std_logic_vector(31 downto 0)
			);
end load_mux;

architecture structure of load_mux is

component eight_to_32_extender
	port(
		  i_In : in std_logic_vector(7 downto 0);
  		  i_S	 : in std_logic;										-- 0 = zero extend, 1 = sign extend
  		  o_F  : out std_logic_vector(31 downto 0)
  		  );
end component;

component sixteen_to_32_extender
	port(
		  i_In : in std_logic_vector(15 downto 0);
  		  i_S	 : in std_logic;										-- 0 = zero extend, 1 = sign extend
  		  o_F  : out std_logic_vector(31 downto 0)
  		  );
end component;

-- Signal for indicating whether we are to sign extend or zero extend
signal s_Signed : std_logic;

-- Our op-code to use the for selecting (just indicate byte, half-word, or word)
signal s_Lop  : std_logic_vector(1 downto 0);

-- Our op-code concatenated with the lower two bits of the address
signal s_CompleteSel : std_logic_vector(3 downto 0);

-- Our extended values
signal s_HW_L, s_HW_U, s_B_0, s_B_1, s_B_2, s_B_3 : std_logic_vector(31 downto 0);

begin

  -- Complete selection signal is our op-code concatenated with the lowest 2 bits of the address
  s_CompleteSel <= s_Lop & i_Addr;

	-- Set the signal that indicates whether the operation is signed or not
	with i_SelW_HW_B select
	s_Signed	<=	'0' when "101",
							'0' when "011",
							'1' when others;

	-- Translate our input code from the control logic to an op-code this mux can use
	with i_SelW_HW_B select
	s_Lop		<=		"00" when "000",
						 "01" when "010",
						 "01" when "011",
						 "10" when "100",
						 "10" when "101",
						 "11" when others;

	-- Half-word Upper
	HWU : sixteen_to_32_extender
		port map(
						i_In => i_Word(31 downto 16),
						i_S  => s_Signed,
						o_F  => s_HW_U
						);
						
	-- Half-word Lower
	HWL : sixteen_to_32_extender
		port map(
						i_In => i_Word(15 downto 0),
						i_S  => s_Signed,
						o_F  => s_HW_L
						);
						
	-- Byte 0
	B0 : eight_to_32_extender
		port map(
						i_In => i_Word(7 downto 0),
						i_S  => s_Signed,
						o_F  => s_B_0
						);

	-- Byte 1
	B1 : eight_to_32_extender
		port map(
						i_In => i_Word(15 downto 8),
						i_S  => s_Signed,
						o_F  => s_B_1
						);

	-- Byte 2
	B2 : eight_to_32_extender
		port map(
						i_In => i_Word(23 downto 16),
						i_S  => s_Signed,
						o_F  => s_B_2
						);

	-- Byte 3
	B3 : eight_to_32_extender
		port map(
						i_In => i_Word(31 downto 24),
						i_S  => s_Signed,
						o_F  => s_B_3
						);
						
	-- Perform the selection for the output
	-- This depends on our op-code, as well as the address
	with s_CompleteSel select
	o_S		<=		i_Word when "0000",
	         i_Word when "0001",
	         i_Word when "0010",
	         i_Word when "0011",
					 s_HW_U when "0110",
					 s_HW_U when "0111",
					 s_HW_L when "0100",
					 s_HW_L when "0101",
					 s_B_0  when "1000",
					 s_B_1  when "1001",
					 s_B_2  when "1010",
					 s_B_3  when "1011",
					 i_Word when others;

end structure;
