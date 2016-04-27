function [] = SaveSOS( file_id, rgb_data, scan_data )
%SaveSOS Write the SOS marker and the scan data
%  Length (2 bytes)
%  Num Components (1 byte)
%  Component:
%    ID (1 byte)
%    DC Selector (4 bits)
%    AC Selector (4 bits)
%    Start of spectral selection (1 byte)
%    End of spectral selection (1 byte)
%    Successive Approximation Bit High (4 bits)
%    Successive Approximation Bit Low (4 bits)
%  Image Data

    % Write the marker
    fwrite(file_id, hex2dec('FF'), 'uint8');
    fwrite(file_id, hex2dec('DA'), 'uint8');

    % Keep track of the start index, so we can come back and write the
    % length information
    start_index = ftell(file_id);

    % Write a placeholder for the length
    fwrite(file_id, 0, 'uint16', 'b');

    % Num Components
    fwrite(file_id, 3, 'uint8');

    % Y Component
    % ID
    fwrite(file_id, 1, 'uint8');

    % DC Selector
    fwrite(file_id, 0, 'ubit4');

    % AC Selector
    fwrite(file_id, 0, 'ubit4');

    % Start of spectral selection (1 byte)
    fwrite(file_id, 0, 'uint8');
    % End of spectral selection (1 byte)
    fwrite(file_id, 0, 'uint8');
    % Successive Approximation Bit High (4 bits)
    fwrite(file_id, 0, 'ubit4');
    % Successive Approximation Bit Low (4 bits)
    fwrite(file_id, 0, 'ubit4');

    % Cb Component
    % ID
    fwrite(file_id, 2, 'uint8');

    % DC Selector
    fwrite(file_id, 1, 'ubit4');

    % AC Selector
    fwrite(file_id, 1, 'ubit4');

    % Start of spectral selection (1 byte)
    fwrite(file_id, 0, 'uint8');
    % End of spectral selection (1 byte)
    fwrite(file_id, 0, 'uint8');
    % Successive Approximation Bit High (4 bits)
    fwrite(file_id, 0, 'ubit4');
    % Successive Approximation Bit Low (4 bits)
    fwrite(file_id, 0, 'ubit4');

    % Cr Component
    % ID
    fwrite(file_id, 3, 'uint8');

    % DC Selector
    fwrite(file_id, 1, 'ubit4');

    % AC Selector
    fwrite(file_id, 1, 'ubit4');

    % Start of spectral selection (1 byte)
    fwrite(file_id, 0, 'uint8');
    % End of spectral selection (1 byte)
    fwrite(file_id, 0, 'uint8');
    % Successive Approximation Bit High (4 bits)
    fwrite(file_id, 0, 'ubit4');
    % Successive Approximation Bit Low (4 bits)
    fwrite(file_id, 0, 'ubit4');



    % TODO write image data



    % Seek to the starting index of the SOS component, and write the length
    length = ftell(file_id) - start_index;
    fseek(file_id, start_index, 'bof');
    fwrite(file_id, length, 'uint16', 'b');

    % Seek back to the end of the file
    fseek(file_id, 0, 'eof');
end
