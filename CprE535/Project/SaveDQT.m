function [] = SaveDQT( file_id, quant_data )
%SaveDQT Save the DQT
%  Length (2 bytes)
%  Table:
%    Precision (4 bits)
%    Index (4 bits)
%    TableData (64 bytes)

    % Write the marker
    fwrite(file_id, hex2dec('FF'), 'uint8');
    fwrite(file_id, hex2dec('DB'), 'uint8');

    % Write the component size
    num_tables = size(quant_data);
    fwrite(file_id, 2 + num_tables(1) * 65, 'uint16', 'b');

    % Write each table
    for i = 1:num_tables
        table_row = quant_data(i);

        % Precision
        fwrite(file_id, uint8(table_row.precision), 'ubit4', 'b');

        % Index
        fwrite(file_id, uint8(i - 1), 'ubit4', 'b');

        % Table Values
        fwrite(file_id, table_row.table, 'uint8');
    end
end

