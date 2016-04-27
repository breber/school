function [ unused ] = ParseSOF0(~)
%ParseSOF0 Handle the SOF0 marker
%  Length (2 bytes)
%  Sample Precision (1 byte)
%  Num Lines (2 bytes)
%  Samples per line (2 bytes)
%  Num Components (1 byte)
%  Component:
%    ID (1 byte)
%    Horizontal Sample Factor (4 bits)
%    Vertical Sample Factor (4 bits)
%    Quanization Table Selector (1 byte)

    % No special handling is needed
    unused = 0;
end
