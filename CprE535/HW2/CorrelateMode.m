function [ status ] = CorrelateMode( inPath, inAvg )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
    if (~exist('iList') == 1)
        fileData = rdir([inPath, '\*.*'], 'regexp(name, [''.mat''], ''ignorecase'')', true);
        iList = {fileData.name};
    end

    nList = size(iList, 2);
    results = zeros([nList - 1, 1]);
    index = 1;
    for i = 1:nList
        if 0 == strcmp(inAvg, iList{i})
            fprintf('Correlating: %s --> %s\n', inAvg, iList{i});
            prnuName = {inAvg, iList{i}};
            [status, ~, ~, ~, corr] = lucasdigicamident('correlate', inPath, '', prnuName);
            results(index) = corr(1) + corr(2) + corr(3);
            fprintf('    Results: %f, %f, %f --> %f\n', corr(1), corr(2), corr(3), results(index));
            
            index = index + 1;
        end
    end
    
    %[0, 0.1, 0.2, 0.3, 0.4, 0.5]
    histogram(results);
end
