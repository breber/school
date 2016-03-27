function y = chisq(B)
 
%A is a matrix representative of the percent of the function for which we
%are taking the chi-squared distribution
counts = zeros(256,1);
for h=1:length(B)
    counts((B(h,1)+1),1)=counts(B(h,1)+1,1)+1;
end
 
%creates a matrix.  First column is actual even values (times -1).  Second
%column is average of PoV.
hist = zeros(128, 2);
for n = 1:128
    hist(n, 1) = -1*counts(2*n); %actual even values
    hist(n, 2) = (counts(2*n) + counts(2*n -1))/2; %estimated values
end
 
%combining bins.  First combines a bin with its neighbor to its right until
%a bin of atleast 5 is encountered.  Then combines a bin with its neighbor
%to its left.
k = 128;
m=1;
while (hist(m, 2)) < 5
    hist(m+1,:) = hist(m, :)+hist(m+1, :);
    hist(m,:) = [1 -1];
    m= m+1;
    k=k-1;
end
for l = (128-k+1): 128
    if hist(l,2) < 5
        hist(l,:) = hist(l,:) + hist(l-1,:);
        hist(l-1,:) = [1 -1];
        k=k-1;
    end
end
 
sum = 0;
for q = 1:128
   sum = sum + ((hist(q,1) + hist(q,2))^2)/hist(q,2);
end
y = 1 - (chi2cdf(sum,k-1));
 
