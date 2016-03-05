function [ status ] = CorrelateMode( inPath, inAvg )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
    if (~exist('iList') == 1)
        fileData = rdir([inPath, '\*.*'], 'regexp(name, [''.mat''], ''ignorecase'')', true);
        iList = {fileData.name};
    end

    nList = size(iList, 2);
    for i = 1:nList
        if 0 == strcmp(inAvg, iList{i})
            fprintf('Correlating: %s --> %s\n', inAvg, iList{i});
            prnuName = {inAvg, iList{i}};
            [status, ref1, ref2, colrNames, corr] = lucasdigicamident('correlate', inPath, '', prnuName);
            corr
            %fprintf('    Results: %f, %f, %f\n', corr{1}, corr{2}, corr{3});
        end
    end
end

