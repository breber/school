function [ rgb_data, scan_data ] = load_jpeg( image_path )
%LOAD_JPEG Summary of this function goes here
%   Detailed explanation goes here
    markers_str = containers.Map( { hex2dec('D8'), hex2dec('E0'), hex2dec('DA'), hex2dec('D9'), hex2dec('DB'), hex2dec('C4'), hex2dec('C0') }, { 'SOI', 'APP0', 'SOS', 'EOI', 'DQT', 'DHT', 'SOF0' } );

    % Read in the raw image data
    rgb_data = imread(image_path);

    scan_data = struct();

    % Start reading in the header information
    file_id = fopen(image_path, 'r');
    while ~feof(file_id)
        current_byte = fread(file_id, 1, 'uint8');

        if current_byte == 255
            next_byte = fread(file_id, 1, 'uint8');

            if next_byte ~= 0
                if isKey(markers_str, next_byte)
                    marker_str = values(markers_str, num2cell(next_byte));
                    fprintf('0x%02x --> %s\n', next_byte, marker_str{1});
                    func = str2func(marker_str{1});
                    func_result = func(file_id);
                    if isstruct(func_result)
                        scan_data.(marker_str{1}) = func_result;
                    end
                else
                    fprintf('Unknown marker: 0x%02x\n', next_byte);
                    skip_marker(file_id);
                end
            end
        end
    end
end
