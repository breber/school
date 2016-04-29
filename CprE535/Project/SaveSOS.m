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

    components(3, 1) = struct('ac', 0, 'dc', 0, 'quant', 0);
    components(1) = struct('ac', 0, 'dc', 0, 'quant', 1);
    components(2) = struct('ac', 1, 'dc', 1, 'quant', 2);
    components(3) = struct('ac', 1, 'dc', 1, 'quant', 2);
    
    % YCbCr Components
    for component_index = 1:3
        % ID
        fwrite(file_id, component_index, 'uint8');

        % DC Selector
        fwrite(file_id, components(component_index).dc, 'ubit4');

        % AC Selector
        fwrite(file_id, components(component_index).ac, 'ubit4');

        % Start of spectral selection (1 byte)
        fwrite(file_id, 0, 'uint8');
        % End of spectral selection (1 byte)
        fwrite(file_id, 0, 'uint8');
        % Successive Approximation Bit High (4 bits)
        fwrite(file_id, 0, 'ubit4');
        % Successive Approximation Bit Low (4 bits)
        fwrite(file_id, 0, 'ubit4');
    end

    % Start writing image data
    image_size = size(rgb_data);

    x_stride = 8;
    y_stride = 8;

    zig_zag_order = [0 1 5 6 14 15 27 28 2 4 7 13 16 26 29 42 3 8 12 17 25 30 41 43 9 11 18 24 31 40 44 53 10 19 23 32 39 45 52 54 20 22 33 38 46 51 55 60 21 34 37 47 50 56 59 61 35 36 48 49 57 58 62 63] + 1;

    dc_values = zeros(3, 1);
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
                for dqt_index = 1:size(scan_data.DQT)
                    if scan_data.DQT(dqt_index).index == components(component).quant
                        quant_table = scan_data.DQT(dqt_index).table;
                        break;
                    end
                end
                
                dct_vals(:, component) = round(dct_vals(:, component) ./ quant_table);
                
                for dht_index = 1:size(scan_data.DHT)
                    if scan_data.DHT(dht_index).ac_dc == 0 && scan_data.DHT(dht_index).table_index == components(component).dc
                        dc_ht = scan_data.DHT(dht_index).blocks;
                    end
                    if scan_data.DHT(dht_index).ac_dc == 1 && scan_data.DHT(dht_index).table_index == components(component).ac
                        ac_ht = scan_data.DHT(dht_index).blocks;
                    end
                end

                % TODO: verify dc_ht and ac_ht exist
               
                % Encode DC values
                diff_dc = dct_vals(1, component) - dc_values(component);
                dc_values(component) = diff_dc;
                if diff_dc == 0
                    fwrite(file_id, dc_ht(1).value, strcat('ubit', num2str(dc_ht(1).length)));
                else
                    pos_diff = int32(abs(diff_dc));
                    bit_length = diff_dc;
                    if diff_dc < 0
                       bit_length = bit_length - 1; 
                    end
                    
                    amplitude = 1;
                    pos_diff = bitsra(pos_diff, 1);
                    while pos_diff > 0
                        amplitude = amplitude + 1;
                        pos_diff = bitsra(pos_diff, 1);
                    end
                    
                    bit_length = bitand(int32(bit_length), (bitshift(1, amplitude) - 1));
                    fwrite(file_id, dc_ht(amplitude + 1).value, strcat('ubit', num2str(dc_ht(amplitude + 1).length)));
                    fwrite(file_id, bit_length, strcat('ubit', num2str(amplitude)));
                end
                
                % Encode AC values
                last_zero = find(dct_vals(:, component), 1, 'last');
                if last_zero == 0
                    % Write EOB
                    fwrite(file_id, ac_ht(1).value, strcat('ubit', num2str(ac_ht(1).length)));
                else
                    % Run-length code the values, and write them
                    code_index = 2;
                    while code_index <= last_zero
                        start_pos = code_index;
                        while dct_vals(code_index) == 0 && code_index <= last_zero
                           code_index = code_index + 1;
                        end
                        
                        count_zeros = code_index - start_pos;
                        if count_zeros >= 16
                            fjdklas = 1;
                            % TODO: write more than 16 zeros
                            % int lng = nrzeroes>>4;
                            % for (int nrmarker=1; nrmarker <= lng; ++nrmarker)
                            %     jo_writeBits(fp, bitBuf, bitCnt, M16zeroes);
                            % nrzeroes &= 15;
                        end
                        
                        % Determine the code
                        code_value = dct_vals(code_index);
                        pos_diff = int32(abs(code_value));
                        bit_length = code_value;
                        if code_value < 0
                           bit_length = bit_length - 1; 
                        end

                        amplitude = 1;
                        pos_diff = bitsra(pos_diff, 1);
                        while pos_diff > 0
                            amplitude = amplitude + 1;
                            pos_diff = bitsra(pos_diff, 1);
                        end

                        bit_length = bitand(int32(bit_length), (bitshift(1, amplitude) - 1));
                        fwrite(file_id, ac_ht(amplitude + 1).value, strcat('ubit', num2str(ac_ht(amplitude + 1).length)));
                        fwrite(file_id, bit_length, strcat('ubit', num2str(amplitude)));
                    end
                    
                    if last_zero ~= 64
                        % Write EOB
                        fwrite(file_id, ac_ht(1).value, strcat('ubit', num2str(ac_ht(1).length)));
                    end
                end
            end
        end
    end

    % Seek to the starting index of the SOS component, and write the length
    length = ftell(file_id) - start_index;
    fseek(file_id, start_index, 'bof');
    fwrite(file_id, length, 'uint16', 'b');

    % Seek back to the end of the file
    fseek(file_id, 0, 'eof');
end
