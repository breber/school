% This code is for Homework 1, Vignetting, in Math 535, spring 2016
% and was authored by Brian Reber

image = 'images/image3.jpg';
scaleFactors = [0, 0.25, 0.30, 0.5, 0.8, 1];

for factor = scaleFactors
    imshow(VignettingEffect(image, factor));
end
