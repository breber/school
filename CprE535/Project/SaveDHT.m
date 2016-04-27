function [] = SaveDHT( file_id, tables )
%SaveDHT Handle the DHT
%  Length (2 bytes)
%  Table:
%    AC/DC (4 bits)
%    Destination Identifier (4 bits)
%    Block Lengths (16 bytes)
%    HuffEncodedValues (sum(block lengths) bytes)

    % Write the marker
    fwrite(file_id, hex2dec('FF'), 'uint8');
    fwrite(file_id, hex2dec('C4'), 'uint8');
    
    % Keep track of the start index, so we can come back and write the
    % length information
    start_index = ftell(file_id);
    
    % Write a placeholder for the length
    fwrite(file_id, 0, 'uint16', 'b');

    % Write each table
    for i = 1:size(tables)
        table_row = tables(i);
        
        % AC/DC indicator
        fwrite(file_id, uint8(table_row.ac_dc), 'ubit4', 'b');
        
        % Destination Identifier
        fwrite(file_id, uint8(table_row.table_index), 'ubit4', 'b');
        
        % Lengths
        fwrite(file_id, table_row.lengths(2:end), 'uint8');
        
        % Blocks
        for b = 1:size(table_row.blocks)
            fwrite(file_id, table_row.blocks(b).value, 'uint8');
        end
    end
    
    % Seek to the starting index of the DHT component, and write the length
    length = ftell(file_id) - start_index;
    fseek(file_id, start_index, 'bof');
    fwrite(file_id, length, 'uint16', 'b');
    
    % Seek back to the end of the file
    fseek(file_id, 0, 'eof');
end
