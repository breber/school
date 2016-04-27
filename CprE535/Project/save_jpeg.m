function [] = save_jpeg( output_path, rgb_data, scan_data )
%SAVE_JPEG Save a JPEG file
%   Save a JPEG file with the given RGB color data, as well as the JPEG
%   parameters (Huffman Tables, Discrete Quantization Tables)
    file_id = fopen(output_path, 'w');

    % Write the initial SOI
    fwrite(file_id, hex2dec('FF'), 'uint8');
    fwrite(file_id, hex2dec('D8'), 'uint8');

    % Write DQT
    SaveDQT(file_id, scan_data.DQT);

    % Write DHT
    SaveDHT(file_id, scan_data.DHT);

    % Write SOF
    fwrite(file_id, hex2dec('FF'), 'uint8');
    fwrite(file_id, hex2dec('C0'), 'uint8');
    % TODO

    % Write Scan Data
    fwrite(file_id, hex2dec('FF'), 'uint8');
    fwrite(file_id, hex2dec('DA'), 'uint8');
    % TODO

    % Write the EOI
    fwrite(file_id, hex2dec('FF'), 'uint8');
    fwrite(file_id, hex2dec('D9'), 'uint8');

    % Close the image file
    fclose(file_id);
end

