% This code is for Homework 3, LSB Embedding, in Math 535, spring 2016
% and was authored by Brian Reber
clear();
close();

% config - should we use bit replacement, or bit matching
use_replacement = 1;

% Generate the message (512x512 of either 0 or 1)
rng(0, 'twister');
message = randi([0 1], 512, 512);

directory = 'input';
image_files = dir(fullfile(directory, '*.bmp'));

% Embed certain percentages
percents = [0, 15, 25, 75, 100];
figure_index = 1;
p_values = zeros(length(image_files) * length(percents), 101);
p_value_index = 1;
cover_rates = zeros(101, 4); % FP, TP, TN, FN
for percent = percents
    rates = cover_rates;
    is_stego = (percent ~= 0);
    
    for index = 1:length(image_files)
        image = imread(fullfile('input', image_files(index).name));
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
        
        % For each percentage, calculate the p value
        p_vals = zeros(100, 2);
        for factor = 1:100
            sublength = uint32(ceil(length(image) * length(image) * factor / 100));
            reshaped_image = reshape(image, length(image) * length(image), 1, 3);
            sub_cover = reshaped_image(1:sublength, 1, 1);
            p_vals(factor, 1) = chisq(sub_cover);
            p_vals(factor, 2) = factor / 100;
        end
        
        % Update the p_values table with this image data for this embed
        % percent
        p_values(p_value_index, 1) = index;
        for pval_index = 1:length(p_vals)
            p_values(p_value_index, pval_index + 1) = p_vals(pval_index, 1);
        end
        p_value_index = p_value_index + 1;
        
        % Determine ROC data
        for threshold = 1:101
            test_stego = any(p_vals(:, 1) >= (threshold - 1) / 100);

            if test_stego && is_stego
                % Test says stego, and is stego --> TP
                rates(threshold, 2) = rates(threshold, 2) + 1;
            elseif test_stego && ~is_stego
                % Test says stego, and is NOT stego --> FP
                rates(threshold, 1) = rates(threshold, 1) + 1;
            elseif ~test_stego && is_stego
                % Test says NOT stego, and is stego --> FN
                rates(threshold, 4) = rates(threshold, 4) + 1;
            elseif ~test_stego && ~is_stego
                % Test says NOT stego, and is NOT stego --> TN
                rates(threshold, 3) = rates(threshold, 3) + 1;
            end
        end
    end
    
    % Write the roc values out to a csv
    csvwrite(strcat('roc_', int2str(percent), '.csv'), rates);

    % Draw the ROC curve
    if is_stego
        true_positive_rate = rates(:, 2) ./ length(image_files);
        true_negative_rate = rates(:, 3) ./ length(image_files);
        false_positive_rate = 1 - true_negative_rate;
        figure(figure_index), plot(true_positive_rate, false_positive_rate); title(['ROC (Embed ', int2str(percent), '%)']);
        figure_index = figure_index + 1;
    else
        cover_rates = rates;
    end
end

% Write the pvalues for all images for all embedding percentages out to a csv
csvwrite('all_pvals.csv', p_values);
