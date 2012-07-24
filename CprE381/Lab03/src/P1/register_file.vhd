-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- register_file.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an Register File using structural VHDL
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity register_file is
  port(
			i_CLK 	 : in std_logic;     -- Clock input
      i_AdIn1  : in std_logic_vector(4 downto 0);
      i_AdIn2  : in std_logic_vector(4 downto 0);
      i_AdOut  : in std_logic_vector(4 downto 0);
      i_WData  : in std_logic_vector(31 downto 0);
      i_WE     : in std_logic;
      o_RData1 : out std_logic_vector(31 downto 0);-- Data value input
      o_RData2 : out std_logic_vector(31 downto 0)-- Data value input
			);

end register_file;

architecture structure of register_file is

type array_32 is array(31 downto 0) of std_logic_vector(31 downto 0);

component five_to_thirtytwo_decoder
  port(i_A : in std_logic_vector(4 downto 0);
       o_F : out std_logic_vector(31 downto 0));
end component;

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

component thirtytwo_to_one_mux_df
	port(
		i_1  : in std_logic_vector(31 downto 0);
		i_2  : in std_logic_vector(31 downto 0);
		i_3  : in std_logic_vector(31 downto 0);
		i_4  : in std_logic_vector(31 downto 0);
		i_5  : in std_logic_vector(31 downto 0);
		i_6  : in std_logic_vector(31 downto 0);
		i_7  : in std_logic_vector(31 downto 0);
		i_8  : in std_logic_vector(31 downto 0);
		i_9  : in std_logic_vector(31 downto 0);
		i_10 : in std_logic_vector(31 downto 0);
		i_11 : in std_logic_vector(31 downto 0);
		i_12 : in std_logic_vector(31 downto 0);
		i_13 : in std_logic_vector(31 downto 0);
		i_14 : in std_logic_vector(31 downto 0);
		i_15 : in std_logic_vector(31 downto 0);
		i_16 : in std_logic_vector(31 downto 0);
		i_17 : in std_logic_vector(31 downto 0);
		i_18 : in std_logic_vector(31 downto 0);
		i_19 : in std_logic_vector(31 downto 0);
		i_20 : in std_logic_vector(31 downto 0);
		i_21 : in std_logic_vector(31 downto 0);
		i_22 : in std_logic_vector(31 downto 0);
		i_23 : in std_logic_vector(31 downto 0);
		i_24 : in std_logic_vector(31 downto 0);
		i_25 : in std_logic_vector(31 downto 0);
		i_26 : in std_logic_vector(31 downto 0);
		i_27 : in std_logic_vector(31 downto 0);
		i_28 : in std_logic_vector(31 downto 0);
		i_29 : in std_logic_vector(31 downto 0);
		i_30 : in std_logic_vector(31 downto 0);
		i_31 : in std_logic_vector(31 downto 0);
		i_32 : in std_logic_vector(31 downto 0);
		i_S  : in std_logic_vector(4 downto 0);
		o_F  : out std_logic_vector(31 downto 0)
		);
end component;

signal o_Dec : std_logic_vector(31 downto 0);
signal i_RN : std_logic_vector(31 downto 0);
signal o_R : array_32;

begin

	DEC : five_to_thirtytwo_decoder
		port map(
				i_A => i_AdOut,
				o_F => o_Dec
				);

	RD1 : thirtytwo_to_one_mux_df
		port map(
						i_1  => o_R(0),
						i_2  => o_R(1),
						i_3  => o_R(2),
						i_4  => o_R(3),
						i_5  => o_R(4),
						i_6  => o_R(5),
						i_7  => o_R(6),
						i_8  => o_R(7),
						i_9  => o_R(8),
						i_10 => o_R(9),
						i_11 => o_R(10),
						i_12 => o_R(11),
						i_13 => o_R(12),
						i_14 => o_R(13),
						i_15 => o_R(14),
						i_16 => o_R(15),
						i_17 => o_R(16),
						i_18 => o_R(17),
						i_19 => o_R(18),
						i_20 => o_R(19),
						i_21 => o_R(20),
						i_22 => o_R(21),
						i_23 => o_R(22),
						i_24 => o_R(23),
						i_25 => o_R(24),
						i_26 => o_R(25),
						i_27 => o_R(26),
						i_28 => o_R(27),
						i_29 => o_R(28),
						i_30 => o_R(29),
						i_31 => o_R(30),
						i_32 => o_R(31),
						i_S  => i_AdIn1,
						o_F  => o_RData1
						);

	RD2 : thirtytwo_to_one_mux_df
		port map(
						i_1  => o_R(0),
						i_2  => o_R(1),
						i_3  => o_R(2),
						i_4  => o_R(3),
						i_5  => o_R(4),
						i_6  => o_R(5),
						i_7  => o_R(6),
						i_8  => o_R(7),
						i_9  => o_R(8),
						i_10 => o_R(9),
						i_11 => o_R(10),
						i_12 => o_R(11),
						i_13 => o_R(12),
						i_14 => o_R(13),
						i_15 => o_R(14),
						i_16 => o_R(15),
						i_17 => o_R(16),
						i_18 => o_R(17),
						i_19 => o_R(18),
						i_20 => o_R(19),
						i_21 => o_R(20),
						i_22 => o_R(21),
						i_23 => o_R(22),
						i_24 => o_R(23),
						i_25 => o_R(24),
						i_26 => o_R(25),
						i_27 => o_R(26),
						i_28 => o_R(27),
						i_29 => o_R(28),
						i_30 => o_R(29),
						i_31 => o_R(30),
						i_32 => o_R(31),
						i_S  => i_AdIn2,
						o_F  => o_RData2
						);

	RN : nbit_register
		port map(
						i_CLK => i_CLK,
			      i_RST => '1',
			      i_WE  => '0',
   			    i_D   => x"00000000",
			      o_Q   => o_R(0)
						);

	GEN : for i in 1 to 31 generate
		i_RN(i) <= ((i_WE) and (o_Dec(i)));
		RN : nbit_register
			port map(
							i_CLK => i_CLK,
				      i_RST => '0',
				      i_WE  => i_RN(i),
	   			    i_D   => i_WData,
				      o_Q   => o_R(i)
							);
	end generate;
end structure;
