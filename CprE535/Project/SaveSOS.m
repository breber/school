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

    % Start writing image data
    image_size = size(rgb_data);

    x_stride = 8;
    y_stride = 8;

    zig_zag_order = [0 1 5 6 14 15 27 28 2 4 7 13 16 26 29 42 3 8 12 17 25 30 41 43 9 11 18 24 31 40 44 53 10 19 23 32 39 45 52 54 20 22 33 38 46 51 55 60 21 34 37 47 50 56 59 61 35 36 48 49 57 58 62 63] + 1;
    quantization_table_index = [1 2 2];

    for y = 1:y_stride:image_size(1)
        for x = 1:x_stride:image_size(2)
            ycbcr = zeros(y_stride * x_stride, 3);

            % Convert RGB to YCbCr
            for y1 = 1:y_stride
                for x1 = 1:x_stride
                    index = (y1 - 1) * y_stride + x1;
                    r = rgb_data(y1, x1, 1);
                    g = rgb_data(y1, x1, 2);
                    b = rgb_data(y1, x1, 3);
                    ycbcr(index, 1) =       (.229 * r) + (.587 * g) + (.114 * b);
                    ycbcr(index, 2) = 128 - (.168736 * r) - (.331264 * g) + (.5 * b);
                    ycbcr(index, 3) = 128 + (.5 * r) - (.418688 * g) - (.081312 * b);
                end
            end

            % Perform DCT, zig-zag the data and quantize it
            dct_vals = zeros(y_stride * x_stride, 3);
            for component = 1:3
                temp_dct = dct2(ycbcr(:, component) - 128);

                % Zig-zag the data
                for item = 1:64
                    dct_vals(item, component) = temp_dct(zig_zag_order(item));
                end

                % Quantize the data (quantization table is already zig-zagged)
                dct_vals(:, component) = round(dct_vals(:, component) ./ scan_data.DQT(quantization_table_index(component)).table);
            end
            
            % TODO run length coding
            % TODO huffman code
        end
    end

    % Seek to the starting index of the SOS component, and write the length
    length = ftell(file_id) - start_index;
    fseek(file_id, start_index, 'bof');
    fwrite(file_id, length, 'uint16', 'b');

    % Seek back to the end of the file
    fseek(file_id, 0, 'eof');
end
