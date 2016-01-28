% This code is for Homework 1, Problem 1, Vignetting, in Math 535, spring 2016
% and was authored by Brian Reber

inputImage1 = 'image1.jpg';
inputImage2 = 'image2.jpg';
inputImage3 = 'image3.jpg';

% Output each variable for each image
disp(strcat('Image info: ', inputImage1)); 
[Color,H,W,Mx,Mn,CntrX,CntrY] = getImageInfo(inputImage1)
disp(strcat('Image info: ', inputImage2)); 
[Color,H,W,Mx,Mn,CntrX,CntrY] = getImageInfo(inputImage2)
disp(strcat('Image info: ', inputImage3)); 
[Color,H,W,Mx,Mn,CntrX,CntrY] = getImageInfo(inputImage3)
