%This was used at the 2004 Cyber Summer Camp
%This program hides a message image in the lower
%bit planes of a cover image
 
%read in cover image filename
covername = input('Enter image file name with extension (like jennifer.bmp): ', 's');  
%read in message image filename
messagename = input('Enter message image file name with extension: ', 's');
 
%open cover and message image files
cover = imread(covername);
message = imread(messagename);
 
%display on screen the two images
figure(1), imshow(cover); title('Original Image (Cover Image)');
figure(2), imshow(message);title('Image to Hide (Message Image)');
 
%change to double to work with addition below
cover=double(cover);
message=double(message);
 
%imbed = no. of bits of message image to embed in cover image
imbed=4;
 
%shift the message image over (8-imbed) bits to right
messageshift=bitshift(message,-(8-imbed));
 
%show the message image with only embed bits on screen
%must shift from LSBs to MSBs
showmess=uint8(messageshift);
showmess=bitshift(showmess,8-imbed);
figure(3),imshow(showmess);title('4 Bit Image to Hide');
 
%now zero out imbed bits in cover image
coverzero = cover;
for i=1:imbed
    coverzero=bitset(coverzero,i,0);
end
 
%now add message image and cover image
stego = uint8(coverzero+messageshift);
 
figure(4),imshow(stego);title('Stego image');
 
%save files if need to
%4 bit file that was embedded = same as file extracted
imwrite(showmess,'showmess4.bmp');  %use bmp to preserve lower bits
                                %jpg will get rid of them
%stego file
imwrite(stego,'stego4.bmp');