function [ status ] = CorrelateMode( inPath, inAvg )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
    if (~exist('iList') == 1)
        fileData = rdir([inPath, '\*.*'], 'regexp(name, [''.mat''], ''ignorecase'')', true);
        iList = {fileData.name};
    end

    nList = size(iList, 1);
    for i = 1:nList
        if 0 == strcmp(inAvg, iList{i})
            prnuName = {inAvg, iList{i}};
            [status, ref1, ref2, colrNames, corr] = lucasdigicamident('correlate', inPath, '', prnuName);            
        end
    end
end

