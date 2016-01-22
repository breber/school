% This code is for Homework 1, Vignetting, in Math 535, spring 2016
% and was authored by Brian Reber

function [ vignetteImg ] = VignettingEffect(inputImage, scaleLevel)
    imageInfo = imfinfo(inputImage);
    inputImageData = imread(inputImage);
    
    centerY = imageInfo.Height / 2;
    centerX = imageInfo.Width / 2;
    
    vignetteImg = zeros(imageInfo.Height, imageInfo.Width, 'uint8');
    
    maxRadius = sqrt(centerY ^ 2 + centerX ^ 2);
    for x = 1:imageInfo.Width
        for y = 1:imageInfo.Height
            radius = sqrt((x - centerX) ^ 2 + (y - centerY) ^ 2);
            vignetteImg(y, x) = inputImageData(y, x) * (1 - scaleLevel * radius / maxRadius);
        end
    end
end
