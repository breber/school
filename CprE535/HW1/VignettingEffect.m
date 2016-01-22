% This code is for Homework 1, Vignetting, in Math 535, spring 2016
% and was authored by Brian Reber

function [ vignetteImg ] = VignettingEffect(inputImage, scaleLevel)
    imageInfo = imfinfo(inputImage);
    inputImageData = imread(inputImage);
    
    centerY = imageInfo.Height / 2;
    centerX = imageInfo.Width / 2;
    
    % To handle color images, just add a third dimension to the output
    % image with the size being number of color channels
    vignetteImg = zeros(imageInfo.Height, imageInfo.Width, imageInfo.NumberOfSamples, 'uint8');
    
    maxRadius = sqrt(centerY ^ 2 + centerX ^ 2);
    for x = 1:imageInfo.Width
        for y = 1:imageInfo.Height
            radius = sqrt((x - centerX) ^ 2 + (y - centerY) ^ 2);
            
            % Need to perform vignetting for each channel individually
            for z = 1:imageInfo.NumberOfSamples
                vignetteImg(y, x, z) = inputImageData(y, x, z) * (1 - scaleLevel * radius / maxRadius);
            end
        end
    end
end
