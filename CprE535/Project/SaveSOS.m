function [ start_index, end_index ] = SaveSOS( file_id, rgb_data, scan_data )
%SaveSOS Write the SOS marker and the scan data
%  Length (2 bytes)
%  Num Components (1 byte)
%  Component:
%    ID (1 byte)
%    DC Selector (4 bits)
%    AC Selector (4 bits)
%  Start of spectral selection (1 byte)
%  End of spectral selection (1 byte)
%  Successive Approximation Bit High (4 bits)
%  Successive Approximation Bit Low (4 bits)
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
        fwrite(file_id, components(component_index).dc, 'ubit4', 'b');

        % AC Selector
        fwrite(file_id, components(component_index).ac, 'ubit4', 'b');
    end

    % Start of spectral selection (1 byte)
    fwrite(file_id, 0, 'uint8');
    % End of spectral selection (1 byte)
    fwrite(file_id, 0, 'uint8');
    % Successive Approximation Bit High (4 bits)
    fwrite(file_id, 0, 'ubit4', 'b');
    % Successive Approximation Bit Low (4 bits)
    fwrite(file_id, 0, 'ubit4', 'b');

    % Seek to the starting index of the SOS component, and write the length
    length = ftell(file_id) - start_index;
    fseek(file_id, start_index, 'bof');
    fwrite(file_id, length, 'uint16', 'b');

    % Seek back to the end of the file
    fseek(file_id, 0, 'eof');

    % Start writing image data
    image_size = size(rgb_data);

    x_stride = 8;
    y_stride = 8;

    zig_zag_order = [0 1 5 6 14 15 27 28 2 4 7 13 16 26 29 42 3 8 12 17 25 30 41 43 9 11 18 24 31 40 44 53 10 19 23 32 39 45 52 54 20 22 33 38 46 51 55 60 21 34 37 47 50 56 59 61 35 36 48 49 57 58 62 63] + 1;

    dc_values = zeros(3, 1);
    for y = 1:y_stride:image_size(1)
        for x = 1:x_stride:image_size(2)
            ycbcr = zeros(y_stride, x_stride, 3);

            % Convert RGB to YCbCr
            for y1 = 0:(y_stride - 1)
                for x1 = 0:(x_stride - 1)
                    r = rgb_data(y1 + y, x1 + x, 1);
                    g = rgb_data(y1 + y, x1 + x, 2);
                    b = rgb_data(y1 + y, x1 + x, 3);
                    ycbcr(y1 + 1, x1 + 1, 1) =       (.229 * r) + (.587 * g) + (.114 * b);
                    ycbcr(y1 + 1, x1 + 1, 2) = 128 - (.168736 * r) - (.331264 * g) + (.5 * b);
                    ycbcr(y1 + 1, x1 + 1, 3) = 128 + (.5 * r) - (.418688 * g) - (.081312 * b);
                end
            end

            % Perform DCT, zig-zag the data and quantize it
            for component = 1:3
                temp_dct = dct2(ycbcr(:, :, component) - 128);

                % Linearize the matrix
                linearized_dct = zeros(y_stride * x_stride, 1);
                for y1 = 1:y_stride
                    for x1 = 1:x_stride
                        index = (y1 - 1) * x_stride + x1;
                        linearized_dct(index) = temp_dct(y1, x1);
                    end
                end

                % Zig-zag the data
                dct_vals = zeros(y_stride * x_stride, 1);
                for item = 1:64
                    dct_vals(item) = linearized_dct(zig_zag_order(item));
                end

                % Quantize the data (quantization table is already zig-zagged)
                for dqt_index = 1:size(scan_data.DQT)
                    if scan_data.DQT(dqt_index).index == components(component).quant
                        quant_table = scan_data.DQT(dqt_index).table;
                        break;
                    end
                end

                dct_vals(:) = round(dct_vals(:) ./ quant_table);

                % Run-length code the values
                % (RUNLENGTH, SIZE)	(AMPLITUDE)
                run_length_coded = [];
                dct_index = int32(2);
                count_zeros = int8(0);
                while dct_index < 65
                    if dct_vals(dct_index) == 0
                        count_zeros = count_zeros + 1;
                    else
                        % Determine number of bits it takes to encode the
                        % given value
                        [bit_count, amplitude] = calc_bits(dct_vals(dct_index));

                        % Append this data to the list of run-length
                        % codings
                        run_length_coded = cat(1, run_length_coded, [count_zeros, bit_count, amplitude]);

                        % Reset zero count to 0
                        count_zeros = int8(0);
                    end

                    dct_index = dct_index + 1;
                end

                % Huffman Encode the run-length data
                % Get the right huffman tables
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
                diff_dc = dct_vals(1) - dc_values(component);
                dc_values(component) = diff_dc;
                if diff_dc == 0
                    fwrite(file_id, dc_ht(1).code, strcat('ubit', num2str(dc_ht(1).length)), 'b');
                else
                    [bit_count, amplitude] = calc_bits(diff_dc);

                    for table_idx = 1:size(dc_ht)
                        if dc_ht(table_idx).value == bit_count
                            dc_table_row = dc_ht(table_idx);
                            break;
                        end
                    end

                    fwrite(file_id, dc_table_row.code, strcat('ubit', num2str(dc_table_row.length)), 'b');
                    fwrite(file_id, amplitude, strcat('ubit', num2str(bit_count)), 'b');
                end

                % Encode AC values
                if size(run_length_coded) == 0
                    % If we don't have any values in the run-length coding,
                    % it means that all the values were 0, and therefore we
                    % just need to write the EOB
                    fwrite(file_id, ac_ht(1).code, strcat('ubit', num2str(ac_ht(1).length)), 'b');
                else
                    count_values = 0;
                    % Otherwise, encode the data
                    for run_length_index = 1:size(run_length_coded)
                        current_row = run_length_coded(run_length_index, :);

                        % Count number of zeros and current value
                        count_values = count_values + current_row(1) + 1;

                        num_zeros = current_row(1);
                        bit_count = current_row(2);
                        amplitude = current_row(3);

                        % If there are more than 16 zeros, we need to write
                        % a special marker
                        if num_zeros > 16
                            mag = bitsra(num_zeros, 4);

                            ht_index = bitshift(15, 4) + 1;

                            for table_idx = 1:size(ac_ht)
                                if ac_ht(table_idx).value == ht_index
                                    ac_table_row = ac_ht(table_idx);
                                    break;
                                end
                            end

                            for marker_num = 1:mag
                                fwrite(file_id, ac_table_row.code, strcat('ubit', num2str(ac_table_row.length)), 'b');
                            end

                            % Remove all but the lowest 4 bits
                            num_zeros = bitand(num_zeros, 15);
                        end

                        ht_index = bitor(bitshift(num_zeros, 4), bit_count);

                        for table_idx = 1:size(ac_ht)
                            if ac_ht(table_idx).value == ht_index
                                ac_table_row = ac_ht(table_idx);
                                break;
                            end
                        end

                        fwrite(file_id, ac_table_row.code, strcat('ubit', num2str(ac_table_row.length)), 'b');
                        fwrite(file_id, amplitude, strcat('ubit', num2str(bit_count)), 'b');
                    end

                    % If the last zero is not the last item, we need to
                    % write an EOB marker to indicate that this block
                    % isn't fully filled
                    if count_values ~= 63
                        fwrite(file_id, ac_ht(1).code, strcat('ubit', num2str(ac_ht(1).length)), 'b');
                    end
                end
            end
        end
    end

    end_index = ftell(file_id);
end
