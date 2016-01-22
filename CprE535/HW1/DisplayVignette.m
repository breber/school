% This code is for Homework 1, Vignetting, in Math 535, spring 2016
% and was authored by Brian Reber
clear();
close();

image = 'images/image3.jpg';
itemsPerRow = 3;
scaleFactors = [0, 0.25, 0.30, 0.5, 0.8, 1];

index = 1;
for factor = scaleFactors
    subplot(size(scaleFactors, 2) / itemsPerRow, itemsPerRow, index), imshow(VignettingEffect(image, factor));
    title(strcat('Scale factor =  ', num2str(factor)));
    index = index + 1;
end
