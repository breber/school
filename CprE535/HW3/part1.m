function p_vals = part1(image_name, message_image_name, embed_percent)
    % Read in the image
    cover = imread(image_name);
    message = imread(message_image_name);
    
    %figure(1),imshow(cover);title('Cover image');
    %figure(2),imshow(message);title('Message image');
    
    % Embed the message
    embed_percent_length = uint16(ceil(length(cover) * embed_percent / 100));
    
    % imbed = no. of bits of message image to embed in cover image
    imbed = 4;

    % shift the message image over (8-imbed) bits to right
    messageshift = bitshift(message, -(8-imbed));

    % now zero out imbed bits in cover image
    for x = 1:embed_percent_length
        for y = 1:embed_percent_length
            for i = 1:imbed
                cover(x, y) = bitset(cover(x, y), i, 0);
            end
        end
    end

    % now add message image and cover image
    stego = uint8(cover + messageshift);
    %figure(3),imshow(stego);title('Stego image');
    
    p_vals = zeros(100, 3);
    
    % For each percentage, calculate the p value
    for factor = 2:100
        sublength = uint16(ceil(length(stego) * factor / 100));
        sub_cover = stego(1:sublength, 1:sublength);
        p_vals(factor, 1) = chisq(sub_cover);
        p_vals(factor, 2) = sublength;
        p_vals(factor, 3) = factor / 100;
    end
    
    plot(p_vals(:, 3), p_vals(:, 1));
end