-------------------------------------------------------------------------
-- Group 9
-- Project Part B 
-- Due November 9th, 2011
-------------------------------------------------------------------------

-- store_mux.vhd
-------------------------------------------------------------------------
-- DESCRIPTION:  This is our logic for storing data into DMEM. It takes
--               in the lowest 2 bits of the memory address, a code from
--               our control logic indicating which instruction is being
--               performed, and the full word of data to be stored.
--
--               It will output the data to be stored (shifted appropriately)
--               as well as the byte enable to be used in the DMEM module.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity store_mux is
  port(
			i_Addr 	 			: in std_logic_vector(1 downto 0);
			i_SelW_HW_B	: in std_logic_vector(2 downto 0);
			i_Word				  : in std_logic_vector(31 downto 0);
			o_S						: out std_logic_vector(31 downto 0);
			o_ByteEn 			: out std_logic_vector(3 downto 0)
			);
end store_mux;

architecture structure of store_mux is

-- Our op-code to use the for selecting (just indicate byte, half-word, or word)
signal s_Lop  : std_logic_vector(1 downto 0);

-- Our op-code concatenated with the lower two bits of the address
signal s_CompleteSel : std_logic_vector(3 downto 0);

begin

  -- Complete selection signal is our op-code concatenated with the lowest 2 bits of the address
  s_CompleteSel <= s_Lop & i_Addr;

	-- Translate our input code from the control logic to an op-code this mux can use
	with i_SelW_HW_B select
	s_Lop		<=		"00" when "000",
						 "01" when "010",
						 "01" when "011",
						 "10" when "100",
						 "10" when "101",
						 "11" when others;
						
	-- Perform the selection for the output
	-- This depends on our op-code, as well as the address
	with s_CompleteSel select
		           -- Handle full word cases
	o_ByteEn		<= "1111" when "0000",
	             "1111" when "0001",
	             "1111" when "0010",
               "1111" when "0011",
               
               -- Handle half-word upper cases
					     "1100" when "0110",
					     "1100" when "0111",
					     
					     -- Handle Half-word lower cases
					     "0011" when "0100",
					     "0011" when "0101",
					     
					     -- Handle byte cases
					     "1000"  when "1000",
					     "0100"  when "1001",
					     "0010"  when "1010",
					     "0001"  when "1011",
					     "1111" when others;

	-- Perform the selection for the output
	-- This depends on our op-code, as well as the address
	with s_CompleteSel select
	         -- Handle full word cases
	o_S		<=  i_Word when "0000",
	         i_Word when "0001",
	         i_Word when "0010",
	         i_Word when "0011",
	         
	         -- Handle half-word upper cases
					 i_Word(15 downto 0) & x"0000" when "0110",
					 i_Word(15 downto 0) & x"0000" when "0111",
					 
					 -- Handle Half-word lower cases
					 i_Word when "0100",
					 i_Word when "0101",
					 
					 -- Handle byte cases
					 i_Word  when "1000",
					 i_Word(23 downto 0) & x"00"      when "1001",
					 i_Word(15 downto 0) & x"0000"    when "1010",
					 i_Word(7 downto 0)  & x"000000"  when "1011",
					 i_Word when others;

end structure;
