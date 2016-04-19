function [] = skip_marker( file_id )
%skip_marker Skip the contents of the current marker

    % While we haven't reached the end of the file, and we haven't reached
    % a new marker (0xFF followed by anything other than 0x00), read in and
    % skip over the data
    while ~feof(file_id)
        current_byte = fread(file_id, 1, 'uint8');
        if current_byte == 255
            next_byte = fread(file_id, 1, 'uint8');
            if next_byte ~= 0
                % We found a 0xFF followed by something other than 0x00, so
                % we should back up 2 bytes (to be before the 0xFF), and
                % return out of the function
                fseek(file_id, -2, 'cof');
                return
            end
        end
    end
end
