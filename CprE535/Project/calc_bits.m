function [ bit_count, amplitude ] = calc_bits( input_value )
%CALC_BITS Calculate the number of bits it takes to represent a value, and
%the amplitude of the value

    % Convert the input to positive. This will be used to determine how
    % many bits are required to encode the value
    pos_value = int32(abs(input_value));

    % Convert to 1's compliment
    ones_compliment = input_value;
    if input_value < 0
        ones_compliment = ones_compliment - 1;
    end

    % Keep shifting the pos version to the right, each time incrementing
    % the amplitude value.
    bit_count = int16(1);
    pos_value = bitsra(pos_value, 1);
    while pos_value > 0
        bit_count = bit_count + 1;
        pos_value = bitsra(pos_value, 1);
    end

    % And the 1's compliment value with ((1 << amplitude) - 1) to determine
    % the amplitude of the resulting value
    amplitude = bitand(int32(ones_compliment), bitshift(1, bit_count) - 1);
end
