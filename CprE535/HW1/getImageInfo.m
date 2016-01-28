% This code is for Homework 1, Vignetting, in Math 535, spring 2016
% and was authored by Brian Reber

function [Color, H, W, Mx, Mn, CntrX, CntrY] = getImageInfo(inputImage)
    imageInfo = imfinfo(inputImage);
    Color = strcmp(imageInfo.ColorType, 'truecolor');
    H = imageInfo.Height;
    W = imageInfo.Width;
    
    imageData = imread(inputImage);
    
    % Find the max and min of all three components
    % Returns a vector of the max for each channel
    Mx = max(max(imageData));
    Mn = min(min(imageData));
    
    % Find the center of the image. Offset the values by one since matlab
    % is 1-based. If the value is not an integer, then it is not a pixel
    % location
    CntrX = (W + 1) / 2;
    CntrY = (H + 1) / 2;
end