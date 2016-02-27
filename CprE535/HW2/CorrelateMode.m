function [ status ] = CorrelateMode( inPath, inAvg, inImgs )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
    outPath = [inPath '_out'];

    refFileNames = { inAvg, inImgs };

    status = lucasdigicamident('correlate', inPath, outPath, refFileNames);
end

