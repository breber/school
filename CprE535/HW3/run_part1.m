% This code is for Homework 3, LSB Embedding, in Math 535, spring 2016
% and was authored by Brian Reber
clear();
close();

% config - should we use bit replacement, or bit matching
use_replacement = 1;

% Generate the message (512x512 of either 0 or 1)
rng(0, 'twister');
message = randi([0 1], 512, 512);

% Read in the cover image
%covername = input('Enter image file name with extension (like jennifer.bmp): ', 's');
covername = 'input\Agfa_DC-504_0_1_512.bmp';

% Embed certain percentages
percents = [0, 15, 25, 75, 100];
figure_index = 1;
for percent = percents
    image = imread(covername);
    embed_percent_length = uint32(ceil(length(image) * length(image) * percent / 100));
    
    % now embed the message
    for x = 1:embed_percent_length
        % Find the pixel location
        i = idivide(x, length(image) + 1, 'floor') + 1;
        j = mod(x - 1, length(image)) + 1;
        
        if use_replacement == 1
            image(i, j) = bitset(image(i, j), 1, message(i, j));
        else
            % LSB matching algorithm:
            % - if the message bit == cover image bit, do nothing
            % - if the message bit != cover image bit:
            %   - pick random value (r) between [-1, 1]
            %   - stego(x, y) = cover(x, y) + r
            if message(i, j) ~= bitget(image(i, j), 1)
                r = randi([-1 1], 1, 1);
                image(i, j) = image(i, j) + r;
            end
        end
    end
    
    % Draw the image
    figure(figure_index), imshow(image); title(['Image (Embed ', int2str(percent), '%)']);
    figure_index = figure_index + 1;
    
    % For each percentage, calculate the p value
    p_vals = zeros(100, 2);
    for factor = 1:100
        sublength = uint32(ceil(length(image) * length(image) * factor / 100));
        reshaped_image = reshape(image, length(image) * length(image), 1, 3);
        sub_cover = reshaped_image(1:sublength, 1, 1);
        p_vals(factor, 1) = chisq(sub_cover);
        p_vals(factor, 2) = factor / 100;
    end
    
    % Write the p-value structures out to a csv
    csvwrite(strcat('pvals_', int2str(percent), '.csv'), p_vals);

    % Draw the p-value graph
    figure(figure_index), plot(p_vals(:, 2), p_vals(:, 1)); title(['P-Values (Embed ', int2str(percent), '%)']);
    figure_index = figure_index + 1;
end
