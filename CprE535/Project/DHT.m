function [ tables ] = DHT( file_id )
%DHT Handle the DHT marker
%  Length (2 bytes)
%  Table:
%    AC/DC (4 bits)
%    Destination Identifier (4 bits)
%    Block Lengths (16 bytes)
%    HuffEncodedValues (sum(block lengths) bytes)
    start_index = ftell(file_id);

    remaining_length = fread(file_id, 1, 'uint16', 'b');
    tables(4, 1) = struct('ac_dc', 0, 'table_index', [], 'lengths', [], 'blocks', '');

    index = 0;
    while ftell(file_id) - start_index < remaining_length
        index = index + 1;
        tables(index).ac_dc = fread(file_id, 1, 'ubit4', 'b');
        tables(index).table_index = fread(file_id, 1, 'ubit4', 'b');
        tables(index).lengths = cat(1, 0, fread(file_id, 16, 'uint8'));

        % Build the basic information for the tables (including counts)
        blocks(sum(tables(index).lengths), 1) = struct('length', 0, 'code', 0, 'value', 0);
        block_index = 1;
        for i = 1:16
            for j = 0:(tables(index).lengths(i + 1) - 1)
                blocks(block_index).length = i;
                block_index = block_index + 1;
            end
        end

        % Generate the huffman codes
        length_count = 1;
        code = 0;
        for i = 1:sum(tables(index).lengths)
            while length_count < blocks(i).length
                code = bitshift(code, 1);
                length_count = length_count + 1;
            end

            blocks(i).code = code;
            blocks(i).value = fread(file_id, 1, 'uint8');
            code = code + 1;
        end

        tables(index).blocks = blocks;
    end
end
