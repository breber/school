function [ tables ] = ParseDHT( file_id )
%ParseDHT Handle the DHT marker
%  Length (2 bytes)
%  Table:
%    AC/DC (4 bits)
%    Destination Identifier (4 bits)
%    Block Lengths (16 bytes)
%    HuffEncodedValues (sum(block lengths) bytes)
    start_index = ftell(file_id);

    remaining_length = fread(file_id, 1, 'uint16', 'b');
    tables = [];

    while ftell(file_id) - start_index < remaining_length
        table = struct('ac_dc', 0, 'table_index', [], 'lengths', [], 'blocks', '');
        table.ac_dc = fread(file_id, 1, 'ubit4', 'b');
        table.table_index = fread(file_id, 1, 'ubit4', 'b');
        table.lengths = cat(1, 0, fread(file_id, 16, 'uint8'));

        % Build the basic information for the tables (including counts)
        blocks(sum(table.lengths), 1) = struct('length', 0, 'code', 0, 'value', 0);
        block_index = 1;
        for i = 1:16
            for j = 0:(table.lengths(i + 1) - 1)
                blocks(block_index).length = i;
                block_index = block_index + 1;
            end
        end

        % Generate the huffman codes
        length_count = 1;
        code = 0;
        for i = 1:sum(table.lengths)
            while length_count < blocks(i).length
                code = bitshift(code, 1);
                length_count = length_count + 1;
            end

            blocks(i).code = code;
            blocks(i).value = fread(file_id, 1, 'uint8');
            code = code + 1;
        end

        table.blocks = blocks;
        tables = cat(1, tables, table);
    end
end
