function [] = SaveSOF0( file_id, rgb_data )
%SaveSOF0 Save the SOF0 data
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

    % Write the marker
    fwrite(file_id, hex2dec('FF'), 'uint8');
    fwrite(file_id, hex2dec('C0'), 'uint8');

    % Keep track of the start index, so we can come back and write the
    % length information
    start_index = ftell(file_id);

    % Write a placeholder for the length
    fwrite(file_id, 0, 'uint16', 'b');

    % Sample Precision
    fwrite(file_id, 8, 'uint8');

    rgb_size = size(rgb_data);

    % Num Lines
    fwrite(file_id, rgb_size(1), 'uint16', 'b');

    % Samples per Line
    fwrite(file_id, rgb_size(2), 'uint16', 'b');

    % Num Components
    fwrite(file_id, 3, 'uint8');


    % Y Component
    % ID
    fwrite(file_id, 1, 'uint8');

    % Horizontal sample factor
    fwrite(file_id, 2, 'ubit4');

    % Vertical sample factor
    fwrite(file_id, 2, 'ubit4');

    % Quantization Table Selector
    fwrite(file_id, 0, 'uint8');

    % Cb Component
    % ID
    fwrite(file_id, 2, 'uint8');

    % Horizontal sample factor
    fwrite(file_id, 1, 'ubit4');

    % Vertical sample factor
    fwrite(file_id, 1, 'ubit4');

    % Quantization Table Selector
    fwrite(file_id, 1, 'uint8');

    % Cr Component
    % ID
    fwrite(file_id, 3, 'uint8');

    % Horizontal sample factor
    fwrite(file_id, 1, 'ubit4');

    % Vertical sample factor
    fwrite(file_id, 1, 'ubit4');

    % Quantization Table Selector
    fwrite(file_id, 1, 'uint8');


    % Seek to the starting index of the SOF0 component, and write the length
    length = ftell(file_id) - start_index;
    fseek(file_id, start_index, 'bof');
    fwrite(file_id, length, 'uint16', 'b');

    % Seek back to the end of the file
    fseek(file_id, 0, 'eof');
end
