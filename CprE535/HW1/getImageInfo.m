% This code is for Homework 1, Vignetting, in Math 535, spring 2016
% and was authored by Brian Reber

function [Color, H, W, Mx, Mn, CntrX, CntrY] = getImageInfo(inputImage)
    imageInfo = imfinfo(inputImage);
    Color = strcmp(imageInfo.ColorType, 'truecolor');
    H = imageInfo.Height;
    W = imageInfo.Width;
    
    imageData = imread(inputImage);
    
    % Find the max and min of all three components
    Mx = max(max(max(imageData)));
    Mn = min(min(min(imageData)));
    CntrX = W / 2;
    CntrY = H / 2;
end