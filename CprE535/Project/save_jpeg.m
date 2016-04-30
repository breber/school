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
    SaveSOF0(file_id, rgb_data);

    % Write Scan Data
    [start_index, end_index] = SaveSOS(file_id, rgb_data, scan_data);

    % Write the EOI
    fwrite(file_id, hex2dec('FF'), 'uint8');
    fwrite(file_id, hex2dec('D9'), 'uint8');

    % Close the image file
    fclose(file_id);

    % Fix any 0xFF found in the scan data
    file_id = fopen(output_path, 'r');
    full_contents = fread(file_id, 'uint8');
    fclose(file_id);

    file_id = fopen(output_path, 'w');
    for index = 1:size(full_contents)
        fwrite(file_id, full_contents(index), 'uint8');
        if index > start_index && index < end_index && full_contents(index) == 255
            fwrite(file_id, 0, 'uint8');
        end
    end
    fclose(file_id);
end


