function [ tables ] = ParseDQT( file_id )
%ParseDQT Handle the DQT marker
%  Length (2 bytes)
%  Table:
%    Precision (4 bits)
%    Index (4 bits)
%    TableData (64 bytes)

    remaining_length = fread(file_id, 1, 'uint16', 'b');
    num_tables = (remaining_length - 2) / 65;
    tables(num_tables, 1) = struct('precision', 0, 'table', []);
    for idx = 1:num_tables
        precision = fread(file_id, 1, 'ubit4', 'b');
        table_index = fread(file_id, 1, 'ubit4', 'b');
        table = fread(file_id, 64, 'uint8');
        tables(table_index + 1) = struct('precision', precision, 'table', table);
    end
end
