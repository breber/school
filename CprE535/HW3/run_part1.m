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
    embed_percent_length = uint16(ceil(length(image) * percent / 100));
    
    % now zero out imbed bits in cover image
    for x = 1:embed_percent_length
        for y = 1:embed_percent_length
            if use_replacement == 1
                image(x, y) = bitset(image(x, y), 1, message(x, y));
            else
                % TODO
            end
        end
    end
    
    % Draw the image
    figure(figure_index), imshow(image); title(['Image (Embed ', int2str(percent), '%)']);
    figure_index = figure_index + 1;
    
    % For each percentage, calculate the p value
    p_vals = zeros(101, 3);
    for factor = 2:101
        actual_factor = (factor - 1);
        sublength = uint16(ceil(length(image) * actual_factor / 100));
        sub_cover = image(1:sublength, 1:sublength);
        p_vals(factor, 1) = chisq(sub_cover);
        p_vals(factor, 2) = sublength;
        p_vals(factor, 3) = actual_factor / 100;
    end
    
    % Write the p-value structures out to a csv
    csvwrite(strcat('pvals_', int2str(percent), '.csv'), p_vals);

    % Draw the p-value graph
    figure(figure_index), plot(p_vals(:, 3), p_vals(:, 1)); title(['P-Values (Embed ', int2str(percent), '%)']);
    figure_index = figure_index + 1;
end
