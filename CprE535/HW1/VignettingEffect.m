% This code is for Homework 1, Vignetting, in Math 535, spring 2016
% and was authored by Brian Reber

function [ vignetteImg ] = VignettingEffect(inputImage, scaleLevel)
    if scaleLevel < 0
        error('Scale value must be > 0');
    end

    imageInfo = imfinfo(inputImage);
    inputImageData = imread(inputImage);
    
    % The actual image center
    centerX = (imageInfo.Width + 1) / 2;
    centerY = (imageInfo.Height + 1) / 2;

    % The location to center the vignetting on. For problems 2 and 3, this
    % is just the actual image center. For problem 4, this will be an
    % arbitrary point
    vignetteCenterX = centerX;
    vignetteCenterY = centerY;
    
    % To handle color images, just add a third dimension to the output
    % image with the size being number of color channels
    vignetteImg = zeros(imageInfo.Height, imageInfo.Width, imageInfo.NumberOfSamples, 'uint8');
    
    % Handle finding max radius when the 'center' isn't actually the center
    % of the image. If it's greater than the center point, subtract the
    % value from the width (height), otherwise use the point as is
    if vignetteCenterX > centerX
        offsetX = imageInfo.Width - vignetteCenterX;
    else
        offsetX = vignetteCenterX;
    end
    
    if vignetteCenterY > centerY
        offsetY = imageInfo.Height - vignetteCenterY;
    else
        offsetY = vignetteCenterY;
    end
    
    maxRadius = sqrt(offsetX ^ 2 + offsetY ^ 2);
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
