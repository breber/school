function [ unused ] = ParseSOS(~)
%ParseSOS Handle the SOS marker
%  Length (2 bytes)
%  Num Components (1 byte)
%  Component:
%    ID (1 byte)
%    DC Selector (4 bits)
%    AC Selector (4 bits)
%  Start of spectral selection (1 byte)
%  End of spectral selection (1 byte)
%  Successive Approximation Bit High (4 bits)
%  Successive Approximation Bit Low (4 bits)
%  Image Data

    % No special handling is needed
    unused = 0;
end
